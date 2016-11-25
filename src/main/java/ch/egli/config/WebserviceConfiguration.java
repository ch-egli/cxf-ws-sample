package ch.egli.config;

import ch.egli.webservice.HelloPortImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

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
        EndpointImpl endpoint = new EndpointImpl(bus, helloPort());
        endpoint.publish("/Hello");
        return endpoint;
    }

}
