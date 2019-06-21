package uk.ac.ed.notify.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by rgood on 26/10/2015.
 */
public class RemoteUserAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    private static final String DENY_ACCESS = "DenyAccess";

    @Value("${cas.server:}")
    private String casServer;

    private static final Logger logger = LoggerFactory.getLogger(RemoteUserAuthenticationFilter.class);

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest hsr) {
//        if (true) return "admin";
        String principal = hsr.getRemoteUser();
        /* 
        if (principal == null) {
            throw new PreAuthenticatedCredentialsNotFoundException("1 Remote user not found in request.");  
        }
        //return DENY_ACCESS;
        //return "hsun1";        
        */
        
        logger.debug("getPreAuthenticatedPrincipal - remote user='{}'", principal);

        if (principal == null) {
            return StringUtils.isBlank(casServer)
                    ? DENY_ACCESS  // No CAS;  give the DenyAccess user
                    : null;        // CAS AuthN is configured;  allow the CasAuthenticationEntryPoint to take over...
        } else if (principal.equals("mistst01")) { //for tmp testing
            return DENY_ACCESS;
        } else {
            return principal;
        }
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest hsr) {
        return "N/A";
    }
}
