package org.easywork.util.poi.model;

import rongding.framework.util.poi.ExcelResources;

public class Student {
	private int id;
	private String name;
	private String no;
	private String sex;
	
	private String a1;
	private String a2;
	private String a3;
	private String a4;
	private String a5;
	private String a6;
	private String a7;
	private String a8;
	
	private String date;
	
	@ExcelResources(title="学生标识",order=1)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@ExcelResources(title="学生姓名")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelResources(title="学生学号", order=2)
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	@ExcelResources(title="学生性别")
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Student(int id, String name, String no, String sex) {
		this.id = id;
		this.name = name;
		this.no = no;
		this.sex = sex;
	}
	
	public Student() {
		super();
	}
	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", no=" + no + ", sex="
				+ sex + "]";
	}
	public String getA1() {
		return a1;
	}
	public void setA1(String a1) {
		this.a1 = a1;
	}
	public String getA2() {
		return a2;
	}
	public void setA2(String a2) {
		this.a2 = a2;
	}
	public String getA3() {
		return a3;
	}
	public void setA3(String a3) {
		this.a3 = a3;
	}
	public String getA4() {
		return a4;
	}
	public void setA4(String a4) {
		this.a4 = a4;
	}
	public String getA5() {
		return a5;
	}
	public void setA5(String a5) {
		this.a5 = a5;
	}
	public String getA6() {
		return a6;
	}
	public void setA6(String a6) {
		this.a6 = a6;
	}
	public String getA7() {
		return a7;
	}
	public void setA7(String a7) {
		this.a7 = a7;
	}
	public String getA8() {
		return a8;
	}
	public void setA8(String a8) {
		this.a8 = a8;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}
