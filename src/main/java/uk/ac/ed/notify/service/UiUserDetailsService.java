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

    public UiUserDetailsService(UiUserRepository uiUserRepository)
    {
        this.uiUserRepository = uiUserRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        try {
            logger.debug("trying to get user"+s);
            if (uiUserRepository==null)
            {
                logger.debug("Repo is null");
            }

            UiUser user = uiUserRepository.findOne(s);
            if (user == null) {
                throw new UsernameNotFoundException(s + "was not found.");
            }
            Collection<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
            for (UiRole uiRole : user.getUiRoles()) {
                logger.debug("found role:"+uiRole.getRoleDescription());
                roles.add(new SimpleGrantedAuthority(uiRole.getRoleCode()));
            }
            return new User(user.getUun(), "", true, true, true, true, roles);
        }
        catch (Exception e)
        {
            logger.error("Error getting user",e);
            throw new UsernameNotFoundException("Error getting user information");
        }
    }
}
