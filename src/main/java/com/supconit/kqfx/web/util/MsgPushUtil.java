package com.supconit.kqfx.web.util;

import java.util.Collection;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgPushUtil {
	
	
	private transient static final Logger logger = LoggerFactory
			.getLogger(MsgPushUtil.class);
	
	/**
	 * 将信息推送到前台，然后前台同DWR框架调用信息
	 * @param msg
	 */
	public static void send(final String msg){
		Runnable run = new Runnable() {
			private ScriptBuffer bf = new ScriptBuffer();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//设置需要调用的JS和参数
				bf.appendCall("show", msg);
				//得到所有的ScriptSession
				Collection<ScriptSession> sessions = Browser.getTargetSessions();
				//遍历所有的session
				for(ScriptSession session : sessions){
					session.addScript(bf);
				}
			}
		};
		try {
			//执行推送
			Browser.withAllSessions(run);
		} catch (Exception e) {
			logger.info("无页面推送:"+e.toString());
			e.printStackTrace();
		}
	}

}
