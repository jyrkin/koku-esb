package fi.koku.esb.services.customer;

import static fi.koku.esb.services.customer.CustomerProxyConstants.BEANID_AUTHORIZATION;
import static fi.koku.esb.services.customer.CustomerProxyConstants.MSG_BODY_PAYLOAD_LOCATION;

import java.util.Map;

import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Action for enforcing access control decision.
 * 
 * @author aspluma
 */
public class AuthorizationEnforcer extends AbstractActionPipelineProcessor {
  private Logger logger = LoggerFactory.getLogger(AuthorizationEnforcer.class);
  
	public AuthorizationEnforcer(ConfigTree config) {
		logger.debug("AuthorizationActionProcessor: config: "+config);
	}
	
	public Message process(Message msg) throws ActionProcessingException {
		logger.debug("AuthorizationEnforcer: process: "+msg);
		
    @SuppressWarnings("unchecked")
    Map<Object, Object> objs = (Map<Object, Object>) msg.getBody().get(MSG_BODY_PAYLOAD_LOCATION);
    if(objs == null) {
      logger.error("payload not present in expected location");
      return null;
    }
    Authorization a = (Authorization) objs.get(BEANID_AUTHORIZATION);
    if(a == null) {
      logger.error("authorization object not present in expected location");
      return null;
    }

    if(!a.isAllow()) {
      logger.debug("deny");
      return null;
    }
    logger.debug("allow");
				
		return msg;
	}

}
