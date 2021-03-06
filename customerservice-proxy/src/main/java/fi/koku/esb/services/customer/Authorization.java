/*
 * Copyright 2012 Ixonos Plc, Finland. All rights reserved.
 * 
 * This file is part of Kohti kumppanuutta.
 *
 * This file is licensed under GNU LGPL version 3.
 * Please see the 'license.txt' file in the root directory of the package you received.
 * If you did not receive a license, please contact the copyright holder
 * (kohtikumppanuutta@ixonos.com).
 *
 */
package fi.koku.esb.services.customer;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represent access control check input data and decision.
 * 
 * @author aspluma
 */
public class Authorization implements Serializable {
  private static final long serialVersionUID = 1L;
  private static final Logger logger = LoggerFactory.getLogger(Authorization.class);
  private String customerPic;
  private String userPic;
  private String operation;

  private String userRole;
  private List<String> customerOID;
  private List<String> userOID;
  
  private boolean allow;

  public Authorization() {
  }
  
  public String getCustomerPic() {
    return customerPic;
  }

  public void setCustomerPic(String customerPic) {
    this.customerPic = customerPic;
  }

  public String getUserPic() {
    return userPic;
  }

  public void setUserPic(String userPic) {
    this.userPic = userPic;
  }

  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  public String getUserRole() {
    return userRole;
  }

  public void setUserRole(String userRole) {
    this.userRole = userRole;
  }

  public List<String> getCustomerOID() {
    return customerOID;
  }

  public void setCustomerOID(List<String> customerOID) {
    this.customerOID = customerOID;
  }

  public List<String> getUserOID() {
    return userOID;
  }

  public void setUserOID(List<String> userOID) {
    this.userOID = userOID;
  }

  @Override
  public String toString() {
    return "Authorization [customerPic=" + customerPic + ", userPic=" + userPic + ", operation=" + operation
        + ", userRole=" + userRole + ", customerOID=" + customerOID + ", userOID=" + userOID + "]";
  }

  public boolean isAllow() {
    return allow;
  }

  public void setAllow(boolean allow) {
    this.allow = allow;
  }
  
  public boolean getUserInCustomerOrganization() {
    logger.debug("getUserInCustomerOrganization");
    if(userPic != null && customerPic !=null && userOID != null && customerOID != null) {
      for(String oid : userOID) {
        if(customerOID.contains(oid)) {
          logger.debug("match: "+oid);
          return true;
        }
      }
      
    }
    return false;
  }

}
