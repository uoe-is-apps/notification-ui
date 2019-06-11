package uk.ac.ed.notify.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import uk.ac.ed.notify.entity.UiRole;
import uk.ac.ed.notify.entity.UiUser;
import uk.ac.ed.notify.repository.UiUserRepository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by rgood on 26/10/2015.
 */
public class UiUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UiUserDetailsService.class);

    UiUserRepository uiUserRepository;

    public UiUserDetailsService(UiUserRepository uiUserRepository) {
        this.uiUserRepository = uiUserRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        try {
            logger.debug("Trying to load user '{}'", s);
            if (uiUserRepository == null) {
                throw new UsernameNotFoundException("Error getting user : uiUserRepository is null");
            }

            UiUser user = uiUserRepository.findOne(s);

            if (user == null) {
                //fix for DTI020-11
                //throw new UsernameNotFoundException(s + "was not found.");
                s = "DenyAccess";
                user = uiUserRepository.findOne(s);
                if (user == null) {
                    throw new UsernameNotFoundException("No default " + s + " user");
                }
            }
            Collection<GrantedAuthority> roles = new ArrayList<>();
            for (UiRole uiRole : user.getUiRoles()) {
                logger.info("Found role: {}", uiRole.getRoleDescription());
                roles.add(new SimpleGrantedAuthority(uiRole.getRoleCode()));
            }
            return new User(user.getUun(), "", true, true, true, true, roles);
        }
        catch (Exception e) {
            logger.error("Error getting user", e);
            throw new UsernameNotFoundException("Error getting user information");
        }
    }
}
