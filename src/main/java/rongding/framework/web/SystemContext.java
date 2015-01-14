package rongding.framework.web;

public class SystemContext {
	private static ThreadLocal<Integer> pageOffset = new ThreadLocal<Integer>();
	private static ThreadLocal<Integer> pageSize = new ThreadLocal<Integer>();
	private static ThreadLocal<Integer> currentPage = new ThreadLocal<Integer>();
	private static ThreadLocal<String> realpath = new ThreadLocal<String>();
	private static ThreadLocal<String> order = new ThreadLocal<String>();
	private static ThreadLocal<String> sort = new ThreadLocal<String>();

	public static String getRealpath() {
		return realpath.get();
	}

	public static void setRealpath(String _realpath) {
		realpath.set(_realpath);
	}

	public static String getOrder() {
		return order.get();
	}

	public static void setOrder(String _order) {
		order.set(_order);
	}

	public static String getSort() {
		return sort.get();
	}

	public static void setSort(String _sort) {
		sort.set(_sort);
	}

	public static int getPageSize() {
		return pageSize.get();
	}
	
	public static void setPageSize(int _pageSize) {
		pageSize.set(_pageSize);
	}

	public static void removePageSize() {
		pageSize.remove();
	}

	public static int getCurrentPage() {
		return currentPage.get();
	}

	public static void removeCurrentPage() {
		currentPage.remove();
	}
	
	public static int getPageOffset () {
		return pageOffset.get();
	}

	public static void setPageOffset(int _pageOffset) {
		pageOffset.set(_pageOffset);
		currentPage.set(_pageOffset / getPageSize() + 1);
	}
}
