package com.supconit.kqfx.web.directives;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.io.FileUtil;
import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.Part;
import com.supconit.kqfx.web.util.IDGenerator;

@Controller
@RequestMapping(value = "/fileUpload")
public class FileUploadController {
	private static final Logger logger = LoggerFactory
			.getLogger(FileUploadController.class);

	@Value("${ftp.temp}")
	private String ftpTemp;

	/**
	 * 上传图片到本地的临时目录,最大限制10M,格式为bmp,jpg,jpeg,gif,psd,png,tiff,tga,eps
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "uploadImgToTemp")
	@ResponseBody
	public void uploadImgToTemp(HttpServletRequest request, HttpServletResponse response) {
		String result = "";
		PrintWriter out = null;
		try {
			String oldFileName = "";
			String newFileName = "";
			logger.debug("开始上传文件！");
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			MultipartParser parser = new MultipartParser(request, 10*1024*1024*1, true, true, "UTF-8");
			Part part;
			while ((part = parser.readNextPart()) != null) {
				if (!part.isFile()){
					continue;
				}
				FilePart filePart = (FilePart) part;
				oldFileName = filePart.getFileName();
				if (StringUtil.isBlank(oldFileName)) {
					continue;
				}
				//判断临时目录是否存在
				if (!new File(ftpTemp).exists()) {
					FileUtil.mkdirs(ftpTemp);
				}	
				//获取后缀名
				String ext = "";
				if(oldFileName.indexOf(".") != -1){
					String[] exts = oldFileName.split("\\.");
					ext = exts[exts.length-1];
				}
				//判断是否是图片类型
				if (!"bmp".equalsIgnoreCase(ext) && !"jpg".equalsIgnoreCase(ext) && !"jpeg".equalsIgnoreCase(ext) 
						&& !"gif".equalsIgnoreCase(ext) && !"psd".equalsIgnoreCase(ext) && !"png".equalsIgnoreCase(ext)
						&& !"tiff".equalsIgnoreCase(ext) && !"tga".equalsIgnoreCase(ext) && !"eps".equalsIgnoreCase(ext)){
					result = "{\"flag\" : \"2\"}";
					continue;
				}
				if (oldFileName.length() > 30) {
					oldFileName = oldFileName.substring(0,30) + "……." + ext;
				}
				//替换新文件名
				newFileName = IDGenerator.idGenerator() +"."+ ext;
				File saveFile = new File(ftpTemp, newFileName);
				filePart.writeTo(saveFile);
				result = "{\"flag\" : \"1\", \"orgName\" : \"" + oldFileName + "\", \"filename\" : \""
						+ newFileName + "\", \"path\": \"" + ftpTemp + "\"}";
				logger.debug("result=" + result);
			}
			logger.debug("文件上传结束");
		} catch (IOException e) {
			result = "{\"flag\" : \"3\"}";
			logger.error("上传文件失败", e);
		} catch (Exception e) {
			result = "{\"flag\" : \"4\"}";
			logger.error("上传文件失败", e);
		}
		out.print(result);
		out.close();
	}
	/**
	 * 上传文件到本地的临时目录,最大限制1G
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "uploadFileToTemp")
	@ResponseBody
	public void uploadFileToTemp(HttpServletRequest request, HttpServletResponse response) {
		String result = "";
		PrintWriter out = null;
		try {
			String oldFileName = "";
			String newFileName = "";
			logger.debug("开始上传文件！");
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			MultipartParser parser = new MultipartParser(request, 1024*1024*1024*1, true, true, "UTF-8");
			Part part;
			while ((part = parser.readNextPart()) != null) {
				if (!part.isFile()){
					continue;
				}
				FilePart filePart = (FilePart) part;
				oldFileName = filePart.getFileName();
				if (StringUtil.isBlank(oldFileName)) {
					continue;
				}
				//判断临时目录是否存在
				if (!new File(ftpTemp).exists()) {
					FileUtil.mkdirs(ftpTemp);
				}	
				//获取后缀名
				String ext = "";
				if(oldFileName.indexOf(".") != -1){
					String[] exts = oldFileName.split("\\.");
					ext = exts[exts.length-1];
				}
				String type = getFileType(ext);
				if(!"2".equalsIgnoreCase(type)){
					if (oldFileName.length() > 30) {
						oldFileName = oldFileName.substring(0,30) + "……." + ext;
					}
					//替换新文件名
					newFileName = IDGenerator.idGenerator() +"."+ ext;
					File saveFile = new File(ftpTemp, newFileName);
					filePart.writeTo(saveFile);
					result = "{\"flag\" : \""+type+"\", \"orgName\" : \"" + oldFileName + "\", \"filename\" : \""
							+ newFileName + "\", \"path\": \"" + ftpTemp + "\"}";
					logger.debug("result=" + result);
				}else{
					result = "{\"flag\" : \"2\"}";
				}
			}
			logger.debug("文件上传结束");
		} catch (IOException e) {
			result = "{\"flag\" : \"3\"}";
			logger.error("上传文件失败", e);
		} catch (Exception e) {
			result = "{\"flag\" : \"4\"}";
			logger.error("上传文件失败", e);
		}
		out.print(result);
		out.close();
	}
	
	/**
	 * 删除本地文件，伪删除
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "deleteUploadFileToTemp")
	@ResponseBody
	public void deleteUploadFileToTemp(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String result = "";
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		result = "{\"flag\" : \"delete\"}";
		out.print(result);
		out.close();
	}
	
	/**
	 * 视频、音频、图片上传
	 * @param ext
	 * @return
	 */
	private String getFileType(String ext){
		String result = "";
		if ("bmp".equalsIgnoreCase(ext) ||"jpg".equalsIgnoreCase(ext) ||"jpeg".equalsIgnoreCase(ext) 
				||"gif".equalsIgnoreCase(ext) ||"psd".equalsIgnoreCase(ext) ||"png".equalsIgnoreCase(ext)
				||"tiff".equalsIgnoreCase(ext) ||"tga".equalsIgnoreCase(ext) ||"eps".equalsIgnoreCase(ext)){
			result = "1";
		}else if("avi".equalsIgnoreCase(ext) || "mp4".equalsIgnoreCase(ext) || "wmv".equalsIgnoreCase(ext)){
			result = "5";
		}else if("mp3".equalsIgnoreCase(ext)){
			result = "6";
		}else if("xls".equalsIgnoreCase(ext)||"xlsx".equalsIgnoreCase(ext)){
			result = "7";
		}else if("doc".equalsIgnoreCase(ext)||"docx".equalsIgnoreCase(ext)){
			result = "8";
		}else{
			result = "2";
		}
		return result;
	}
	
}