package com.meitao.model;

import java.io.Serializable;

public class OrderBudget
  implements Serializable
{
  private static final long serialVersionUID = -994484390060238713L;
  private String weight;
  private String length;
  private String high;
  private String width;

  public String getWeight()
  {
    return this.weight;
  }
  public void setWeight(String weight) {
    this.weight = weight;
  }
  public String getLength() {
    return this.length;
  }
  public void setLength(String length) {
    this.length = length;
  }
  public String getHigh() {
    return this.high;
  }
  public void setHigh(String high) {
    this.high = high;
  }
  public String getWidth() {
    return this.width;
  }
  public void setWidth(String width) {
    this.width = width;
  }
}