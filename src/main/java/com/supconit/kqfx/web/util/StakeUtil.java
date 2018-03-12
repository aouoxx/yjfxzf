package com.supconit.kqfx.web.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.springframework.util.StringUtils;

/**
 * 
 * @author zongkai
 *
 */
public class StakeUtil {
   

	
	/**
     * 将数值桩号转为K值桩号
     * @param dZH
     * @return
     */
    public static String trimZero(Double dZH){
    	if(dZH == null){
    		return "";
    	}
    	DecimalFormat df5 = new DecimalFormat("0.000"); 
    	String K = df5.format(dZH);
    	String beforeStr = K.substring(0,K.indexOf("."));
    	if(dZH < 0){
    		beforeStr = beforeStr.substring(1);
    	}
    	String afterStr = K.substring(K.indexOf(".")+1, K.length());
    	if(afterStr.equals("000")){
    		return "K"+beforeStr;
    	}
    	char[] afterChar = afterStr.toCharArray();
    	int st = 0;
    	for(int i=0;i<afterChar.length;i++){
    		if(afterChar[i] == '0'){
    			st++;
    		}
    		if(afterChar[i] != '0'){
    			break;
    		}
    	}
    	String kzh = afterStr.substring(st, afterStr.length());
    	if(dZH >= 0){
    		kzh = "+"+kzh;
    	}else{
    		kzh = "-"+kzh;
    	}
    	K = "K"+beforeStr+kzh;
    	return K;
   }
    
    /**
     * 把K值桩号转化为数值桩号
     * @param k
     * @return
     */
    public static String trimToFloat(String k,String m){
    	String returnStr = "";
    	if(k==null || "".equals(k)){
    		returnStr = "0";
    	}else{
    		returnStr = k;
    	}
    	if(m==null || "".equals(m)){
    		m = "0";
    	}
    	if(Long.parseLong(m)<10){
    		returnStr = returnStr+".00"+m;
    	}else if(Long.parseLong(m)<100){
    		returnStr = returnStr+".0"+m;
    	}else{
    		returnStr = returnStr+"."+m;
    	}
    	return returnStr;
    }
    
    /**
     * 把K值桩号转化为数值桩号
     * @param k
     * @return
     */
    public static Double trimToDouble(String k,String m){
    	String returnStr = "";
    	if(StringUtils.isEmpty(k) || StringUtils.isEmpty(m)){
    		return null;
    	}else{
    		returnStr = k;
    	}
    	if(Long.parseLong(m)<10){
    		returnStr = returnStr+".00"+m;
    	}else if(Long.parseLong(m)<100){
    		returnStr = returnStr+".0"+m;
    	}else{
    		returnStr = returnStr+"."+m;
    	}
    	return Double.valueOf(returnStr);
    }
    
    /**
     * 把String型的桩号数据转换成数组表示，如500.045转换出来就是String[0]=500，String[1]=45
     * 把K值桩号转化为数值桩号 (K89+_时处理成89.0， K_+89时处理成0.89)
     * @param k
     * @return
     */
    public static Double trimToDouble2(String k,String m){
    	String returnStr = "";
    	if(StringUtils.isEmpty(k) && StringUtils.isEmpty(m)){
    		return null;
    	}else if(StringUtils.isEmpty(k)){
    		returnStr = "0";
    	}else if(StringUtils.isEmpty(m)){
    		returnStr = k;
    		m = "0";
    	}
    	if(Long.parseLong(m)<10){
    		returnStr = returnStr+".00"+m;
    	}else if(Long.parseLong(m)<100){
    		returnStr = returnStr+".0"+m;
    	}else{
    		returnStr = returnStr+"."+m;
    	}
    	return Double.valueOf(returnStr);
    }    
    
    /**
     * 把String型的桩号数据转换成map表示，如500.045转换出来就是key=500，value=45
     * 用于页面显示K_500__+ 45两个字段桩号表示
     * @param k
     * @return
     */
    public static String[] trimStakeToString(Double dZH){
    	String[] stakArr=new String[2];
    	if(dZH == null){
    		stakArr[0]="";
    		stakArr[1]="";
    		return stakArr;
    	}
    	DecimalFormat df5 = new DecimalFormat("0.000"); 
    	String K = df5.format(dZH);
    	String beforeStr = K.substring(0,K.indexOf("."));
    	if(dZH < 0){
    		beforeStr = beforeStr.substring(1);
    	}
    	String afterStr = K.substring(K.indexOf(".")+1, K.length());
    	if(afterStr.equals("000")){
    		stakArr[0]=beforeStr;
    		stakArr[1]="0";
    		return stakArr;
    	}
    	char[] afterChar = afterStr.toCharArray();
    	int st = 0;
    	for(int i=0;i<afterChar.length;i++){
    		if(afterChar[i] == '0'){
    			st++;
    		}
    		if(afterChar[i] != '0'){
    			break;
    		}
    	}
    	String kzh = afterStr.substring(st, afterStr.length());    	   	
    	stakArr[0]=beforeStr;
    	stakArr[1]=kzh;
    	
    	return stakArr;
    }
    
	
    /**
     * 将错误桩号转化为正确桩号
     * @param stakeNo
     * @return
     */
    public static String ToStakeno(String stakeNo){
    	//将桩号转化为数字
    	String[] stakeNoStrArr = stakeNo.split(",");
    	Double[] stakeNoDblArr = new Double[4];
    	stakeNoDblArr[0] = Double.parseDouble(stakeNoStrArr[0] == null ? "0" : stakeNoStrArr[0]);
    	stakeNoDblArr[1] = Double.parseDouble(stakeNoStrArr[1] == null ? "0" : stakeNoStrArr[1]);
    	stakeNoDblArr[2] = Double.parseDouble(stakeNoStrArr[2] == null ? "0" : stakeNoStrArr[2]);
    	stakeNoDblArr[3] = Double.parseDouble(stakeNoStrArr[3] == null ? "0" : stakeNoStrArr[3]);
    	//计算起点桩号止点桩号
    	Double startStakeNo = stakeNoDblArr[0] + stakeNoDblArr[1]/1000;
    	Double endStakeNo = stakeNoDblArr[2] + stakeNoDblArr[3]/1000;
    	//重新包装起点桩号止点桩号字符串
    	DecimalFormat df  = new DecimalFormat("0.000"); 
    	String returnStr = df.format(startStakeNo)+","+df.format(endStakeNo);
    	returnStr = returnStr.replace(".", ",");
    	return returnStr;
    }
    
    /**
     * 从格式为a,a,a,a中转化出起点桩号
     * @param stakeNo
     * @return
     */
    public static Double toStartStakeNo(String stakeNo){
    	String[] stakeNoArr = stakeNo.split(",");
    	Double startStakeNo = Double.parseDouble(stakeNoArr[0])+Double.parseDouble(stakeNoArr[1])/1000.0;
    	return startStakeNo;
    }
    
    /**
     * 从格式为a,a,a,a中转化出起点桩号
     * @param stakeNo
     * @return
     */
    public static Double toEndStakeNo(String stakeNo){
    	String[] stakeNoArr = stakeNo.split(",");
    	Double endStakeNo = Double.parseDouble(stakeNoArr[2])+Double.parseDouble(stakeNoArr[3])/1000.0;
    	return endStakeNo;
    }
    
    /**
     * 提供精确的减法运算。
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }   
    
    /**
     * 提供精确的加法运算。

     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
     public static double add(double v1,double v2){
	     BigDecimal b1 = new BigDecimal(Double.toString(v1));
	     BigDecimal b2 = new BigDecimal(Double.toString(v2));
	     return b1.add(b2).doubleValue();
     }

    /**
     * 提供精确的小数位四舍五入处理。
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
                "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    /**
     * 将起点桩号和止点桩号显示在页面的title上
     * @param name 名称 不为空
     * @param qdzh 起点桩号 可为空
     * @param lxjc 路线简称  可为空
     * @return
     */
    public static String stakeForTitle(String lxjc, String name, Double qdzh){
    	String title = name;
    	if (qdzh != null && !StringUtils.isEmpty(lxjc)) {
			String[] array = StakeUtil.trimStakeToString(qdzh);
			title = lxjc + "K" + array[0] + "+" + array[1] + " " + name; 
		}
		return title;
    }
}
