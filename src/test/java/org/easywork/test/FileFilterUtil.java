package org.easywork.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class FileFilterUtil {
	private static File fromDir = new File("D:/zxf/workspace/lanxin/WebRoot");
	private static File toDir=new File("D:/zxf/temp/WebRoot");
	
	private static String[] array = new String[] { "FilesysPrivAction.class", "filesysprivupdate.jsp"};

	private static List<File> restFiles = new ArrayList<File>();

	public static void execute(File targetDir) throws IOException {
		for (File f : targetDir.listFiles()) {
			if (f.isDirectory()) {
				execute(f);
			}
			if (ArrayUtils.contains(array, f.getName())) {
				restFiles.add(f);
				System.out.println(f.getName());
			}
		}
		
		for (File f : restFiles) {
			String pathname=toDir+f.getAbsolutePath().substring(fromDir.getAbsolutePath().length());
			FileUtils.copyFile(f, new File(pathname));
		}
	}

	@Test
	public void test1() throws Exception {
		execute(fromDir);
		StringUtils.defaultString("q");
	}
}
