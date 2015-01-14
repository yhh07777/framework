package org.easywork.util;

import static org.junit.Assert.assertThat;

import java.io.File;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

import rongding.framework.util.io.FileValidateUtil;

public class FileValidateUtilTest {
	
	private File file=new File("C:/Program Files/Java/jdk1.6.0_10/README.html");
	private String fileName="README.html";
	private String contentType="html";

	@Test
	public void test1() throws Exception {
		boolean flag=FileValidateUtil.validateImageDefault(file, fileName, contentType);
		assertThat(flag, is(false));
	}
	
	/**
	 * 正确格式 
	 * @throws Exception
	 */
	@Test
	public void test2() throws Exception {
		File file=new File("D:/Program Files/Youdao/YoudaoNote/res/DrawingBoard_Next.png");
		String fileName="DrawingBoard_Next.png";
		String contentType="image/png";
		boolean flag=FileValidateUtil.validateImageDefault(file, fileName, contentType);
		assertThat(flag, is(true));
	}
}
