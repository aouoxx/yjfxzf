package com.supconit.kqfx.web.fxzf.msg.services.Impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.supconit.honeycomb.business.authorization.entities.Role;
import com.supconit.honeycomb.business.authorization.services.RoleService;
import com.supconit.kqfx.web.fxzf.msg.daos.MsgDao;
import com.supconit.kqfx.web.fxzf.msg.entities.Msg;
import com.supconit.kqfx.web.fxzf.msg.services.MsgService;
import com.supconit.kqfx.web.fxzf.msg.services.PushMsgFlagService;
import com.supconit.kqfx.web.xtgl.entities.ExtUser;
import com.supconit.kqfx.web.xtgl.services.ExtUserService;

@Service("taizhou_offsite_enforcement_msg_service")
public class MsgServiceImpl implements MsgService {

	@Autowired
	private MsgDao msgDao;
	
	@Autowired
	private ExtUserService extUserService;
	
	@Autowired
	private PushMsgFlagService pushMsgFlagService;
	
	@Autowired
	private RoleService roleService;
	
	@Value("${msg.hour}")
	private  String msgHour;   /*告警有效小时时间*/

	@Value("${msg.min}")
	private  String msgMin;   /*告警有效期分钟*/
	
	@Value("${android.apiKey}")
	private  String apiKeyAndroid; ; /*手机端对应应用的KEY*/
	
	@Value("${android.secretKey}")
	private  String secretKeyAndroid; ; /*手机端对应应用的KEY*/
	
	@Value("${ios.apiKey}")
	private  String apiKeyIos; ; /*手机端对应应用的KEY*/
	
	@Value("${ios.secretKey}")
	private  String secretKeyIos; ; /*手机端对应应用的KEY*/
	
	@Value("${device.android}")
	private String android;	/*安卓手机类型*/
	
	@Value("${device.ios}")
	private String ios;	/*苹果手机类型*/
	
	
	private transient static final Logger logger = LoggerFactory.getLogger(MsgServiceImpl.class);
	
	@Override
	public Msg getById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Msg arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insert(Msg entity) {
		msgDao.insert(entity);
	}

	@Override
	public void update(Msg entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Msg entity) {
		msgDao.delete(entity);
	}

	@Override
	public void insertWarnInfo(String pid, String flag, Date updateTime,
			String content,String jgid) {
		//根据告警或公告的JGID来获取对应的告警或公告要发送的用户
		List<String> userId = getWebUserId(jgid);
		//告警信息设置有效期
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(updateTime);
			calendar.add(Calendar.HOUR, Integer.valueOf(msgHour)); 
			calendar.add(Calendar.MINUTE, Integer.valueOf(msgMin));
			Date usefulDate = calendar.getTime();
		List<Msg> msgs = new ArrayList<Msg>();
		for(String iid : userId){
			Msg obj = new Msg();
			obj.setId("W_"+pid);
			obj.setFlag(flag);
			obj.setContent(content);
			obj.setUserId(iid);
			obj.setJgid(jgid);
			obj.setPid(pid);
			obj.setUpdateTime(usefulDate);
			obj.setUpdateTime(updateTime);
			obj.setUsefuldateTime(usefulDate);
			msgs.add(obj);
		}
		//批处理插入
		msgDao.batchInert(msgs);
	}
	
	
	/**
	 * 根据JGID获取找到的userid,WEB添加告警和公告信息的时候,添加上对应的JGID和超级管理员管理关联的信息
	 * 便于在进行添加用户的时候,可以将相应的信息关联到该用户中
	 * @param jgid
	 * @return
	 */
	public List<String> getWebUserId(String jgid){
		String[] jgids = jgid.split(",");
		List<String> userId = new ArrayList<String>();
		for(int i=0;i<jgids.length ; i++){
			List<ExtUser> userJgbh = extUserService.getUserIdByJgbh(Long.valueOf(jgids[i]));
			if(!CollectionUtils.isEmpty(userJgbh)){
				//用户非空将用户信息插入
				for(ExtUser user: userJgbh){
					userId.add(String.valueOf(user.getId()));
				}
			}
			//将告警和警告信息添加到对应的角色中，用于添加新的用户或切换用户是获取未结束的告警或警告信息
			userId.add(jgids[i]);
		}
		//添加超级管理员角色，用户添加新的超级管理员时获取未结束的告警或警告信息
		userId.add("133_134");
		
		//根据超级管理员角色获取所有对应的用户
		Role admin = roleService.getByCode("ROLE_ADMIN");
		List<ExtUser> userRole = extUserService.getUserIdByRoleId(admin.getId());
		if(!CollectionUtils.isEmpty(userRole)){
			for(ExtUser user: userRole){
				if(!userId.contains(String.valueOf(user.getId()))){
					userId.add(String.valueOf(user.getId()));
				}
			}
		}
		return userId;
	}
	
	/**
	 * 根据JGID获取找到的userid,仅获取与该jgid关联的用户和超级管理员用户，用于手机消息推送
	 * @param jgid
	 * @return
	 */
	public List<String> getAppUserId(String jgid){
		String[] jgids = jgid.split(",");
		List<String> userId = new ArrayList<String>();
		for(int i=0;i<jgids.length ; i++){
			List<ExtUser> userJgbh = extUserService.getUserIdByJgbh(Long.valueOf(jgids[i]));
			if(!CollectionUtils.isEmpty(userJgbh)){
				//用户非空将用户信息插入
				for(ExtUser user: userJgbh){
					userId.add(String.valueOf(user.getId()));
				}
			}
		}
		//根据超级管理员角色获取所有对应的用户
		Role admin = roleService.getByCode("ROLE_ADMIN");
		List<ExtUser> userRole = extUserService.getUserIdByRoleId(admin.getId());
		if(!CollectionUtils.isEmpty(userRole)){
			for(ExtUser user: userRole){
				if(!userId.contains(String.valueOf(user.getId()))){
					userId.add(String.valueOf(user.getId()));
				}
			}
		}
		return userId;
	}

	/**
	 * 根据ID删除对应的信息
	 */
	@Override
	public void deleteById(String id) {
		msgDao.deleteById(id);
		
	}

	@Override
	public void inserNotifyInfo(String pid, String flag, Date updateTime,
			Date usefulDate, String content, String jgid) {
		List<String> userId = getWebUserId(jgid);
		List<Msg> msgs = new ArrayList<Msg>();
		for(String iid : userId){
			Msg obj = new Msg();
			obj.setId("N_"+pid);
			obj.setFlag(flag);
			obj.setContent(content);
			obj.setUserId(iid);
			obj.setJgid(jgid);
			obj.setPid(pid);
			obj.setUpdateTime(usefulDate);
			obj.setUpdateTime(updateTime);
			obj.setUsefuldateTime(usefulDate);
			msgs.add(obj);
		}
		//批处理插入
		msgDao.batchInert(msgs);
	}

	@Override
	public List<Msg> getByUserId(String id) {
		// TODO Auto-generated method stub
		return msgDao.getByUserId(id);
	}

	@Override
	public void deleteByUsefulDate(Msg condition) {
		msgDao.deleteByUsefulDate(condition);
		
	}

	@Override
	public void deleteByUserId(Msg msg) {
		msgDao.deleteByUserId(msg);
	}

	
	
}
