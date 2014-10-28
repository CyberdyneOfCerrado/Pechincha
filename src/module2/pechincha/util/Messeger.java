package module2.pechincha.util;

import module2.pechincha.enumeration.MsgTypes;

public class Messeger {
	private int idEmissor; 
	private int idLeilao;
	private MsgTypes tipoMsg; 
	private String msg;
	
	
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
}
