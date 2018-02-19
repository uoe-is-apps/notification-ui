/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ed.notify.entity.JsonLdap;
import uk.ac.ed.notify.entity.LdapGroup;
import uk.ac.ed.notify.entity.UiUser;
import uk.ac.ed.notify.repository.UiUserRepository;
import uk.ac.ed.notify.service.LdapService;


@RestController
public class LdapController {

    private String clientSecret;
    private String tokenUrl;
    private String clientId;

    @Autowired
    LdapService ldapService;        
    
    @Autowired
    UiUserRepository uiUserRepository;    
    
    @Autowired
    public LdapController( @Value("${spring.oauth2.client.clientSecret}") String clientSecret,
                                   @Value("${spring.oauth2.client.accessTokenUri}") String tokenUrl,
                                   @Value("${spring.oauth2.client.clientId}") String clientId) {
        this.clientId=clientId;
        this.clientSecret=clientSecret;
        this.tokenUrl=tokenUrl;
        restTemplate = new OAuth2RestTemplate(resource());
    }

    private OAuth2RestTemplate restTemplate;

    protected OAuth2ProtectedResourceDetails resource() {
        ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
        resource.setAccessTokenUri(tokenUrl);
        resource.setClientSecret(clientSecret);
        resource.setClientId(clientId);
        return resource;
    }

    @RequestMapping(value = "/findGroups", method = RequestMethod.GET)
    public List<JsonLdap> findGroups(@RequestParam("id") String id, HttpServletRequest request) throws ServletException {
        String root = "";
        
        UiUser user = uiUserRepository.findOne(request.getRemoteUser());
        String ldapBase = user.getOrgGroupDN();
        
        if(id.equals("#")){
            root = ldapBase;
        }else{
            root = id;
        }

        List<LdapGroup> nextLevelList = ldapService.getNextLevelGroups(root);
        
        List<JsonLdap> list = new ArrayList<JsonLdap>();
        if(nextLevelList != null){
            for(int i = 0; i < nextLevelList.size(); i++){            
                LdapGroup group = nextLevelList.get(i);
                String ou = group.getOu();
                String description = group.getDescription();

                JsonLdap ldap = new JsonLdap();        
                ldap.setId("ou=" + ou + "," + root);
                ldap.setText(description);
                ldap.setChildren(true);

                list.add(ldap);
            }
        }

        /*
        List<String> listOfUUNs1 = ldapService.getMembersFromParentGroup("ou=D902,ou=P5J,ou=ISG3,ou=ISG,ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk");
        for(int i = 0; i < listOfUUNs1.size(); i++){
            System.out.println("listOfUUNs1 - " + i + " " + listOfUUNs1.get(i));                  
        }
        
        List<String> listOfUUNs2 = ldapService.getMembersFromParentGroup("ou=P5J,ou=ISG3,ou=ISG,ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk");               
        for(int i = 0; i < listOfUUNs2.size(); i++){
            System.out.println("listOfUUNs2 - " +i + " " + listOfUUNs2.get(i));                  
        }        
        
        
        List<String> listOfUUNs1 = ldapService.getMembersFromParentGroup("ou=AHSS,ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk");
        for(int i = 0; i < listOfUUNs1.size(); i++){
            System.out.println("listOfUUNs1 - " + i + " " + listOfUUNs1.get(i));                  
        }        
        */
        
        return list;
    }    
 
    
}
