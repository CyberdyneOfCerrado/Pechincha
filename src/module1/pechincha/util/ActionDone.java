package module1.pechincha.util;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.lang.model.util.Elements;

public class ActionDone {
	private boolean status;
	private boolean processed;
	private Hashtable<String,Object> data;
	private String  message;
	private String  useCase;
	private String  action;
	
	public ActionDone(String userCase , String action , Hashtable<String,Object> data ){
		this.data = data;
		this.useCase = userCase;
		this.action = action;
		this.setProcessed(false);
	};
	
	public ActionDone(String userCase , String action){
		this.data = new Hashtable<>();
		this.useCase = userCase;
		this.action = action;
		this.setProcessed(false);
	};
	public ActionDone(DoAction da){
		this.useCase = da.getUseCase();
		this.action   = da.getAction();
		this.data     = (Hashtable<String, Object>) da.getHashtable().clone();
		this.setProcessed(false);
	};
	
	public ActionDone(){
		this.data = new Hashtable<>();
	};
	
	public Enumeration<Object> getCollection(){
		return data.elements();
	}
	public Object getData(String key){
		return data.get(key);
	};
	
	public void setData(String key , Object o){
		data.put(key,o);
	};
	
	public boolean isStatus() {
		return status;
	};
	
	public void setStatus(boolean status) {
		this.status = status;
	};
	
	public String getMessage() {
		return message;
	};
	
	public void setMessage(String message) {
		this.message = message;
	};
	
	public String getUseCase() {
		return useCase;
	}
	public void setUseCase(String userCase) {
		this.useCase = userCase;
	};
	
	public String getAction() {
		return action;
	};
	
	public void setAction(String action) {
		this.action = action;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	};
}
