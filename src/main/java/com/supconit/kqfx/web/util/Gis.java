package com.supconit.kqfx.web.util;
/**
 * GIS页面的工具栏和图层显示配置类.
 * 参数说明：该类对应地图公共页面的参数化配置类.
 * 参数类型分为地图的宽高、图层、交通流量
 * 宽高为innerWidth、innerHeight
 * 图层为isLayer,如果要显示图层必须isLayer为true,然后其他的参数就是具体图层显示的功能,图层具体功能包括是否显示此图层、此图层是否默认显示、图层参数
 * 交通流量为isYjd,包括是否显示及是否默认显示
 * @author liuxiaohuo
 * 
 */
public class Gis {
	/** 是否调用画点服务 */
	private Boolean isDrawOnePoint;
	/** 外面调用弹出对话框的id */
	private String dlgId;
	/** 工具栏 */
	private boolean isTool;
	/** 显示此经度为中心 */
	private String jd;
	/** 显示此纬度为中心 */
	private String wd;
	/** 宽度 */
	private Integer innerWidth;
	/** 高度 */
	private Integer innerHeight;
	/** 处置信息 */
	private boolean isCzxx;
	/** 处置信息参数 */
	private String czxxSjId;
	/** 交通流量  */
	private boolean isYjd;
	/** 交通流量默认是否显示  */
	private boolean yjdDefault;
	/** 图层  */
	private boolean isLayer;
	/** 情报板  */
	private boolean isQbb;
	/** 情报板默认是否显示  */
	private boolean qbbDefault;
	/** 视频调度 */
	private boolean isSpdd;
	/** 视频调度默认是否显示 */
	private boolean spddDefault;
	/** 施工  */
	private boolean isSggl;
	/** 施工默认是否显示  */
	private boolean sgglDefault;
	/** 风险隐患  */
	private boolean isFxyhgl;
	/** 风险隐患默认是否显示  */
	private boolean fxyhglDefault;
	/** 巡逻车  */
	private boolean isVehicle;
	/** 巡逻车默认是否显示  */
	private boolean vehicleDefault;
	/** 单兵  */
	private boolean isDb;
	/** 单兵默认是否显示  */
	private boolean dbDefault;
	/** 应急抢险中心  */
	private boolean isYjqxzx;
	/** 应急抢险中心默认是否显示  */
	private boolean yjqxzxDefault;
	/** 应急物资储备站点 */
	private boolean isYjwzcbzd;
	/** 应急物资储备站点默认是否显示 */
	private boolean yjwzcbzdDefault;
	/** 应急相关单位  */
	private boolean isYjxgdw;
	/** 应急相关单位默认是否显示  */
	private boolean yjxgdwDefault;
	/** 影响区域  */
	private boolean isYxqy;
	/** 影响区域 默认是否显示 */
	private boolean yxqyDefault;
	/** 影响区域 参数 */
	private String yxqyZnfaId;
	/** 路径诱导  */
	private boolean isLjyd;
	/** 路径诱导 默认是否显示 */
	private boolean ljydDefault;
	/** 路径诱导参数 */
	private String ljydZnfaId;
	/** 机构  */
	private boolean isJgxx;
	/** 机构 默认是否显示 */
	private boolean jgxxDefault;
	/** 公路站  */
	private boolean isGlz;
	/** 公路站 默认是否显示 */
	private boolean glzDefault;
	/** 路政中队  */
	private boolean isLzzd;
	/** 路政中队默认是否显示 */
	private boolean lzzdDefault;
	/** 桥梁  */
	private boolean isQl;
	/** 桥梁 默认是否显示 */
	private boolean qlDefault;
	/** 隧道  */
	private boolean isSd;
	/** 隧道 默认是否显示 */
	private boolean sdDefault;
	/** 治超站  */
	private boolean isZcz;
	/** 治超站 默认是否显示 */
	private boolean zczDefault;
	/** 加油站  */
	private boolean isJyz;
	/** 加油站默认是否显示 */
	private boolean jyzDefault;
	/** 收费站 */
	private boolean isSfz;
	/** 收费站默认是否显示 */
	private boolean sfzDefault;
	/** 服务区  */
	private boolean isFwq;
	/** 服务区默认是否显示 */
	private boolean fwqDefault;
	/** 行政区划  */
	private boolean isXzqh;
	/** 行政区划默认是否显示 */
	private boolean xzqhDefault;
	/** 县乡道  */
	private boolean isXxd;
	/** 县乡道  默认是否显示 */
	private boolean xxdDefault;
	
	public boolean getIsQbb() {
		return isQbb;
	}
	public void setIsQbb(boolean isQbb) {
		this.isQbb = isQbb;
	}
	public boolean getIsSpdd() {
		return isSpdd;
	}
	public void setIsSpdd(boolean isSpdd) {
		this.isSpdd = isSpdd;
	}
	public boolean getIsSggl() {
		return isSggl;
	}
	public void setIsSggl(boolean isSggl) {
		this.isSggl = isSggl;
	}
	public boolean getIsFxyhgl() {
		return isFxyhgl;
	}
	public void setIsFxyhgl(boolean isFxyhgl) {
		this.isFxyhgl = isFxyhgl;
	}
	public boolean getIsVehicle() {
		return isVehicle;
	}
	public void setIsVehicle(boolean isVehicle) {
		this.isVehicle = isVehicle;
	}
	public boolean getIsDb() {
		return isDb;
	}
	public void setIsDb(boolean isDb) {
		this.isDb = isDb;
	}
	public boolean getIsLayer() {
		return isLayer;
	}
	public void setIsLayer(boolean isLayer) {
		this.isLayer = isLayer;
	}
	public Integer getInnerWidth() {
		return innerWidth;
	}
	public void setInnerWidth(Integer innerWidth) {
		this.innerWidth = innerWidth;
	}
	public Integer getInnerHeight() {
		return innerHeight;
	}
	public void setInnerHeight(Integer innerHeight) {
		this.innerHeight = innerHeight;
	}
	public boolean getIsYjd() {
		return isYjd;
	}
	public void setIsYjd(boolean isYjd) {
		this.isYjd = isYjd;
	}
	public boolean getIsYjqxzx() {
		return isYjqxzx;
	}
	public void setIsYjqxzx(boolean isYjqxzx) {
		this.isYjqxzx = isYjqxzx;
	}
	public boolean getIsYjwzcbzd() {
		return isYjwzcbzd;
	}
	public void setIsYjwzcbzd(boolean isYjwzcbzd) {
		this.isYjwzcbzd = isYjwzcbzd;
	}
	public boolean getIsYjxgdw() {
		return isYjxgdw;
	}
	public void setIsYjxgdw(boolean isYjxgdw) {
		this.isYjxgdw = isYjxgdw;
	}
	public boolean isYjdDefault() {
		return yjdDefault;
	}
	public void setYjdDefault(boolean yjdDefault) {
		this.yjdDefault = yjdDefault;
	}
	public boolean isQbbDefault() {
		return qbbDefault;
	}
	public void setQbbDefault(boolean qbbDefault) {
		this.qbbDefault = qbbDefault;
	}
	public boolean isSpddDefault() {
		return spddDefault;
	}
	public void setSpddDefault(boolean spddDefault) {
		this.spddDefault = spddDefault;
	}
	public boolean isSgglDefault() {
		return sgglDefault;
	}
	public void setSgglDefault(boolean sgglDefault) {
		this.sgglDefault = sgglDefault;
	}
	public boolean isFxyhglDefault() {
		return fxyhglDefault;
	}
	public void setFxyhglDefault(boolean fxyhglDefault) {
		this.fxyhglDefault = fxyhglDefault;
	}
	public boolean isVehicleDefault() {
		return vehicleDefault;
	}
	public void setVehicleDefault(boolean vehicleDefault) {
		this.vehicleDefault = vehicleDefault;
	}
	public boolean isDbDefault() {
		return dbDefault;
	}
	public void setDbDefault(boolean dbDefault) {
		this.dbDefault = dbDefault;
	}
	public boolean isYjqxzxDefault() {
		return yjqxzxDefault;
	}
	public void setYjqxzxDefault(boolean yjqxzxDefault) {
		this.yjqxzxDefault = yjqxzxDefault;
	}
	public boolean isYjwzcbzdDefault() {
		return yjwzcbzdDefault;
	}
	public void setYjwzcbzdDefault(boolean yjwzcbzdDefault) {
		this.yjwzcbzdDefault = yjwzcbzdDefault;
	}
	public boolean isYjxgdwDefault() {
		return yjxgdwDefault;
	}
	public void setYjxgdwDefault(boolean yjxgdwDefault) {
		this.yjxgdwDefault = yjxgdwDefault;
	}
	public boolean isYxqyDefault() {
		return yxqyDefault;
	}
	public void setYxqyDefault(boolean yxqyDefault) {
		this.yxqyDefault = yxqyDefault;
	}
	public boolean isLjydDefault() {
		return ljydDefault;
	}
	public void setLjydDefault(boolean ljydDefault) {
		this.ljydDefault = ljydDefault;
	}
	public boolean getIsYxqy() {
		return isYxqy;
	}
	public void setIsYxqy(boolean isYxqy) {
		this.isYxqy = isYxqy;
	}
	public boolean getIsLjyd() {
		return isLjyd;
	}
	public void setIsLjyd(boolean isLjyd) {
		this.isLjyd = isLjyd;
	}
	public String getYxqyZnfaId() {
		return yxqyZnfaId;
	}
	public void setYxqyZnfaId(String yxqyZnfaId) {
		this.yxqyZnfaId = yxqyZnfaId;
	}
	public String getLjydZnfaId() {
		return ljydZnfaId;
	}
	public void setLjydZnfaId(String ljydZnfaId) {
		this.ljydZnfaId = ljydZnfaId;
	}
	public boolean getIsCzxx() {
		return isCzxx;
	}
	public void setIsCzxx(boolean isCzxx) {
		this.isCzxx = isCzxx;
	}
	public String getCzxxSjId() {
		return czxxSjId;
	}
	public void setCzxxSjId(String czxxSjId) {
		this.czxxSjId = czxxSjId;
	}
	public void setYxqy(boolean isYxqy) {
		this.isYxqy = isYxqy;
	}
	public boolean getIsJgxx() {
		return isJgxx;
	}
	public void setIsJgxx(boolean isJgxx) {
		this.isJgxx = isJgxx;
	}
	public boolean getJgxxDefault() {
		return jgxxDefault;
	}
	public void setJgxxDefault(boolean jgxxDefault) {
		this.jgxxDefault = jgxxDefault;
	}
	public boolean getIsQl() {
		return isQl;
	}
	public void setIsQl(boolean isQl) {
		this.isQl = isQl;
	}
	public boolean getIsQlDefault() {
		return qlDefault;
	}
	public void setIsQlDefault(boolean qlDefault) {
		this.qlDefault = qlDefault;
	}
	public boolean getIsSd() {
		return isSd;
	}
	public void setIsSd(boolean isSd) {
		this.isSd = isSd;
	}
	public boolean getIsSdDefault() {
		return sdDefault;
	}
	public void setIsSdDefault(boolean sdDefault) {
		this.sdDefault = sdDefault;
	}
	public boolean getIsZcz() {
		return isZcz;
	}
	public void setIsZcz(boolean isZcz) {
		this.isZcz = isZcz;
	}
	public boolean getZczDefault() {
		return zczDefault;
	}
	public void setZczDefault(boolean zczDefault) {
		this.zczDefault = zczDefault;
	}
	public boolean getIsJyz() {
		return isJyz;
	}
	public void setIsJyz(boolean isJyz) {
		this.isJyz = isJyz;
	}
	public boolean getJyzDefault() {
		return jyzDefault;
	}
	public void setJyzDefault(boolean jyzDefault) {
		this.jyzDefault = jyzDefault;
	}
	public boolean getIsSfz() {
		return isSfz;
	}
	public void setIsSfz(boolean isSfz) {
		this.isSfz = isSfz;
	}
	public boolean getSfzDefault() {
		return sfzDefault;
	}
	public void setSfzDefault(boolean sfzDefault) {
		this.sfzDefault = sfzDefault;
	}
	public boolean getIsFwq() {
		return isFwq;
	}
	public void setIsFwq(boolean isFwq) {
		this.isFwq = isFwq;
	}
	public boolean getFwqDefault() {
		return fwqDefault;
	}
	public void setFwqDefault(boolean fwqDefault) {
		this.fwqDefault = fwqDefault;
	}
	public boolean getIsXzqh() {
		return isXzqh;
	}
	public void setIsXzqh(boolean isXzqh) {
		this.isXzqh = isXzqh;
	}
	public boolean getXzqhDefault() {
		return xzqhDefault;
	}
	public void setXzqhDefault(boolean xzqhDefault) {
		this.xzqhDefault = xzqhDefault;
	}
	public String getJd() {
		return jd;
	}
	public void setJd(String jd) {
		this.jd = jd;
	}
	public String getWd() {
		return wd;
	}
	public void setWd(String wd) {
		this.wd = wd;
	}
	public boolean getIsXxd() {
		return isXxd;
	}
	public void setIsXxd(boolean isXxd) {
		this.isXxd = isXxd;
	}
	public boolean getXxdDefault() {
		return xxdDefault;
	}
	public void setXxdDefault(boolean xxdDefault) {
		this.xxdDefault = xxdDefault;
	}
	public String getDlgId() {
		return dlgId;
	}
	public void setDlgId(String dlgId) {
		this.dlgId = dlgId;
	}
	public Boolean getIsDrawOnePoint() {
		return isDrawOnePoint;
	}
	public void setIsDrawOnePoint(Boolean isDrawOnePoint) {
		this.isDrawOnePoint = isDrawOnePoint;
	}
	public boolean getIsGlz() {
		return isGlz;
	}
	public void setIsGlz(boolean isGlz) {
		this.isGlz = isGlz;
	}
	public boolean getGlzDefault() {
		return glzDefault;
	}
	public void setGlzDefault(boolean glzDefault) {
		this.glzDefault = glzDefault;
	}
	public boolean getIsLzzd() {
		return isLzzd;
	}
	public void setIsLzzd(boolean isLzzd) {
		this.isLzzd = isLzzd;
	}
	public boolean getLzzdDefault() {
		return lzzdDefault;
	}
	public void setLzzdDefault(boolean lzzdDefault) {
		this.lzzdDefault = lzzdDefault;
	}
	public boolean getIsTool() {
		return isTool;
	}
	public void setIsTool(boolean isTool) {
		this.isTool = isTool;
	}
	
}
