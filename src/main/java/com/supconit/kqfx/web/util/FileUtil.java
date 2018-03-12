/**
 * 
 * 
 */
package com.supconit.kqfx.web.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;



/**
 * 文件处理共通函数
 * 
 */
public class FileUtil {

	public final static String BASE_UPLOAD_PATH = System
			.getProperty("webapp.root")
//			+ Constants.PATH_SEPARATOR
			+ Constants.FILE_BASE_PATH;

	private final static int BUFFER_SIZE = 16 * 1024;

	/**
	 * 验证文件路径是否存在，如果存在返回true
	 * 
	 * @param filePath
	 *            文件路径
	 */
	public static boolean isPathExist(String filePath) {
		boolean temp = false;
		File baseFile = new File(filePath);
		temp = baseFile.isDirectory();
		return temp;
	}

	/**
	 * 验证文件是否存在，如果存在返回true
	 * 
	 * @param fullFileName
	 *            文件完整路径
	 */
	public static boolean isFileExist(String fullFileName) {
		boolean temp = false;
		File baseFile = new File(fullFileName);
		temp = baseFile.isFile();
		return temp;
	}

	/**
	 * 创建一个文件路径，可以创建很深的路径，而不是一级文件夹。
	 * 
	 * @param filePath
	 *            文件路径
	 */
	public static boolean createPath(String filePath) {
		String strpa = filePath.replace("\\", "/");
		boolean temp = false;
		File baseFile = new File(strpa);
		if (!baseFile.exists()) {
			temp = baseFile.mkdirs();
		}
		return temp;
	}

	/**
	 * 判断输入的文件类型是否合法
	 * 
	 * @param fileType
	 *            输入的文件类型
	 * @param requestType
	 *            合法的文件类型
	 * @return
	 */
	public static boolean isLegalType(String fileType, String requestType) {
		String[] types = requestType.split(",");
		for (String type : types) {

			if (type.equals(fileType)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 *            文件路径
	 */
	public static boolean deleteFile(String filePath) {
		boolean temp = false;
		try {
			File f = new File(filePath);
			if (f.exists()) {
				temp = f.delete();
			}
		} catch (Exception e) {
			LogUtil.error("删除文件失败：（" + filePath + "）" + e.toString());
		}
		return temp;
	}

	/**
	 * 读取文件内容，但并不在服务端上保存
	 * 
	 * @param filePath
	 *            文件路径
	 */
	public static List<String> readFileContents(File file) {
		List<String> s = new ArrayList<String>();
		FileReader fr = null;
		try {
			fr = new FileReader(file);
			// 建立BufferedReader对象，并实例化为br
			BufferedReader br = new BufferedReader(fr);
			// 从文件读取一行字符串
			String Line = br.readLine();
			// 判断读取到的字符串是否不为空
			while (Line != null) {
				s.add(Line);
				// 从文件中继续读取一行数据
				Line = br.readLine();
			}
			// 关闭BufferedReader对象
			br.close();
		} catch (Exception e) {
			LogUtil.error("读取文件出错误：(" + file + ")" + e.toString());
		} finally {
			try {
				if (fr != null) {
					// 关闭文件
					fr.close();
				}
			} catch (IOException e) {
				LogUtil.info("关闭文件出错!" + e.toString());
			}
		}
		return s;
	}

	/**
	 * 复制文件方法
	 * 
	 * @param src
	 *            文件源
	 * @param dst
	 *            目标文件
	 */
	public static void copy(File src, File dst) {
		try {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(new FileInputStream(src),
						BUFFER_SIZE);
				out = new BufferedOutputStream(new FileOutputStream(dst),
						BUFFER_SIZE);
				byte[] buffer = new byte[BUFFER_SIZE];
				while (in.read(buffer) > 0) {
					out.write(buffer);
				}
			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {
			LogUtil.error(e);
		}
	}
	
	/**
	 * 压缩图片
	 * @param sourceFile 原图片
	 * @param targetFile 生成压缩图片
	 */
	public static void compressImage(String sourceFile, String targetFile){
		try {
			//读取图片数据
			File file = new File(sourceFile);
			BufferedImage image = ImageIO.read(file);
			int width = image.getWidth();
			int height = image.getHeight();
			//写图片信息
			Image img = image.getScaledInstance(width, height, Image.SCALE_REPLICATE);
			BufferedImage oImage = new BufferedImage(width, height, Image.SCALE_REPLICATE);
			oImage.getGraphics().drawImage(img, 0, 0, null);
			File oFile = new File(targetFile);
			String[] exts = sourceFile.split("\\.");
			ImageIO.write(oImage, exts[exts.length-1], oFile);
		} catch (Exception e) {
			new RuntimeException("",e);
		}
	}
	
}
