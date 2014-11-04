package module1.pechincha.model;

import module1.pechincha.util.ReflectiveModel;

public class Lance extends ReflectiveModel {
	private int idleilao;
	private int idusuario;
	private float lance;
	
	public Lance(int idleilao, int idusuario, float lance){
		this.idleilao = idleilao; 
		this.idusuario = idusuario;
		this.lance = lance; 
	};
	
	public int getIdLeilao() {
		return this.idleilao;
	}
	
	public int getIdUsuario() {
		return this.idusuario;
	}

	public float getLance() {
		return this.lance;
	}
	public void setIdLeilao(int idleilao){
		this.idleilao = idleilao;
	}
	public void setIdusuario(int idusuario){
		this.idusuario = idusuario;
	}
	public void setLance(float lance){
		this.lance = lance;
	}
}
