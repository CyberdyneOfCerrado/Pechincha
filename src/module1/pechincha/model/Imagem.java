package module1.pechincha.model;

import module1.pechincha.util.ReflectiveModel;

public class Imagem extends ReflectiveModel {
	private int pk = 0;
	private int fkproduto = 0;
	
	public Imagem(){
		
	}
	public Imagem(int pk, int fkproduto){
		this.pk = pk; 
		this.fkproduto = fkproduto;
	};
	
	public int getPk(){
		return this.pk;
	}
	public int getFkProduto(){
		return this.fkproduto;
	}
	public void setPk(int pk){
		this.pk = pk;
	}
	public void setFkProduto(int fk){
		this.fkproduto = fk;
	}
}
