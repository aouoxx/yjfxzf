package org.tempuri.dag_xsd.Service_wsdl;

public class ServicePortTypeProxy implements ServicePortType {
  private String _endpoint = null;
  private ServicePortType servicePortType = null;
  
  public ServicePortTypeProxy() {
    _initServicePortTypeProxy();
  }
  
  public ServicePortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initServicePortTypeProxy();
  }
  
  private void _initServicePortTypeProxy() {
    try {
      servicePortType = (new ServiceLocator()).getService();
      if (servicePortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)servicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)servicePortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (servicePortType != null)
      ((javax.xml.rpc.Stub)servicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ServicePortType getServicePortType() {
    if (servicePortType == null)
      _initServicePortTypeProxy();
    return servicePortType;
  }
  
  public String sendVehicleWeightEx(String vehicleWeightInfo, byte[] platePicData) throws java.rmi.RemoteException{
    if (servicePortType == null)
      _initServicePortTypeProxy();
    return servicePortType.sendVehicleWeightEx(vehicleWeightInfo, platePicData);
  }
  
  public String sendVehicleWeight(String vehicleWeightInfo) throws java.rmi.RemoteException{
    if (servicePortType == null)
      _initServicePortTypeProxy();
    return servicePortType.sendVehicleWeight(vehicleWeightInfo);
  }
  
  public String sendDeviceState(String deviceState) throws java.rmi.RemoteException{
    if (servicePortType == null)
      _initServicePortTypeProxy();
    return servicePortType.sendDeviceState(deviceState);
  }
  
  public String synTime() throws java.rmi.RemoteException{
    if (servicePortType == null)
      _initServicePortTypeProxy();
    return servicePortType.synTime();
  }
  
  
}