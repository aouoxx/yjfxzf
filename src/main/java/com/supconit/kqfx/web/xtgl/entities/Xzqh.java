/**
 * 
 */
package com.supconit.kqfx.web.xtgl.entities;
						

/**
 * @author 
 * 
 * @create 2014-03-17 16:03:40 
 * @since 
 * 修改by 贺秉廷 加构造函数
 */
public class Xzqh  implements hc.base.domains.PK<String>{

		
	/**
	 * 
	 */
	private static final long serialVersionUID = 285706673457994113L;
	private String id;
	//行政区划代码
	private String xzqhdm;
	//地区名称
	private String dqmc;
	//地区全称
	private String dqqc;
	//所属区县拼音简称
	private String ssqxpyjc;
		
	public 	Xzqh(){
		super();
	}
	
	public Xzqh(String dqmc){
		this.dqmc = dqmc;
	}
	public String getXzqhdm() {
		return xzqhdm;
	}
	
	public void setXzqhdm(String xzqhdm) {
		this.xzqhdm = xzqhdm;
	}
				
	public String getDqmc() {
		return dqmc;
	}
	
	public void setDqmc(String dqmc) {
		this.dqmc = dqmc;
	}
				
	public String getDqqc() {
		return dqqc;
	}
	
	public void setDqqc(String dqqc) {
		this.dqqc = dqqc;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getSsqxpyjc() {
		return ssqxpyjc;
	}

	public void setSsqxpyjc(String ssqxpyjc) {
		this.ssqxpyjc = ssqxpyjc;
	}
	
}
