/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.service;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.DefaultDirObjectFactory;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;
import uk.ac.ed.notify.entity.LdapGroup;

@Service
public class LdapService {
    
    protected final Log logger = LogFactory.getLog(this.getClass());
    
    @Value("${ldap.contextSource.url}")
    private String ldapUrl;    
    
    @Autowired
    LdapTemplate ldapTemplate;
    
    
    /**
     * Get a group's children group
     * @param base
     * @return a list of group
     */
    public List<LdapGroup> getNextLevelGroups(String base)
    {       
        AndFilter andFilter = new AndFilter();

        andFilter.and(new EqualsFilter("objectclass","organizationalUnit"));

        LdapContextSource lcs = new LdapContextSource();
        lcs.setUrl(ldapUrl);
        //sample base - ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk
        lcs.setBase(base);
        lcs.setDirObjectFactory(DefaultDirObjectFactory.class);
        lcs.afterPropertiesSet();
        ldapTemplate.setContextSource(lcs);    
        List<LdapGroup> result = ldapTemplate.search("",  andFilter.encode(), SearchControls.ONELEVEL_SCOPE,new GroupAttributeMapper()); 
        
        if (result.size()>0)
        {
            return result;
        }
        else 
        {
            return null;
        }                      
    }
    
    /**
     * Get a group's name
     * @param base
     * @return name of group
     */
    public String getGroupName(String base)
    {       
        AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass","organizationalUnit"));

        LdapContextSource lcs = new LdapContextSource();
        lcs.setUrl(ldapUrl);
        //sample base - ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk
        lcs.setBase(base);
        lcs.setDirObjectFactory(DefaultDirObjectFactory.class);
        lcs.afterPropertiesSet();

        ldapTemplate.setContextSource(lcs);
    
        List<LdapGroup> result = ldapTemplate.search("",  andFilter.encode(), SearchControls.OBJECT_SCOPE,new GroupAttributeMapper()); //new PersonContextMapper());
        
        if(result.size() > 0){
            return result.get(0).getDescription();
        }else{
            return null;
        }                      
    }

    /**
     * Get a group's members
     * @param base
     * @return a list of member uun
     */
    public List<String> getMembers(String base)
    {       
        AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass","groupOfNames"));
        
        LdapContextSource lcs = new LdapContextSource();
        lcs.setUrl(ldapUrl);
        //sample base - cn=P5J,ou=P5J,ou=ISG3,ou=ISG,ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk
        lcs.setBase("cn=" + base.substring(base.indexOf("=") + 1, base.indexOf(",")) + "," + base);    
        lcs.setDirObjectFactory(DefaultDirObjectFactory.class);
        lcs.afterPropertiesSet();
        ldapTemplate.setContextSource(lcs);

        List<List<String>> result;
        result = ldapTemplate.search("",  andFilter.encode(), new MembersAttributeMapper()); //new PersonContextMapper());

        if (result.size()>0)
        {
            return result.get(0);
        }
        else 
        {
            return null;
        }               
    }
    
    
    private String getStringAttribute(Attributes attributes, String attributeName) throws NamingException
    {
        if (attributes.get(attributeName)==null)
        {
            return null;
        }
        else
        {
            return (String)attributes.get(attributeName).get();
        }
    }
    
    private Integer getIntAttribute(Attributes attributes, String attributeName) throws NamingException
    {
        if (attributes.get(attributeName)==null)
        {
            return null;
        }
        else
        {
            return Integer.getInteger((String)attributes.get(attributeName).get());
           
        }
    }
    
    private List<String> getStringAttributes(Attributes attributes, String attributeName) throws NamingException
    {
        if (attributes.get(attributeName)==null)
        {
            return null;
        }
        else
        {
            NamingEnumeration<?> all = attributes.get(attributeName).getAll();
            String value;
            List<String> attributeValues = new ArrayList<>();
            while (all.hasMore()) {
                value = (String)all.next();
                attributeValues.add(value);
            }
            return attributeValues;
        }
    }
    

    public class GroupAttributeMapper implements AttributesMapper{
        @Override
        public Object mapFromAttributes(Attributes attributes) throws NamingException {
            LdapGroup orgunit = new LdapGroup();                    
            orgunit.setOu(getStringAttribute(attributes,"ou"));     
            orgunit.setDescription(getStringAttribute(attributes,"description"));     
            return orgunit;
        }
    }        

    
    
    public class MembersAttributeMapper implements AttributesMapper{        
        @Override
        public List<String> mapFromAttributes(Attributes attributes) throws NamingException {
            List<String> memberof = new ArrayList();
            try{                
                for (Enumeration vals = attributes.get("member").getAll(); vals.hasMoreElements();) {                    
                    String base = (String)vals.nextElement();
                    String member = base.substring(base.indexOf("=") + 1, base.indexOf(","));
                    memberof.add(member);
                }
            }catch(Exception e){
                
            }
            return memberof;
        }
    }    
}