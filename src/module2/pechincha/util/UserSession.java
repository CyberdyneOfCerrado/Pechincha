package module2.pechincha.util;

import javax.websocket.Session;

public class UserSession {
	private Session session;
	private String nickName;
	private int idUser; 
	private int idLeilao; 
	
	public UserSession( String nickName , Session session, int idUser, int idLeilao){
		this.session = session; 
		this.nickName = nickName;
		this.idUser = idUser; 
		this.idLeilao = idLeilao; 
	}; 
	
	public Session getSession() {
		return session;
	}
	
	public void setSession(Session session) {
		this.session = session;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getIdLeilao() {
		return idLeilao;
	}

	public void setIdLeilao(int idLeilao) {
		this.idLeilao = idLeilao;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}
	
}
