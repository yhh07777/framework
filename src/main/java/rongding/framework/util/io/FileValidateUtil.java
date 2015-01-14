package rongding.framework.util.io;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

public class FileValidateUtil {
	/**
	 * 验证上传文件类型是否属于图片格式
	 */
	public static boolean validateImage(File file,List<String> arrowType,List<String> arrowExtension,String fileName,String contentType) {
		return validateImage(file, arrowType, arrowExtension, fileName, contentType);
	}
	
	/**
	 * 验证上传文件类型是否属于图片格式
	 */
	public static boolean validateImageDefault(File file,String fileName,String contentType) {
		// 判断是否有文件上传
		if (file != null) {
			// 获取可上传文件名列表
			List<String> arrowType = Arrays.asList("image/bmp", "image/png","image/gif", "image/jpg", "image/jpeg", "image/pjpeg");
			// 获取扩展名集合
			List<String> arrowExtension = Arrays.asList("gif", "jpg", "bmp","png","swf","doc","txt","xls","pdf","exe");
			// 获取文件扩展名
			String ext = FilenameUtils.getExtension(fileName);
			// 判断是否是可上传文件类型且扩展名正确
			return arrowType.contains(contentType.toLowerCase())&& arrowExtension.contains(ext);
		}
		return true;
	}
	
	/**
	 * 验证一组图片
	 * @param files
	 * @param fileName
	 * @param contentType
	 * @return
	 */
	public static boolean validateImageDefault(File[] files,String[] fileName,String[] contentType) {
		if (files != null) {
			for (int i=0;i<files.length;i++) {
				// 获取可上传文件名列表
				List<String> arrowType = Arrays.asList("image/bmp", "image/png","image/gif", "image/jpg", "image/jpeg", "image/pjpeg");
				// 获取扩展名集合
				List<String> arrowExtension = Arrays.asList("gif", "jpg", "bmp","png","swf","doc","txt","xls","pdf","exe");
				// 获取文件扩展名
				String ext = FilenameUtils.getExtension(fileName[i]);
				// 判断是否是可上传文件类型且扩展名正确
				if(!arrowType.contains(contentType[i].toLowerCase()) || !arrowExtension.contains(ext)){
					throw new RuntimeException(fileName[i]+"-->"+contentType[i]+"文件格式不正确!");
				}
			}
		}
		return true;
	}
}
