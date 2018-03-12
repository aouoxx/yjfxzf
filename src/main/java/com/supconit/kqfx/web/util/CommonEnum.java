package com.supconit.kqfx.web.util;


/**
 * 公共枚举 
 * @author liuxiaohuo
 * 2014-5-16
 */
public interface CommonEnum {

	/**
	 * 预警阀值类型
	 *
	 */
    enum YjFzTypeCode {
    	
        equal("1", "等于"), greater("2", "大于"), less("3","小于"), greater_equal("4","大于等于"), less_equal("5","小于等于");

        private final String code;
        private final String desc;

        private YjFzTypeCode(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String code() {
            return code;
        }
        
        public String desc(){
            return desc;
        }
    }
    
    /**
	 * 预警信息类型
	 *
	 */
    enum YjxxTypeCode {
    	
        yjd("1", "交通拥挤度"), sggl("2", "施工");

        private final String code;
        private final String desc;

        private YjxxTypeCode(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String code() {
            return code;
        }
        
        public String desc(){
            return desc;
        }
    }
    
    /**
	 * 通知信息类型
	 *
	 */
    enum NoticeTypeCode {
    	
        sggl("1", "施工");

        private final String code;
        private final String desc;

        private NoticeTypeCode(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String code() {
            return code;
        }
        
        public String desc(){
            return desc;
        }
    }
    
    /**
	 * 交调方向
	 *
	 */
    enum JiaotiaoDirectionCode {
    	
        up("1", "上行"), down("2", "下行");

        private final String code;
        private final String desc;

        private JiaotiaoDirectionCode(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String code() {
            return code;
        }
        
        public String desc(){
            return desc;
        }
        
        public static String getDescByCode(String code){
        	for (JiaotiaoDirectionCode c : JiaotiaoDirectionCode.values()) {
                if (c.code().equals(code)) {
                    return c.desc();
                }
            }
        	return null;
        }
    }
    
    /**
	 * 交调拥挤度
	 *
	 */
    enum JiaotiaoYjdCode {
    	
        veryUnblock("1", "非常畅通"), unBlock("2", "畅通"), common("3", "一般"), block("4", "拥挤"), veryBlock("5", "堵塞");

        private final String code;
        private final String desc;

        private JiaotiaoYjdCode(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String code() {
            return code;
        }
        
        public String desc(){
            return desc;
        }
        
        public static String getDescByCode(String code){
        	for (JiaotiaoYjdCode c : JiaotiaoYjdCode.values()) {
                if (c.code().equals(code)) {
                    return c.desc();
                }
            }
        	return null;
        }
    }
    
    /**
	 * 施工管理
	 *
	 */
    enum SgglCode {
    	
        innerOne("1", "一个月内"), oneToThree("2", "一到三个月"), threeToSix("3", "三到六个月"), sixToOneYear("4", "半年到一年"),
        oneYearToTwoYear("5", "一年到两年"), twoYearToThreeYear("6", "两年到三年"), threeYear("7", "三年以上");

        private final String code;
        private final String desc;

        private SgglCode(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String code() {
            return code;
        }
        
        public String desc(){
            return desc;
        }
        
        public static String getDescByCode(String code){
        	for (SgglCode c : SgglCode.values()) {
                if (c.code().equals(code)) {
                    return c.desc();
                }
            }
        	return null;
        }
    }
    
    /**
	 * 预案匹配资源类型
	 *
	 */
    enum YappType {
    	
    	yjldxz("0", "应急领导小组"), yjb("1", "应急办"), yjdw("2", "应急队伍"),
    	yjwz("3", "应急物资"), yjsb("4", "应急设备"), yjzj("5", "应急专家"), yjlddw("6", "应急联动单位");

        private final String code;
        private final String desc;

        private YappType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String code() {
            return code;
        }
        
        public String desc(){
            return desc;
        }
        
    }
    
    /**
	 * 机构树
	 *
	 */
    enum JgxxType {
    	
    	wzs(24l, "温州市");

        private final Long code;
        private final String desc;

        private JgxxType(Long code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Long code() {
            return code;
        }
        
        public String desc(){
            return desc;
        }
        
    }
    
    
    /**
	 * 预案匹配资源类型
	 *
	 */
    enum xcjlType {
    	
    	road("1", "公路巡查记录"), facility("2", "沿线设施检查"), culvert_termly("3", "涵洞定期检查"),
    	culvert_regular("4", "涵洞经常性检查"), bridge_regular("5", "桥梁经常性检查");

        private final String code;
        private final String desc;

        private xcjlType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String code() {
            return code;
        }
        
        public String desc(){
            return desc;
        }
        
    }
}
