package module2.pechincha.util;

import javax.websocket.Session;

public class UserSession {
	private Session session;
	private int idUusuario; 
	private int idLeilao; 
	private String nickName;
	
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public int getIdUusuario() {
		return idUusuario;
	}
	public void setIdUusuario(int idUusuario) {
		this.idUusuario = idUusuario;
	}
	public int getIdLeilao() {
		return idLeilao;
	}
	public void setIdLeilao(int idLeilao) {
		this.idLeilao = idLeilao;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	} 
	
	
}
