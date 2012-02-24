package fi.koku.esb.services.customer;

import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;

/**
 * 
 * @author aspluma
 */
public class AuthorizationActionProcessor extends AbstractActionPipelineProcessor {

	public AuthorizationActionProcessor(ConfigTree config) {
		System.out.println("AuthorizationActionProcessor: config: "+config);
	}
	
	public Message process(Message msg) throws ActionProcessingException {
		System.out.println("AuthorizationActionProcessor: process: "+msg);
		if(msg != null)
			throw new ActionProcessingException("auth error");
		return null;
	}

}
