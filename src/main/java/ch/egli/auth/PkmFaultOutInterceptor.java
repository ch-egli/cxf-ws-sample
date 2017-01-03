package ch.egli.auth;

import com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultElement;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;

import javax.xml.namespace.QName;

/**
 * TODO: Describe
 *
 * @author Christian Egli
 * @since 12/23/16.
 */
public class PkmFaultOutInterceptor extends AbstractSoapInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PkmFaultOutInterceptor.class);



    public PkmFaultOutInterceptor() {

        super(Phase.PREPARE_SEND);

    }



    public void handleMessage(SoapMessage message) throws Fault {

        Fault f = (Fault) message.getContent(Exception.class);

        Throwable cause = f.getCause();
        if (cause instanceof BadCredentialsException) {
            //LOGGER.info("BadCredentialsException", cause);
            f.setFaultCode(new QName("", "PKM-2"));
            f.setMessage("Authorization refused...");
            f.setDetail(new DefaultElement());
        } else {
            LOGGER.warn("Unexpected Exception thrown ", cause);
        }

        LOGGER.debug("##### FaultOutInterceptor called!");

        //throw new DinarException(DinarErrorCode.USER_NOT_AUTHORIZED_FOR_WS, new BadCredentialsException("PKM: Bad Credentials"));

/*

        if (message.getVersion() == Soap11.getInstance()) {

            message.getInterceptorChain().add(Soap11FaultOutInterceptor.Soap11FaultOutInterceptorInternal.INSTANCE);

        } else {

            message.getInterceptorChain().add(Soap12FaultOutInterceptor.Soap12FaultOutInterceptorInternal.INSTANCE);

        }

*/

    }



}