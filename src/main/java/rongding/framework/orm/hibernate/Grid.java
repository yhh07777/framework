package rongding.framework.orm.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Grid implements java.io.Serializable {
	private Long total = 0L;
	private List rows = new ArrayList();
	private List<Map<String, Object>> footer;
	
	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

	public List<Map<String, Object>> getFooter() {
		return footer;
	}

	public void setFooter(List<Map<String, Object>> footer) {
		this.footer = footer;
	}

}
