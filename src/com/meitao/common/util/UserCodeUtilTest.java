package com.meitao.common.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;



public class UserCodeUtilTest
{
  public static void main(String[] args)
  {
  //  UserCodeUtil u = new UserCodeUtil();
  //  System.out.println(UserCodeUtil.getUserCode());
	try{  
	  String xmlData="";//构造数据
	  String soapActionString="http://tempuri.org/GetMailInfo";
	  
	  URL url = new URL("http://218.56.39.110:9090/WebService/WebServiceClient.asmx");
	  //URL url = new URL("http://218.56.39.110/WebService/WebServiceClient.asmx");
	  HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
	  
	  
	  
	  String xmlstring = "";
	  xmlstring+="<?xml version=\"1.0\" encoding=\"utf-8\"?>";
	  
	  xmlstring+="<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">";
	  xmlstring+="<soap12:Body>";
	  xmlstring+="<GetMailInfo xmlns=\"http://tempuri.org/\">";
	  xmlstring+="<xmlData>";
	  //xmlstring+=createxmldata();
	  
	  xmlstring+="<?xml version=\"1.0\" encoding=\"utf-8\"?><XMLObject><DATASET><CUSTOMER_CODE>mtexpress</CUSTOMER_CODE><CUSTOMER_NAME>mtexpress</CUSTOMER_NAME><ORDER_ID>MT00009409NY</ORDER_ID><RECEIVEMAN>收件人name1</RECEIVEMAN><RECEIVEPROVINCE>广西</RECEIVEPROVINCE><RECEIVECITY>南宁</RECEIVECITY><RECEIVECOUNTRY>秀峰区</RECEIVECOUNTRY><RECEIVEMANADDRESS>民族大道奇峰小区105号</RECEIVEMANADDRESS><RECEIVEMANPHONE>18707730557</RECEIVEMANPHONE><SENDMAN>发件人name1</SENDMAN><SENDPROVINCE>广东</SENDPROVINCE><SENDCITY>广州</SENDCITY><SENDCOUNTRY>沙门区</SENDCOUNTRY><SENDMANADDRESS>珠江大道102号</SENDMANADDRESS><SENDMANPHONE>6266936743</SENDMANPHONE><ITEMNAME>衣服100件</ITEMNAME></DATASET></XMLObject>";
	  xmlstring+="</xmlData>";
	  xmlstring+="</GetMailInfo>";
	  xmlstring+="</soap12:Body>";
	  xmlstring+="</soap12:Envelope>";
	  
	  byte[] buf = new byte[(int) xmlstring.length()];
      buf=xmlstring.getBytes();
      
      httpConn.setRequestProperty("Content-Length", String.valueOf(buf.length));
      httpConn.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
      httpConn.setRequestProperty("SOAPAction", soapActionString);
      httpConn.setRequestMethod("POST");
      //httpConn.setRequestProperty("Host", "218.56.39.110");
      
      httpConn.setDoOutput(true);
      httpConn.setDoInput(true);
      OutputStream out = httpConn.getOutputStream();
      out.write(buf);
      out.close();

      byte[] datas=readInputStream(httpConn.getInputStream());
      
      String result=new String(datas);
      DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
      DocumentBuilder builder=factory.newDocumentBuilder();
      SAXReader reader = new SAXReader();
     // String file = Resource.init("/WEB-INF/toolbox.xml"); // 返回绝对路径
      Document document = null;
      document=DocumentHelper.parseText(result);
      Element root = document.getRootElement();
      System.out.println("result:" + root.getName());
	}catch(Exception e)
	{
		e.printStackTrace();
		System.out.println("操作发生异常!");
	}
 }
  /**
   * 从输入流中读取数据
   * @param inStream
   * @return
   * @throws Exception
   */
  public static byte[] readInputStream(InputStream inStream) throws Exception{
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int len = 0;
      while( (len = inStream.read(buffer)) !=-1 ){
          outStream.write(buffer, 0, len);
      }
      byte[] data = outStream.toByteArray();//网页的二进制数据
      outStream.close();
      inStream.close();
      return data;
  }
  
  public static String createxmldata()
  {
	  String str="";
	  str+="<?xml version=\"1.0\" encoding=\"utf-8\"?>";
	  str+="<XMLObject>";
	  str+="<DATASET>";
	  str+="<CUSTOMER_CODE>"+"mtexpress"+"</CUSTOMER_CODE>";//组织机构代码
	  str+="<CUSTOMER_NAME>"+"mtexpress"+"</CUSTOMER_NAME>";//公司全称
	  str+="<ORDER_ID>"+"MT00009409NY"+"</ORDER_ID>";//订单号
	  str+="<RECEIVEMAN>"+"收件人name1"+"</RECEIVEMAN>";//收件人姓名
	  str+="<RECEIVEPROVINCE>"+"广西"+"</RECEIVEPROVINCE>";//收件人所在省
	  str+="<RECEIVECITY>"+"南宁"+"</RECEIVECITY>";//收件人所在市
	  str+="<RECEIVECOUNTRY>"+"秀峰区"+"</RECEIVECOUNTRY>";//收件人所在县区
	  str+="<RECEIVEMANADDRESS>"+"民族大道奇峰小区105号"+"</RECEIVEMANADDRESS>";//收件人所在地址
	  str+="<RECEIVEMANPHONE>"+"18707730557"+"</RECEIVEMANPHONE>";//收件人电话 
	  str+="<SENDMAN>"+"发件人name1"+"</SENDMAN>";//发件人姓名
	  str+="<SENDPROVINCE>"+"广东"+"</SENDPROVINCE>";//发件人所在省
	  str+="<SENDCITY>"+"广州"+"</SENDCITY>";//发件人所在市
	  str+="<SENDCOUNTRY>"+"沙门区"+"</SENDCOUNTRY>";//发件人所在县区
	  str+="<SENDMANADDRESS>"+"珠江大道102号"+"</SENDMANADDRESS>";//发件人详细地址
	  str+="<SENDMANPHONE>"+"6266936743"+"</SENDMANPHONE>";//发件人电话
	  str+="<ITEMNAME>"+"衣服100件"+"</ITEMNAME>";//商品明细
	  str+="</DATASET>";
	  str+="</XMLObject>";
	 
	  return str;
  }
}