package com.meitao.model;

import java.io.Serializable;

public class SumCommodity
  implements Serializable
{
  private static final long serialVersionUID = -5344195080297082035L;
  private String commodityid;
  private String commodityname;
  private String sumquantity;
  private String sumsjweight;
  //private String sumjfweight;

  public String getCommodityname()
  {
    return this.commodityname;
  }
  public void setCommodityname(String commodityname) {
    this.commodityname = commodityname;
  }

  public String getCommodityid()
  {
    return this.commodityid;
  }
  public void setCommodityid(String commodityId) {
    this.commodityid = commodityId;
  }
  public String getSumquantity() {
    return this.sumquantity;
  }
  public void setSumquantity(String sumquantity) {
    this.sumquantity = sumquantity;
  }
  public String getSumsjweight() {
    return this.sumsjweight;
  }
  public void setSumsjweight(String sumsjweight) {
    this.sumsjweight = sumsjweight;
  }
  /*public String getSumjfweight() {
    return this.sumjfweight;
  }
  public void setSumjfweight(String sumjfweight) {
    this.sumjfweight = sumjfweight;
  }*/

  public String toString()
  {
    return "sumcommodity [commodityid=" + this.commodityid + ", sumquetity=" + this.sumquantity + ", sumsjweight=" + this.sumsjweight + /*", sumjfweight=" + this.sumjfweight +*/ "]";
  }

}