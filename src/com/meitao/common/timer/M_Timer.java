package com.meitao.common.timer;

import java.text.SimpleDateFormat; 
import java.util.Date; 
import java.util.TimerTask; 

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.common.util.UserUtil;
import com.meitao.dao.MessageControlDao;
import com.meitao.dao.MorderDao;
import com.meitao.model.M_order;
import com.meitao.model.MessageControl;
import com.meitao.model.ResponseObject;
import com.meitao.service.MorderService;



public class M_Timer extends TimerTask {
	//揽收短信发送通知参数
	private int morder_2_seconds=1000;//每10s检查一次
	private int morder_2_number=0;//计算执行的次数
	private boolean cur_morder_2_state=false;//当前是否正在执行状态
	
	@Autowired
	private MorderDao m_orderDao;
	
	@Autowired
	private MessageControlDao messageControlDao;
	
	@Resource(name = "m_orderService")
	private MorderService m_orderService;
	
	@Override 
	public void run() { 
	//Date date = new Date(); 
	//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	//System.out.println(sdf.format(date) + "{*****继承TimerTask类，实现run方法,此处定时需要处理的业务*****}"); 
		//处理相关业务,判断当前是否下在执行
		if(cur_morder_2_state==false&&morder_2_number>morder_2_seconds)
		{
			cur_morder_2_state=true;
			morder_2_ms();
			morder_2_number=0;
			cur_morder_2_state=false;
		}
		else
		{
			morder_2_number++;
		}
	} 
	//揽收短信通知
	private void morder_2_ms()
	{
		try{
			MessageControl  control=this.messageControlDao.getonebyflag("auto_send_2");
			if(control==null)
			{
				return ;
			}
			if(StringUtil.isEmpty(control.getAuto())||(!control.getAuto().equalsIgnoreCase("1")))//不自动发送
			{
				return ;
			}
			
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date=sdf.parse(control.getContent());//定时发送的时间
			Date ndate=new Date();
			String nowDate=sdf.format(ndate);
			
			if(DateUtil.compareDate(sdf.parse(nowDate), sdf.parse(control.getContent()))>0)//表示记录时间大于当前时间，直接保存
			{
				
			}
			else
			{
				return;
			}
			
			
			
			
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			
			String sd_date=sdf1.format(new Date());//只管当天，要不如果停机很外了，突然开机将会出现问题
			
			String s_date = UserUtil.transformerDateString(sd_date, " 00:00:00");
			String e_date = UserUtil.transformerDateString(sd_date, " 23:59:59");
			
			//自动发送信息
			this.m_orderService.send_rev_message("0",s_date, e_date,"自动发送揽收短信");
			
			//保存下一天发送的时间
			//保存下一个发送时间
			String content=control.getContent().substring(10, control.getContent().length());
			Date d= DateUtil.getNextDate(ndate);
			String str=sdf1.format(d);
			content=str+content;
			control.setContent(content);
			String modifyDate=sdf.format(new Date());
			control.setModifyDate(modifyDate);
			control.setRemark("自动发送揽收变更!");
			int k=this.messageControlDao.updateContent(control);
			
			
		}catch(Exception e){
			System.out.print("发生异常！");
			cur_morder_2_state=false;
		}
	}
	
}
