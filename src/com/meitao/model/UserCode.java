package com.meitao.model;

import java.io.Serializable;

public class UserCode
  implements Serializable
{
  private static final long serialVersionUID = 5291073068121025593L;
  private String id;
  private String usercode;
  private String state;

  public String getId()
  {
    return this.id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getUsercode() {
    return this.usercode;
  }
  public void setUsercode(String usercode) {
    this.usercode = usercode;
  }
  public String getState() {
    return this.state;
  }
  public void setState(String state) {
    this.state = state;
  }
}