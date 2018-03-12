package com.supconit.kqfx.web.service.data;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.tempuri.dag_xsd.Service_wsdl.ServiceStub;

import com.supconit.kqfx.web.device.entities.DeviceInfo;
import com.supconit.kqfx.web.device.services.DeviceInfoService;
import com.supconit.kqfx.web.fxzf.search.entities.Fxzf;
import com.supconit.kqfx.web.util.XmlAnalysisUtil;

@Component
public class DataWebServiceImpl {

	private static final Logger logger =  LoggerFactory.getLogger(DataWebServiceImpl.class);
	
	@Autowired
	private DeviceInfoService deviceInfoService;
	@Value("${image.haikang}")
	private String addrString;
	
	
	SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	
	
	/**
	 * 数据接口上传
	 * @param fxzf
	 * @return
	 */
	public boolean tranferData(Fxzf fxzf,ServiceStub serviceStub,HashMap<String, String> stationMap,
			Double length,Double width, Double height){
		try {
			logger.info("==上传非现执法数据到市局==");
			String vehicleWeightInfo = fxzfDataToString(fxzf,stationMap,length,width,height);
			logger.info(vehicleWeightInfo);
			String xmlReult=serviceStub.sendVehicleWeight(vehicleWeightInfo);
			String flag=XmlAnalysisUtil.getCode(xmlReult);
			//<!—CODE为非0时，MESSAGE 字段为失败的原因，CODE为0时为空-->
			if(flag.equals("0")){
				logger.error("==上传非现执法数据到市局成功==");
				return true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error("==上传非现执法数据到市局失败==");
		}
		return false;
	}
	
	/**
	 * 将非现执法转换为上传市局接口的字符串
	 * @param fxzf
	 * @return
	 */
	public String fxzfDataToString(Fxzf fxzf,HashMap<String, String> stationMap,
			Double length,Double width, Double height
			){
		try {
		java.text.DecimalFormat   df=new   java.text.DecimalFormat("#.##");   
		String parameter = "";
		String license="";
		if(fxzf.getLicense()==null){
			license="车牌";
		}else{
			license=fxzf.getLicense();
		}
		
		//0	白色      1	黄色     2	蓝色    3	黑色   4	其它颜色
		String licenseColor="";
		if(fxzf.getLicenseColor()!=null){
			switch (fxzf.getLicenseColor()) {
				case "白色" :
					licenseColor="0";
					break;
				case "黄色 " :
					licenseColor="1";
					break;
				case "蓝色" :
					licenseColor="2";
					break;
				case "黑色" :
					licenseColor="3";
					break;
				default :
					licenseColor="4";
					break;
			}
		}
		
		int IsOverWeight=0;
		if(fxzf.getOverLoadStatus()>0){
			IsOverWeight=1;
		}
		int LimitWeight=0;
		switch (fxzf.getAxisCount()) {
			case 2 :
				LimitWeight=20;
				break;
			case 3 :
				LimitWeight=30;
				break;
			case 4 :
				LimitWeight=40;
				break;
			case 5 :
				LimitWeight=50;
				break;
			case 6 :
				LimitWeight=55;
				break;
			default :
				LimitWeight=55;
				break;
		}
		
		DeviceInfo deviceInfo = new DeviceInfo();
		String stationName = stationMap.get(fxzf.getDetectStation());
		deviceInfo.setDeviceStation(stationName.substring(0, 8));
		deviceInfo.setDeviceDirection(stationName.substring(8, stationName.length()));
		deviceInfo.setDeviceType("06");
		deviceInfo = deviceInfoService.getDeviceInfo(deviceInfo);
		
		String head = fxzf.getHeadCarPicdir();
		int headIndex = head.indexOf("/"); 
		String headdir1 = head.substring(0, headIndex+1);
		String headdir2 = head.substring(headIndex+1, head.length());
		String headdir= headdir1 + URLEncoder.encode(headdir2, "utf-8");
		
		String tail = fxzf.getTailCarPicdir();
		int tailIndex = tail.indexOf("/");
		String taildir1 = tail.substring(0, tailIndex+1);
		String taildir2 = tail.substring(tailIndex+1, tail.length());
		String taildir= taildir1+URLEncoder.encode(taildir2, "utf-8");
		
		parameter = "<?xml version=\"1.0\" encoding=\" UTF-8 \" standalone=\"yes\" ?>"
				   +"<ROOT>"
				   //设备ID
				   +"<DeviceKey>"+deviceInfo.getId()+"</DeviceKey>"
				   //设备编号
				   +"<IndexCode>"+deviceInfo.getNo()+"</IndexCode>"
				   //对应治超点编号
				   +"<CheckID>"+fxzf.getDetectStation()+"</CheckID >"
				   //车牌号
				   +"<PlateNo>"+license+"</PlateNo>"
				   //车牌颜色
				   +"<PlateColor>"+licenseColor+"</PlateColor>"
				   //车辆速度
				   +"<VehicleSpeed>"+String.valueOf(df.format(fxzf.getSpeed()))+"</VehicleSpeed>"
				   //车道号
				   +"<LaneNo>"+"0"+fxzf.getLane()+"</LaneNo>"
				   //是否超重
				   +"<IsOverWeight>"+IsOverWeight+"</IsOverWeight>"
				   //轴数
				   +"<AxleNum>"+fxzf.getAxisCount()+"</AxleNum>";
				   //轴长
				  	if(fxzf.getOverLoad()==null){
					   parameter=parameter+"<AxleLen></AxleLen>";
				   }else{
					   Double d = Double.valueOf(fxzf.getAxisLength())/1000;
					   parameter=parameter+"<AxleLen>"+String.valueOf(df.format(d))+"</AxleLen>";
				   }
				
				   //超限量
				   if(fxzf.getOverLoad()==null){
					   parameter=parameter+"<OverWeight>0</OverWeight>";
				   }else{
					   parameter=parameter+"<OverWeight>"+String.valueOf(df.format(fxzf.getOverLoad()))+"</OverWeight>";
				   }
				   
				  parameter=parameter
				   //车辆总重
				   +"<VehicleWeight>"+String.valueOf(df.format(fxzf.getWeight()))+"</VehicleWeight>"
				   //行车方向
				   //1 表示逆行
				   //0表示正常
				   +"<IsDirect>0"+"</IsDirect>";
				 
				   //各个轴重
					String[] axiws = null;
					if(fxzf.getAxisWeightTotal()!=null){
						axiws = fxzf.getAxisWeightTotal().split(",");
					}
					String axisWrights="";
					for(int i=0;i<8;i++){
						String str="";
						if(axiws!=null){
							if(i<axiws.length){
								 Double d = Double.valueOf(axiws[i])/1000;
								 str = "<Axleweight"+(i+1)+">"+String.valueOf(df.format(d))+"</Axleweight"+(i+1)+">";
							}else{
								 str = "<Axleweight"+(i+1)+">"+0+"</Axleweight"+(i+1)+">";
							}
						}else{
							 str = "<Axleweight"+(i+1)+">"+0+"</Axleweight"+(i+1)+">";
						}
						
					
						axisWrights=axisWrights+str;
					}
					
					//**行车的长宽高
					String ckg="";
					if(fxzf.getLength()==-1){
						ckg=ckg+"<VehicleLength></VehicleLength>";
					}else{
						ckg=ckg+"<VehicleLength>"+fxzf.getLength()+"</VehicleLength>";
					}
					
					if(fxzf.getWidth()==-1){
						ckg=ckg+"<VehicleWidth></VehicleWidth>";
					}else{
						ckg=ckg+"<VehicleWidth>"+fxzf.getWidth()+"</VehicleWidth>";
					}
					
					if(fxzf.getHeight()==-1){
						ckg=ckg+"<VehicleHeight></VehicleHeight>";
					}else{
						ckg=ckg+"<VehicleHeight>"+fxzf.getLength()+"</VehicleHeight>";
					}
					if (fxzf.getLength()>length) {
						ckg=ckg+"<OverLength>"+String.valueOf(fxzf.getLength()-length)+"</OverLength>";
					}else{
						ckg=ckg+"<OverLength>"+0+"</OverLength>";
					}
					
					if (fxzf.getWidth()>width) {
						ckg=ckg+"<OverWidth>"+String.valueOf(fxzf.getWidth()-width)+"</OverWidth>";
					}else{
						ckg=ckg+"<OverWidth>"+0+"</OverWidth>";
					}
					
					if (fxzf.getHeight()>height) {
						ckg=ckg+"<OverHeight>"+String.valueOf(fxzf.getHeight()-height)+"</OverHeight>";
					}else{
						ckg=ckg+"<OverHeight>"+0+"</OverHeight>";
					}
    	parameter=parameter
    			  +axisWrights
    			  +ckg
    			  +"<VehicleType></VehicleType>"
				  +"<LimitWeight>"+LimitWeight+"</LimitWeight>"
				  +"<CheckTime>"+format.format(fxzf.getCaptureTime())+"</CheckTime>"
				  +"<HeadImageURL>"+addrString+headdir+"</HeadImageURL>"
				  +"<BodyImageURL></BodyImageURL>"
				  +"<TailImageURL>"+addrString+taildir+"</TailImageURL>"
				  +"</ ROOT >";
				   
		return parameter;
	}catch (Exception e) {
		e.printStackTrace();
		return null;
	}
	}
}
