package com.supconit.kqfx.web.util;


public class GisUtil {
	/**
	 * 矩形
	 * (baseX,baseY)为参照点，
	 * (x,y)为要检查的点，
	 * radius为距离，
	 * 返回 (x,y)是否在参照点(baseX,baseY)以radius的记录的矩形内
	 * **/
	public static boolean checkGisPoint(Double baseX, Double baseY, Double x, Double y, int radius){
		if(x < (baseX + radius/111000) && x > (baseX - radius/111000) && y < (baseY + radius/111000) && y > (baseY - radius/111000)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 圆形
	 * (baseX,baseY)为参照点，
	 * (x,y)为要检查的点，
	 * radius为距离，
	 * 返回 (x,y)是否在参照点(baseX,baseY)以radius的记录的圆形内
	 * **/
	public static boolean checkGisPointWithRound(Double baseX, Double baseY, Double x, Double y, int radius){
		Double d = lineSpace(baseX, baseY, x, y);
		if(d<=radius){//在圆形内
			return true;
		}else{//不在圆形内
			return false;
		}
	}
	
	
//	public static double minDistance(List<TrackData> tdList,double x, double y){
//		double dist = 10000000;
//		for(int i=0;i<tdList.size();i++){
//			double length = pointToLine(tdList.get(i).getGpsJd(),tdList.get(i).getGpsWd(),tdList.get(i+1).getGpsJd(),tdList.get(i+1).getGpsWd(),x,y);
//			if(dist > length){
//				dist = length;
//			}
//		}
//		return dist;
//	}
	
	
	/***
	 * 圆形(判断经纬度是否在区域内)
	 * @param baseX 参照点x经度
	 * @param baseY 参照点Y纬度
	 * @param latlong 需要判断的经纬度集合
	 * @param radius 距离
	 * @return
	 */
	public static Double checkGisPointWithLatLongAndRound(Double baseX, Double baseY,String latlong,int radius){
		String[] xy = latlong.split(";");
		double dis =0;
		for (String string : xy) 
		{
			String[] jdwd = string.split(",");
			if(jdwd!=null&&jdwd.length>1)
			{
				//判断经纬度是否在周边区域内。如果是count++,跳出循环
				if(checkGisPointWithRound(baseX,baseY,Double.valueOf(jdwd[0]),Double.valueOf(jdwd[1]),radius))
				{
					dis = lineSpace(baseX, baseY,Double.valueOf(jdwd[0]),Double.valueOf(jdwd[1]));
					return dis;
				}
				
				
			}
		}
		//判断count。count==0表示经纬度集合中所有点不符合
		return dis;
	}
	/**
	  * 计算点到线段的距离
	  * @param x1
	  * @param y1
	  * @param x2
	  * @param y2
	  * @param x0
	  * @param y0
	  * @return
	  */
	 public static double pointToLine(double x1, double y1, double x2, double y2, double x0, double y0) {
		  double space = 0;
		  double a, b, c;
		  a = lineSpace(x1, y1, x2, y2);// 线段的长度
		  b = lineSpace(x1, y1, x0, y0);// (x1,y1)到点的距离
		  c = lineSpace(x2, y2, x0, y0);// (x2,y2)到点的距离
		  if (c <= 0.000001 || b <= 0.000001) {
			   space = 0;
			   return space;
		  }
		  if (a <= 0.000001) {
			   space = b;
			   return space;
		  }
		  if (c * c >= a * a + b * b) {
			   space = b;
			   return space;
		  }
		  if (b * b >= a * a + c * c) {
			   space = c;
			   return space;
		  }
		  double p = (a + b + c) / 2;// 半周长
		  double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积
		  space = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）
		  return space;
	 }
	 
	 
	// 计算两点之间的距离
	 public static double lineSpace(double x1, double y1, double x2, double y2) {
		  double lineLength = 0;
		  double R = 6378140.0; // earth's mean radius in km
		  double dLat = tad(Math.abs(y2 - y1));
		  double dLon = tad(Math.abs(x2 - x1));
		  double lat1 = tad(y1);
		  double lat2 = tad(y2);
		  double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		  double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		  lineLength = R * c;
	
		  return lineLength;
	 }
	 
	 private static double tad(double d) {
		 return d * Math.PI / 180.0;
	 }
	 
	 //转角度
	 private double toDeg(double d) {
		 return d * 180 / Math.PI;
	 }
	 
	 public static double[] bd09Tobz84t(double x, double y){
		 double A = -1410.691, B = 15.41051, C = 38.71999, D = -0.04008667, E = -0.1694358, F = -0.326781,       
				H = 671.7619, I = -6.299108, J = -19.86499, K = 0.01519475, L = 0.09412918, M = 0.169988;   
		 double X = A*x*x + B*x*y + C*y*y + D*x + E*y + F;
		 double Y = H*x*x + I*x*y + J*y*y + K*x + L*y + M;
		 double[] array = new double[2];
		 array[0] = X;
		 array[1] = Y;
 		 return array;
	 }
	 
	 public static double[] bz84Tobd09t(double x, double y){
		 double A = 1374.828, B = -13.047, C = -37.73444, D = 0.03918341, E = 0.1642822, F = 0.3203621,       
				 H = -656.9006, I = 6.162367, J = 21.39801, K = -0.01490386, L = -0.0917767, M = -0.1667608 ;   
		 double X = A*x*x + B*x*y + C*y*y + D*x + E*y + F;
		 double Y = H*x*x + I*x*y + J*y*y + K*x + L*y + M;
		 double[] array = new double[2];
		 array[0] = X;
		 array[1] = Y;
 		 return array;
	 }
	 
	 public static double[] bd09Tobz84(double x, double y){
		 double A = 1.000277, B = -8.995008E-05, C = -0.04178602,       
				 D = 1.333871E-05, E = 0.9997696, F = 0.001995208;   
		 double X = A*x + B*y + C;
		 double Y = D*x + E*y + F;
		 double[] array = new double[2];
		 array[0] = X;
		 array[1] = Y;
 		 return array;
	 }
	 
	 public static double[] bz84Tobd09(double x, double y){
		 double A = 1.002027, B = 0.01115618, C = -0.5500267,       
				 D = -0.001108888, E = 0.9935312, F = 0.3205872;   
		 double X = A*x + B*y + C;
		 double Y = D*x + E*y + F;
		 double[] array = new double[2];
		 array[0] = X;
		 array[1] = Y;
 		 return array;
	 }
	 
	 public static void main(String[] args) {
		System.out.println(bd09Tobz84(120.675991, 28.016043)[0]+","+bd09Tobz84(120.675991, 28.016043)[1]);
	}
	 
}


