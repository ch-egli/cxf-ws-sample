package ch.egli.auth;

import org.apache.wss4j.common.ext.WSSecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

import javax.annotation.PostConstruct;

/**
 * Caching authentication provider.
 *
 * @author Christian Egli
 * @since 12/2/16.
 */
public class CachingLdapAuthenticationProvider extends LdapAuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(CachingLdapAuthenticationProvider.class);

    public CachingLdapAuthenticationProvider(LdapAuthenticator authenticator, LdapAuthoritiesPopulator authoritiesPopulator) {
        super(authenticator, authoritiesPopulator);
    }

    @PostConstruct
    public void init() {
        cache = cacheManager.getCache("authentications");
    }

    @Autowired
    private CacheManager cacheManager;

    private Cache cache;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = (String) authentication.getCredentials();

        // return Authentication object for current user from cache, if it is available
        Authentication result = cache.get(userName, Authentication.class);
        if (result != null) {
            LOGGER.debug("### found auth obj for user '{}' in cache", userName);

            // check that password matches...
            String pwFromCache = (String) result.getCredentials();
            if (!password.equals(pwFromCache)) {
                LOGGER.error("Authentication failed for user '{}'. Credentials in cache do not match the given credentials!", userName);
                throw new BadCredentialsException("Credentials in cache do not match the credentials given by the current user '" + userName + "'");
            }
            return result;
        }

        LOGGER.debug("### no auth obj found for user '{}' in cache -> perform authentication on ldap...", userName);

        result = super.authenticate(authentication);
        cache.put(userName, result);

        return result;
    }
}
