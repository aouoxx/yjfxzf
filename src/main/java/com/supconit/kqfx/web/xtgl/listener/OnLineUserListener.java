package com.supconit.kqfx.web.xtgl.listener;

import hc.safety.manager.SafetyManager;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;

import com.supconit.honeycomb.business.authorization.entities.User;

public class OnLineUserListener implements  ServletContextListener, ServletContextAttributeListener, HttpSessionListener, HttpSessionAttributeListener{
	@Autowired
	private SafetyManager safetyManager;
	
	private static final  HashMap<String,User> onLineUserMap=new HashMap<String, User>();
	private ServletContext context = null;
	
    public void sessionCreated(HttpSessionEvent event) {
    	
    	
    }

    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
       
        onLineUserMap.remove(session.getId());
 
    }

	@Override
	public void attributeAdded(HttpSessionBindingEvent arg0) {
		
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent arg0) {
		
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		
	}

	@Override
	public void attributeAdded(ServletContextAttributeEvent arg0) {
		
	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent arg0) {
		
	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent arg0) {
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		 this.context = sce.getServletContext();
		 this.context.setAttribute("online", new HashMap());
	}

	public static HashMap<String, User> getOnLineUserMap() {
		return onLineUserMap;
	}

//	public static void setOnLineUserMap(HashMap<String, String> onLineUserMap) {
//		MyListener.onLineUserMap = onLineUserMap;
//	}
	
}

