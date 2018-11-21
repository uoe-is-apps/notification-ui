/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ed.notify.entity.JsonLdap;
import uk.ac.ed.notify.entity.LdapGroup;
import uk.ac.ed.notify.entity.UiUser;
import uk.ac.ed.notify.repository.UiUserRepository;
import uk.ac.ed.notify.service.GroupService;

@RestController
public class LdapController {

    @Autowired
    private GroupService groupService;
    
    @Autowired
    private UiUserRepository uiUserRepository;

    @RequestMapping(value = "/findGroups", method = RequestMethod.GET)
    public List<JsonLdap> findGroups(@RequestParam("id") String id, HttpServletRequest request) {
        String root = "";
        
        UiUser user = uiUserRepository.findOne(request.getRemoteUser());
        String ldapBase = user.getOrgGroupDN();
        
        if(id.equals("#")){
            root = ldapBase;
        }else{
            root = id;
        }

        List<LdapGroup> nextLevelList = groupService.getNextLevelGroups(root);
        
        List<JsonLdap> list = new ArrayList<>();
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

        return list;
    }    
 
    
}
