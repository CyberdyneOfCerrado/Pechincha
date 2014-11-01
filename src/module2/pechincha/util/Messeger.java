package module2.pechincha.util;

import module2.pechincha.enumeration.MsgTypes;

public class Messeger {
	private int idEmissor; 
	private int idLeilao;
	private String userName;
	private MsgTypes tipoMsg; 
	private String msg;
	
	public Messeger(){
		
	}
	
	public Messeger(int idEmissor, int idLeilao, String userName, MsgTypes tipoMsg, String msg ){
		this.idEmissor = idEmissor; 
		this.idLeilao = idLeilao; 
		this.tipoMsg = tipoMsg; 
		this.setUserName(userName);
		this.msg = msg;
	}
	
	public int getIdEmissor() {
		return idEmissor;
	}
	public void setIdEmissor(int idEmissor) {
		this.idEmissor = idEmissor;
	}
	public int getIdLeilao() {
		return idLeilao;
	}
	public void setIdLeilao(int idLeilao) {
		this.idLeilao = idLeilao;
	}
	public MsgTypes getTipoMsg() {
		return tipoMsg;
	}
	public void setTipoMsg(MsgTypes tipoMsg) {
		this.tipoMsg = tipoMsg;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
