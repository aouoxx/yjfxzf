package com.supconit.kqfx.web.util;


/**
 * 线程工具类
 * @author caijianming
 *
 */
public class ThreadUtil {

		/**
		 * 获取当前运行的全部线程
		 * @return
		 */
		public static Thread[] findAllThreads(){
			ThreadGroup group = Thread.currentThread().getThreadGroup();
			ThreadGroup topGroup = group;
			while(group !=null)
			{
				topGroup = group;
				group = group.getParent();
			}
			int estimatedSize = topGroup.activeCount()*2;
			Thread[] slackList = new Thread[estimatedSize];
			
			int actualSize = topGroup.enumerate(slackList);
			Thread[] list = new Thread[actualSize];
			System.arraycopy(slackList, 0, list, 0, actualSize);
			return list;
		}
}
