package com.supconit.kqfx.web.xtgl.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jodd.util.StringUtil;
import hc.base.domains.AuditedSimpleEntity;
import hc.base.domains.ImprovedTree;
import hc.base.domains.Tableable;
import hc.base.domains.XssFilterSupport;
import hc.orm.search.Conditional;
import hc.orm.search.SQLTerm;
import hc.orm.search.Term;

/**
 * 机构信息
 * 
 * @author shenxinfeng@supcon.com
 *
 */
public class Jgxx extends AuditedSimpleEntity implements ImprovedTree<Jgxx>, Tableable, Conditional, XssFilterSupport {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5395396941129313754L;

	public static final long	ROOT_PID			= 0L;
	public static final Long	ROOT_ID			= 1L;
	
	public static final String	TABLE_NAME			= "T_XTGL_ZHGL_JGXX";
	private String name;
	private Boolean expanded=true;
	private Boolean hasChildren;
	private Long parentId;
	
	private long modifier=1;
	/** 机构名称 */
	protected String jgmc;
	/** 机构简称 */
	protected String jgjc;
	/** 组织机构代码 */
	protected String zzjgdm;
	/** 行政区划代码 */
	protected String xzqhdm;
	/** 上级主管部门 */
	protected Long pid;
	/** 负责人 */
	protected String fzr;
	/** 负责人联系电话 */
	protected String fzrlxfs;
	/** 传真 */
	protected String cz;
	/** 详细地址 */
	protected String xxdz;
	/** 邮编 */
	protected String yb;
	/** 经度 */
	protected BigDecimal jd;
	/** 纬度 */
	protected BigDecimal wd;
	/** 备注 */
	protected String bz;
	/**  */
	protected Long lft;
	/**  */
	protected Long rgt;
	/** 删除标记默认0，1表示删除 */
	protected Integer deleted;
	/** 机构等级 */
	protected String jgdj;
	
	/** 统计分析简称 */
	protected String tjfxjc;
	
	/** 机构拼音简称 */
	protected String jgpyjc;
	
	
	protected Jgxx parent;
	protected List<Jgxx> children;
	
	
	
	
	private String parentJgmc;
	private String parentJgId;
	
	private String xzqhmc;
	
	
	public String getParentJgId() {
		return parentJgId;
	}

	public void setParentJgId(String parentJgId) {
		this.parentJgId = parentJgId;
	}

	public String getParentJgmc() {
		return parentJgmc;
	}

	public void setParentJgmc(String parentJgmc) {
		this.parentJgmc = parentJgmc;
	}

	public Jgxx(){
		super();
	}
	
	public Jgxx(Long id){
		setId(id);
	}
	
	
	
	public String getJgmc() {
		return jgmc;
	}

	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}

	public String getJgjc() {
		return jgjc;
	}

	public void setJgjc(String jgjc) {
		this.jgjc = jgjc;
	}

	public String getZzjgdm() {
		return zzjgdm;
	}

	public void setZzjgdm(String zzjgdm) {
		this.zzjgdm = zzjgdm;
	}

	public String getXzqhdm() {
		return xzqhdm;
	}

	public void setXzqhdm(String xzqhdm) {
		this.xzqhdm = xzqhdm;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getFzr() {
		return fzr;
	}

	public void setFzr(String fzr) {
		this.fzr = fzr;
	}

	public String getFzrlxfs() {
		return fzrlxfs;
	}

	public void setFzrlxfs(String fzrlxfs) {
		this.fzrlxfs = fzrlxfs;
	}

	public String getCz() {
		return cz;
	}

	public void setCz(String cz) {
		this.cz = cz;
	}

	public String getXxdz() {
		return xxdz;
	}

	public void setXxdz(String xxdz) {
		this.xxdz = xxdz;
	}

	public String getYb() {
		return yb;
	}

	public void setYb(String yb) {
		this.yb = yb;
	}

	public BigDecimal getJd() {
		return jd;
	}

	public void setJd(BigDecimal jd) {
		this.jd = jd;
	}

	public BigDecimal getWd() {
		return wd;
	}

	public void setWd(BigDecimal wd) {
		this.wd = wd;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public void setLft(Long lft) {
		this.lft = lft;
	}

	public void setRgt(Long rgt) {
		this.rgt = rgt;
	}

	public void setParent(Jgxx parent) {
		this.parent = parent;
	}

	public void setChildren(List<Jgxx> children) {
		this.children = children;
	}

	@Override
	public Jgxx getParent() {
		return this.parent;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Jgxx> getChildren() {
		return this.children;
	}
	@Override
	public void addChild(Jgxx child) {
		if (null == children) children = new ArrayList<Jgxx>();
		children.add(child);
	}
	@Override
	public Term getTerm(String prefix) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		if (null != this.getId()) {
			buildSQLAnd(sql, prefix, "ID");
			params.add(this.getId());
		}
		if (null != this.getPid() && this.getPid() > 0) {
			buildSQLAnd(sql, prefix, "PID");
			params.add(this.getPid());
		}
		if (StringUtil.isNotBlank(this.getJgmc())) {
			buildSQLLike(sql, prefix, "JGMC");
			params.add("%" + this.getJgmc() + "%");
		}
		if (StringUtil.isNotBlank(this.getZzjgdm())) {
			buildSQLLike(sql, prefix, "ZZJGDM");
			params.add("%" + this.getZzjgdm() + "%");
		}
		if (StringUtil.isNotBlank(this.getFzr())) {
			buildSQLLike(sql, prefix, "FZR");
			params.add("%" + this.getFzr() + "%");
		}
		if (StringUtil.isNotBlank(this.getJgdj())){
			buildSQLAnd(sql, prefix, "JGDJ");
			params.add(this.getJgdj());
		}
		//获取未删除的记录
		buildSQLAnd(sql, prefix, "DELETED");
		params.add(0);
		if (sql.indexOf("AND ") == 0)
			sql.delete(0, 3);
		SQLTerm term = new SQLTerm(sql.toString());
		term.setParams(params.toArray());
		return term;
			
	}
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
	@Override
	public Long getLft() {
		return this.lft;
	}
	@Override
	public Long getRgt() {
		return this.rgt;
	}

	public String getJgdj() {
		return jgdj;
	}

	public Boolean getExpanded() {
		return expanded;
	}

	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}

	public void setJgdj(String jgdj) {
		this.jgdj = jgdj;
	}

	public String getTjfxjc() {
		return tjfxjc;
	}

	public void setTjfxjc(String tjfxjc) {
		this.tjfxjc = tjfxjc;
	}

	public String getJgpyjc() {
		return jgpyjc;
	}

	public void setJgpyjc(String jgpyjc) {
		this.jgpyjc = jgpyjc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(Boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public Long getModifier() {
		return modifier;
	}

	public void setModifier(long modifier) {
		this.modifier = modifier;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getXzqhmc() {
		return xzqhmc;
	}

	public void setXzqhmc(String xzqhmc) {
		this.xzqhmc = xzqhmc;
	}



}
