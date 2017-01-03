package ch.egli.config;

import ch.egli.auth.PkmFaultOutInterceptor;
import ch.egli.auth.UsernameTokenLdapValidator;
import ch.egli.webservice.HelloPortImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Describe
 *
 * @author Christian Egli
 * @since 11/25/16.
 */
@Configuration
public class WebserviceConfiguration {

    @Autowired
    private Bus bus;

    /**
     * Define the webservice implementation class as Spring bean, otherwise SpringBeanAutowiringSupport does not work!
     */
    @Bean
    public HelloPortImpl helloPort() {
        return new HelloPortImpl();
    }

    @Bean
    public Endpoint endpoint() {
        Map<String, Object> endpointProps = new HashMap<>();
        endpointProps.put("ws-security.is-bsp-compliant", "false");
        endpointProps.put("ws-security.ut.validator", UsernameTokenLdapValidator.class.getName());

        EndpointImpl endpoint = new EndpointImpl(bus, helloPort());
        endpoint.setProperties(endpointProps);
        endpoint.publish("/Hello");
        return endpoint;
    }

    @Bean
    public PkmFaultOutInterceptor pkmFaultOutInterceptor() {
        return new PkmFaultOutInterceptor();
    }

}
