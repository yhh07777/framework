package rongding.framework.util.exception;

public class CustomException extends RuntimeException {
	private String messageKey;  
    private String[] messageArgs;  
	
	public CustomException() {
		super();
	}

	public CustomException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public CustomException(String arg0) {
		super(arg0);
	}

	public CustomException(Throwable arg0) {
		super(arg0);
	}
	
	public String getMessageKey() {  
        return messageKey;  
    }  
  
	public CustomException(String messageKey, String message) {  
        super(message);  
        this.messageKey = messageKey;  
    }  
  
    public CustomException(String messageKey, String messageArgs, String message) {  
        super(message);  
        this.messageKey = messageKey;  
        this.messageArgs = new String[] { messageArgs };  
    }  
  
    public CustomException(String messageKey, String[] messageArgs,  
            String message) {  
        super(message);  
        this.messageKey = messageKey;  
        this.messageArgs = messageArgs;  
    }  
  
	
    public String[] getMessageArgs() {  
        return messageArgs;  
    }  
	
}
