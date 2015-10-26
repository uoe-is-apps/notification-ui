package uk.ac.ed.notify.config;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by rgood on 26/10/2015.
 */
public class RemoteUserAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {


    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest hsr) {
        String principal = hsr.getRemoteUser();
        if (principal == null) {
            throw new PreAuthenticatedCredentialsNotFoundException(" Remote user not found in request.");
        }
        return principal;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest hsr) {
        return "N/A";
    }

}
