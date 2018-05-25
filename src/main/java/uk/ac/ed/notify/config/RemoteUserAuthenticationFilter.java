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
        //(true) return "hsun1";       
        String principal = hsr.getRemoteUser();
        /* 
        if (principal == null) {
            throw new PreAuthenticatedCredentialsNotFoundException("1 Remote user not found in request.");  
        }
        //return "DenyAccess";
        //return "hsun1";        
        */
        

        if (principal == null) {
          logger.info("getPreAuthenticatedPrincipal - " + null);
          return "DenyAccess";
        }else if (principal.equals("mistst01")) { //for tmp testing
          return "DenyAccess";        
        }else{
          logger.info("getPreAuthenticatedPrincipal - " + principal);
          return principal;
        }
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest hsr) {
        return "N/A";
    }
}
