package com.supconit.kqfx.web.xtgl.entities;

import hc.orm.search.SQLTerm;
import hc.orm.search.Term;

import java.util.ArrayList;
import java.util.List;

import jodd.util.StringUtil;

import com.supconit.honeycomb.business.organization.entities.Person;

public class ExtPerson extends Person {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8748027901147925280L;
	private String jgxxJgmc;
	/** 所属机构ID */
	protected Long jgbh;
	/** 性别 */
	protected String ryxb;
	/** 出生日期 */
	protected String rycsrq;
	/** 证件类型 */
	protected String ryzjlx;
	/** 证件号码 */
	protected String ryzjhm;
	/** 民族 */
	protected String rymz;
	/** 政治面貌 */
	protected String ryzzmm;
	/** 进机构时间 */
	protected String ryjjgsj;
	/** 职务 */
	protected String ryzw;
	/** 学历 */
	protected String ryxl;
	/** 毕业学校 */
	protected String rybyxx;
	/** 所学专业 */
	protected String rysxzy;
	/** 职称 */
	protected String ryzc;
	/** 手机号码 */
	protected String rysjhm;
	/** 联系方式 */
	protected String rylxfs;
	/** 传真 */
	protected String rycz;
	/** 电子邮箱 */
	protected String rydzyx;
	
	protected Long departmentId;
	
	protected String rygh;
	
	protected String ryxnw;
	
	private String departmentName;
	
	/** 机构信息 */
	protected Jgxx jgxx;
	
	protected ExtDepartment department;
	
	/*机构等级，用于查询条件，此查询条件与jgbh互斥*/
	private String jgdj; 
	
	/* 机构编号列表 如："1,2,3"用逗号分隔  此查询条件与jgbh、jgdj互斥*/
	private String jgbhs;
	
	public String getJgxxJgmc() {
		return jgxxJgmc;
	}

	public void setJgxxJgmc(String jgxxJgmc) {
		this.jgxxJgmc = jgxxJgmc;
	}

	/**
	 * 是否选中
	 */
	private String checked; 

	/**
	 * 
	 */
	public ExtPerson() {
		super();
	}

	/**
	 * 
	 * @param id
	 */
	public ExtPerson(long id) {
		setId(id);
	}
	
	

    public Long getJgbh() {
		return jgbh;
	}

	public void setJgbh(Long jgbh) {
		this.jgbh = jgbh;
	}

	public String getRyxb() {
		return ryxb;
	}

	public void setRyxb(String ryxb) {
		this.ryxb = ryxb;
	}

	public String getRycsrq() {
		return rycsrq;
	}

	public void setRycsrq(String rycsrq) {
		this.rycsrq = rycsrq;
	}

	public String getRyzjlx() {
		return ryzjlx;
	}

	public void setRyzjlx(String ryzjlx) {
		this.ryzjlx = ryzjlx;
	}

	public String getRyzjhm() {
		return ryzjhm;
	}

	public void setRyzjhm(String ryzjhm) {
		this.ryzjhm = ryzjhm;
	}

	public String getRymz() {
		return rymz;
	}

	public void setRymz(String rymz) {
		this.rymz = rymz;
	}

	public String getRyzzmm() {
		return ryzzmm;
	}

	public void setRyzzmm(String ryzzmm) {
		this.ryzzmm = ryzzmm;
	}

	public String getRyjjgsj() {
		return ryjjgsj;
	}

	public void setRyjjgsj(String ryjjgsj) {
		this.ryjjgsj = ryjjgsj;
	}

	public String getRyzw() {
		return ryzw;
	}

	public void setRyzw(String ryzw) {
		this.ryzw = ryzw;
	}

	public String getRyxl() {
		return ryxl;
	}

	public void setRyxl(String ryxl) {
		this.ryxl = ryxl;
	}

	public String getRybyxx() {
		return rybyxx;
	}

	public void setRybyxx(String rybyxx) {
		this.rybyxx = rybyxx;
	}

	public String getRysxzy() {
		return rysxzy;
	}

	public void setRysxzy(String rysxzy) {
		this.rysxzy = rysxzy;
	}

	public String getRyzc() {
		return ryzc;
	}

	public void setRyzc(String ryzc) {
		this.ryzc = ryzc;
	}

	public String getRysjhm() {
		return rysjhm;
	}

	public void setRysjhm(String rysjhm) {
		this.rysjhm = rysjhm;
	}

	public String getRylxfs() {
		return rylxfs;
	}

	public void setRylxfs(String rylxfs) {
		this.rylxfs = rylxfs;
	}

	public String getRycz() {
		return rycz;
	}

	public void setRycz(String rycz) {
		this.rycz = rycz;
	}

	public String getRydzyx() {
		return rydzyx;
	}

	public void setRydzyx(String rydzyx) {
		this.rydzyx = rydzyx;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getRygh() {
		return rygh;
	}

	public void setRygh(String rygh) {
		this.rygh = rygh;
	}

	public String getRyxnw() {
		return ryxnw;
	}

	public void setRyxnw(String ryxnw) {
		this.ryxnw = ryxnw;
	}

	public Jgxx getJgxx() {
		return jgxx;
	}

	public void setJgxx(Jgxx jgxx) {
		this.jgxx = jgxx;
	}

	public ExtDepartment getDepartment() {
		return department;
	}

	public void setDepartment(ExtDepartment department) {
		this.department = department;
	}
	
	public String getJgdj() {
		return jgdj;
	}

	public void setJgdj(String jgdj) {
		this.jgdj = jgdj;
	}
	
	public String getJgbhs() {
		return jgbhs;
	}

	public void setJgbhs(String jgbhs) {
		this.jgbhs = jgbhs;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	/*
     * @see hc.base.domains.Tableable#getTableName()
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
    
    @Override
    public Term getTerm(String prefix) {
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<Object>();
        if (StringUtil.isNotBlank(this.getCode())) {
            buildSQLLike(sql, prefix, "CODE");
            params.add("%" + this.getCode() + "%");
        }
        if (StringUtil.isNotBlank(this.getName())) {
            buildSQLLike(sql, prefix, "NAME");
            params.add("%" + this.getName() + "%");
        }
        
        if(StringUtil.isNotBlank(this.getJgbhs())){
        	if (StringUtil.isNotBlank(prefix)) {
				sql.append("AND " + prefix).append(".");
			}
        	sql.append("JGBH IN ( "+ this.getJgbhs()+" )");
        }else{
        	if(null != this.getJgbh() && this.getJgbh() > 1){
    			buildSQLAnd(sql, prefix, "JGBH");
    			params.add(this.getJgbh());
    		}else{
    			if(StringUtil.isNotBlank(this.getJgdj())) {
    				if (StringUtil.isNotBlank(prefix)) {
//    					sql.append(prefix).append(".");
    					sql.append("AND " + prefix).append(".");
    				}
    				sql.append("JGBH IN ( select id from " + Jgxx.TABLE_NAME+ " where jgdj = ? )");
    				params.add(this.getJgdj());
    			}
    		}
        }
        
		

        if (sql.indexOf("AND ") == 0) sql.delete(0, 3);
        SQLTerm term = new SQLTerm(sql.toString());
        term.setParams(params.toArray());
        return term;
    }
}
