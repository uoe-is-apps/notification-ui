package uk.ac.ed.notify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.ac.ed.notify.entity.UiRole;
import uk.ac.ed.notify.entity.UiUser;
import uk.ac.ed.notify.entity.UiUserRole;
import uk.ac.ed.notify.repository.UiRoleRepository;
import uk.ac.ed.notify.repository.UiUserRepository;
import uk.ac.ed.notify.repository.UiUserRoleRepository;

import java.util.List;

/**
 * Created by rgood on 29/10/2015.
 */
@RestController
public class UiUserController {

    @Autowired
    UiUserRepository uiUserRepository;

    @Autowired
    UiRoleRepository uiRoleRepository;

    @Autowired
    UiUserRoleRepository uiUserRoleRepository;

    @RequestMapping(value="/ui-users",method = RequestMethod.GET)
    public List<UiUser> getUsers()
    {
        return (List<UiUser>)uiUserRepository.findAll();
    }

    @RequestMapping(value="/ui-user/{uun}", method = RequestMethod.GET)
    public UiUser getUser(@PathVariable("uun") String uun)
    {
        return uiUserRepository.findOne(uun);
    }

    @RequestMapping(value="/ui-user/{uun}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable("uun") String uun)
    {

        for (UiUserRole uiUserRole : uiUserRoleRepository.findByUun(uun)) {
            uiUserRoleRepository.delete(uiUserRole);
        }

        uiUserRepository.delete(uun);
    }

    @RequestMapping(value="/ui-user/{uun}", method = RequestMethod.PUT)
    public void updateUser(@PathVariable("uun") String uun,@RequestBody UiUser uiUser)
    {
        uiUserRepository.save(uiUser);
    }

    @RequestMapping(value="/ui-roles", method = RequestMethod.GET)
    public List<UiRole> getRoles()
    {
        return (List<UiRole>)uiRoleRepository.findAll();
    }

}
