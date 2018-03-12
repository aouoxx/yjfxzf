package com.supconit.kqfx.kqfx.controllers;

import hc.base.domains.AjaxMessage;
import hc.safety.manager.SafetyManager;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.supconit.honeycomb.business.authorization.entities.Menu;
import com.supconit.honeycomb.business.authorization.entities.User;
import com.supconit.honeycomb.business.authorization.services.MenuService;
import com.supconit.kqfx.web.xtgl.listener.OnLineUserListener;


/**
 * @author yuhui@supcon.com
 * @create 2015-07-07T23:00:28

 * @since 1.0.0-SNAPSHOT
 *
 */
@Controller
@RequestMapping("/")
public class IndexController {
	
	@Autowired
	private MenuService menuService;
	@Resource
	private HttpServletRequest request;
	@Autowired
	private SafetyManager safetyManager;
	
	private transient static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@Value("${ip.in}")
	private String ipIn;
	@Value("${ip.out}")
	private String ipOut;
	@Value("${imageServer.in}")
	private String imageServerIn;
	@Value("${imageServer.out}")
	private String imageServerOut;
	
	
	 @RequestMapping(method = RequestMethod.GET)
     public String app(ModelMap model) {
		 
		//以下为在线用户功能
		HttpSession session =  request.getSession();//取得session
		String ip=request.getRemoteAddr();//取得当前用户的ip地址
		User aUser = (User) safetyManager.getAuthenticationInfo().getUser();//取得当前正在登录用户的信息
		aUser.setAvatar(ip);//user将某一个属性设置为IP
		OnLineUserListener.getOnLineUserMap().put(session.getId(), aUser);//将登录信息写入一个在OnLineUserListener里面定义的全局静态map里面
		//以上为在线用户功能
		
		/**
		 * 获取登陆用户关联的人名
		 */
		if(aUser.getPerson()!=null){
			if(!aUser.getPerson().getName().equals("")&&aUser.getPerson().getName()!=null){
				session.setAttribute("username", aUser.getPerson().getName());
				model.put("username", aUser.getPerson().getName());
			}else{
				session.setAttribute("username", "xxx");
				model.put("username", "xxx");
			}	
		}
		
		/**
		 * 获取请求系统的IP,用于判断是内网还是外网
		 */
		String localIp = request.getLocalAddr();
		if(localIp.equals(ipIn)){
			session.setAttribute("imageServer", imageServerIn);
		}else if(localIp.equals(ipOut)){
			logger.info("*************外部请求的IP地址为："+localIp+"***************");
			logger.info("*************ipOut："+ipOut+"***************");
			logger.info("*************imageServerOut："+imageServerOut+"***************");
			session.setAttribute("imageServer", imageServerOut);
		}else{
			logger.info("*************请求的IP地址为："+localIp+"***************");
			session.setAttribute("imageServer", imageServerOut);
		}
		
    	//获取菜单
    	User user = (User) safetyManager.getCurrentUser();
    	Menu menu = menuService.getUserMenuTree(user.getId(), "ROOT_FUNCTION");
    	model.put("menu", JSON.toJSONString(menu));
        return "index/app";
     }
	
	
	 @RequestMapping(value = "index", method = RequestMethod.GET)
     public String index(String code,ModelMap map) {
    	User user = (User) safetyManager.getCurrentUser();
		Menu root_menu = menuService.getUserMenuTree(user.getId(), "ROOT_FUNCTION");
		Menu menus = menuService.getUserMenuTree(user.getId(), code);
		map.put("root_menu", root_menu);
		map.put("menus", menus);
		if(user.getPerson()!=null){
			if(!user.getPerson().getName().equals("")&&user.getPerson().getName()!=null){
				map.put("username", user.getPerson().getName());
			}else{
				map.put("username", "xxx");
			}
		}
        return "index/index";
    }

    @RequestMapping(value = "unauthenticated")
    public String unauthenticated() {
        return "unauthenticated";
    }

    @RequestMapping(value = "unauthorized")
    public String unauthorized() {
        return "unauthorized";
    }
    
    @ResponseBody
    @RequestMapping("ajaxSession")
    public AjaxMessage ajaxSession(){
    	hc.safety.support.User u = safetyManager.getCurrentUser();
    	if(u == null){
    		return AjaxMessage.success(401);
    	}
    	return AjaxMessage.success(200);
    }
}
