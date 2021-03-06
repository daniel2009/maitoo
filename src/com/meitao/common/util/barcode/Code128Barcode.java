package com.meitao.common.util.barcode;

import java.awt.image.BufferedImage;

import org.jbarcode.JBarcode;
//import org.jbarcode.encode.Code128Encoder;
import org.jbarcode.encode.Code39Encoder;
//import org.jbarcode.encode.EAN8Encoder;
import org.jbarcode.paint.BaseLineTextPainter;
//import org.jbarcode.paint.EAN8TextPainter;
import org.jbarcode.paint.WideRatioCodedPainter;
import org.jbarcode.paint.WidthCodedPainter;

public class Code128Barcode implements IBarcode {

    public BufferedImage createBarcode(String codes) throws Exception {
        try {
        	
        	// JBarcode localJBarcode = new JBarcode(Code128Encoder.getInstance(), WidthCodedPainter.getInstance(), BaseLineTextPainter.getInstance());
        	 JBarcode localJBarcode = new JBarcode(Code39Encoder.getInstance(), WidthCodedPainter.getInstance(), BaseLineTextPainter.getInstance());
            // BufferedImage localBufferedImage = localJBarcode.createBarcode(code);
           //  localJBarcode.setEncoder(Code39Encoder.getInstance());
        	// localJBarcode.
        	 
             localJBarcode.setPainter(WideRatioCodedPainter.getInstance());
             localJBarcode.setTextPainter(BaseLineTextPainter.getInstance());
             localJBarcode.setShowCheckDigit(false);
            /* FileOutputStream localFileOutputStream = new FileOutputStream(filePath);
             ImageUtil.encodeAndWrite(localBufferedImage, "png", localFileOutputStream, WIDTH, HEIGHT);
             localFileOutputStream.close();
             print(new File(filePath));
             return true;*/
        	
            //JBarcode localJBarcode = new JBarcode(Code128Encoder.getInstance(), WidthCodedPainter.getInstance(), BaseLineTextPainter.getInstance());
           // JBarcode localJBarcode = new JBarcode(EAN8Encoder.getInstance(),WidthCodedPainter.getInstance(),BaseLineTextPainter.getInstance());
           // JBarcode localJBarcode = new JBarcode(Code39Encoder.getInstance(),WidthCodedPainter.getInstance(),BaseLineTextPainter.getInstance());
            
            localJBarcode.setCheckDigit(false);
            localJBarcode.setShowCheckDigit(true);
            localJBarcode.setShowText(true);
            //codes="*"+codes+"*";
            BufferedImage localBufferedImage = localJBarcode.createBarcode(codes);
            return localBufferedImage;
        } catch (Exception e) {
            throw e;
        }
    }

}
