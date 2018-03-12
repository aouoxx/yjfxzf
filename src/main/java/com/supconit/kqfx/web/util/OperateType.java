package com.supconit.kqfx.web.util;



public enum OperateType {
	
	insert("1","添加"),
	update("2","修改"),
	delete("3","删除"),
	query("4","查询"),
	export("5","导出数据"),
	check("6","信息审核"),
	union("7","处罚关联"),
	send("8","信息发布"),
	view("9","查看"),
	mapLocation("10","地图定位");
	
	private String code;
	private String desc;
	
	private OperateType(String code, String desc){
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	

	public static String getDescByCode(String code){
		OperateType[] types = OperateType.values();
		for (OperateType operateType : types) {
			if(operateType.getCode().equals(code)){
				return operateType.getDesc();
			}
		}
		return "";
	}

}
