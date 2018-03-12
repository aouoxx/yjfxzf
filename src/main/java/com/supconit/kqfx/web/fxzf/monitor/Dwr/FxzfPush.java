package com.supconit.kqfx.web.fxzf.monitor.Dwr;

import java.util.Collection;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FxzfPush {
	
	private static final Logger logger = LoggerFactory
			.getLogger(FxzfPush.class);
	
	public static void send(final String detectStation,final String fxzf){
		Runnable run = new Runnable() {
			private ScriptBuffer bf = new ScriptBuffer();
			@Override
			public void run() {
				bf.appendCall("showAll", fxzf);
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
//			Thread.sleep(1000*5);
			Browser.withAllSessions(run);
		} catch (Exception e) {
			logger.info("无页面推送:"+e.toString());
//			e.printStackTrace();
		}
	}

}
