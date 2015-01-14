package org.easywork.util.common;

import java.util.List;

import org.junit.Test;

import rongding.framework.util.web.FtpUtil;

/**
 * FTP远程命令列表
 * USER PORT RETR ALLO DELE SITE XMKD CDUP FEAT PASS PASV STOR REST
 * CWD STAT RMD XCUP OPTS ACCT TYPE APPE RNFR XCWD HELP XRMD STOU AUTH REIN STRU
 * SMNT RNTO LIST NOOP PWD SIZE PBSZ QUIT MODE SYST ABOR NLST MKD XPWD MDTM PROT
 * 在服务器上执行命令,如果用sendServer来执行远程命令(不能执行本地FTP命令)的话，所有FTP命令都要加上\r\n
 * ftpclient.sendServer("XMKD /test/bb\r\n"); //执行服务器上的FTP命令
 * ftpclient.readServerResponse一定要在sendServer后调用 nameList("/test")获取指目录下的文件列表
 * XMKD建立目录，当目录存在的情况下再次创建目录时报错 XRMD删除目录 DELE删除文件
 */
public class FtpUtilTest {
	@Test
	public void test1() {
		FtpUtil ftp = new FtpUtil("10.71.0.123", "ycbike", "lantoon");
		ftp.connectServer();
		ftp.upload("D:/test_data/hzbk/blackmanagecard.dt", "/home/ycbike/blackmanagecard.dt");
//		List list = ftp.getFileList("/home/zxf");
//		for (int i = 0; i < list.size(); i++) {
//			String name = list.get(i).toString();
//			System.out.println(name);
//		}
		ftp.closeServer();
	}
}
