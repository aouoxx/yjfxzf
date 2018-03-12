package com.supconit.kqfx.web.util;
/**
 * 坐标系转换
 * @author liuxiaohuo
 *
 */
public class CoordinateTransfor {

	/**
	 * 将Mapbar02经纬坐标解密为真实84地理坐标
	 *
	 * @param x 经度值
	 * @param y 维度值
	 * @returns [x,y]
	 */
	public static String[] coordOffsetDecrypt(double x, double y){
		double A = 3.331886e-004;
		double B = 3.822402e-004;
		double C = -3.457685e-003;
		double D = 9.072682e-001;
		double E = 1.495382e-001;
		double F = 3.569503e+000 ;
		double H = 9.406949e-004;
		double I = 6.138899e-004;
		double J = -1.042876e-004;
		double K = -2.415904e-001;
		double L = 9.387685e-001;
		double M = 1.517737e+001 ;
		double reX = A*x*x + B*x*y + C*y*y + D*x + E*y + F;
		double reY = H*x*x + I*x*y + J*y*y + K*x + L*y + M;
		String[] array = new String[2];
		array[0] = String.valueOf(reX).substring(0,String.valueOf(reX).indexOf(".")+8);
		array[1] = String.valueOf(reY).substring(0,String.valueOf(reY).indexOf(".")+8);
		return array;
	}
		
		
	/**
	 * 将真实84地理坐标加密为Mapbar02经纬度坐标
	 *
	 * @param x 经度值
	 * @param y 维度值
	 * @returns [x,y]
	 */
	public static String[] coordOffsetEncrypt(double x, double y){
		double A = -3.340712e-004;
		double B = -3.859667e-004;
		double C = 3.426071e-003;
		double D = 1.093054e+000;
		double E = -1.473082e-001;
		double F = -3.620558e+000;
		double H = -9.382967e-004;
		double I = -6.068963e-004;
		double J = 1.068326e-004;
		double K = 2.408250e-001;
		double L = 1.060293e+000;
		double M = -1.511927e+001;	
		double reX = A*x*x + B*x*y + C*y*y + D*x + E*y + F;
		double reY = H*x*x + I*x*y + J*y*y + K*x + L*y + M;
		String[] array = new String[2];
		array[0] = String.valueOf(reX).substring(0,String.valueOf(reX).indexOf(".")+8);
		array[1] = String.valueOf(reY).substring(0,String.valueOf(reY).indexOf(".")+8);
		return array;
	}
}
