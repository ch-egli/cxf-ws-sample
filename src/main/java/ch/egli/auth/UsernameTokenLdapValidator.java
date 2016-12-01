package ch.egli.auth;

import ch.egli.config.WebApplicationContextLocator;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.dom.handler.RequestData;
import org.apache.wss4j.dom.message.token.UsernameToken;
import org.apache.wss4j.dom.validate.Credential;
import org.apache.wss4j.dom.validate.UsernameTokenValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.LdapAuthority;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collection;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

/**
 * TODO: Describe
 *
 * @author Christian Egli
 * @since 11/25/16.
 */
public class UsernameTokenLdapValidator extends UsernameTokenValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsernameTokenLdapValidator.class);

    @Autowired
    LdapTemplate ldapTemplate;

    @Autowired
    LdapAuthenticationProvider ldapAuthenticationProvider;

    public UsernameTokenLdapValidator() {
        AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
        WebApplicationContext currentContext = WebApplicationContextLocator.getCurrentWebApplicationContext();
        bpp.setBeanFactory(currentContext.getAutowireCapableBeanFactory());
        bpp.processInjection(this);
    }

    @Override
    public Credential validate(Credential credential, RequestData data) throws WSSecurityException {
        UsernameToken usernameToken = credential.getUsernametoken();
        String user = usernameToken.getName();
        String password = usernameToken.getPassword();

        // Authentication
        try {
            ldapTemplate.authenticate(query().where("uid").is(user), password);
        } catch (EmptyResultDataAccessException eae) {
            // if the current user has not been found
            LOGGER.error("Authentication error: user '{}' not found ", eae);
            throw new WSSecurityException(WSSecurityException.ErrorCode.FAILED_AUTHENTICATION, eae);
        } catch (IncorrectResultSizeDataAccessException iae) {
            // if more than one users were found
            LOGGER.error("Authentication error: more than one entry for user '{}' ", iae);
            throw new WSSecurityException(WSSecurityException.ErrorCode.FAILED_AUTHENTICATION, iae);
        } catch (NamingException ne) {
            // if something went wrong in authentication, e.g. invalid password
            LOGGER.error("Authentication failed for user '{}' ", ne);
            throw new WSSecurityException(WSSecurityException.ErrorCode.FAILED_AUTHENTICATION, ne);
        }

        // dummy authorization: add role
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new LdapAuthority("DINAR_PKM_R", "dn1-test"));
        Authentication auth = new UsernamePasswordAuthenticationToken(user, password, authorities);

        // Authentication request = new UsernamePasswordAuthenticationToken(user, password);
        // Authentication result = ldapAuthenticationProvider.authenticate(request);
        // SecurityContextHolder.getContext().setAuthentication(result);

        SecurityContextHolder.getContext().setAuthentication(auth);

        return credential;
    }

}
