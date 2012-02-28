package fi.koku.esb.services.customer;

import java.io.Serializable;
import java.util.List;

/**
 * Represent access control check input data and decision.
 * 
 * @author aspluma
 */
public class Authorization implements Serializable {
  private static final long serialVersionUID = 1L;
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

}
