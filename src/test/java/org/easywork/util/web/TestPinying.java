package org.easywork.util.web;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.junit.Test;

import rongding.framework.util.web.PinyinUtil;

public class TestPinying {

	@Test
	public void test01() {
		try {
			HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
			format.setCaseType(HanyuPinyinCaseType.LOWERCASE);						//小写
			format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);					//不要声调
			format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);				//设置U的格式
			String kouhao = "我是中国人我要抵制日货china";
			StringBuffer re = new StringBuffer();
			for(int i=0;i<kouhao.length();i++) {
				System.out.println(kouhao.charAt(i));
//				re.append((PinyinHelper.toHanyuPinyinStringArray(kouhao.charAt(i), format))[0]);
			}
			System.out.println(re);
			String[] strs = PinyinHelper.toHanyuPinyinStringArray('中',format);
			for(String str:strs) {
				System.out.println(str);
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test02() {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
		String kouhao = "支持国产，抵制日货,kill japan";
		System.out.println(PinyinUtil.getInstance().str2Pinyin(kouhao, ","));
		System.out.println(PinyinUtil.getInstance().strFirst2Pinyin(kouhao));
	}

}
