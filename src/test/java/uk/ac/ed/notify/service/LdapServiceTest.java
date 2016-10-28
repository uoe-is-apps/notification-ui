/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.service;

import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ed.notify.TestApplication;
import uk.ac.ed.notify.entity.LdapGroup;

/**
 *
 * @author hsun1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplication.class)
public class LdapServiceTest {
    
    @Autowired
    LdapService ldapService;
    
    private final String base = "ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk";
    
    @Test
    public void ldapGetGroupName()
    {
        String name = ldapService.getGroupName(base);
        
        //test UoE's name
        assertEquals("UoE",name);
    }

    @Test
    public void ldapGetNextLevelGroups()
    {
        List<LdapGroup> list = ldapService.getNextLevelGroups(base);
        String ifSCEFound = "no";
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getOu().equals("SCE")){
                ifSCEFound = "SCE";
                break;
            }
        }
        
        //test SCE is child of UOE
        assertEquals("SCE",ifSCEFound);
    }
    
    @Test
    public void ldapGetMembers()
    {
        List<String> list = ldapService.getMembers(base);
        int memberSize = list.size();
        
        //test the base group have members in it.
        assertNotSame(new Integer(0), new Integer(memberSize));
    }       
}
