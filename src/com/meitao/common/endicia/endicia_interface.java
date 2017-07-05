package com.meitao.common.endicia;

import com.meitao.model.EndiciaLabel;
import com.meitao.model.Endicial_arg;
import com.meitao.model.ExportEndiciaResult;
import com.meitao.model.ResponseObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;






















import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class endicia_interface {
	
	//计算价格
	String S_CalculatePostageRate_RequesterID[]={"Body","CalculatePostageRate","PostageRateRequest","RequesterID"};
	String S_CalculatePostageRate_AccountID[]={"Body","CalculatePostageRate","PostageRateRequest","CertifiedIntermediary","AccountID"};
	String S_CalculatePostageRate_PassPhrase[]={"Body","CalculatePostageRate","PostageRateRequest","CertifiedIntermediary","PassPhrase"};
	String S_CalculatePostageRate_MailClass[]={"Body","CalculatePostageRate","PostageRateRequest","MailClass"};
	String S_CalculatePostageRate_WeightOz[]={"Body","CalculatePostageRate","PostageRateRequest","WeightOz"};
	String S_CalculatePostageRate_Weight[]={"Body","CalculatePostageRate","PostageRateRequest","Weight"};
	String S_CalculatePostageRate_FromPostalCode[]={"Body","CalculatePostageRate","PostageRateRequest","FromPostalCode"};
	String S_CalculatePostageRate_ToCountry[]={"Body","CalculatePostageRate","PostageRateRequest","ToCountry"};
	String S_CalculatePostageRate_ToCountryCode[]={"Body","CalculatePostageRate","PostageRateRequest","ToCountryCode"};
	String S_CalculatePostageRate_ToPostalCode[]={"Body","CalculatePostageRate","PostageRateRequest","ToPostalCode"};	
	
	
	String R_CalculatePostageRateResponse_Status[]={"Body","CalculatePostageRateResponse","PostageRateResponse","Status"};
	String R_CalculatePostageRateResponse_ErrorMessage[]={"Body","CalculatePostageRateResponse","PostageRateResponse","ErrorMessage"};//查找返回错误价格的原因
	
	
	String R_CalculatePostageRateResponse_PostagePrice[]={"Body","CalculatePostageRateResponse","PostageRateResponse","PostagePrice"};
	
	
	//label请求值
	String S_GetPostageLabel_LabelRequest[]={"Body","GetPostageLabel","LabelRequest"};
	String S_GetPostageLabel_RequesterID[]={"Body","GetPostageLabel","LabelRequest","RequesterID"};
	String S_GetPostageLabel_AccountID[]={"Body","GetPostageLabel","LabelRequest","AccountID"};
	String S_GetPostageLabel_PassPhrase[]={"Body","GetPostageLabel","LabelRequest","PassPhrase"};
	String S_GetPostageLabel_MailClass[]={"Body","GetPostageLabel","LabelRequest","MailClass"};
	String S_GetPostageLabel_DateAdvance[]={"Body","GetPostageLabel","LabelRequest","DateAdvance"};
	String S_GetPostageLabel_WeightOz[]={"Body","GetPostageLabel","LabelRequest","WeightOz"};
	String S_GetPostageLabel_Quantity[]={"Body","GetPostageLabel","LabelRequest","Quantity"};
	
	String S_GetPostageLabel_Weight[]={"Body","GetPostageLabel","LabelRequest","Weight"};
	String S_GetPostageLabel_MailpieceShape[]={"Body","GetPostageLabel","LabelRequest","MailpieceShape"};
	String S_GetPostageLabel_Value[]={"Body","GetPostageLabel","LabelRequest","Value"};
	String S_GetPostageLabel_PartnerTransactionID[]={"Body","GetPostageLabel","LabelRequest","PartnerTransactionID"};
	String S_GetPostageLabel_Description[]={"Body","GetPostageLabel","LabelRequest","Description"};
	
	String S_GetPostageLabel_ToName[]={"Body","GetPostageLabel","LabelRequest","ToName"};
	String S_GetPostageLabel_ToCompany[]={"Body","GetPostageLabel","LabelRequest","ToCompany"};
	String S_GetPostageLabel_ToAddress1[]={"Body","GetPostageLabel","LabelRequest","ToAddress1"};
	String S_GetPostageLabel_ToCity[]={"Body","GetPostageLabel","LabelRequest","ToCity"};
	String S_GetPostageLabel_ToState[]={"Body","GetPostageLabel","LabelRequest","ToState"};
	String S_GetPostageLabel_ToCountry[]={"Body","GetPostageLabel","LabelRequest","ToCountry"};
	String S_GetPostageLabel_ToCountryCode[]={"Body","GetPostageLabel","LabelRequest","ToCountryCode"};
	String S_GetPostageLabel_ToPostalCode[]={"Body","GetPostageLabel","LabelRequest","ToPostalCode"};
	String S_GetPostageLabel_ToPhone[]={"Body","GetPostageLabel","LabelRequest","ToPhone"};
	String S_GetPostageLabel_FromName[]={"Body","GetPostageLabel","LabelRequest","FromName"};
	String S_GetPostageLabel_ReturnAddress1[]={"Body","GetPostageLabel","LabelRequest","ReturnAddress1"};
	String S_GetPostageLabel_FromCity[]={"Body","GetPostageLabel","LabelRequest","FromCity"};
	String S_GetPostageLabel_FromState[]={"Body","GetPostageLabel","LabelRequest","FromState"};
	String S_GetPostageLabel_FromPostalCode[]={"Body","GetPostageLabel","LabelRequest","FromPostalCode"};
	
	String S_GetPostageLabel_FromZIP4[]={"Body","GetPostageLabel","LabelRequest","FromZIP4"};
	String S_GetPostageLabel_FromPhone[]={"Body","GetPostageLabel","LabelRequest","FromPhone"};
	
	
	//定义返回的字符
	String R_GetPostageLabelResponse_Status[]={"Body","GetPostageLabelResponse","LabelRequestResponse","Status"};
	String R_GetPostageLabelResponse_TrackingNumber[]={"Body","GetPostageLabelResponse","LabelRequestResponse","TrackingNumber"};
	String R_GetPostageLabelResponse_ErrorMessage[]={"Body","GetPostageLabelResponse","LabelRequestResponse","ErrorMessage"};
	
	String R_GetPostageLabelResponse_Base64LabelImage[]={"Body","GetPostageLabelResponse","LabelRequestResponse","Base64LabelImage"};
	
	String R_GetPostageLabelResponse_Image[]={"Body","GetPostageLabelResponse","LabelRequestResponse","Label","Image"};//分多部分返回的图片
	
	String R_GetPostageLabelResponse_PostagePrice[]={"Body","GetPostageLabelResponse","LabelRequestResponse","PostagePrice"};
	 
	public String checklabelprice(Endicial_arg info,EndiciaLabel label) throws Exception
	{
		FileInputStream in=null;
		try {
	        //String urlString = "http://elstestserver.endicia.com/LabelService/EwsLabelService.asmx";
			String urlString = info.getApiUrl();
	        String xml = endicia_interface.class.getClassLoader().getResource("com/meitao/common/endicia/checklabelprice.xml").getFile();
	        String xmlFile=replace(xml, "", "").getPath();
	        //String soapActionString = "www.envmgr.com/LabelService/ChangePassPhrase";
	       // String soapActionString = "www.envmgr.com/LabelService/GetPostageLabel";
	        String soapActionString = "www.envmgr.com/LabelService/CalculatePostageRate";
	        
	        URL url = new URL(urlString);
	        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
	        File fileToSend = new File(xmlFile);
	        
	        byte[] buf = new byte[(int) fileToSend.length()];
	        in=new FileInputStream(xmlFile);
	       int k= in.read(buf);
	        
	        String result1=new String(buf);
	        //System.out.println("result:" + result1);
	        buf=result1.getBytes();
	        
	        Document document1 = null;
	        document1=DocumentHelper.parseText(result1);
	        Element root1 = document1.getRootElement();
	       
	        if(!parse_xml_repalce_text(root1, S_CalculatePostageRate_RequesterID,info.getRequesterId())){return "";}
	        if(!parse_xml_repalce_text(root1, S_CalculatePostageRate_AccountID,info.getAccountId())){return "";}
	        if(!parse_xml_repalce_text(root1, S_CalculatePostageRate_PassPhrase,info.getPassPhrase())){return "";}
	        if(!parse_xml_repalce_text(root1, S_CalculatePostageRate_MailClass,label.getMailClass())){return "";}
	        if(!parse_xml_repalce_text(root1, S_CalculatePostageRate_WeightOz,label.getWeightOz())){return "";}
	       // if(!parse_xml_repalce_text(root1, S_CalculatePostageRate_Weight,label.getWeight())){return "";}
	        if(!parse_xml_repalce_text(root1, S_CalculatePostageRate_FromPostalCode,label.getFromPostalCode())){return "";}
	        if(!parse_xml_repalce_text(root1, S_CalculatePostageRate_ToCountry,label.getToCountry())){return "";}
	        if(!parse_xml_repalce_text(root1, S_CalculatePostageRate_ToCountryCode,label.getToCountryCode())){return "";}
	        if(!parse_xml_repalce_text(root1, S_CalculatePostageRate_ToPostalCode,label.getToPostalCode())){return "";}
	        
	        
	       // if(parse_xml_repalce_text(root1, S_CalculatePostageRate_RequesterID,info.getRequesterID())==false);
	       /* {
	        	return "";
	        }*/
	        
	        java.io.StringWriter out1 = new java.io.StringWriter();
	        XMLWriter xw = new XMLWriter (out1, new OutputFormat (" ", true, "utf-8"));
	        xw.write(document1);
	        String xmlstring = out1.toString();
	        
	        buf=xmlstring.getBytes();
	        
	        httpConn.setRequestProperty("Content-Length", String.valueOf(buf.length));
	        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
	        httpConn.setRequestProperty("soapActionString", soapActionString);
	        httpConn.setRequestMethod("POST");
	        httpConn.setDoOutput(true);
	        httpConn.setDoInput(true);
	        OutputStream out = httpConn.getOutputStream();
	        out.write(buf);
	        out.close();
	
	        byte[] datas=readInputStream(httpConn.getInputStream());
	        
	        String result=new String(datas);
	        //DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
	       // DocumentBuilder builder=factory.newDocumentBuilder();
	      //  SAXReader reader = new SAXReader();
	       // String file = Resource.init("/WEB-INF/toolbox.xml"); // 返回绝对路径
	        Document document = null;
	        document=DocumentHelper.parseText(result);
	        Element root = document.getRootElement();
	       // System.out.println("result:" + root.getName());
	       
	      
	        String aaaa="";
	        List<String> list=parse_xml_str(root,R_CalculatePostageRateResponse_Status); 
	        for(String text:list)
	        {
	        	if(text!=null)
	        	{
	        		aaaa=aaaa+text;
	        	}
	        }
	        
	        if(aaaa!="")
	        {
	        	if(aaaa.equalsIgnoreCase("0"))
	        	{
	        		//List<String> list1=parse_xml_str(root,R_CalculatePostageRateResponse_Status);
	        		List<String> list1=parse_xml_get_attr(root, R_CalculatePostageRateResponse_PostagePrice,"TotalAmount");
	        		if((list1!=null)&&(list1.size()==1))
	        		{
	        			return list1.get(0);
	        		}
	        		else
	        		{
	        			List<String> error=parse_xml_str(root,R_CalculatePostageRateResponse_ErrorMessage); 
	        			String error1="";
	        			if((error!=null)&&(error.size()==1))
	        			{
	        				error1=error.get(0);
	        			}
	        			
	        			throw new Exception(error1);//抛出错误原因
	        		}
	        	}
	        	else
	        	{

        			List<String> error=parse_xml_str(root,R_CalculatePostageRateResponse_ErrorMessage); 
        			String error1="";
        			if((error!=null)&&(error.size()==1))
        			{
        				error1=error.get(0);
        			}
        			
        			throw new Exception(error1);//抛出错误原因
        		
	        	}
	        }
	        else
	        {

    			List<String> error=parse_xml_str(root,R_CalculatePostageRateResponse_ErrorMessage); 
    			String error1="";
    			if((error!=null)&&(error.size()==1))
    			{
    				error1=error.get(0);
    			}
    			
    			throw new Exception(error1);//抛出错误原因
    		
	        }
	        
	     /*   datas=DatatypeConverter.parseBase64Binary(aaaa);
	        File imageFile = new File("BeautyGirl3.png"); 
	        FileOutputStream outStream = new FileOutputStream(imageFile);
	        outStream.write(datas);
	        outStream.close();
	       
	        //打印返回结果
	        System.out.println("result:" + result);*/
	        //return "";
	}
	catch(Exception e)
	{
		throw new Exception(e.getMessage());
	}
		finally {
			//最后关闭流
			try {
				
				if (in != null) {
					
					in.close();
				}
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}

		}    
		
		
	}
	
	
	
	
	
	public ResponseObject<Map<String, String>> printLabel(Endicialinfo info,EndiciaLabel label)
	{
		ResponseObject<Map<String, String>> obj=null;
		
		try {
			
			
	        //String urlString = "http://elstestserver.endicia.com/LabelService/EwsLabelService.asmx";
			String urlString = info.getAPI_url();
	        String xml = endicia_interface.class.getClassLoader().getResource("com/meitao/common/endicia/PostageLabel.xml").getFile();
	        String xmlFile=replace(xml, "", "").getPath();
	        //String soapActionString = "www.envmgr.com/LabelService/ChangePassPhrase";
	        String soapActionString = "www.envmgr.com/LabelService/GetPostageLabel";
	       // String soapActionString = "www.envmgr.com/LabelService/CalculatePostageRate";
	        
	        URL url = new URL(urlString);
	        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
	        File fileToSend = new File(xmlFile);
	        
	        byte[] buf = new byte[(int) fileToSend.length()];
	        
	        new FileInputStream(xmlFile).read(buf);
	        
	        String result1=new String(buf);
	        System.out.println("result:" + result1);
	        buf=result1.getBytes();
	        
	        Document document1 = null;
	        document1=DocumentHelper.parseText(result1);
	        Element root1 = document1.getRootElement();
	       
	        
	        if(!parse_xml_repalce_attr(root1, S_GetPostageLabel_LabelRequest,"Test",info.getTest())){return obj;}
	        if(!parse_xml_repalce_attr(root1, S_GetPostageLabel_LabelRequest,"LabelType",label.getLabelType())){return obj;}
	        if(!parse_xml_repalce_attr(root1, S_GetPostageLabel_LabelRequest,"LabelSize",info.getLabelSize())){return obj;}
	        if(!parse_xml_repalce_attr(root1, S_GetPostageLabel_LabelRequest,"ImageFormat",info.getImageFormat())){return obj;}
	        
	        
	       
	        //LabelRequestResponse
	        
	        
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_RequesterID,info.getRequesterID())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_AccountID,info.getAccountID())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_PassPhrase,info.getPassPhrase())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_MailClass,label.getMailClass())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_DateAdvance,label.getDateAdvance())){return obj;}
	        
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_WeightOz,label.getWeightOz())){return obj;}
	        
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_Quantity,label.getQuantity())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_Weight,label.getWeight())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_MailpieceShape,label.getMailpieceShape())){return obj;}
	        
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_Value,label.getValue())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_PartnerTransactionID,"1111")){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_Description,label.getDescription())){return obj;}
	        
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_ToName,label.getToName())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_ToCompany,label.getToCompany())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_ToAddress1,label.getToAddress1())){return obj;}
	       
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_ToCity,label.getToCity())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_ToState,label.getToState())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_ToCountry,label.getToCountry())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_ToCountryCode,label.getToCountryCode())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_ToPostalCode,label.getToPostalCode())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_ToPhone,label.getToPhone())){return obj;}
	        
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_FromName,label.getFromName())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_ReturnAddress1,label.getReturnAddress1())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_FromCity,label.getFromCity())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_FromState,label.getFromState())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_FromPostalCode,label.getFromPostalCode())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_FromZIP4,label.getFromZIP4())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_FromPhone,label.getFromPhone())){return obj;}
	      
	        
	        java.io.StringWriter out1 = new java.io.StringWriter();
	        XMLWriter xw = new XMLWriter (out1, new OutputFormat (" ", true, "utf-8"));
	        xw.write(document1);
	        String xmlstring = out1.toString();
	        
	        buf=xmlstring.getBytes();
	        
	        httpConn.setRequestProperty("Content-Length", String.valueOf(buf.length));
	        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
	        httpConn.setRequestProperty("soapActionString", soapActionString);
	        httpConn.setRequestMethod("POST");
	        httpConn.setDoOutput(true);
	        httpConn.setDoInput(true);
	        OutputStream out = httpConn.getOutputStream();
	        out.write(buf);
	        out.close();
	
	        byte[] datas=readInputStream(httpConn.getInputStream());
	        
	        String result=new String(datas);
	        System.out.println(result);
	        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder=factory.newDocumentBuilder();
	        SAXReader reader = new SAXReader();
	       // String file = Resource.init("/WEB-INF/toolbox.xml"); // 返回绝对路径
	        Document document = null;
	        document=DocumentHelper.parseText(result);
	        Element root = document.getRootElement();
	        System.out.println("result:" + root.getName());
	       
	      
	        String aaaa="";
	        List<String> list=parse_xml_str(root,R_GetPostageLabelResponse_Status); 
	        for(String text:list)
	        {
	        	if(text!=null)
	        	{
	        		aaaa=aaaa+text;
	        	}
	        }
	        
	        
	        
	        
	        
	        if(aaaa!="")
	        {
	        	if(aaaa.equalsIgnoreCase("0"))
	        	{
	        		//List<String> list1=parse_xml_str(root,R_CalculatePostageRateResponse_Status);
	        		List<String> list1=parse_xml_get_attr(root, R_GetPostageLabelResponse_PostagePrice,"TotalAmount");
	        		
	        		
	        		Map<String, String> map = new HashMap<String, String>();
	        		
					map.put("TotalAmount", list1.get(0));
					
					//String R_GetPostageLabelResponse_Base64LabelImage[]={"Body","GetPostageLabelResponse ","LabelRequestResponse","Base64LabelImage"};
					
					//String R_GetPostageLabelResponse_Label[]={"Body","GetPostageLabelResponse ","LabelRequestResponse","Label"};//分多部分返回的图片
					aaaa="";
					if(label.getLabelType().equalsIgnoreCase("International"))
					{
						List<String> image=parse_xml_str(root,R_GetPostageLabelResponse_Image);
							
							for(String text:image)
					        {
					        	if(text!=null)
					        	{
					        		aaaa=aaaa+text;
					        	}
					        }
						
					}
					else
					{
						List<String> image=parse_xml_str(root,R_GetPostageLabelResponse_Base64LabelImage);
						aaaa="";
						for(String text:image)
				        {
				        	if(text!=null)
				        	{
				        		aaaa=aaaa+text;
				        	}
				        }
					}
					map.put("image", aaaa);
					
					obj=new ResponseObject<Map<String, String>>(
							ResponseCode.SUCCESS_CODE);
					
					obj.setData(map);
					
	        	}
	        	else
	        	{
	        		Map<String, String> map = new HashMap<String, String>();
	        		List<String> ErrorMessage=parse_xml_str(root,R_GetPostageLabelResponse_ErrorMessage); 
	        		aaaa="";
	    	        for(String text:ErrorMessage)
	    	        {
	    	        	if(text!=null)
	    	        	{
	    	        		aaaa=aaaa+text;
	    	        	}
	    	        }
	    	        map.put("ErrorMessage", aaaa);
	    	        obj=new ResponseObject<Map<String, String>>(
							ResponseCode.PARAMETER_ERROR);
	    	        obj.setData(map);
	        		return obj;
	        	}
	        }
	        else
	        {
	        	return obj;
	        }
	        
	     /*   datas=DatatypeConverter.parseBase64Binary(aaaa);
	        File imageFile = new File("BeautyGirl3.png"); 
	        FileOutputStream outStream = new FileOutputStream(imageFile);
	        outStream.write(datas);
	        outStream.close();
	       
	        //打印返回结果
	        System.out.println("result:" + result);*/
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	    
		
		return obj;
	}
	
    /**
     * 文件内容替换
     * 
     * @param inFileName 源文件
     * @param from
     * @param to
     * @return 返回替换后文件
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public static File replace(String inFileName, String from, String to)
            throws IOException, UnsupportedEncodingException {
        File inFile = new File(inFileName);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(inFile), "utf-8"));
        File outFile = new File(inFile + ".tmp");
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outFile), "utf-8")));
        String reading;
        while ((reading = in.readLine()) != null) {
            out.println(reading.replaceAll(from, to));
        }
        out.close();
        in.close();
        //infile.delete(); //删除源文件
        //outfile.renameTo(infile); //对临时文件重命名
        return outFile;
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
    
	public List<String> parse_xml_str(Element root, String[] Nodenames) {
		List<String> array = new ArrayList<String>();
		if ((Nodenames == null) || (Nodenames.length < 1)) {
			return null;
		} else {

			int flag = 0;
			List<Element> nodes = null;
			for (int i = 0; i < Nodenames.length; i++) {

				int a = 0;
				nodes = root.elements(Nodenames[i]);
				System.out.println("result:" + root.getName());
				for (Element node : nodes) {
					System.out.println("result:" + node.getName());
					flag = 0;
					if (Nodenames[i].equals(node.getName())) {

						flag = 1;
						if (i == Nodenames.length - 1) {
							array.add(node.getTextTrim());
							continue;
						} else {

							root = node;
							nodes = node.elements();
							System.out.println("result:" + node.getName());
							break;
						}

					}

				}
				if (flag == 0) {
					return null;
				}

			}

			return array;
		}

	}
	
	public Boolean parse_xml_repalce_text(Element root, String[] Nodenames,String text) {
		List<String> array = new ArrayList<String>();
		if ((Nodenames == null) || (Nodenames.length < 1)) {
			return false;
		} else {

			int flag = 0;
			List<Element> nodes = null;
			for (int i = 0; i < Nodenames.length; i++) {

				int a = 0;
				nodes = root.elements(Nodenames[i]);
				//System.out.println("result:" + root.getName());
				for (Element node : nodes) {
					//System.out.println("result:" + node.getName());
					flag = 0;
					if (Nodenames[i].equals(node.getName())) {

						flag = 1;
						if (i == Nodenames.length - 1) {
							node.setText(text);
							continue;
						} else {

							root = node;
							nodes = node.elements();
							//System.out.println("result:" + node.getName());
							break;
						}

					}

				}
				if (flag == 0) {
					return false;
				}

			}

			return true;
		}

	}
	
	public List<String> parse_xml_get_attr(Element root, String[] Nodenames,String attr) {
		List<String> array = new ArrayList<String>();
		if ((Nodenames == null) || (Nodenames.length < 1)) {
			return null;
		} else {

			int flag = 0;
			List<Element> nodes = null;
			for (int i = 0; i < Nodenames.length; i++) {

				int a = 0;
				nodes = root.elements(Nodenames[i]);
				System.out.println("result:" + root.getName());
				for (Element node : nodes) {
					System.out.println("result:" + node.getName());
					flag = 0;
					if (Nodenames[i].equals(node.getName())) {

						flag = 1;
						if (i == Nodenames.length - 1) {
							
							array.add(node.attributeValue(attr));
							continue;
						} else {

							root = node;
							nodes = node.elements();
							System.out.println("result:" + node.getName());
							break;
						}

					}

				}
				if (flag == 0) {
					return null;
				}

			}

			return array;
		}

	}

	public Boolean parse_xml_repalce_attr(Element root, String[] Nodenames,String attr,String value) {
		List<String> array = new ArrayList<String>();
		if ((Nodenames == null) || (Nodenames.length < 1)) {
			return false;
		} else {

			int flag = 0;
			List<Element> nodes = null;
			for (int i = 0; i < Nodenames.length; i++) {

				int a = 0;
				nodes = root.elements(Nodenames[i]);
				//System.out.println("result:" + root.getName());
				for (Element node : nodes) {
					//System.out.println("result:" + node.getName());
					flag = 0;
					if (Nodenames[i].equals(node.getName())) {

						flag = 1;
						if (i == Nodenames.length - 1) {
							node.addAttribute(attr, value);
							continue;
						} else {

							root = node;
							nodes = node.elements();
							//System.out.println("result:" + node.getName());
							break;
						}

					}

				}
				if (flag == 0) {
					return false;
				}

			}

			return true;
		}

	}
	
	
	
	
	public ResponseObject<ExportEndiciaResult> printLabel_manyimage(Endicial_arg info,EndiciaLabel label)
	{
		ResponseObject<ExportEndiciaResult> obj=null;
		FileInputStream in=null;
		try {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyyMMddHHmmss");
			String timestr = sdf.format(date);
			
	        //String urlString = "http://elstestserver.endicia.com/LabelService/EwsLabelService.asmx";
			String urlString = info.getApiUrl();
	        String xml = endicia_interface.class.getClassLoader().getResource("com/meitao/common/endicia/PostageLabel.xml").getFile();
	        String xmlFile=replace(xml, "", "").getPath();
	        //String soapActionString = "www.envmgr.com/LabelService/ChangePassPhrase";
	        String soapActionString = "www.envmgr.com/LabelService/GetPostageLabel";
	       // String soapActionString = "www.envmgr.com/LabelService/CalculatePostageRate";
	        
	        URL url = new URL(urlString);
	        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
	        File fileToSend = new File(xmlFile);
	        
	        byte[] buf = new byte[(int) fileToSend.length()];
	        in=new FileInputStream(xmlFile);
	       int k=in.read(buf);
	        
	        String result1=new String(buf);
	       // System.out.println("result:" + result1);
	        buf=result1.getBytes();
	        
	        Document document1 = null;
	        document1=DocumentHelper.parseText(result1);
	        Element root1 = document1.getRootElement();
	       
	        
	        if(!parse_xml_repalce_attr(root1, S_GetPostageLabel_LabelRequest,"Test",info.getTest())){return obj;}
	        if(!parse_xml_repalce_attr(root1, S_GetPostageLabel_LabelRequest,"LabelType",label.getLabelType())){return obj;}
	        if(!parse_xml_repalce_attr(root1, S_GetPostageLabel_LabelRequest,"LabelSize",info.getLabelSize())){return obj;}
	        if(!parse_xml_repalce_attr(root1, S_GetPostageLabel_LabelRequest,"ImageFormat",info.getImageFormat())){return obj;}
	        
	        
	       
	        //LabelRequestResponse
	        
	        
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_RequesterID,info.getRequesterId())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_AccountID,info.getAccountId())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_PassPhrase,info.getPassPhrase())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_MailClass,label.getMailClass())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_DateAdvance,label.getDateAdvance())){return obj;}
	        
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_WeightOz,label.getWeightOz())){return obj;}
	        
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_Quantity,label.getQuantity())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_Weight,label.getWeight())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_MailpieceShape,label.getMailpieceShape())){return obj;}
	        
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_Value,label.getValue())){return obj;}
	       // if(!parse_xml_repalce_text(root1, S_GetPostageLabel_PartnerTransactionID,"1111")){return obj;}
	        if(StringUtil.isEmpty(label.getUserId()))
	        {
	        	if(!parse_xml_repalce_text(root1, S_GetPostageLabel_PartnerTransactionID,timestr)){return obj;}
	        }
	        else
	        {
	        	if(!parse_xml_repalce_text(root1, S_GetPostageLabel_PartnerTransactionID,label.getUserId()+"-"+timestr)){return obj;}
	        }
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_Description,label.getDescription())){return obj;}
	        
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_ToName,label.getToName())){return obj;}
	        //if(!parse_xml_repalce_text(root1, S_GetPostageLabel_ToCompany,label.getToCompany())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_ToAddress1,label.getToAddress1())){return obj;}
	       
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_ToCity,label.getToCity())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_ToState,label.getToState())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_ToCountry,label.getToCountry())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_ToCountryCode,label.getToCountryCode())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_ToPostalCode,label.getToPostalCode())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_ToPhone,label.getToPhone())){return obj;}
	        
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_FromName,label.getFromName())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_ReturnAddress1,label.getReturnAddress1())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_FromCity,label.getFromCity())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_FromState,label.getFromState())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_FromPostalCode,label.getFromPostalCode())){return obj;}
	        //if(!parse_xml_repalce_text(root1, S_GetPostageLabel_FromZIP4,label.getFromZIP4())){return obj;}
	        if(!parse_xml_repalce_text(root1, S_GetPostageLabel_FromPhone,label.getFromPhone())){return obj;}
	      
	        
	        java.io.StringWriter out1 = new java.io.StringWriter();
	        XMLWriter xw = new XMLWriter (out1, new OutputFormat (" ", true, "utf-8"));
	        xw.write(document1);
	        String xmlstring = out1.toString();
	        
	        buf=xmlstring.getBytes();
	        
	        httpConn.setRequestProperty("Content-Length", String.valueOf(buf.length));
	        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
	        httpConn.setRequestProperty("soapActionString", soapActionString);
	        httpConn.setRequestMethod("POST");
	        httpConn.setDoOutput(true);
	        httpConn.setDoInput(true);
	        OutputStream out = httpConn.getOutputStream();
	        out.write(buf);
	        out.close();
	
	        byte[] datas=readInputStream(httpConn.getInputStream());
	        
	        String result=new String(datas);
	       // System.out.println(result);
	      //  DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
	       // DocumentBuilder builder=factory.newDocumentBuilder();
	     //   SAXReader reader = new SAXReader();
	       // String file = Resource.init("/WEB-INF/toolbox.xml"); // 返回绝对路径
	        Document document = null;
	        document=DocumentHelper.parseText(result);
	        Element root = document.getRootElement();
	       // System.out.println("result:" + root.getName());
	       
	        //导出返回结果
	        ExportEndiciaResult exportr=new ExportEndiciaResult();
	      
	        String aaaa="";
	        List<String> list=parse_xml_str(root,R_GetPostageLabelResponse_Status); 
	        for(String text:list)
	        {
	        	if(text!=null)
	        	{
	        		aaaa=aaaa+text;
	        	}
	        }
	        
	        if(aaaa!="")
	        {
	        	if(aaaa.equalsIgnoreCase("0"))
	        	{
	        		//List<String> list1=parse_xml_str(root,R_CalculatePostageRateResponse_Status);
	        		List<String> list1=parse_xml_get_attr(root, R_GetPostageLabelResponse_PostagePrice,"TotalAmount");
	        		
	        		
	        		Map<String, List<String>> map = new HashMap<String, List<String>>();
	        		exportr.setTotalmoney(list1.get(0));
					//map.put("TotalAmount", list1.get(0));
					
					//String R_GetPostageLabelResponse_Base64LabelImage[]={"Body","GetPostageLabelResponse ","LabelRequestResponse","Base64LabelImage"};
					
					//String R_GetPostageLabelResponse_Label[]={"Body","GetPostageLabelResponse ","LabelRequestResponse","Label"};//分多部分返回的图片
					aaaa="";
					List<String> image;
					if(label.getLabelType().equalsIgnoreCase("International"))
					{
						image=parse_xml_str(root,R_GetPostageLabelResponse_Image);
						
							/*for(String text:image)
					        {
					        	if(text!=null)
					        	{
					        		aaaa=aaaa+text;
					        	}
					        }*/
						
					}
					else
					{
						image=parse_xml_str(root,R_GetPostageLabelResponse_Base64LabelImage);
						/*aaaa="";
						for(String text:image)
				        {
				        	if(text!=null)
				        	{
				        		aaaa=aaaa+text;
				        	}
				        }*/
					}
					exportr.setImages(image);
					
					
					
					
					
					 List<String> tlist=parse_xml_str(root,R_GetPostageLabelResponse_TrackingNumber); 
					 String trno="";
			        for(String text:tlist)
			        {
			        	if(text!=null)
			        	{
			        		if(trno.equalsIgnoreCase(""))
			        		{
			        			trno=text;
			        		}
			        		else
			        		{
			        			trno=trno+";"+text;
			        		}
			        	}
			        }
			        exportr.setTrackingNumber(trno);
					
					//map.put("image", image);
					
					obj=new ResponseObject<ExportEndiciaResult>(
							ResponseCode.SUCCESS_CODE);
					
					//obj.setData(map);
					obj.setData(exportr);
					
	        	}
	        	else
	        	{
	        		Map<String, String> map = new HashMap<String, String>();
	        		List<String> ErrorMessage=parse_xml_str(root,R_GetPostageLabelResponse_ErrorMessage); 
	        		aaaa="";
	    	        for(String text:ErrorMessage)
	    	        {
	    	        	if(text!=null)
	    	        	{
	    	        		aaaa=aaaa+text;
	    	        	}
	    	        }
	    	        map.put("ErrorMessage", aaaa);
	    	        obj=new ResponseObject<ExportEndiciaResult>(
							ResponseCode.PARAMETER_ERROR);
	    	        exportr.setErrorMessage(aaaa);
	    	        obj.setData(exportr);
	    	       // obj.setData(map);
	        		return obj;
	        	}
	        }
	        else
	        {
	        	return obj;
	        }
	        
	     /*   datas=DatatypeConverter.parseBase64Binary(aaaa);
	        File imageFile = new File("BeautyGirl3.png"); 
	        FileOutputStream outStream = new FileOutputStream(imageFile);
	        outStream.write(datas);
	        outStream.close();
	       
	        //打印返回结果
	        System.out.println("result:" + result);*/
	}
	catch(Exception e)
	{
		//e.printStackTrace();
		return obj;
	}
		finally {
			//最后关闭流
			try {
				
				if (in != null) {
					
					in.close();
				}
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}

		}    
	    
		
		return obj;
	}
	
}
