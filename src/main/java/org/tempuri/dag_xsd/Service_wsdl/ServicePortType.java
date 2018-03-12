/**
 * ServicePortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri.dag_xsd.Service_wsdl;

public interface ServicePortType extends java.rmi.Remote {

    /**
     * Service definition of function dag__SendVehicleWeightEx
     */
    public String sendVehicleWeightEx(String vehicleWeightInfo, byte[] platePicData) throws java.rmi.RemoteException;

    /**
     * Service definition of function dag__SendVehicleWeight
     */
    public String sendVehicleWeight(String vehicleWeightInfo) throws java.rmi.RemoteException;

    /**
     * Service definition of function dag__SendDeviceState
     */
    public String sendDeviceState(String deviceState) throws java.rmi.RemoteException;

    /**
     * Service definition of function dag__SynTime
     */
    public String synTime() throws java.rmi.RemoteException;
}
