package com.google.gwt.project.climateproject.server;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.users.User;

/**
 * Class responsible for persisting users' data sets on the server.
 * @author Rastislav Ondrasek
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class DataSet {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;
  @Persistent
  private User user;
  @Persistent
  private Date createDate;
  @Persistent
  private String[] data;
  	
  public DataSet() {
    this.createDate = new Date();
  }

  public DataSet(User user, String[] data) {
    this();
    this.user = user;
    this.data = data;
  }

  public Long getId() {
    return this.id;
  }

  public User getUser() {
    return this.user;
  }

  public Date getCreateDate() {
    return this.createDate;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String[] getDataSet() {
  	return this.data;
  }
}
