package ch.egli.webservice;


import ch.egli.business.DummyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.annotation.PostConstruct;
import javax.jws.WebService;
import java.util.logging.Logger;

/**
 * TODO: Describe
 *
 * @author Christian Egli
 * @since 11/25/16.
 */
@WebService(serviceName = "HelloService", portName = "HelloPort",
        targetNamespace = "http://webservice.egli.ch/",
        endpointInterface = "ch.egli.webservice.Hello")
public class HelloPortImpl {

    private static final Logger LOG = Logger.getLogger(HelloPortImpl.class.getName());

    @Autowired
    private DummyService dummyService;

    @PostConstruct
    public void init() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    public String sayHello(String myname) {
        LOG.info("Executing operation sayHello" + myname);

        //SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

        int dummyNumber = dummyService.getNumber();
        try {
            return "Hello, welcome to CXF Spring boot: " + myname + "!!!";

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

}
