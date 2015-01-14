package org.easywork.test;

public class PropertiesObject {
	private Double pCenterlng;
	private String pCenterlat;
	public Double getpCenterlng() {
		return pCenterlng;
	}
	public void setpCenterlng(Double pCenterlng) {
		this.pCenterlng = pCenterlng;
	}
	public String getpCenterlat() {
		return pCenterlat;
	}
	public void setpCenterlat(String pCenterlat) {
		this.pCenterlat = pCenterlat;
	}
	@Override
	public String toString() {
		return "PropertiesObject [pCenterlng=" + pCenterlng + ", pCenterlat=" + pCenterlat + "]";
	}
	
	
	
}
