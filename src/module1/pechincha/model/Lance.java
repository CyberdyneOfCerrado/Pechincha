package module1.pechincha.model;

import module1.pechincha.util.ReflectiveModel;

public class Lance extends ReflectiveModel {
	private int pk = 0;
	private int idleilao = 0;
	private int idusuario = 0;
	private float lance = 0;
	
	public Lance(){
		
	}
	public Lance(int idleilao, int idusuario, float lance){
		this.idleilao = idleilao; 
		this.idusuario = idusuario;
		this.lance = lance; 
	};
	
	public int getPk(){
		return this.pk;
	}
	
	public int getIdLeilao() {
		return this.idleilao;
	}
	
	public int getIdUsuario() {
		return this.idusuario;
	}

	public float getLance() {
		return this.lance;
	}
	public void setPk(int pk){
		this.pk = pk;
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
