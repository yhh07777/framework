package rongding.framework.util.web;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtil {
	private HanyuPinyinOutputFormat format;
	private static PinyinUtil instance;
	
	private PinyinUtil(){
		format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
	}
	
	public static PinyinUtil getInstance() {
		if (instance==null) {
			instance=new PinyinUtil();
		}
		return instance;
	}
	
	/**
	 * 将中文转换成拼音,以填充字符分隔
	 * @param str
	 * @param fill 填充字符
	 * @return
	 */
	public String str2Pinyin(String str,String fill) {
		try {
			StringBuffer sb = new StringBuffer();
			if(fill==null) fill="";
			boolean isCn = true;	
			for(int i=0;i<str.length();i++) {
				char c = str.charAt(i);					//获取单个字符
				if(i>0&&isCn) {									//如果上一个是中文就填充
					sb.append(fill);
				}
				if(c==' ') {										//英文中有空格也要填充
					sb.append(fill);
				}
				
				if(c>='\u4e00'&&c<='\u9fa5') {	//如果是中文
					isCn = true;
					sb.append(PinyinHelper.toHanyuPinyinStringArray(c, format)[0]);
				} else {
					if((c>='a'&&c<='z')||(c>='A'&&c<='Z')) {	//如果是英文
						sb.append(c);
					}
					isCn = false;
				}
			}
			return sb.toString();
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取每个词语的头字母
	 * @param str
	 * @return
	 */
	public String strFirst2Pinyin(String str) {
		boolean flag=false;
		try {
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<str.length();i++) {
				char c = str.charAt(i);
				if(c>='\u4e00'&&c<='\u9fa5') {		//如果是中文
					sb.append(PinyinHelper.toHanyuPinyinStringArray(c, format)[0].charAt(0));
					flag=true;
				} 
			}
			if (flag) {							//如果全是中文
				return sb.toString();
			}
			return str;								
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return null;
	}
}
