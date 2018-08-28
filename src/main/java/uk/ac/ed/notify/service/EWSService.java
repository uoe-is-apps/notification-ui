/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.service;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.util.Iterator;

import microsoft.exchange.webservices.data.autodiscover.IAutodiscoverRedirectionUrl;
import microsoft.exchange.webservices.data.autodiscover.exception.AutodiscoverLocalException;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.LogicalOperator;
import microsoft.exchange.webservices.data.core.enumeration.service.DeleteMode;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.EmailMessageSchema;
import microsoft.exchange.webservices.data.core.service.schema.ItemSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;
import microsoft.exchange.webservices.data.search.filter.SearchFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.ac.ed.notify.entity.Notification;
import uk.ac.ed.notify.repository.NotificationErrorRepository;
import uk.ac.ed.notify.repository.NotificationRepository;
import uk.ac.ed.notify.repository.PublisherDetailsRepository;
import uk.ac.ed.notify.repository.UserNotificationAuditRepository;

/**
 *
 * @author hsun1
 */
@Service
public class EWSService   implements IAutodiscoverRedirectionUrl { //extends EmailNotificationHandlingService

    protected final Log logger = LogFactory.getLog(this.getClass());
    private ExchangeService exchangeService;

    public class MyAuthenticator extends Authenticator {

        String user;
        String pass;
        
        MyAuthenticator(String username,String password)
        {
            user=username;
            pass=password;
        }
        
        @Override
        public PasswordAuthentication getPasswordAuthentication() {      
            return new PasswordAuthentication(user,pass.toCharArray());
        }
    }    
    
    @Autowired
    EmailNotificationHandlingService emailNotificationHandlingService;
    
    @Autowired
    PublisherDetailsRepository publisherDetailsRepository;
                            
    @Autowired
    NotificationRepository notificationRepository;
                
    @Autowired
    NotificationErrorRepository notificationErrorRepository;    
            
    @Autowired
    UserNotificationAuditRepository userNotificationAuditRepository;                
     
    @Autowired
    Office365JsonService office365JsonService;        
        
    @Value("${ewsVersion}")
    private String ewsVersion;
    
    @Value("${ewsUser}")
    private String ewsUser;
    
    @Value("${ewsPassword}")
    private String ewsPassword;
    
    @Value("${ewsUrl}")
    private String ewsUrl;
    
    @Value("${ewsDeleteMode}")
    private String ewsDeleteMode;    
    
    
    @Override
    public boolean autodiscoverRedirectionUrlValidationCallback(String string) throws AutodiscoverLocalException {
        logger.debug("autodiscover redirection:" + string);
        if (string.indexOf("autodiscover-s.outlook.com") != -1) {
            return true;
        } else {
            return false;
        }
    }    
    
    
    private ExchangeService getExchangeService(String uid) throws Exception {

        if (exchangeService == null) {
            if (ewsVersion.equals("2007")) {
                exchangeService = new ExchangeService(ExchangeVersion.Exchange2007_SP1);
            } else if (ewsVersion.equals("2010_SP1")) {
                exchangeService = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
            } else if (ewsVersion.equals("2010_SP2")) {
                exchangeService = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
            } else {
                exchangeService = new ExchangeService(ExchangeVersion.Exchange2010);
            }
            
            ExchangeCredentials credentials = new WebCredentials(ewsUser + "@ed.ac.uk", ewsPassword, "ed.ac.uk");
            
            exchangeService.setCredentials(credentials);                     
            exchangeService.setUrl(new URI(ewsUrl));
        }
        return exchangeService;                
    }

    
    public void processUnreadEmail(){
        try{
            logger.info("EWSService processing unread emails from exchange");
            
            ExchangeService service = this.getExchangeService(ewsUser);

            ItemView itemView = new ItemView(Integer.MAX_VALUE);
            SearchFilter unreadFilter = new SearchFilter.SearchFilterCollection(LogicalOperator.And, new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, false));
            FindItemsResults<Item> findResults = service.findItems(WellKnownFolderName.Inbox, unreadFilter, itemView);

            logger.info("Number of email notifications to be processed - " + findResults.getItems().size());

            PropertySet propSet = new PropertySet(BasePropertySet.IdOnly, ItemSchema.Body);
            
            Iterator<Item> iterator = findResults.iterator();
            
            while(iterator.hasNext()){
                ItemId itemId = iterator.next().getId();
                EmailMessage email = EmailMessage.bind(service, itemId, propSet);

                processEmailByBody(itemId.getUniqueId(), email.getBody().toString());

                if(ewsDeleteMode.equals("hard")){
                    email.delete(DeleteMode.HardDelete);
                }else if(ewsDeleteMode.equals("soft")){
                    email.delete(DeleteMode.MoveToDeletedItems);
                }else if(ewsDeleteMode.equals("none")){
                    //do nothing, no delete, leave this option for testing, otherwise we need to make an email notification everytime this runs.
                }
            }
            logger.info("processUnreadEmail completed... ");
        }catch(Exception e){
            logger.error("processUnreadEmail errored... " + e.toString());    
        }
    }    
    
    
    public void processEmailByBody(String id, String body){
        try {                
            logger.debug("processEmailByBody - " + id + " - " + body);
            Notification notification = office365JsonService.parseNotification(body);   
            
            logger.debug("construct notification from email - " + notification);                      
            emailNotificationHandlingService.processSingleNotification(notification);    
             
            logger.debug("success");  

        }catch(Exception e){
            logger.error("processEmailByBody errored... " + id + " " + body + " " + e.toString());

        }
    }       
}
