package module1.pechincha.model;

import module1.pechincha.util.ReflectiveModel;

public class Imagem extends ReflectiveModel {
	private int pk = 0;
	private int fkproduto = 0;
	private String formato = null;
	
	public Imagem(){
		
	}
	public Imagem(int pk, int fkproduto, String formato){
		this.pk = pk; 
		this.fkproduto = fkproduto;
		this.formato = formato;
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
	public void setPk(int pk){
		this.pk = pk;
	}
	public void setFkProduto(int fk){
		this.fkproduto = fk;
	}
	public void setFormato(String formato){
		this.formato = formato;
	}
}
