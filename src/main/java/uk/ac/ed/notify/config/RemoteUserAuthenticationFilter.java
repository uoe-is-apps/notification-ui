package uk.ac.ed.notify.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by rgood on 26/10/2015.
 */
public class RemoteUserAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    private static final Logger logger = LoggerFactory.getLogger(RemoteUserAuthenticationFilter.class);

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest hsr) {
        
        String principal = hsr.getRemoteUser();
        /* 
        if (principal == null) {
            System.out.println("111");
            throw new PreAuthenticatedCredentialsNotFoundException("1 Remote user not found in request.");  
        }
        
        if(principal.equals("hsun1")){
            System.out.println("222");
            throw new PreAuthenticatedCredentialsNotFoundException("2 Remote user not found in request.");
        }
        
        
        return principal; 
        */
        /* 
        return "hsun1";
        * */
        
        if (principal == null) {
          return "UserAccessDenied";
        }else if (principal.equals("mistst01")) {
          return "UserAccessDenied";
        
        }else{
          return principal;
        }
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest hsr) {
        return "N/A";
    }
}
