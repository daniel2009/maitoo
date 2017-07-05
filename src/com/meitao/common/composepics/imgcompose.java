package com.meitao.common.composepics;




	import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

	/**

	 * 该类实现了图片的合并功能，可以选择水平合并或者垂直合并。
	 * 当然此例只是针对两个图片的合并，如果想要实现多个图片的合并，只需要自己实现方法 BufferedImage
	 * mergeImage(BufferedImage[] imgs, boolean isHorizontal)即可；
	 * 而且这个方法更加具有通用性，但是时间原因不实现了，方法和两张图片实现是一样的
	 */

	public class imgcompose {

	 
		/** 
	     * 改变图片的尺寸
	     *  
	     * @param source 
	     *            源文件 
	     * @param targetW 
	     *            目标长 
	     * @param targetH 
	     *            目标宽 
	     */  
	    public static BufferedImage resize(BufferedImage source, int targetW, int targetH) throws IOException  
	    {  
	        int type = source.getType();  
	        BufferedImage target = null;  
	        double sx = (double) targetW / source.getWidth();  
	        double sy = (double) targetH / source.getHeight();  
	        // 这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放  
	        // 则将下面的if else语句注释即可  
	        if (sx > sy)  
	        {  
	            sx = sy;  
	            targetW = (int) (sx * source.getWidth());  
	        }  
	        else  
	        {  
	            sy = sx;  
	            targetH = (int) (sy * source.getHeight());  
	        }  
	        if (type == BufferedImage.TYPE_CUSTOM)  
	        { // handmade  
	            ColorModel cm = source.getColorModel();  
	            WritableRaster raster = cm.createCompatibleWritableRaster(targetW,  
	                    targetH);  
	            boolean alphaPremultiplied = cm.isAlphaPremultiplied();  
	            target = new BufferedImage(cm, raster, alphaPremultiplied, null);  
	        }  
	        else  
	        {  
	            //固定宽高，宽高一定要比原图片大  
	            //target = new BufferedImage(targetW, targetH, type);  
	            target = new BufferedImage(800, 600, type);  
	        }  
	          
	        Graphics2D g = target.createGraphics();  
	          
	        //写入背景  
	        g.drawImage(ImageIO.read(new File("ok/blank.png")), 0, 0, null);  
	          
	        // smoother than exlax:  
	        g.setRenderingHint(RenderingHints.KEY_RENDERING,  
	                RenderingHints.VALUE_RENDER_QUALITY);  
	        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));  
	        g.dispose();  
	        return target;  
	    }  	
		
		
		
		/**
	     * @param fileUrl
	     *            文件绝对路径或相对路径
	     * @return 读取到的缓存图像
	     * @throws IOException
	     *             路径错误或者不存在该文件时抛出IO异常
	     */
	    public static BufferedImage getBufferedImage(String fileUrl)
	            throws IOException {
	        File f = new File(fileUrl);
	        return ImageIO.read(f);
	    }

	    /**
	     * @param savedImg
	     *            待保存的图像
	     * @param saveDir
	     *            保存的目录
	     * @param fileName
	     *            保存的文件名，必须带后缀，比如 "beauty.jpg"
	     * @param format
	     *            文件格式：jpg、png或者bmp
	     * @return
	     */
	    public static boolean saveImage(BufferedImage savedImg, String saveDir,
	            String fileName, String format) {
	        boolean flag = false;

	        // 先检查保存的图片格式是否正确
	        String[] legalFormats = { "jpg", "JPG", "png", "PNG", "bmp", "BMP" };
	        int i = 0;
	        for (i = 0; i < legalFormats.length; i++) {
	            if (format.equals(legalFormats[i])) {
	                break;
	            }
	        }
	        if (i == legalFormats.length) { // 图片格式不支持
	            System.out.println("不是保存所支持的图片格式!");
	            return false;
	        }

	        // 再检查文件后缀和保存的格式是否一致
	        String postfix = fileName.substring(fileName.lastIndexOf('.') + 1);
	        if (!postfix.equalsIgnoreCase(format)) {
	            System.out.println("待保存文件后缀和保存的格式不一致!");
	            return false;
	        }

	        String fileUrl = saveDir + fileName;
	        File file = new File(fileUrl);
	        try {
	            flag = ImageIO.write(savedImg, format, file);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        return flag;
	    }

	    /**
	     * 待合并的两张图必须满足这样的前提，如果水平方向合并，则高度必须相等；如果是垂直方向合并，宽度必须相等。
	     * mergeImage方法不做判断，自己判断。
	     * 
	     * @param img1
	     *            待合并的第一张图
	     * @param img2
	     *            带合并的第二张图
	     * @param isHorizontal
	     *            为true时表示水平方向合并，为false时表示垂直方向合并
	     * @return 返回合并后的BufferedImage对象
	     * @throws IOException
	     */
	    public static BufferedImage mergeImage(BufferedImage img1,
	            BufferedImage img2, boolean isHorizontal) throws IOException {
	        int w1 = img1.getWidth();
	        int h1 = img1.getHeight();
	        int w2 = img2.getWidth();
	        int h2 = img2.getHeight();
	        
	        // 生成新图片
	       /* int newwidth1=0;
	        int newwidth2=0;
	        int newheight1=0;
	        int newheight2=0;
	        //接高度进行等比例调整为大小一样的，以小为准，大的缩
	        if(h1<h2)
	        {
	        	newheight1=h1;
	        	newwidth1=w1;
	        	
	        	newheight2=h1;
	        	newwidth2=(int)(((float)h1/h2)*w2);
	        	
	        }
	        else
	        {
	        	newheight2=h2;
	        	newwidth2=w2;	        	
	        	newheight1=h2;
	        	newwidth1=(int)(((float)h2/h1)*w1);
	        	        	
	        }*/
	       
	        
	      //  ImageZipUtil u = new ImageZipUtil();
	      //  u.zipImageFile("e:\\pictest\\1.png", 128, 128, 1f, "x2");   
	        
	        
	        
	       // tag.getGraphics().drawImage(srcFile, 0, 0, width, height, null);   
	       // img1.getGraphics().drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer)
	       
	        // 从图片中读取RGB
	        int[] ImageArrayOne = new int[w1 * h1];
	        ImageArrayOne = img1.getRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 逐行扫描图像中各个像素的RGB到数组中
	        int[] ImageArrayTwo = new int[w2 * h2];
	        ImageArrayTwo = img2.getRGB(0, 0, w2, h2, ImageArrayTwo, 0, w2);

	       
	        
	        BufferedImage DestImage = null;
	        if (isHorizontal) { // 水平方向合并
	            DestImage = new BufferedImage(w1+w2, h1, BufferedImage.TYPE_INT_RGB);
	            DestImage.setRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
	            DestImage.setRGB(w1, 0, w2, h2, ImageArrayTwo, 0, w2);
	        } else { // 垂直方向合并
	        	if(w1>w2)//宽度以最大为准备
	        	{
	        		DestImage = new BufferedImage(w1+10, h1+h2+20,
	        				BufferedImage.TYPE_INT_RGB);
	        	}
	        	else
	        	{
	        		DestImage = new BufferedImage(w2+10, h1 + h2+20,
	        				BufferedImage.TYPE_INT_RGB);
	        	}
	        	//设置背景为白色
	        	Graphics2D dg=(Graphics2D)DestImage.createGraphics();
	        	dg.setColor(Color.white);
	        	dg.fillRect(0, 0, DestImage.getWidth(), DestImage.getHeight());
	        	dg.dispose();
	        	int morelen=0;
	        	if(w1>w2)//把宽度小的居中显示
	        	{
	        		morelen=w1-w2;
	        		DestImage.setRGB(0+5, 0+5, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
	 	            DestImage.setRGB(0+5+morelen/2, h1+10, w2, h2, ImageArrayTwo, 0, w2); // 设置下半部分的RGB
	        	}
	        	else
	        	{
	        		morelen=w2-w1;
	        		DestImage.setRGB(0+5+morelen/2, 0+5, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
	 	            DestImage.setRGB(0+5, h1+10, w2, h2, ImageArrayTwo, 0, w2); // 设置下半部分的RGB
	        	}
	           
	        }
	        ImageArrayOne=null;
	        ImageArrayTwo=null;
	        return DestImage;
	    }

	    
	    /**  
	     * 压缩图片文件<br>  
	     * 先保存原文件，再压缩、上传  
	     *   
	     * @param oldFile  
	     *            要进行压缩的文件全路径  
	     * @param width  
	     *            宽度  
	     * @param height  
	     *            高度  
	     * @param quality  
	     *            质量  
	     * @param smallIcon  
	     *            小图片的后缀  
	     * @return 返回压缩后的临时文件缓存
	     */  
	    public BufferedImage zipImageFile(String oldFile, int width, int height,   
	            float quality, String smallIcon) {   
	        if (oldFile == null) {   
	            return null;   
	        }   
	        BufferedImage newImage = null;   
	        try {   
	            /** 对服务器上的临时文件进行处理 */  
	            Image srcFile = ImageIO.read(new File(oldFile));
	           // int w = srcFile.getWidth(null);
	            //System.out.println(w);
	            
	           // System.out.println(smallIcon);
	           // System.out.println(smallIcon);
	            //int h = srcFile.getHeight(null);
	          //  System.out.println(h);
	            //width = w/4;
	            //height = h/4;
	            
	            /** 宽,高设定 */  
	            BufferedImage tag = new BufferedImage(width, height,   
	                    BufferedImage.TYPE_INT_RGB);   
	            tag.getGraphics().drawImage(srcFile, 0, 0, width, height, null);   
	           
	            return tag;
	           
	  
	        } catch (FileNotFoundException e) {   
	            e.printStackTrace();   
	        } catch (IOException e) {   
	            e.printStackTrace();   
	        }   
	        return newImage;   
	    }   
	  
	    
	    public boolean createcompics(String str1,String str2,String name_str)
	    {
	    	BufferedImage  img1=null;
	    	BufferedImage  img2=null;
	        try {
	        	img1 = getBufferedImage(str1);
	        	img2 = getBufferedImage(str2);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        int w1 = img1.getWidth();
	        int h1 = img1.getHeight();
	        int w2 = img2.getWidth();
	        int h2 = img2.getHeight();
	        
	        // 生成新图片
	        int newwidth1=0;
	        int newwidth2=0;
	        int newheight1=0;
	        int newheight2=0;
	        //接高度进行等比例调整为大小一样的，以小为准，大的缩
	        if(h1<h2)
	        {
	        	newheight1=h1;
	        	newwidth1=w1;
	        	
	        	newheight2=h1;
	        	newwidth2=(int)(((float)h1/h2)*w2);
	        	
	        }
	        else
	        {
	        	newheight2=h2;
	        	newwidth2=w2;	        	
	        	newheight1=h2;
	        	newwidth1=(int)(((float)h2/h1)*w1);
	        	        	
	        }
	       
	        
	        ImageZipUtil u = new ImageZipUtil();
	        BufferedImage b1=  u.zipImageFile(str1, newwidth1, newheight1, 1f, "x1");
	        BufferedImage b2=  u.zipImageFile(str2, newwidth2, newheight2, 1f, "x2"); 
	        try {
	        BufferedImage destImg = mergeImage(b1, b2, false);
	        
	        // 保存图像
	       boolean flag= saveImage(destImg, name_str, ".jpg", "jpg");//必须包含完整路径
	       return flag;
	        
	       
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return false;
	        
	    }
	    
	    
	    public static void main11(String[] args) {
	        
	    	//String test="asfsadf\\asdfsa";
	    	//int aa=test.indexOf("\\");
	    	
	    	
	    	// 读取待合并的文件
	        //BufferedImage bi1 = null;
	       // BufferedImage bi2 = null;
//	        try {
//	            bi1 = getBufferedImage("src/ImageProcessDemos/图像合并/垂直合并1.jpg");
//	            bi2 = getBufferedImage("src/ImageProcessDemos/图像合并/垂直合并2.jpg");
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
	//
	        // 调用mergeImage方法获得合并后的图像
	       // BufferedImage destImg = null;
//	        try {
//	            destImg = mergeImage(bi1, bi2, false);
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
	//
//	        // saveImage(BufferedImage savedImg, String saveDir, String fileName,
//	        // String format)
//	        // 保存图像
//	        saveImage(destImg, "src/ImageProcessDemos/图像合并/", "垂直合并图像.png", "png");
//	        System.out.println("垂直合并完毕!");

	        //System.out.println("下面是水平合并的情况：");

	      /*  try {
	            bi1 = getBufferedImage("e:\\pictest\\1.png");
	            bi2 = getBufferedImage("e:\\pictest\\2.png");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }*/

	        // 调用mergeImage方法获得合并后的图像
	      
	          //  destImg = mergeImage(bi1, bi2, true);
	        	//String str1="e:\\pictest\\1.png";
	        	//String str2="e:\\pictest\\2.png";
	        	//createcompics(str1,str2,str2);
	  

	        // 保存图像
	      //  saveImage(destImg, "src/ImageProcessDemos/图像合并/", "luguo.png", "png");
	       // System.out.println("水平合并完毕!");

	    }

	}



//pic.composePic("e:\\pictest\\1.png","e:\\pictest\\2.png","e:\\pictest\\out.png",490,360);

