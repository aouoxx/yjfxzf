package com.supconit.kqfx.web.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * 文件工具类
 * @author caijianming
 *
 */
public class FileTool {
	
    /** 
     * 复制文件或者目录,复制前后文件完全一样。 
     * 
     * @param resFilePath 源文件路径 
     * @param distFolder    目标文件夹 
     * @IOException 当操作发生异常时抛出 
     */ 
    public static void copyFile(String resFilePath, String distFolder) throws IOException { 
    	    File resFile = new File(resFilePath); 
            File distFile = new File(distFolder); 
            if (resFile.isDirectory()) { 
                    FileUtils.copyDirectory(resFile, distFile); 
            } else if (resFile.isFile()) { 
                    FileUtils.copyFileToDirectory(resFile, distFile); 
            } 
    }
    
    /** 
     * 删除一个文件或者目录 
     * 
     * @param targetPath 文件或者目录路径 
     * @IOException 当操作发生异常时抛出 
     */ 
    public static void deleteFile(String targetPath) throws IOException { 
            File targetFile = new File(targetPath); 
            if (targetFile.isDirectory()) { 
                    FileUtils.deleteDirectory(targetFile); 
            } else if (targetFile.isFile()) { 
                    targetFile.delete(); 
            } 
    } 
    
    /** 
     * 读取文件或者目录的大小 
     * 
     * @param distFilePath 目标文件或者文件夹 
     * @return 文件或者目录的大小，如果获取失败，则返回-1 
     */ 
    public static Long genFileSize(String distFilePath) { 
            File distFile = new File(distFilePath); 
            if (distFile.isFile()) { 
                    return distFile.length(); 
            } else if (distFile.isDirectory()) { 
                    return FileUtils.sizeOfDirectory(distFile); 
            } 
            return -1L; 
    } 
    
}
