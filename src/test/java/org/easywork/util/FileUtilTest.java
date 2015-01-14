package org.easywork.util;

import java.io.File;

import org.junit.Test;

import rongding.framework.util.io.FileUtil;

public class FileUtilTest {
	File fromFile=new File("C:/Program Files/Java/jdk1.6.0_10/README.html");
	File toFile=new File("C:/Users/rdkjdn/Desktop/temp.html");
	private String imageName="README.html";
	
	@Test
	public void test1() throws Exception {
		FileUtil.save(fromFile, toFile);
	}
	
	@Test
	public void test2() throws Exception {
		FileUtil.save(fromFile, "C:/Users/rdkjdn/Desktop", imageName);
	}
}
