package com.supconit.kqfx.web.analysis.entities;

import hc.base.domains.PK;

import java.util.Date;

/**
 * 非现执法每日按时间区间统计实体
 * @author gs
 *
 */
public class TimeZone implements PK<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	/**
	 * 机构ID
	 */
	private Long jgid;
	/**
	 * 治超点
	 */
	private String detectStation;
	
	/**
	 * 超重等级
	 */
	private Integer overloadStatus;
	
	/**
	 * 统计次数
	 */
	private Long reportTimes;
	
	/**
	 * 开始时间
	 */
	private Date beginTime;
	
	/**
	 * 结束时间
	 */
	private Date endTime;
	
	/**
	 * 统计日期
	 */
	private Date tjDate;
	
	/**
	 * 更新时间
	 */
	private Date updateTime;
	
	/**
	 * 按日期,小时统计使用虚拟字段
	 */
	private Integer Total;
	
	private String station;
	/**
	 * 按小时统计
	 */
	private Integer ah;
	private Integer bh;
	private Integer ch;
	private Integer dh;
	private Integer eh;
	private Integer fh;
	
	private Integer gh;
	private Integer hh;
	private Integer ih;
	private Integer jh;
	private Integer kh;
	private Integer lh;
	
	private Integer mh;
	private Integer nh;
	private Integer oh;
	private Integer ph;
	private Integer qh;
	private Integer rh;
	
	private Integer sh;
	private Integer th;
	private Integer uh;
	private Integer vh;
	private Integer wh;
	private Integer xh;

	
	/**
	 * 按日期统计
	 * @return
	 */
	private Integer aday;
	private Integer bday;
	private Integer cday;
	private Integer dday;
	private Integer eday;
	private Integer fday;
	
	private Integer gday;
	private Integer hday;
	private Integer iday;
	private Integer jday;
	private Integer kday;
	private Integer lday;
	
	private Integer mday;
	private Integer nday;
	private Integer oday;
	private Integer pday;
	private Integer qday;
	private Integer rday;
	
	private Integer sday;
	private Integer tday;
	private Integer uday;
	private Integer vday;
	private Integer wday;
	private Integer xday;
	private Integer yday;
	private Integer zday;
	
	private Integer aaday;
	private Integer abday;
	private Integer acday;
 	private Integer aeday;
	private Integer afday;
	
	/**
	 * 分页查询虚拟字段
	 */
	private Integer stationA;
	private Integer stationB;
	private Integer stationC;
	private Integer stationD;
	private Integer stationE;
	private String  timeString; 
	

	
	
	
	public Integer getStationA() {
		return stationA;
	}

	public void setStationA(Integer stationA) {
		this.stationA = stationA;
	}

	public Integer getStationB() {
		return stationB;
	}

	public void setStationB(Integer stationB) {
		this.stationB = stationB;
	}

	public Integer getStationC() {
		return stationC;
	}

	public void setStationC(Integer stationC) {
		this.stationC = stationC;
	}

	public Integer getStationD() {
		return stationD;
	}

	public void setStationD(Integer stationD) {
		this.stationD = stationD;
	}

	public Integer getStationE() {
		return stationE;
	}

	public void setStationE(Integer stationE) {
		this.stationE = stationE;
	}

	public String getTimeString() {
		return timeString;
	}

	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}

	public Integer getYday() {
		return yday;
	}

	public void setYday(Integer yday) {
		this.yday = yday;
	}

	public Integer getZday() {
		return zday;
	}

	public void setZday(Integer zday) {
		this.zday = zday;
	}

	public Integer getAday() {
		return aday;
	}

	public void setAday(Integer aday) {
		this.aday = aday;
	}

	public Integer getBday() {
		return bday;
	}

	public void setBday(Integer bday) {
		this.bday = bday;
	}

	public Integer getCday() {
		return cday;
	}

	public void setCday(Integer cday) {
		this.cday = cday;
	}

	public Integer getDday() {
		return dday;
	}

	public void setDday(Integer dday) {
		this.dday = dday;
	}

	public Integer getEday() {
		return eday;
	}

	public void setEday(Integer eday) {
		this.eday = eday;
	}

	public Integer getFday() {
		return fday;
	}

	public void setFday(Integer fday) {
		this.fday = fday;
	}

	public Integer getGday() {
		return gday;
	}

	public void setGday(Integer gday) {
		this.gday = gday;
	}

	public Integer getHday() {
		return hday;
	}

	public void setHday(Integer hday) {
		this.hday = hday;
	}

	public Integer getIday() {
		return iday;
	}

	public void setIday(Integer iday) {
		this.iday = iday;
	}

	public Integer getJday() {
		return jday;
	}

	public void setJday(Integer jday) {
		this.jday = jday;
	}

	public Integer getKday() {
		return kday;
	}

	public void setKday(Integer kday) {
		this.kday = kday;
	}

	public Integer getLday() {
		return lday;
	}

	public void setLday(Integer lday) {
		this.lday = lday;
	}

	public Integer getMday() {
		return mday;
	}

	public void setMday(Integer mday) {
		this.mday = mday;
	}

	public Integer getNday() {
		return nday;
	}

	public void setNday(Integer nday) {
		this.nday = nday;
	}

	public Integer getOday() {
		return oday;
	}

	public void setOday(Integer oday) {
		this.oday = oday;
	}

	public Integer getPday() {
		return pday;
	}

	public void setPday(Integer pday) {
		this.pday = pday;
	}

	public Integer getQday() {
		return qday;
	}

	public void setQday(Integer qday) {
		this.qday = qday;
	}

	public Integer getRday() {
		return rday;
	}

	public void setRday(Integer rday) {
		this.rday = rday;
	}

	public Integer getSday() {
		return sday;
	}

	public void setSday(Integer sday) {
		this.sday = sday;
	}

	public Integer getTday() {
		return tday;
	}

	public void setTday(Integer tday) {
		this.tday = tday;
	}

	public Integer getUday() {
		return uday;
	}

	public void setUday(Integer uday) {
		this.uday = uday;
	}

	public Integer getVday() {
		return vday;
	}

	public void setVday(Integer vday) {
		this.vday = vday;
	}

	public Integer getWday() {
		return wday;
	}

	public void setWday(Integer wday) {
		this.wday = wday;
	}

	public Integer getXday() {
		return xday;
	}

	public void setXday(Integer xday) {
		this.xday = xday;
	}

	public Integer getAaday() {
		return aaday;
	}

	public void setAaday(Integer aaday) {
		this.aaday = aaday;
	}

	public Integer getAbday() {
		return abday;
	}

	public void setAbday(Integer abday) {
		this.abday = abday;
	}

	public Integer getAcday() {
		return acday;
	}

	public void setAcday(Integer acday) {
		this.acday = acday;
	}

	public Integer getAeday() {
		return aeday;
	}

	public void setAeday(Integer aeday) {
		this.aeday = aeday;
	}

	public Integer getAfday() {
		return afday;
	}

	public void setAfday(Integer afday) {
		this.afday = afday;
	}

	public Integer getTotal() {
		return Total;
	}

	public void setTotal(Integer total) {
		Total = total;
	}

	public Integer getAh() {
		return ah;
	}

	public void setAh(Integer ah) {
		this.ah = ah;
	}

	public Integer getBh() {
		return bh;
	}

	public void setBh(Integer bh) {
		this.bh = bh;
	}

	public Integer getCh() {
		return ch;
	}

	public void setCh(Integer ch) {
		this.ch = ch;
	}

	public Integer getDh() {
		return dh;
	}

	public void setDh(Integer dh) {
		this.dh = dh;
	}

	public Integer getEh() {
		return eh;
	}

	public void setEh(Integer eh) {
		this.eh = eh;
	}

	public Integer getFh() {
		return fh;
	}

	public void setFh(Integer fh) {
		this.fh = fh;
	}

	public Integer getGh() {
		return gh;
	}

	public void setGh(Integer gh) {
		this.gh = gh;
	}

	public Integer getHh() {
		return hh;
	}

	public void setHh(Integer hh) {
		this.hh = hh;
	}

	public Integer getIh() {
		return ih;
	}

	public void setIh(Integer ih) {
		this.ih = ih;
	}

	public Integer getJh() {
		return jh;
	}

	public void setJh(Integer jh) {
		this.jh = jh;
	}

	public Integer getKh() {
		return kh;
	}

	public void setKh(Integer kh) {
		this.kh = kh;
	}

	public Integer getLh() {
		return lh;
	}

	public void setLh(Integer lh) {
		this.lh = lh;
	}

	public Integer getMh() {
		return mh;
	}

	public void setMh(Integer mh) {
		this.mh = mh;
	}

	public Integer getNh() {
		return nh;
	}

	public void setNh(Integer nh) {
		this.nh = nh;
	}

	public Integer getOh() {
		return oh;
	}

	public void setOh(Integer oh) {
		this.oh = oh;
	}

	public Integer getPh() {
		return ph;
	}

	public void setPh(Integer ph) {
		this.ph = ph;
	}

	public Integer getQh() {
		return qh;
	}

	public void setQh(Integer qh) {
		this.qh = qh;
	}

	public Integer getRh() {
		return rh;
	}

	public void setRh(Integer rh) {
		this.rh = rh;
	}

	public Integer getSh() {
		return sh;
	}

	public void setSh(Integer sh) {
		this.sh = sh;
	}

	public Integer getTh() {
		return th;
	}

	public void setTh(Integer th) {
		this.th = th;
	}

	public Integer getUh() {
		return uh;
	}

	public void setUh(Integer uh) {
		this.uh = uh;
	}

	public Integer getVh() {
		return vh;
	}

	public void setVh(Integer vh) {
		this.vh = vh;
	}

	public Integer getWh() {
		return wh;
	}

	public void setWh(Integer wh) {
		this.wh = wh;
	}

	public Integer getXh() {
		return xh;
	}

	public void setXh(Integer xh) {
		this.xh = xh;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getJgid() {
		return jgid;
	}

	public void setJgid(Long jgid) {
		this.jgid = jgid;
	}

	public String getDetectStation() {
		return detectStation;
	}

	public void setDetectStation(String detectStation) {
		this.detectStation = detectStation;
	}

	public Integer getOverloadStatus() {
		return overloadStatus;
	}

	public void setOverloadStatus(Integer overloadStatus) {
		this.overloadStatus = overloadStatus;
	}

	public Long getReportTimes() {
		return reportTimes;
	}

	public void setReportTimes(Long reportTimes) {
		this.reportTimes = reportTimes;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getTjDate() {
		return tjDate;
	}

	public void setTjDate(Date tjDate) {
		this.tjDate = tjDate;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
	public Integer getCntDays(String day){
		int Cnt = 1;
		switch (day) {
		case "01":Cnt=this.getAday();break;
		case "02":Cnt=this.getBday();break;
		case "03":Cnt=this.getCday();break;
		case "04":Cnt=this.getDday();break;
		case "05":Cnt=this.getEday();break;
		case "06":Cnt=this.getFday();break;
		case "07":Cnt=this.getGday();break;
		case "08":Cnt=this.getHday();break;
		case "09":Cnt=this.getIday();break;
		case "10":Cnt=this.getJday();break;
		case "11":Cnt=this.getKday();break;
		case "12":Cnt=this.getLday();break;
		case "13":Cnt=this.getMday();break;
		case "14":Cnt=this.getNday();break;
		case "15":Cnt=this.getOday();break;
		case "16":Cnt=this.getPday();break;
		case "17":Cnt=this.getQday();break;
		case "18":Cnt=this.getRday();break;
		case "19":Cnt=this.getSday();break;
		case "20":Cnt=this.getTday();break;
		case "21":Cnt=this.getUday();break;
		case "22":Cnt=this.getVday();break;
		case "23":Cnt=this.getWday();break;
		case "24":Cnt=this.getXday();break;
		case "25":Cnt=this.getYday();break;
		case "26":Cnt=this.getZday();break;

		case "27":Cnt=this.getAaday();break;
		case "28":Cnt=this.getAbday();break;
		case "29":Cnt=this.getAcday();break; 
		case "30":Cnt=this.getAeday();break;
		case "31":Cnt=this.getAfday();break;
		default:
			break;
		}
		return Cnt;
	}
	

	
	public Integer getCntHours(String hour){
		int Cnt = 1;
		switch (hour) {
		case "0~1点":Cnt=this.getAh();break;
		case "1~2点":Cnt=this.getBh();break;
		case "2~3点":Cnt=this.getCh();break;
		case "3~4点":Cnt=this.getDh();break;
		case "4~5点":Cnt=this.getEh();break;
		case "5~6点":Cnt=this.getFh();break;
		case "6~7点":Cnt=this.getGh();break;
		case "7~8点":Cnt=this.getHh();break;
		case "8~9点":Cnt=this.getIh();break;
		case "9~10点":Cnt=this.getJh();break;
		case "10~11点":Cnt=this.getKh();break;
		case "11~12点":Cnt=this.getLh();break;
		
		case "12~13点":Cnt=this.getMh();break;
		case "13~14点":Cnt=this.getNh();break;
		case "14~15点":Cnt=this.getOh();break;
		case "15~16点":Cnt=this.getPh();break;
		case "16~17点":Cnt=this.getQh();break;
		case "17~18点":Cnt=this.getRh();break;
		case "18~19点":Cnt=this.getSh();break;
		case "19~20点":Cnt=this.getTh();break;
		case "20~21点":Cnt=this.getUh();break;
		case "21~22点":Cnt=this.getVh();break;
		case "22~23点":Cnt=this.getWh();break;
		case "23~24点":Cnt=this.getXh();break;
		default:
			break;
		}
		return Cnt;
	}
}
