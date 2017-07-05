package com.meitao.common.composepics;

import java.awt.Image;   
import java.awt.image.BufferedImage;   
import java.io.File;   
import java.io.FileNotFoundException;   
import java.io.FileOutputStream;   
import java.io.IOException;   
import java.io.InputStream;   
  
import javax.imageio.ImageIO;   
  
//import com.sun.image.codec.jpeg.JPEGCodec;   
//import com.sun.image.codec.jpeg.JPEGEncodeParam;   
//import com.sun.image.codec.jpeg.JPEGImageEncoder;   
  
public class ImageZipUtil {   
  
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
     * @return 返回压缩后的文件的全路径  
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
           // System.out.println(w);
            
          //  System.out.println(smallIcon);
          //  System.out.println(smallIcon);
           // int h = srcFile.getHeight(null);
          //  System.out.println(h);
            //width = w/4;
            //height = h/4;
            
            /** 宽,高设定 */  
            BufferedImage tag = new BufferedImage(width, height,   
                    BufferedImage.TYPE_INT_RGB);   
            tag.getGraphics().drawImage(srcFile, 0, 0, width, height, null);   
           
            return tag;
           // String filePrex = oldFile.substring(0, oldFile.indexOf('.'));   
            /** 压缩后的文件名 */  
           // newImage = filePrex + smallIcon   
             //       + oldFile.substring(filePrex.length());   
  
            /** 压缩之后临时存放位置 */  
           /* FileOutputStream out = new FileOutputStream(newImage);   
  
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);   
            JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);   */
            /** 压缩质量 */  
          /*  jep.setQuality(quality, true);   
            encoder.encode(tag, jep);   
            out.close();   */
  
        } catch (FileNotFoundException e) {   
            e.printStackTrace();   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
        return newImage;   
    }   
  
    /**  
     * 保存文件到服务器临时路径  
     *   
     * @param fileName  
     * @param is  
     * @return 文件全路径  
     */  
    public String writeFile(String fileName, InputStream is) {   
        if (fileName == null || fileName.trim().length() == 0) {   
            return null;   
        }   
        try {   
            /** 首先保存到临时文件 */  
            FileOutputStream fos = new FileOutputStream(fileName);   
            byte[] readBytes = new byte[512];// 缓冲大小   
            int readed = 0;   
            while ((readed = is.read(readBytes)) > 0) {   
                fos.write(readBytes, 0, readed);   
            }   
            fos.close();   
            is.close();   
        } catch (FileNotFoundException e) {   
            e.printStackTrace();   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
        return fileName;   
    }   
 /*   public static void main(String[] args){  
    	ImageZipUtil u = new ImageZipUtil();
        u.zipImageFile("e:\\pictest\\1.png", 128, 128, 1f, "x2");   
           
    }   */
}
