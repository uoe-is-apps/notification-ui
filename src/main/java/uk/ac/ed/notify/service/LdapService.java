/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
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
            results.addAll(result.get(0));            
            return result.get(0);
        }
        else 
        {
            return null;
        }    
    }
    
    
    List<String> results = null;
            
    public List<String> getMembersFromParentGroup(String base)
    {               
        //always clear the array before each invocation 
        results = new ArrayList<String>();
        System.out.println("getMembersFromParentGroup - " + base);

        List<OrgUnitMembers> orgUnitMembers = new ArrayList<OrgUnitMembers>();
		
        String reqHierarchy = base;
        String terminateToken = ",ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk";
        int hierarchySize = countOccurrences(reqHierarchy.replace(terminateToken, ""), '=');
                
        System.out.println("reqHierarchy - " + base);
        System.out.println("hierarchySize - " + hierarchySize);
                
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

    private class MembersAttributeMapper implements AttributesMapper{        
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
		 if(groups.size() > 0 ) { 
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
			List<Member> members = new ArrayList<Member>();
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
                
                 if (!amendBase.startsWith("ou=SU") &&  getNextLevelGroups(amendBase).size() >0   && level>0) {
                    traverseAllNodes(amendBase, getNextLevelGroups(amendBase),level); 
                 }else{
                     System.out.println("exit exit exit");
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

class OrgUnit implements Serializable{
	
	private static final long serialVersionUID = 1565656556565L;

	private String orgUnit;
	
	private List<String> members = new ArrayList<String>();

	public String getOrgUnit() {
		return orgUnit;
	}

	public void setOrgUnit(String orgUnit) {
		this.orgUnit = orgUnit;
	}

	public List<String> getMembers() {
		return members;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((members == null) ? 0 : members.hashCode());
		result = prime * result + ((orgUnit == null) ? 0 : orgUnit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrgUnit other = (OrgUnit) obj;
		if (members == null) {
			if (other.members != null)
				return false;
		} else if (!members.equals(other.members))
			return false;
		if (orgUnit == null) {
			if (other.orgUnit != null)
				return false;
		} else if (!orgUnit.equals(other.orgUnit))
			return false;
		return true;
	}
	
}

class Person implements Serializable{
	
	private static final long serialVersionUID = -2723409478365865769L;

	private String fullName;
	
	private String surname;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
}

class Member implements Serializable {
	
	private static final long serialVersionUID = -339684362913276623L;

	private String uun;
	
	private String name;

	public String getUun() {
		return uun;
	}

	public void setUun(String uun) {
		this.uun = uun;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}

class OrgUnitMembers implements Serializable {

	private static final long serialVersionUID = -3596643333898462959L;
	
	private String orgunit;
	
	private List<Member> members = new ArrayList<>();

	public String getOrgunit() {
		return orgunit;
	}

	public void setOrgunit(String orgunit) {
		this.orgunit = orgunit;
	}

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}

}

class OrgHierarchy implements Serializable {
	
	private static final long serialVersionUID = 1657657343243L;

	private String code;
	
	private String description;
	
	private int level;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + level;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrgHierarchy other = (OrgHierarchy) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (level != other.level)
			return false;
		return true;
	}
	
}

class OrgunitUtils {

	public static String buildLdapPathByOrgHierarchy(List<OrgHierarchy> orgunitHierarchy, int size,
			String ldapContextBasepath) {
		
		StringBuilder sb = new StringBuilder();
		if (size > 0) {

			for (int i = size - 1; i >= 0; i--) {
				sb.append("ou=");
				sb.append(orgunitHierarchy.get(i).getCode());
				sb.append(",");
			}
			sb.append(ldapContextBasepath);
		}
		return sb.toString();
	}

}