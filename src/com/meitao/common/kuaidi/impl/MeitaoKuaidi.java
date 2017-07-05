package com.meitao.common.kuaidi.impl;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.meitao.common.kuaidi.KuaiDi;

public class MeitaoKuaidi implements KuaiDi{
	
	/*public static void main(String[] agrs) {
		MeitaoKuaidi kuaidi = new MeitaoKuaidi();
		// http://api.kuaidi100.com/api?id=&com=huitongkuaidi&nu=&show=2&muti=1&order=desc
		String content = null;
		try {
			content = kuaidi.SearchkuaiDiInfo("DCS11072033748US");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(content);
	}*/

	@Override
	public String SearchkuaiDiInfo(String orderId, String ...args) throws Exception {
		// TODO Auto-generated method stub
		String content = "";
		URL url = new URL("http://usitao.net/TrackSearch.aspx?TXT_TRACKNO="+orderId);
		System.out.println("http://usitao.net/TrackSearch.aspx?TXT_TRACKNO="+orderId);
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