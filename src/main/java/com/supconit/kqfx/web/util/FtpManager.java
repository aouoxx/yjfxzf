package com.supconit.kqfx.web.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * FTP常用功能
 * @author Roger
 *
 */
@Component
@Scope("prototype")
public class FtpManager {
	//
	private static final Logger log = Logger.getLogger(FtpManager.class);
	
	public static final String GBK = "GBK";
	
	public static final String UTF_8 = "UTF-8";
	
	//
	private static FtpManager instance ;
	/** 客户端实例 */
	private FTPClient ftpClient = null;
	/** 服务端ip */
	@Value("${ftp.ip}")
	private String ftpServiceIp;
	/** 服务端端口 */
	@Value("${ftp.port}")
	private Integer ftpServicePort;
	/** 服务端用户名 */
	@Value("${ftp.username}")
	private String ftpServiceUsername;
	/** 服务端密码 */
	@Value("${ftp.password}")
	private String ftpServicePassword;
	/**
	 * 单例获取连接对象
	 * @return
	 */
	public static synchronized FtpManager getInstance(){
		if(instance == null){
			instance = new FtpManager();
		}
		return instance;
	}
	
	private FtpManager(){
		super();
	}
	/**
	 * 登录验证
	 * @param hostName ftp服务器地址
	 * @param port	自定义端口
	 * @param username 用户名
	 * @param password 密码
	 * @return 
	 */
	public void login(String hostName,int port,String userName,String password){
		try {
			ftpClient = new FTPClient();
			ftpClient.connect(hostName, port);
			ftpClient.login(userName, password);
			int reply = ftpClient.getReplyCode();
			if(!FTPReply.isPositiveCompletion(reply)){
				throw new RuntimeException();
			}
		} catch (Exception e) {
			log.error("ftp login fail.");
			throw new RuntimeException(e);
		}
	}
	/**
	 * 关闭ftp连接
	 * @param ftpClient
	 */
	public void close(){
		if(ftpClient!=null){
			if(ftpClient.isConnected()){
				try{
					ftpClient.logout();
					ftpClient.disconnect();
				}catch(IOException e){
					log.error("ftp connect close fail.");
					throw new RuntimeException(e);
				}
			}
		}
	}
	/**
	 * 上传文件到指定目录
	 * @param path
	 * @param filename
	 * @param characterEncoding
	 * @return
	 */
	public boolean uploadFile(String path, String filename,String characterEncoding){
		 InputStream input = null;
		boolean flag = false;
		if(ftpClient != null && ftpClient.isConnected()){
			try {
				
				input = new FileInputStream(filename);
				//转到指定目录
				ftpClient.changeWorkingDirectory(path); 
				//设置缓存大小
				ftpClient.setBufferSize(1024);
				//设置字符集
				ftpClient.setControlEncoding(characterEncoding);
				//设置文件类型(二进制)
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				//上传文件
				flag = ftpClient.storeFile(filename, input);
				input.close();
				ftpClient.logout();
			} catch (IOException e) {
				log.error("Ftp upload file fail.");
			}
		}
		return flag;
	}
	
	/**
	 * 上传单个文件到指定目录
	 * @param in 原文件流
	 * @param outPath 目标文件路径
	 * @param outFileName 目标文件名
	 * @return
	 * @throws IOException 
	 */
	public void upload(InputStream in, String outPath, String outFileName){
		try {
			//建立连接
			login(ftpServiceIp, ftpServicePort, ftpServiceUsername, ftpServicePassword);
			//转到指定目录
			boolean exists = ftpClient.changeWorkingDirectory(outPath); 
			if (!exists) {
				ftpClient.makeDirectory(outPath);
				ftpClient.changeWorkingDirectory(outPath);
			}
			//设置缓存大小
			ftpClient.setBufferSize(1024);
			//设置字符集
			ftpClient.setControlEncoding(UTF_8);
			//设置文件类型(二进制)
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			//上传文件
			boolean flag = ftpClient.storeFile(outPath + outFileName, in);
			if (!flag) {
				throw new RuntimeException();
			}
		} catch (Exception e) {
			log.error("Ftp upload file fail.");
			throw new RuntimeException(e);
		} finally{
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error("ftp上传关闭输入流异常");
					throw new RuntimeException(e);
				}
			}
			this.close();
		}
	}
	
	/**
	 * 批量上传文件到指定目录
	 * @param in 原文件集合包括文件名
	 * @param outPath 目标文件路径不包括文件名
	 * @return
	 * @throws IOException 
	 */
	public void batchUpload(List<String> in, String outPath){
		List<InputStream> inputStreams = new ArrayList<InputStream>();
		try {
			for (int i = 0; i < in.size(); i++) {
				inputStreams.add(new FileInputStream(in.get(i)));
			}
			//建立连接
			login(ftpServiceIp, ftpServicePort, ftpServiceUsername, ftpServicePassword);
			//转到指定目录
			boolean exists = ftpClient.changeWorkingDirectory(outPath); 
			if (!exists) {
				ftpClient.makeDirectory(outPath);
				ftpClient.changeWorkingDirectory(outPath);
			}
			//设置缓存大小
			ftpClient.setBufferSize(1024);
			//设置字符集
			ftpClient.setControlEncoding(UTF_8);
			//设置文件类型(二进制)
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			//上传文件
			for (int i = 0; i < in.size(); i++) {
				String[] array = in.get(i).split("\\/");
				String fileName = array[array.length-1];
				boolean flag = ftpClient.storeFile(outPath + fileName, inputStreams.get(i));
				if (!flag) {
					throw new RuntimeException();
				}
			}
		} catch (Exception e) {
			log.error("Ftp upload file fail.");
			throw new RuntimeException(e);
		} finally{
			if (inputStreams.size() > 0) {
				try {
					for (int i = 0; i < inputStreams.size(); i++) {
						inputStreams.get(i).close();
					}
				} catch (IOException e) {
					log.error("ftp上传关闭输入流异常");
					throw new RuntimeException(e);
				}
			}
			this.close();
		}
	}
	/**
	 * 在指定目录下新建目录
	 * @param ftpPath 指定目录
	 * @param dirName 新建目录名称(不填时根据时间生成目录名)
	 * @return
	 */
	public boolean createDirectory(String ftpPath,String dirName) {  
        boolean flag = false;  
        
        if(ftpClient != null){
        	// 切换文件路径, 到FTP上的ftpPath文件夹下  
            try {
				if (ftpPath != null && ftpPath.compareTo("") != 0  
				        && ftpClient.changeWorkingDirectory(ftpPath)) {  
					String time = DateUtil.formatDate(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD);
				    String reTransmitFolderName = time + "_Folder";  
				   flag = ftpClient.makeDirectory(dirName == null || dirName.length() == 0 ?reTransmitFolderName:dirName);  
				}
			} catch (IOException e) {
				log.error("ftp create dir fail.");
			}  
        }
        return flag;  
    }  
	/**
	 * 在指定目录下删除文件
	 * @param ftpPath
	 * @param srcFname
	 * @return
	 */
	public boolean removeFile(String ftpPath,String srcFname){
		boolean flag = false;
		if( ftpClient!=null ){
			try{
				if(ftpPath != null){
					ftpClient.changeWorkingDirectory(ftpPath);
				}
				
				flag = ftpClient.deleteFile(srcFname);
			}catch(IOException e){
				log.error("ftp delete file fail.");
				this.close();
			}
		}
		return flag;
	}
	/**
	 * 删除指定文件
	 * @param srcFname
	 * @return
	 */
	public boolean removeFile(String srcFname){
		return this.removeFile(null, srcFname);
	}
	
	/**
	 * ftp服务器上不同目录之间的文件拷贝
	 * @param fromPath
	 * @param fromFileName
	 * @param toPath
	 */
	public void copyFileInRemote(String fromPath, String fromFileName, String toPath){
		ByteArrayOutputStream fos = null;
		ByteArrayInputStream in = null;
		try {
			//建立连接
			login(ftpServiceIp, ftpServicePort, ftpServiceUsername, ftpServicePassword);
			//设置缓存大小
			ftpClient.setBufferSize(1024);
			//设置字符集
			ftpClient.setControlEncoding(UTF_8);
			//设置文件类型(二进制)
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			//上传文件
			fos = new ByteArrayOutputStream();
			ftpClient.retrieveFile(fromPath + fromFileName, fos);
			in = new ByteArrayInputStream(fos.toByteArray());
			boolean flag = ftpClient.storeFile(toPath + fromFileName, in);
			if (!flag) {
				throw new RuntimeException();
			}
		} catch (Exception e) {
			log.error("Ftp upload file fail.");
			throw new RuntimeException(e);
		} finally{
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error("ftp远程文件拷贝关闭输入流异常");
					throw new RuntimeException(e);
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					log.error("ftp远程文件拷贝关闭输出流异常");
					throw new RuntimeException(e);
				}
			}
			this.close();
		}
	}
}
