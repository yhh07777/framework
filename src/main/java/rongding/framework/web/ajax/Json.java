package rongding.framework.web.ajax;

/**
 * 返回Ajax处理之后结果json数据的对象
 */
public class Json {
	private boolean success=false;
	/**
	 * 提示信息
	 */
	private String msg="操作失败!";
	/**
	 * 附加对象，用来存储一些特定的返回信息
	 */
	private Object obj;

	public Json() {
	}
	
	public Json(boolean success, String msg, Object obj) {
		this.success = success;
		this.msg = msg;
		this.obj = obj;
	}
	public void success() {
		this.success=true;
		this.msg="操作成功!";
	}
	public void success(String msg) {
		this.success=true;
		this.msg=msg;
	}
	
	public void fail(String msg) {
		this.success=false;
		this.msg=msg;
	}
	
	public void fail() {
		this.success=false;
		this.msg="操作失败!";
	}
	
	public Json(boolean success) {
		this.success = success;
	}
	
	public Json(boolean success, String msg) {
		this.success = success;
		this.msg = msg;
	}

	public Json(boolean success, Object obj) {
		this.success = success;
		this.obj = obj;
	}
	
	public Json(Object obj) {
		this.obj = obj;
	}

	public Json(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
