/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (kohtikumppanuutta@ixonos.com).
 * 
 */
package fi.koku.esb.services.customer;

import static javax.xml.soap.SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;

import org.jboss.soa.esb.actions.ActionProcessingDetailFaultException;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.actions.annotation.Process;
import org.jboss.soa.esb.http.HttpHeader;
import org.jboss.soa.esb.http.HttpResponse;
import org.jboss.soa.esb.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import fi.koku.services.entity.customer.v1.CustomerType;
import fi.koku.services.entity.customer.v1.ObjectFactory;

/**
 * Jboss ESB action class that blocks access to customerservice and returns a fault or and empty response.
 * 
 * @author aspluma
 */
public class BlockAction {
    private static final Logger logger = LoggerFactory.getLogger(BlockAction.class);
    private static final QName FAULT_CODE_SERVER = new QName(URI_NS_SOAP_1_1_ENVELOPE, "Server", "soap");
    private static final String CUSTOMER_SERVICE_NS = "http://services.koku.fi/entity/customer/v1";
    private static final JAXBContext jaxbContext = initJaxbContext();
    private static final ObjectFactory customerFactory = new ObjectFactory();
    private static final UnauthorizedReturnType UNAUTHORIZED_RETURN_TYPE = UnauthorizedReturnType.FAULT;
    
    private enum UnauthorizedReturnType {
      FAULT, EMPTY;
    }
    
    private static JAXBContext initJaxbContext() {
      try {
        return JAXBContext.newInstance(CustomerType.class.getPackage().getName());
      } catch (JAXBException e) {
        logger.error("failed to create jaxbcontext", e);
        throw new RuntimeException("failed to create jaxbcontext", e);
      }
    }
        
    public BlockAction() {
      logger.debug("BlockAction()");
    }

    @Process 
    public Message process(Message msg) throws ActionProcessingDetailFaultException {
      
      try {
        String message = null;
        HttpResponse response = null;
        
        switch(UNAUTHORIZED_RETURN_TYPE) {
        case FAULT:
          response = new HttpResponse(500);

          message = getFaultMessage(1001, "unauthorized");
          break;
        case EMPTY:
          response = new HttpResponse(200);

          JAXBElement<?> content = customerFactory.createCustomer(new CustomerType());
          message = getSOAPMessage(content);
          break;
        }

        response.addHeader(new HttpHeader("content-type", "text/xml"));
        response.setResponse(msg);

        logger.debug("message: "+message);
        msg.getBody().add(message);
        
        return msg;

      } catch (ActionProcessingException e) {
        throw new ActionProcessingDetailFaultException(FAULT_CODE_SERVER, "abc", "def");
      }

    }
    
    private String getFaultMessage(int code, String message) throws ActionProcessingException {
      // TODO: construct with JAXB
      try {
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage m = mf.createMessage();
        SOAPBody b = m.getSOAPBody();
        SOAPFault f = b.addFault();

        f.setFaultCode(FAULT_CODE_SERVER);
        f.setFaultString(message);
        SOAPElement d = f.addDetail().addChildElement("serviceFaultDetail", "cust", CUSTOMER_SERVICE_NS);

        d.addChildElement("code").addTextNode(String.valueOf(code));
        d.addChildElement("message").addTextNode(message);

        ByteArrayOutputStream sm = new ByteArrayOutputStream();
        m.writeTo(sm);
        return sm.toString();
      } catch (SOAPException e) {
        logger.error("failed to create SOAP message", e);
        throw new ActionProcessingException(e);
      } catch (IOException e) {
        logger.error("failed to marshal SOAP message", e);
        throw new ActionProcessingException(e);
      }
    }
    
    private String getSOAPMessage(JAXBElement<?> content) throws ActionProcessingException {
      try {
        // create SOAP message
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage m = mf.createMessage();
        SOAPBody b = m.getSOAPBody();

        // create DOM document
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document d = db.newDocument();

        // add content to SOAP message
        Marshaller ma = jaxbContext.createMarshaller();
        ma.marshal(content, d);
        b.addDocument(d);
        
        // marshal SOAP message
        ByteArrayOutputStream sm = new ByteArrayOutputStream();
        m.writeTo(sm);

        return sm.toString();
      } catch (SOAPException e) {
        logger.error("failed to create SOAP message", e);
        throw new ActionProcessingException(e);
      } catch (JAXBException e) {
        logger.error("JAXB marshaller failed", e);
        throw new ActionProcessingException(e);
      } catch (ParserConfigurationException e) {
        logger.error("document parser error", e);
        throw new ActionProcessingException(e);
      } catch (IOException e) {
        logger.error("failed to marshal SOAP message", e);
        throw new ActionProcessingException(e);
      }
    }
    
}
