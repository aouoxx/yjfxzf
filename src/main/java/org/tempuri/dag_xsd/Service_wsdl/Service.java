/**
 * Service.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri.dag_xsd.Service_wsdl;

public interface Service extends javax.xml.rpc.Service {

/**
 * gSOAP 2.8.6 generated service definition
 */
    public String getServiceAddress();

    public org.tempuri.dag_xsd.Service_wsdl.ServicePortType getService() throws javax.xml.rpc.ServiceException;

    public org.tempuri.dag_xsd.Service_wsdl.ServicePortType getService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
