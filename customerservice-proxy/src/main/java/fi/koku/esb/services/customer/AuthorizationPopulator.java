/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.esb.services.customer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionLifecycleException;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static fi.koku.esb.services.customer.CustomerProxyConstants.*;

/**
 * Action for pre-populating Authorization info.
 * 
 * @author aspluma
 */
public class AuthorizationPopulator extends AbstractActionPipelineProcessor {
  private static final Logger logger = LoggerFactory.getLogger(AuthorizationPopulator.class);
  private static Map<String, List<String>> employeeOrgs = new HashMap<String, List<String>>();
  private static Map<String, List<String>> customerOrgs = new HashMap<String, List<String>>();
  @SuppressWarnings("unused")
  private static Map<String, String> userRoles = new HashMap<String, String>();
  

  public AuthorizationPopulator(ConfigTree config) {
  }
  
  @Override
  public void initialise() throws ActionLifecycleException {
    super.initialise();

    // employee and customer organizations would be fetched from a backend system.
    employeeOrgs.put("777777-7777", Arrays.asList(new String[]{"123", "234", "345"}));
    employeeOrgs.put("888888-8888", Arrays.asList(new String[]{"122", "233", "346"}));
    
    customerOrgs.put("111111-1111", Arrays.asList(new String[]{"123", "567"}));
    customerOrgs.put("222222-2222", Arrays.asList(new String[]{"124", "568"}));
    
    // the role here refers to the role in which the operation is being invoked, not the list of
    // all possible roles a user may have in an organization.
    // NB: role would need to be included as an input parameter to the service invocation.
//    userRoles.put("777777-7777", "emp_daycare_worker");
  }

  @Override
  public Message process(Message msg) throws ActionProcessingException {
    @SuppressWarnings("unchecked")
    Map<Object, Object> objs = (Map<Object, Object>) msg.getBody().get(MSG_BODY_PAYLOAD_LOCATION);
    Authorization a = (Authorization) objs.get(BEANID_AUTHORIZATION);
    
    String customerPic = a.getCustomerPic();
    String userPic = a.getUserPic();

    a.setCustomerOID(customerOrgs.get(customerPic));
    a.setUserOID(employeeOrgs.get(userPic));
    
    msg.getBody().add(BEANID_AUTHORIZATION, a);
    logger.debug("OK");
    
    return msg;
  }
  
  // TODO
  private class OrganizationService {
    public OrganizationService() {
    }

    public void getOrganizations() {
    }
  }

}
