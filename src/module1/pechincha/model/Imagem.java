package module1.pechincha.model;

import module1.pechincha.util.ReflectiveModel;

public class Imagem extends ReflectiveModel {
	private int pk = 0;
	private int fkproduto = 0;
	private String formato = null;
	private byte[] bytes = null;
	
	public Imagem(){
		
	}
	public Imagem(int pk, int fkproduto, String formato, byte[] bytes){
		this.pk = pk; 
		this.fkproduto = fkproduto;
		this.formato = formato;
		this.bytes = bytes;
	};
	
	public int getPk(){
		return this.pk;
	}
	public int getFkProduto(){
		return this.fkproduto;
	}
	public String getFormato(){
		return this.formato;
	}
	public byte[] getBytes(){
		return this.bytes;
	}
	public void setPk(int pk){
		this.pk = pk;
	}
	public void setFkProduto(int fk){
		this.fkproduto = fk;
	}
	public void setFormato(String formato){
		this.formato = formato;
	}
	public void setBytes(byte[] bytes){
		this.bytes = bytes;
	}
}
