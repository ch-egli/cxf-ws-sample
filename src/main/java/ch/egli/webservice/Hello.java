package ch.egli.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * TODO: Describe
 *
 * @author Christian Egli
 * @since 11/25/16.
 */
@WebService(targetNamespace = "http://webservice.egli.ch/", name = "Hello")
public interface Hello {

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "sayHello",
            targetNamespace = "http://webservice.egli.ch/",
            className = "ch.egli.webservice.SayHello")
    @WebMethod(action = "urn:SayHello")
    @ResponseWrapper(localName = "sayHelloResponse",
            targetNamespace = "http://webservice.egli.ch/",
            className = "ch.egli.webservice.SayHelloResponse")
    String sayHello(@WebParam(name = "myname", targetNamespace = "") String myname);
}
