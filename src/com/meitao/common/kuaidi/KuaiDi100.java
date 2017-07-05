package com.meitao.common.kuaidi;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;


import com.meitao.common.constants.Constant;
import com.meitao.common.util.PropertiesReader;
import com.meitao.common.util.StringUtil;

public class KuaiDi100 {
	//private String key = "1a34d5adddbaf841";
	//@Autowired
	//private globalargsDao globalargsDao;
	public String SearchkuaiDiInfo(String com, String nu,String key) throws Exception {
		//String key = "";
		//key = globalargsDao.getcontentbyflag("kuaidi100_key");
		
		
		String[] htmllist=null;
		try {
			Properties props = PropertiesReader.read(Constant.SYSTEM_PROPERTIES_FILE);
			//key = props.getProperty("kuaidi.apikey");
			String htmllist_temp=props.getProperty("kuaidi100.use.url.list");
			if(!StringUtil.isEmpty(htmllist_temp))
			{
				htmllist=htmllist_temp.split(";");
			}
		} catch (Exception e) {
			key = "1a34d5adddbaf841";
		}
		
//		try {
//			Properties props = PropertiesReader.read(Constant.SYSTEM_PROPERTIES_FILE);
//			key = props.getProperty("kuaidi.apikey");
//		} catch (Exception e) {
//			key = "1a34d5adddbaf841";
//		}
		String content = "";

		URL url;
		
		
		
		String flag="0";
		if((htmllist!=null)&&(htmllist.length>0))
		{
			if(!StringUtil.isEmpty(com))
			{
				for(int i=0;i<htmllist.length;i++)
				{
					if(!StringUtil.isEmpty(htmllist[i]))
					{
						if(com.equalsIgnoreCase(htmllist[i]))
						{
							flag="1";
							break;
						}
					}
				}
			}
		}
		//if((com!=null)&&(com.equalsIgnoreCase("ems")))
		if((!StringUtil.isEmpty(com))&&(flag.equalsIgnoreCase("1")))
		{
			com=com.toLowerCase();
			url = new URL("http://www.kuaidi100.com/applyurl?key=" + key
					+ "&com=" + com + "&nu=" + nu );
			System.out.println(url);
		}
		else
		{
			url = new URL("http://api.kuaidi100.com/api?id=" + key
				+ "&com=" + com + "&nu=" + nu + "&show=0&muti=1&order=desc");
		System.out.println("http://api.kuaidi100.com/api?id=" + key
				+ "&com=" + com + "&nu=" + nu + "&show=2&muti=1&order=desc");
		}
		URLConnection con = url.openConnection();
		con.setAllowUserInteraction(false);
		InputStream urlStream = url.openStream();
		byte[] b = new byte[10000];
		int numRead = urlStream.read(b);
		content = new String(b, 0, numRead);
		while (numRead != -1) {
			numRead = urlStream.read(b);
			if (numRead != -1) {
				String newContent = new String(b, 0, numRead, "UTF-8");
				content = content + newContent;
			}
		}
		urlStream.close();
		return content;
	}

	/*public static void main(String[] agrs) {
		KuaiDi100 kuaidi = new KuaiDi100();
		// http://api.kuaidi100.com/api?id=&com=huitongkuaidi&nu=&show=2&muti=1&order=desc
		String content = null;
		try {
			content = kuaidi
					.SearchkuaiDiInfo("usps", "CY000208408US");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(content);
	}*/
		
		


}