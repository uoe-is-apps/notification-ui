/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.service;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import org.apache.commons.lang3.StringUtils;
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
import uk.ac.ed.notify.entity.LdapGroup;
import uk.ac.ed.notify.groups.Member;
import uk.ac.ed.notify.groups.OrgUnit;
import uk.ac.ed.notify.groups.OrgUnitMembers;

public class LdapGroupService implements GroupService {
    
    protected final Log logger = LogFactory.getLog(this.getClass());
    
    @Value("${ldap.contextSource.url:}")
    private String ldapUrl;    
    
    @Autowired
    private LdapTemplate ldapTemplate;
        
	@Override
    public List<LdapGroup> getNextLevelGroups(String base) {
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
    
    @Override
    public String getGroupName(String base) {
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

    @Override
    public List<String> getMembers(String base) {
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
            results.addAll(result.get(0));            
            return result.get(0);
        }
        else 
        {
            return null;
        }    
    }
    
    
    List<String> results = null;
            
	@Override
    public List<String> getMembersFromParentGroup(String base) {
        /*
         * input  ou=SU685,ou=D685,ou=P5M,ou=ISG3,ou=ISG,ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk
         * output          ou=D685,ou=P5M,ou=ISG3,ou=ISG,ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk
         */
        if(base.startsWith("ou=SU")){                
             String su = "";
             String ou = "";
             StringTokenizer st = new StringTokenizer(base, ",");
             while (st.hasMoreTokens()) {
                    String nextToken = st.nextToken();
                    if(nextToken.contains("ou=SU")){
                        su = nextToken;
                    }
                    if(nextToken.contains("ou=D")){
                        ou = nextToken;
                    }
                    
                    if(!su.equals("") && !ou.equals("")){
                        
                        if(su.replace("ou=SU", "").equals(ou.replace("ou=D", ""))){
                            base = base.substring(base.indexOf(",") + 1);
                        }
                        
                        break;
                    }
            }
        }        
        
            
        //always clear the array before each invocation 
        results = new ArrayList<>();

        List<OrgUnitMembers> orgUnitMembers = new ArrayList<>();
		
        String reqHierarchy = base;
        String terminateToken = ",ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk";
        int hierarchySize = countOccurrences(reqHierarchy.replace(terminateToken, ""), '=');
                 
        List<OrgUnit> orgunits = getLdapEntriesByOrgUnit(reqHierarchy, hierarchySize);
	populateOrgUnitMembers(orgUnitMembers, orgunits);
		
        return results;
    }
    
    private int countOccurrences(String haystack, char needle)
    {
        int count = 0;
        for (int i=0; i < haystack.length(); i++)
        {
            if (haystack.charAt(i) == needle)
            {
                 count++;
            }
        }
        return count;
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

    private class GroupAttributeMapper implements AttributesMapper{
        @Override
        public Object mapFromAttributes(Attributes attributes) throws NamingException {
            LdapGroup orgunit = new LdapGroup();                    
            orgunit.setOu(getStringAttribute(attributes,"ou"));     
            orgunit.setDescription(getStringAttribute(attributes,"description"));     
            return orgunit;
        }
    }        

    private class MembersAttributeMapper implements AttributesMapper {
        @Override
        public List<String> mapFromAttributes(Attributes attributes) {
            List<String> memberof = new ArrayList();
            try{                
                for (Enumeration vals = attributes.get("member").getAll(); vals.hasMoreElements();) {                    
                    String base = (String)vals.nextElement();
                    String member = base.substring(base.indexOf("=") + 1, base.indexOf(","));
                    memberof.add(member);
                }
            } catch(NamingException e){
                throw new RuntimeException(e);
            }
            return memberof;
        }
    }    
    
//------------------------------------------------------------------------------
	private List<OrgUnit> orgUnits = new ArrayList<>();

	private List<OrgUnit> getLdapEntriesByOrgUnit(String basePath, int level) {

		if (!getOrgUnits().isEmpty()) {
			getOrgUnits().clear();
		}

		OrgUnit orgUnit = getMemebersMap(basePath);
		orgUnit.setOrgUnit(getGroup(basePath).getOu());
		getOrgUnits().add(orgUnit);

		
		List<LdapGroup> groups = getNextLevelGroups(basePath); 
                if(level==1) {
		return getOrgUnits(); 
                } 
                 
                 
                 
                //level= level-1;
		if(groups != null && groups.size() > 0 ) { 
                   if (groups != null && groups.size() > 0) { 
                       traverseAllNodes(basePath, groups, level);
                   }
                }

		return getOrgUnits();
	}

        
	private LdapGroup getGroup(String base) {
		AndFilter andFilter = new AndFilter();
		andFilter.and(new EqualsFilter("objectclass", "organizationalUnit"));

		LdapContextSource lcs = new LdapContextSource();
		lcs.setUrl(ldapUrl);
		// sample base -
		// ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk
		lcs.setBase(base);
		lcs.setDirObjectFactory(DefaultDirObjectFactory.class);
		lcs.afterPropertiesSet();

		ldapTemplate.setContextSource(lcs);

		@SuppressWarnings("unchecked")
		List<LdapGroup> result = ldapTemplate.search("", andFilter.encode(), SearchControls.OBJECT_SCOPE,
				new GroupAttributeMapper());

		if (result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}


	private void populateOrgUnitMembers(List<OrgUnitMembers> orgUnitMembers, List<OrgUnit> orgunits) {
		for (OrgUnit orgUnit : orgunits) {
			OrgUnitMembers orgUnitMember = new OrgUnitMembers();
			orgUnitMember.setOrgunit(orgUnit.getOrgUnit());
			List<Member> members = new ArrayList<>();
			if (null != orgUnit.getMembers() && orgUnit.getMembers().size() > 0) {
				setNameByUUN(orgUnit, members);
				// adding members to organisation (orgunit)
				orgUnitMember.setMembers(members);
			}
			// if (orgUnitMember.getMembers() != null &&
			// orgUnitMember.getMembers().size() > 0) {
			orgUnitMembers.add(orgUnitMember);
			// }
		}
	}

	private void setNameByUUN(OrgUnit orgUnit, List<Member> members) {
		for (String uun : orgUnit.getMembers()) {
			if (!StringUtils.isEmpty(uun)) {
				Member member = new Member();
				member.setUun(uun);
				members.add(member);
			}
		}
	}
        
	private List<OrgUnit> traverseAllNodes(String base, List<LdapGroup> groups, int level) { 
             
            for (LdapGroup group : groups) { 
                 
                 String orgUnit = group.getOu(); 
                 String amendBase = "ou=" + orgUnit + "," + base; OrgUnit
                 nextLevelOfGroupMembers = getMemebersMap(amendBase);
                 nextLevelOfGroupMembers.setOrgUnit(orgUnit);

                 getOrgUnits().add(nextLevelOfGroupMembers); 
                 //level--;
                
                 try {
                     if (amendBase != null && !amendBase.startsWith("ou=SU") &&  getNextLevelGroups(amendBase).size() >0   && level>0) {
                        traverseAllNodes(amendBase, getNextLevelGroups(amendBase),level);
                     } else {
                         //exit recursive
                     }
                 } catch(Exception e) {
                     logger.error("error traverseAllNodes - " + base + e);
                 }
            }
             
            return getOrgUnits(); 
        }        
         
	private OrgUnit getMemebersMap(String base) {
		List<String> members = getMembers(base);
		OrgUnit orgUnit = new OrgUnit();
		orgUnit.setMembers(members);
		return orgUnit;
	}

	private List<OrgUnit> getOrgUnits() {
		return orgUnits;
	}

}

