package com.meitao.common.kuaidi.impl;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;


import com.meitao.common.constants.Constant;
import com.meitao.common.kuaidi.KuaiDi;
import com.meitao.common.util.PropertiesReader;
import com.meitao.common.util.StringUtil;

public class Kuaidi100 implements KuaiDi{
	
	//@Autowired
	//private globalargsDao globalargsDao;
	
	@Override
	public String SearchkuaiDiInfo(String orderId, String ...args) throws Exception {
		// TODO Auto-generated method stub
		String key="";
		if(StringUtil.isEmpty(args[2]))
		{
			//key = "1a34d5adddbaf841";
		}
		else
		{
			key = args[2];
		}
		
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
		
		String content = "";
		URL url;
		String flag="0";
		if((htmllist!=null)&&(htmllist.length>0))
		{
			if(!StringUtil.isEmpty(args[0]))
			{
				for(int i=0;i<htmllist.length;i++)
				{
					if(!StringUtil.isEmpty(htmllist[i]))
					{
						if(args[0].equalsIgnoreCase(htmllist[i]))
						{
							flag="1";
							break;
						}
					}
				}
			}
		}
		
		if((!StringUtil.isEmpty(args[0]))&&(flag.equalsIgnoreCase("1")))
		{
			args[0]=args[0].toLowerCase();
			url = new URL("http://www.kuaidi100.com/applyurl?key=" + key
					+ "&com=" + args[0] + "&nu=" + args[1] );
			System.out.println(url);
		}
		else
		{
			url = new URL("http://api.kuaidi100.com/api?id=" + key
				+ "&com=" + args[0] + "&nu=" + args[1] + "&show=0&muti=1&order=desc");
		System.out.println("http://api.kuaidi100.com/api?id=" + key
				+ "&com=" + args[0] + "&nu=" + args[1] + "&show=2&muti=1&order=desc");
		}
		URLConnection con = url.openConnection();
		con.setAllowUserInteraction(false);
		InputStream urlStream = url.openStream();
		byte[] b = new byte[10000];
		int numRead = urlStream.read(b);
		content = new String(b, 0, numRead, "UTF-8");
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
}