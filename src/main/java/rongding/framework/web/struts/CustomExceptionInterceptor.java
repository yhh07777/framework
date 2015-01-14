package rongding.framework.web.struts;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rongding.framework.util.exception.CustomException;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("serial")
public class CustomExceptionInterceptor extends AbstractInterceptor {
	private static final Log logger = LogFactory.getLog(CustomExceptionInterceptor.class);
	private String error;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		try {
			return invocation.invoke();
		} catch (CustomException CustomException) {
			logger.error("程序出现异常", CustomException);

			ValueStack valueStack = invocation.getInvocationContext().getValueStack();
			ActionSupport actionSupport = (ActionSupport) invocation.getAction();

			exception(CustomException, actionSupport, valueStack);

			return Action.ERROR;
		}
	}

	private void exception(CustomException customException,
			ActionSupport action, ValueStack valueStack) {
		// 取出key值
		String messageKey = customException.getMessageKey();
		String[] args = customException.getMessageArgs();

		if (messageKey == null) {
			error = customException.getMessage();
		} else if (args != null && args.length > 0) {
			error = action.getText(messageKey, args);
		} else {
			error = action.getText(messageKey);
		}
		//把值放到ValueStack中
		valueStack.set("error", error);
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
