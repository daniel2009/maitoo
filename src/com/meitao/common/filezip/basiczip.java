package com.meitao.common.filezip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
//import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
//import java.util.zip.ZipOutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
public class basiczip {
	   /**
     * 功能:压缩多个文件成一个zip文件
     * <p>作者 陈亚标 Jul 16, 2010 10:59:40 AM
     * @param srcfile：源文件列表
     * @param zipfile：压缩后的文件
     * //kai 20150911
     */
    public static void zipFiles(File[] srcfile,File zipfile,String[] prename){
        byte[] buf=new byte[1024];
        ZipOutputStream out=null;
        try {
            //ZipOutputStream类：完成文件或文件夹的压缩
        	System.setProperty("sun.zip.encoding", System.getProperty("sun.jnu.encoding")); 
            out=new ZipOutputStream(new FileOutputStream(zipfile));
           //out.setEncoding(System.getProperty("sun.jnu.encoding"));//设置文件名编码方式
           out.setEncoding("UTF-8");//设置文件名编码方式
            for(int i=0;i<srcfile.length;i++){
            	if(srcfile[i]==null)
            	{
            		continue;
            	}
            	if(!srcfile[i].exists())//如果文件不存在，将会异常
            	{
            		continue;
            	}
            	//File file1 = new File(i+"1.jpg");
            	//.transferTo(file1);
				
                FileInputStream in=new FileInputStream(srcfile[i]);
                String strname;//要保证不重名，否则会出错
               /* if(prename[i]!=null)
                {
                	strname=prename[i]+"_"+i+"_"+srcfile[i].getName();
                }
                else
                {
                	strname=i+"_"+srcfile[i].getName();
                }*/
                strname=prename[i]+".jpg";
                
                out.putNextEntry(new ZipEntry(strname));
                int len;
                while((len=in.read(buf))>0){
                    out.write(buf,0,len);
                }
                
                out.closeEntry();
                
                in.close();
            }
            out.flush();
            out.close();
            System.out.println("压缩完成.");
        } catch (Exception e) {
        	
        	
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
        	buf=null;
        	if(out!=null)
        	{
        		
        		try{
        			out.flush();
        			out.close();
        		}catch( Exception e)
        		{
        			
        		}
        		
        	}
        }
    }
    
    
    
    public static void zipFilesSameName(File[] srcfile,File zipfile){
        byte[] buf=new byte[1024];
        ZipOutputStream out=null;
        try {
            //ZipOutputStream类：完成文件或文件夹的压缩
        	System.setProperty("sun.zip.encoding", System.getProperty("sun.jnu.encoding")); 
            out=new ZipOutputStream(new FileOutputStream(zipfile));
            out.setEncoding("UTF-8");//设置文件名编码方式
            for(int i=0;i<srcfile.length;i++){
            	if(srcfile[i]==null)
            	{
            		continue;
            	}
            	if(!srcfile[i].exists())//如果文件不存在，将会异常
            	{
            		continue;
            	}
            
				
                FileInputStream in=new FileInputStream(srcfile[i]);
               // String strname;//要保证不重名，否则会出错
    
                
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                int len;
                while((len=in.read(buf))>0){
                    out.write(buf,0,len);
                }
                
                out.closeEntry();
                in.close();
            }
            out.flush();
            out.close();
            System.out.println("压缩完成.");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
        	buf=null;
        	if(out!=null)
        	{
        		
        		try{
        			out.flush();
        			out.close();
        		}catch( Exception e)
        		{
        			
        		}
        		
        	}
        }
    }
    
    
    /**
     * 功能:解压缩
     * <p>作者 陈亚标 Jul 16, 2010 12:42:20 PM
     * @param zipfile：需要解压缩的文件
     * @param descDir：解压后的目标目录
     */
    public static void unZipFiles(File zipfile,String descDir){
    	 //ZipFile zf=new ZipFile(zipfile);
         OutputStream out=null;
         ZipFile zf=null;
        try {
            zf=new ZipFile(zipfile);
           // OutputStream out=null;
            for(Enumeration entries=zf.entries();entries.hasMoreElements();){
                ZipEntry entry=(ZipEntry) entries.nextElement();
                String zipEntryName=entry.getName();
                InputStream in=zf.getInputStream(entry);
                out=new FileOutputStream(descDir+zipEntryName);
                byte[] buf1=new byte[1024];
                int len;
                while((len=in.read(buf1))>0){
                    out.write(buf1,0,len);
                    out.flush();
                }
                buf1=null;
                if(in!=null)
                {
                	in.close();
                	in=null;
                }
                if(out!=null)
                {
                	out.flush();
                	out.close();
                	out=null;
                }
                //System.out.println("解压缩完成.");
            }
            
            if(zf!=null)
            {
            	zf.close();
            	zf=null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally{
        	if(zf!=null)
            {
        		try{
            	zf.close();
            	zf=null;
        		}catch( Exception e)
        		{
        			
        		}
            }
        	if(out!=null)
        	{
        		
        		try{
        			out.flush();
        			out.close();
        		}catch( Exception e)
        		{
        			
        		}
        		
        	}
        }
    }
    
    
    public static void unZipFiles1(File zipFile,String descDir)throws IOException{  
        File pathFile = new File(descDir);  
        if(!pathFile.exists()){  
            pathFile.mkdirs();  
        }  
        ZipFile zip = new ZipFile(zipFile);  
        try{
	        for(Enumeration entries = zip.entries();entries.hasMoreElements();){  
	            ZipEntry entry = (ZipEntry)entries.nextElement();  
	            String zipEntryName = entry.getName();  
	            InputStream in = zip.getInputStream(entry);  
	            String outPath = (descDir+zipEntryName).replaceAll("\\*", "/");;  
	            //判断路径是否存在,不存在则创建文件路径  
	            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));  
	            if(!file.exists()){  
	                file.mkdirs();  
	            }  
	            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压  
	            if(new File(outPath).isDirectory()){  
	                continue;  
	            }  
	            //输出文件路径信息  
	            System.out.println(outPath);  
	              
	            OutputStream out = new FileOutputStream(outPath);  
	            byte[] buf1 = new byte[1024];  
	            int len;  
	            while((len=in.read(buf1))>0){  
	                out.write(buf1,0,len);  
	                out.flush();
	            }
	            buf1=null;
	            if(in!=null)
	            {
	            	
	            	in.close();  
	            }
	            if(out!=null)
	            {
	            	out.flush();
	            	out.close();  
	            }
	            in=null;
	            out=null;
	            }  
	       
	        if(zip!=null)
	        {
	        	zip.close();
	        	zip=null;
	        }
        }
        finally{
        	if(zip!=null)
	        {
	        	zip.close();
	        	zip=null;
	        }
        }
        //System.out.println("******************解压完毕********************");  
    }
    
    
    
  /*  public static void main(String[] args) {
        //2个源文件
        File f1=new File("E:\\myeclipse wordspace2\\.metadata\\.me_tcat7\\webapps\\meitao\\resources\\pics\\card\\1003_sCQiT_48234.jpg");
        File f2=new File("E:\\myeclipse wordspace2\\.metadata\\.me_tcat7\\webapps\\meitao\\resources\\pics\\card\\1115_EWjbz_60253.jpg");
        File[] srcfile={f1,f2};
        //压缩后的文件
        File zipfile=new File("E:\\myeclipse wordspace2\\.metadata\\.me_tcat7\\webapps\\meitao\\resources\\pics\\card\\biao.zip");
        basiczip.zipFiles(srcfile, zipfile);
        
        //需要解压缩的文件
       // File file=new File("D:\\workspace\\flexTest\\src\\com\\biao\\test\\biao.zip");
        //解压后的目标目录
       // String dir="D:\\workspace\\flexTest\\src\\com\\biao\\test\\";
      //  basiczip.unZipFiles(file, dir);
    }*/

   
}
