package module2.pechincha.util;

import javax.websocket.Session;

public class UserSession {
	private Session session;
	private String nickname;
	private int idUser; 
	private int idLeilao; 
	
	public UserSession( String nickname , Session session, int idUser, int idLeilao){
		this.session = session; 
		this.nickname = nickname;
		this.idUser =  idUser; 
		this.idLeilao = idLeilao; 
	}; 
	
	public UserSession(){
		this.session = null; 
		this.nickname = null;
		this.idUser = 0; 
		this.idLeilao = 0; 
	}; 
	
	public Session getSession() {
		return session;
	}
	
	public void setSession(Session session) {
		this.session = session;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickName(String nickname) {
		this.nickname = nickname;
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
