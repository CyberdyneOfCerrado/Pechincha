package module1.pechincha.model;

import module1.pechincha.util.ReflectiveModel;

public class Usuario extends ReflectiveModel {
	private String nomeCompleto;
	private String nickname;
	private String senha;
	private String dataNascimento;
	private String emailPrincipal;
	private String emailAlternativo;
	private String skype;
	private String telCelular;
	private String telFixo;
	private int pk;
	public Usuario(int pk,String nomeCompleto, String cpf, String nomeApresentação,
			String nickname, String senha, String dataNascimento,
			String endereco, String emailPrincipal, String emailAlternativo,
			String skype, String telCelular, String telFixo) {
		super();
		this.nomeCompleto = nomeCompleto;
		this.nickname = nickname;
		this.senha = senha;
		this.dataNascimento = dataNascimento;
		this.emailPrincipal = emailPrincipal;
		this.emailAlternativo = emailAlternativo;
		this.skype = skype;
		this.telCelular = telCelular;
		this.telFixo = telFixo;
		this.pk = pk;
	}
	public Usuario() {}
	public int getPk() {
		return pk;
	}
	public void setPk(int pk) {
		this.pk = pk;
	}
	public String getNomeCompleto() {
		return nomeCompleto;
	}
	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	public String getEmailPrincipal() {
		return emailPrincipal;
	}
	public void setEmailPrincipal(String emailPrincipal) {
		this.emailPrincipal = emailPrincipal;
	}
	public String getEmailAlternativo() {
		return emailAlternativo;
	}
	public void setEmailAlternativo(String emailAlternativo) {
		this.emailAlternativo = emailAlternativo;
	}
	public String getSkype() {
		return skype;
	}
	public void setSkype(String skype) {
		this.skype = skype;
	}
	public String getTelCelular() {
		return telCelular;
	}
	public void setTelCelular(String telCelular) {
		this.telCelular = telCelular;
	}
	public String getTelFixo() {
		return telFixo;
	}
	public void setTelFixo(String telFixo) {
		this.telFixo = telFixo;
	}
}
