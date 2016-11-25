package ch.egli.auth;

import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.dom.handler.RequestData;
import org.apache.wss4j.dom.message.token.UsernameToken;
import org.apache.wss4j.dom.validate.Credential;
import org.apache.wss4j.dom.validate.UsernameTokenValidator;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;

import java.io.IOException;

import static org.apache.cxf.common.security.TokenType.UsernameToken;

/**
 * TODO: Describe
 *
 * @author Christian Egli
 * @since 11/25/16.
 */
public class UsernameTokenLdapValidator extends UsernameTokenValidator {

    @Override
    public Credential validate(Credential credential, RequestData data) throws WSSecurityException {
        //Credential result = super.validate(credential, data);

        UsernameToken usernameToken = credential.getUsernametoken();
        String user = usernameToken.getName();
        String password = usernameToken.getPassword();
        String nonce = usernameToken.getNonce();
        String createdTime = usernameToken.getCreated();
        String pwType = usernameToken.getPasswordType();

        // just do validation here...

/*
        WSPasswordCallback pwCb = new WSPasswordCallback(user, password, pwType, WSPasswordCallback.USERNAME_TOKEN);

        try {
            data.getCallbackHandler().handle(new Callback[]{pwCb});
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedCallbackException e) {
            e.printStackTrace();
        }
*/


/*
        String securityDomainName = "SecurityDomainNameNameOfJBOSSConfig"; //<login-module>

        LoginContext lc;
        try {
            lc = new LoginContext( securityDomainName, new CallbackHandler() {
                public void handle( Callback[] callbacks ) throws IOException, UnsupportedCallbackException {
                    NameCallback nc = (NameCallback)callbacks[0];
                    nc.setName( userId );

                    PasswordCallback pc2 = (PasswordCallback)callbacks[1];
                    pc2.setPassword( password.toCharArray() );
                }
            } );
            lc.login();
        } catch( LoginException e ) {
            throw new WSSecurityException( WSSecurityException.ErrorCode.FAILED_AUTHENTICATION, e );
        }
*/



        return credential;
    }


}
