package com.meitao.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.meitao.service.AutoSendService;
import com.meitao.service.CodeService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.UserUtil;
import com.meitao.common.util.barcode.Code128Barcode;

import com.meitao.exception.ServiceException;
import com.meitao.model.ResponseObject;
import com.meitao.model.User;







import java.io.ByteArrayOutputStream;



import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfiguration;

import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.BarcodeUtil;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.output.eps.EPSCanvasProvider;
import org.krysalis.barcode4j.output.svg.SVGCanvasProvider;
import org.krysalis.barcode4j.tools.MimeTypes;


@Controller
public class CodeController extends BasicController {
	@Resource(name = "codeService")
	private CodeService codeService;
	@Resource(name = "autoSendService")
	private AutoSendService autoSendService;
	private static final long serialVersionUID = 9095917149775028196L;
	private static final Logger log = Logger.getLogger(CodeController.class);
	private final static char[] BASIC_CHARACTERS = "qwertyuioplkjhgfdsazxxcvbnm1234567890".toCharArray();
	private final static int[] DEFAULT_WORD_COLOR = { 0x000000, 0x292421, 0x708069, 0x4169E1, 0x6A5ACD, 0xFF6100,
	        0x082E54, 0x385E0F, 0xFF0000, 0x802A2A, 0x8A360F, 0x873324, 0x5E2612, 0x8B4513, 0x0000FF, 0xB0171F,
	        0xB22222, 0xE3170D, 0x228B22, 0x8A2BE2, 0x0B1746, 0x191970, 0x483D8B, 0x191970 };
	private static int WORD_COLOR_SIZE;

	static {
		WORD_COLOR_SIZE = DEFAULT_WORD_COLOR.length;
	}

	public int codeLength = 4;
	public int width = 100;
	public int height = 30;
	
	
	

    /** Parameter name for the message */
    public static final String BARCODE_MSG                 = "msg";
    /** Parameter name for the barcode type */
    public static final String BARCODE_TYPE                = "type";
    /** Parameter name for the barcode height */
    public static final String BARCODE_HEIGHT              = "height";
    /** Parameter name for the module width */
    public static final String BARCODE_MODULE_WIDTH        = "mw";
    /** Parameter name for the wide factor */
    public static final String BARCODE_WIDE_FACTOR         = "wf";
    /** Parameter name for the quiet zone */
    public static final String BARCODE_QUIET_ZONE          = "qz";
    /** Parameter name for the human-readable placement */
    public static final String BARCODE_HUMAN_READABLE_POS  = "hrp";
    /** Parameter name for the output format */
    public static final String BARCODE_FORMAT              = "fmt";
    /** Parameter name for the image resolution (for bitmaps) */
    public static final String BARCODE_IMAGE_RESOLUTION    = "res";
    /** Parameter name for the grayscale or b/w image (for bitmaps) */
    public static final String BARCODE_IMAGE_GRAYSCALE     = "gray";
    /** Parameter name for the font size of the human readable display */
    public static final String BARCODE_HUMAN_READABLE_SIZE = "hrsize";
    /** Parameter name for the font name of the human readable display */
    public static final String BARCODE_HUMAN_READABLE_FONT = "hrfont";
    /** Parameter name for the pattern to format the human readable message */
    public static final String BARCODE_HUMAN_READABLE_PATTERN = "hrpattern";





	@RequestMapping(value = "/barcode/generate", method = { RequestMethod.GET })
	public void generateBarcode(HttpServletResponse response, @RequestParam(value = "code") String code) {
		
		BufferedImage src = null;
		//code="*"+code+"*";
		try {
			src = new Code128Barcode().createBarcode(code);
		
		} catch (Exception e) {
			log.error("create the barcode buffered image occur error", e);
		}

		if (src != null) {
			try {
				int width = 400;
				int height = 60;

				Image image = src.getScaledInstance(width, height, Image.SCALE_DEFAULT);
				//BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(image, 0, 0, null);
				g.dispose();
				response.setContentType("image/gif");
				//response.setContentType("image/jpg");
				response.setHeader("Pragma", "no-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
				ImageIO.write(tag, "JPEG", response.getOutputStream());
			} catch (IOException e) {
				log.error("写出数据error", e);
			}
		}
	}

	@RequestMapping(value = "/code/generate", method = { RequestMethod.GET })
	public void generateCode(HttpServletRequest request, HttpServletResponse response) {
		// 定义图像buffer
		BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffImg.createGraphics();

		// 创建一个随机数生成器类
		Random random = new Random(System.currentTimeMillis());

		// 将图像填充为白色
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		// 画边框。
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width - 1, height - 1);

		// 随机产生160条干扰线，使图象中的认证码不易被其它程序探测到。
		for (int i = 0; i < 20; i++) {
			int x = random.nextInt(width) - 5;
			int y = random.nextInt(height) - 5;
			int xl = random.nextInt(width);
			int yl = random.nextInt(height);
			g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			g.drawLine(x, y, x + xl, y + yl);
		}

		// randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		StringBuffer randomCode = new StringBuffer();
		int x = 5;
		int y = height - 5;

		g.setColor(Color.BLACK);
		g.setFont(g.getFont().deriveFont(Font.BOLD, 30f));

		// 随机产生codeCount数字的验证码。
		for (int i = 0; i < codeLength; i++) {
			// 得到随机产生的验证码数字。
			String strRand = String.valueOf(BASIC_CHARACTERS[random.nextInt(36)]);

			// 用随机产生的颜色将验证码绘制到图像中。
			g.setColor(new Color(DEFAULT_WORD_COLOR[random.nextInt(WORD_COLOR_SIZE)]));

			/**** 随机缩放文字并将文字旋转指定角度 **/
			// 将文字旋转指定角度
			Graphics2D g2d_word = (Graphics2D) g;
			AffineTransform trans = new AffineTransform();
			// trans.rotate(random.nextInt(20) * 3.14 / 180, 15 * i + 10, 7);
			// 缩放文字
			float scaleSize = random.nextFloat() + 0.8f;
			if (scaleSize > 1.1f) {
				scaleSize = 1f;
			}
			trans.scale(scaleSize, scaleSize);
			g2d_word.setTransform(trans);
			/************************/

			g.drawString(strRand, x, y);
			x += random.nextInt(10) + 20;

			// 将产生的四个随机数组合在一起。
			randomCode.append(strRand);
		}

		// 将四位数字的验证码保存到Session中。
		request.getSession().setAttribute(Constant.SECURITY_CODE_KEY, randomCode.toString());
		g.dispose();

		try {
			// 禁止缓存
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "No-cache");
			response.setDateHeader("Expires", 0);
			// 指定生成的响应是图片
			response.setContentType("image/jpeg");
			ImageIO.write(buffImg, "JPEG", response.getOutputStream());
		} catch (IOException e) {
			log.error("创建验证码失败", e);
		}
	}

	@RequestMapping(value = "/code/check", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> checkCode(
	        @RequestParam(value = ParameterConstants.COMMON_VALIDATE_CODE) String vcode, HttpServletRequest request) {
		if (checkVerifyCode(request, vcode)) {
			request.getSession().removeAttribute(Constant.SECURITY_CODE_KEY);
			return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
		} else {
			return new ResponseObject<Object>(ResponseCode.VALIDATE_CODE_ERROR, "验证码输入错误，请重新输入");
		}
	}
	
	
	 @RequestMapping(value = "/code/phone", method = { RequestMethod.GET, RequestMethod.POST })
	 @ResponseBody  
	 protected ResponseObject<Object> sendCode( HttpServletRequest request,
			 @RequestParam(value = ParameterConstants.USER_PHONE) String phone){  
	     StringBuilder code = new StringBuilder();  
	     Random random = new Random();  
	     // 6位验证码 
	     for (int i = 0; i < 6; i++) {  
	    	 code.append(String.valueOf(random.nextInt(10)));  
	     }  
	    
	     try {
	    	User user = new User();
	    	user.setPhone(phone);
	    	this.autoSendService.send(user, Constant.AUTO_SEND_FORGET_PASSWORD, code.toString());
//			System.out.println(SmsSendUtil.sendRegisterMsg(code.toString(), phone));
			HttpSession session = request.getSession();  
		    session.setAttribute(Constant.PHONE_KEY, phone);  
		    session.setAttribute(Constant.PHONE_SEND_CODE, code.toString());  
		    session.setAttribute(Constant.PHONE_SEND_CODE_TIME, new Date().getTime());
		    return generateResponseObject(ResponseCode.SUCCESS_CODE);
		} catch (Exception e) {
			log.error("发送验证码异常", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "发送验证码失败");
		}
	 }  
	 
	 @RequestMapping(value = "/code/phone_reg", method = { RequestMethod.GET, RequestMethod.POST })
	 @ResponseBody  
	 protected ResponseObject<Object> sendCodebyreg( HttpServletRequest request,
			 @RequestParam(value = ParameterConstants.USER_PHONE) String phone){  
		 
		 
		
		 
		 
	     StringBuilder code = new StringBuilder();  
	     Random random = new Random();  
	     // 6位验证码 
	     for (int i = 0; i < 6; i++) {  
	    	 code.append(String.valueOf(random.nextInt(10)));  
	     }  
	    
	     try {
	    	 if (this.codeService.checkExistsByPhone(phone)) { 
				 
	    		 return generateResponseObject(ResponseCode.USER_PHONE_EXISTS, "该手机号码已经注册过了，找回密码或者登录！");
	    		 
			 }
	    	User user = new User();
		    user.setPhone(phone);
		    
		    	 ResponseObject<Object> obj=this.autoSendService.send(user, Constant.AUTO_SEND_REGISTER_CODE, code.toString());
		    	 if(!obj.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE))
		    	 {
		    		 obj.setMessage("发送失败!");
		    		 return obj;
		    	 }
//			System.out.println(SmsSendUtil.sendRegisterMsg(code.toString(), phone));
			HttpSession session = request.getSession();  
		    session.setAttribute(Constant.PHONE_KEY, phone);  
		    session.setAttribute(Constant.PHONE_SEND_CODE, code.toString());  
		    session.setAttribute(Constant.PHONE_SEND_CODE_TIME, new Date().getTime());
		    return generateResponseObject(ResponseCode.SUCCESS_CODE);
		} catch (Exception e) {
			log.error("发送验证码异常", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "发送验证码失败");
		}
	 }  
	 @RequestMapping(value = "/code/reset_pwd", method = { RequestMethod.GET, RequestMethod.POST })
	 @ResponseBody  
	 protected ResponseObject<Object> sendResetPwdCode( HttpServletRequest request,
			 @RequestParam(value = ParameterConstants.USER_PHONE) String phone){  
	     StringBuilder code = new StringBuilder();  
	     Random random = new Random();  
	     // 6位验证码 
	     for (int i = 0; i < 6; i++) {  
	    	 code.append(String.valueOf(random.nextInt(10)));  
	     }  
	    
	     try {
	    	User user = new User();
			user.setPhone(phone);
			this.autoSendService.send(user, Constant.AUTO_SEND_FORGET_PASSWORD, code.toString());
//			System.out.println(SmsSendUtil.sendRegisterMsg(code.toString(), phone));
			HttpSession session = request.getSession();  

			session.setAttribute(Constant.PHONE_RESET_PWD_KEY, phone);  
		    session.setAttribute(Constant.PHONE_SEND_RESET_PWD_CODE, code.toString());  
		    session.setAttribute(Constant.PHONE_SEND_RESET_PWD_CODE_TIME, new Date().getTime());
		    return generateResponseObject(ResponseCode.SUCCESS_CODE);
		} catch (Exception e) {
			log.error("发送验证码异常", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "发送验证码失败");
		}
	 }  
	 
	 
	 /*
		 * kai 20151103 发送邮箱验证码
		 * */

		@RequestMapping(value = "/code/sendemail_verify_code", method = { RequestMethod.GET,RequestMethod.POST })
		@ResponseBody
		public ResponseObject<Object> verifyfEmail(HttpServletRequest request,
		        @RequestParam(value = ParameterConstants.USER_EMAIL) String email) {
			ResponseObject<Object> responseObj = null;

			if (!UserUtil.validateEmail(email)) {
				responseObj = generateResponseObject(ResponseCode.USER_EMAIL_ERROR, "邮箱输入不正确，请重新输入！");
			} else {
				// 验证通过, 检测用户是否存在
				boolean exists = false;
				try {
					//exists = this.userService.checkExistsByEmail(email);
					exists =this.codeService.checkExistsByEmail(email);
					if(exists==true)
					{
						responseObj = generateResponseObject(ResponseCode.USER_EMAIL_ERROR, "邮箱已存在，请使用其它邮箱！");
					}
				} catch (ServiceException e) {
					log.error("根据email获取账户失败,email=" + email, e);
					responseObj = generateResponseObject(ResponseCode.SHOW_EXCEPTION, "根据email检测会员是否存在失败!");
				}
			     StringBuilder code = new StringBuilder();  
			     Random random = new Random();  
			     // 6位验证码 
			     for (int i = 0; i < 6; i++) {  
			    	 code.append(String.valueOf(random.nextInt(10)));  
			     } 
			     HttpSession session = request.getSession();
			     session.setAttribute(Constant.EMAIL_SEND_CODE, code);
			     session.setAttribute(Constant.EMAIL_KEY, email);
			     
			     responseObj = generateResponseObject(ResponseCode.SUCCESS_CODE, "验证码已发送到邮箱");

				if (exists==false) {
					try {
						
					
							try {
								String baseUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
								        + request.getContextPath();
								User user = new User();
							    user.setPhone(email);
						    	this.autoSendService.send(user, Constant.AUTO_SEND_REGISTER_CODE, baseUrl+"  &nbsp;"+code.toString());
								//MailSendUtil.sendResetPwdMsg(baseUrl, email, result.getData());
//								MailSendUtil.sendverifyMsg(baseUrl, email, code.toString());
								responseObj = generateResponseObject(ResponseCode.SUCCESS_CODE, "验证码已发送到邮箱");
							} catch (Throwable e) {
								log.error("发送邮件失败", e);
								responseObj = generateResponseObject(ResponseCode.SHOW_EXCEPTION, "发送邮件失败!");
							}
						
					} catch (Exception e) {
						log.error("保存token失败", e);
						responseObj = generateResponseObject(ResponseCode.SHOW_EXCEPTION, "发送邮件失败!");
					}
				} else {
					responseObj = generateResponseObject(ResponseCode.USER_EMAIL_NOT_EXISTS, "该email已经注册!");
				}
			}
			return responseObj;
		}
		
		
		@RequestMapping(value = "/barcode4j/generate2", method = { RequestMethod.GET })
		public void generateBarcode4j2(
				HttpServletRequest request, 
				HttpServletResponse response, 
				@RequestParam(value = "msg") String msg,
				@RequestParam(value = "ranno") String ran,//加入随机数
				@RequestParam(value = "type") String type) {

			msg=msg.trim();
	        try {
	            String format = determineFormat(request);
	            format="image/gif";
	            int orientation = 0;

	            Configuration cfg = buildCfg(request);

	           // String msg = request.getParameter(BARCODE_MSG);
	            if (msg == null) msg = "0123456789";

	            BarcodeUtil util = BarcodeUtil.getInstance();
	            BarcodeGenerator gen = util.createBarcodeGenerator(cfg);

	            ByteArrayOutputStream bout = new ByteArrayOutputStream(4096);
	            try {
	                if (format.equals(MimeTypes.MIME_SVG)) {
	                    //Create Barcode and render it to SVG
	                    SVGCanvasProvider svg = new SVGCanvasProvider(false, orientation);
	                    gen.generateBarcode(svg, msg);
	                    org.w3c.dom.DocumentFragment frag = svg.getDOMFragment();

	                    //Serialize SVG barcode
	                    TransformerFactory factory = TransformerFactory.newInstance();
	                    Transformer trans = factory.newTransformer();
	                    Source src = new javax.xml.transform.dom.DOMSource(frag);
	                    Result res = new javax.xml.transform.stream.StreamResult(bout);
	                    trans.transform(src, res);
	                } else if (format.equals(MimeTypes.MIME_EPS)) {
	                    EPSCanvasProvider eps = new EPSCanvasProvider(bout, orientation);
	                    gen.generateBarcode(eps, msg);
	                    eps.finish();
	                } else {
	                    String resText = request.getParameter(BARCODE_IMAGE_RESOLUTION);
	                    int resolution = 300; //dpi
	                    if (resText != null) {
	                        resolution = Integer.parseInt(resText);
	                    }
	                    if (resolution > 2400) {
	                        throw new IllegalArgumentException(
	                            "Resolutions above 2400dpi are not allowed");
	                    }
	                    if (resolution < 10) {
	                        throw new IllegalArgumentException(
	                            "Minimum resolution must be 10dpi");
	                    }
	                    String gray = request.getParameter(BARCODE_IMAGE_GRAYSCALE);
	                    BitmapCanvasProvider bitmap = ("true".equalsIgnoreCase(gray)
	                        ? new BitmapCanvasProvider(
	                                bout, format, resolution,
	                                BufferedImage.TYPE_BYTE_GRAY, true, orientation)
	                        : new BitmapCanvasProvider(
	                                bout, format, resolution,
	                                BufferedImage.TYPE_BYTE_BINARY, false, orientation));
	                    gen.generateBarcode(bitmap, msg);
	                    bitmap.finish();
	                }
	            } finally {
	                bout.close();
	            }
	            
	         /*   response.setContentType("image/gif");
				//response.setContentType("image/jpg");
				response.setHeader("Pragma", "no-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
				ImageIO.write(tag, "JPEG", response.getOutputStream());*/
	            
	            response.setContentType(format);
	            response.setContentLength(bout.size());
	            response.getOutputStream().write(bout.toByteArray());
	            response.getOutputStream().flush();
	        } catch (Exception e) {
	            log.error("Error while generating barcode", e);
	           // throw new ServletException(e);
	        } catch (Throwable t) {
	            log.error("Error while generating barcode", t);
	         //   throw new ServletException(t);
	        }
	    }
		
		
		@RequestMapping(value = "/barcode4j/generate1", method = { RequestMethod.GET })
		public void generateBarcode4j1(
				HttpServletRequest request, 
				HttpServletResponse response, 
				@RequestParam(value = "msg") String msg,
				@RequestParam(value = "ranno") String ran,//加入随机数
				@RequestParam(value = "type") String type) {

			msg=msg.trim();
	        try {
	            String format = determineFormat(request);
	            format="image/gif";
	            int orientation = 0;

	            Configuration cfg = buildCfg(request);

	           // String msg = request.getParameter(BARCODE_MSG);
	            if (msg == null) msg = "0123456789";

	            BarcodeUtil util = BarcodeUtil.getInstance();
	            BarcodeGenerator gen = util.createBarcodeGenerator(cfg);

	            ByteArrayOutputStream bout = new ByteArrayOutputStream(4096);
	            try {
	                if (format.equals(MimeTypes.MIME_SVG)) {
	                    //Create Barcode and render it to SVG
	                    SVGCanvasProvider svg = new SVGCanvasProvider(false, orientation);
	                    gen.generateBarcode(svg, msg);
	                    org.w3c.dom.DocumentFragment frag = svg.getDOMFragment();

	                    //Serialize SVG barcode
	                    TransformerFactory factory = TransformerFactory.newInstance();
	                    Transformer trans = factory.newTransformer();
	                    Source src = new javax.xml.transform.dom.DOMSource(frag);
	                    Result res = new javax.xml.transform.stream.StreamResult(bout);
	                    trans.transform(src, res);
	                } else if (format.equals(MimeTypes.MIME_EPS)) {
	                    EPSCanvasProvider eps = new EPSCanvasProvider(bout, orientation);
	                    gen.generateBarcode(eps, msg);
	                    eps.finish();
	                } else {
	                    String resText = request.getParameter(BARCODE_IMAGE_RESOLUTION);
	                    int resolution = 300; //dpi
	                    if (resText != null) {
	                        resolution = Integer.parseInt(resText);
	                    }
	                    if (resolution > 2400) {
	                        throw new IllegalArgumentException(
	                            "Resolutions above 2400dpi are not allowed");
	                    }
	                    if (resolution < 10) {
	                        throw new IllegalArgumentException(
	                            "Minimum resolution must be 10dpi");
	                    }
	                    String gray = request.getParameter(BARCODE_IMAGE_GRAYSCALE);
	                    BitmapCanvasProvider bitmap = ("true".equalsIgnoreCase(gray)
	                        ? new BitmapCanvasProvider(
	                                bout, format, resolution,
	                                BufferedImage.TYPE_BYTE_GRAY, true, orientation)
	                        : new BitmapCanvasProvider(
	                                bout, format, resolution,
	                                BufferedImage.TYPE_BYTE_BINARY, false, orientation));
	                    gen.generateBarcode(bitmap, msg);
	                    bitmap.finish();
	                }
	            } finally {
	                bout.close();
	            }
	            
	         /*   response.setContentType("image/gif");
				//response.setContentType("image/jpg");
				response.setHeader("Pragma", "no-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
				ImageIO.write(tag, "JPEG", response.getOutputStream());*/
	            
	            response.setContentType(format);
	            response.setContentLength(bout.size());
	            response.getOutputStream().write(bout.toByteArray());
	            response.getOutputStream().flush();
	        } catch (Exception e) {
	            log.error("Error while generating barcode", e);
	           // throw new ServletException(e);
	        } catch (Throwable t) {
	            log.error("Error while generating barcode", t);
	         //   throw new ServletException(t);
	        }
	    }
		
		@RequestMapping(value = "/barcode4j/generate", method = { RequestMethod.GET })
		public void generateBarcode4j(
				HttpServletRequest request, 
				HttpServletResponse response, 
				@RequestParam(value = "msg") String msg,
				@RequestParam(value = "ranno") String ran,//加入随机数
				@RequestParam(value = "type") String type) {

			msg="*"+msg.trim()+"*";
	        try {
	            String format = determineFormat(request);
	            format="image/gif";
	            int orientation = 0;

	            Configuration cfg = buildCfg(request);

	           // String msg = request.getParameter(BARCODE_MSG);
	           // if (msg == null) msg = "0123456789";

	            BarcodeUtil util = BarcodeUtil.getInstance();
	            BarcodeGenerator gen = util.createBarcodeGenerator(cfg);

	            ByteArrayOutputStream bout = new ByteArrayOutputStream(8096);
	            try {
	                if (format.equals(MimeTypes.MIME_SVG)) {
	                    //Create Barcode and render it to SVG
	                    SVGCanvasProvider svg = new SVGCanvasProvider(false, orientation);
	                    gen.generateBarcode(svg, msg);
	                    org.w3c.dom.DocumentFragment frag = svg.getDOMFragment();

	                    //Serialize SVG barcode
	                    TransformerFactory factory = TransformerFactory.newInstance();
	                    Transformer trans = factory.newTransformer();
	                    Source src = new javax.xml.transform.dom.DOMSource(frag);
	                    Result res = new javax.xml.transform.stream.StreamResult(bout);
	                    trans.transform(src, res);
	                } else if (format.equals(MimeTypes.MIME_EPS)) {
	                    EPSCanvasProvider eps = new EPSCanvasProvider(bout, orientation);
	                    gen.generateBarcode(eps, msg);
	                    eps.finish();
	                } else {
	                    String resText = request.getParameter(BARCODE_IMAGE_RESOLUTION);
	                    int resolution = 300; //dpi
	                    if (resText != null) {
	                        resolution = Integer.parseInt(resText);
	                    }
	                    if (resolution > 2400) {
	                        throw new IllegalArgumentException(
	                            "Resolutions above 2400dpi are not allowed");
	                    }
	                    if (resolution < 10) {
	                        throw new IllegalArgumentException(
	                            "Minimum resolution must be 10dpi");
	                    }
	                    String gray = request.getParameter(BARCODE_IMAGE_GRAYSCALE);
	                    BitmapCanvasProvider bitmap = ("true".equalsIgnoreCase(gray)
	                        ? new BitmapCanvasProvider(
	                                bout, format, resolution,
	                                BufferedImage.TYPE_BYTE_GRAY, true, orientation)
	                        : new BitmapCanvasProvider(
	                                bout, format, resolution,
	                                BufferedImage.TYPE_BYTE_BINARY, false, orientation));
	                    gen.generateBarcode(bitmap, msg);
	                    bitmap.finish();
	                }
	            } finally {
	            	bout.flush();
	                bout.close();
	            }
	            
	         /*   response.setContentType("image/gif");
				//response.setContentType("image/jpg");
				response.setHeader("Pragma", "no-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
				ImageIO.write(tag, "JPEG", response.getOutputStream());*/
	            
	            response.setContentType(format);
	            response.setContentLength(bout.size());
	            response.getOutputStream().write(bout.toByteArray());
	            response.getOutputStream().flush();
	        } catch (Exception e) {
	            log.error("Error while generating barcode", e);
	           // throw new ServletException(e);
	        } catch (Throwable t) {
	            log.error("Error while generating barcode", t);
	         //   throw new ServletException(t);
	        }
	    }
		
		 /**
	     * Check the request for the desired output format.
	     * @param request the request to use
	     * @return MIME type of the desired output format.
	     */
	    protected String determineFormat(HttpServletRequest request) {
	        String format = request.getParameter(BARCODE_FORMAT);
	        format = MimeTypes.expandFormat(format);
	        if (format == null) format = MimeTypes.MIME_SVG;
	        return format;
	    }

	    /**
	     * Build an Avalon Configuration object from the request.
	     * @param request the request to use
	     * @return the newly built COnfiguration object
	     * @todo Change to bean API
	     */
	    protected Configuration buildCfg(HttpServletRequest request) {
	        DefaultConfiguration cfg = new DefaultConfiguration("barcode");
	        //Get type
	        String type = request.getParameter(BARCODE_TYPE);
	        if (type == null) type = "code128";
	        DefaultConfiguration child = new DefaultConfiguration(type);
	        cfg.addChild(child);
	        //Get additional attributes
	        DefaultConfiguration attr;
	        String height = request.getParameter(BARCODE_HEIGHT);
	        if (height != null) {
	            attr = new DefaultConfiguration("height");
	            attr.setValue(height);
	            child.addChild(attr);
	        }
	        String moduleWidth = request.getParameter(BARCODE_MODULE_WIDTH);
	        if (moduleWidth != null) {
	            attr = new DefaultConfiguration("module-width");
	            attr.setValue(moduleWidth);
	            child.addChild(attr);
	        }
	        String wideFactor = request.getParameter(BARCODE_WIDE_FACTOR);
	        if (wideFactor != null) {
	            attr = new DefaultConfiguration("wide-factor");
	            attr.setValue(wideFactor);
	            child.addChild(attr);
	        }
	        String quietZone = request.getParameter(BARCODE_QUIET_ZONE);
	        if (quietZone != null) {
	            attr = new DefaultConfiguration("quiet-zone");
	            if (quietZone.startsWith("disable")) {
	                attr.setAttribute("enabled", "false");
	            } else {
	                attr.setValue(quietZone);
	            }
	            child.addChild(attr);
	        }

	        // creating human readable configuration according to the new Barcode Element Mappings
	        // where the human-readable has children for font name, font size, placement and
	        // custom pattern.
	        String humanReadablePosition = request.getParameter(BARCODE_HUMAN_READABLE_POS);
	        String pattern = request.getParameter(BARCODE_HUMAN_READABLE_PATTERN);
	        String humanReadableSize = request.getParameter(BARCODE_HUMAN_READABLE_SIZE);
	        String humanReadableFont = request.getParameter(BARCODE_HUMAN_READABLE_FONT);

	        if (!((humanReadablePosition == null)
	                && (pattern == null)
	                && (humanReadableSize == null)
	                && (humanReadableFont == null))) {
	            attr = new DefaultConfiguration("human-readable");

	            DefaultConfiguration subAttr;
	            if (pattern != null) {
	                subAttr = new DefaultConfiguration("pattern");
	                subAttr.setValue(pattern);
	                attr.addChild(subAttr);
	            }
	            if (humanReadableSize != null) {
	                subAttr = new DefaultConfiguration("font-size");
	                subAttr.setValue(humanReadableSize);
	                attr.addChild(subAttr);
	            }
	            if (humanReadableFont != null) {
	                subAttr = new DefaultConfiguration("font-name");
	                subAttr.setValue(humanReadableFont);
	                attr.addChild(subAttr);
	            }
	            if (humanReadablePosition != null) {
	              subAttr = new DefaultConfiguration("placement");
	              subAttr.setValue(humanReadablePosition);
	              attr.addChild(subAttr);
	            }

	            child.addChild(attr);
	        }

	        return cfg;
	    }
}
