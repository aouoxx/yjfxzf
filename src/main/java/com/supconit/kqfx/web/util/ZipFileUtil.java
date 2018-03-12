package com.supconit.kqfx.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
/**
 * 
 * @author zongkai
 *
 */
public class ZipFileUtil {
	public static boolean zipToFile(String sZipPathFile, String sDestPath) {
		boolean flag = false;
		try {

			FileInputStream fins = new FileInputStream(sZipPathFile);

			ZipInputStream zins = new ZipInputStream(fins);
			ZipEntry ze = null;
			byte ch[] = new byte[256];
			while ((ze = zins.getNextEntry()) != null) {
				File zfile = new File(sDestPath + "//" + ze.getName());
				File fpath = new File(zfile.getParentFile().getPath());
				if (ze.isDirectory()) {
					if (!zfile.exists())
						zfile.mkdirs();
					zins.closeEntry();
				} else {
					if (!fpath.exists())
						fpath.mkdirs();
					FileOutputStream fouts = new FileOutputStream(zfile);
					int i;
					String zfilePath = zfile.getAbsolutePath();
					while ((i = zins.read(ch)) != -1)
						fouts.write(ch, 0, i);
					zins.closeEntry();
					fouts.close();

					if (zfilePath.endsWith(".zip")) {
						zipToFile(zfilePath, zfilePath.substring(0, zfilePath
								.lastIndexOf(".zip")));
					}

				}
			}
			fins.close();
			zins.close();
			// if necessary, delete original zip-file
//			@SuppressWarnings("unused")
//            File file = new File(sZipPathFile);
			// file.delete();
			flag = true;
		} catch (Exception e) {
			System.err.println("Extract error:" + e.getMessage());
		}
		return flag;

	}
}
