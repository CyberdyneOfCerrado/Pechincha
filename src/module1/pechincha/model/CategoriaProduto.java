package module1.pechincha.model;

import module1.pechincha.util.ReflectiveModel;

public class CategoriaProduto extends ReflectiveModel {
	private int pk = 0;
	private int fkcategoria = 0;
	private int fkproduto = 0;
	
	public CategoriaProduto(){
		
	}
	public CategoriaProduto(int pk, int fkcategoria, int fkproduto){
		this.pk = pk;
		this.fkcategoria = fkcategoria;
		this.fkproduto = fkproduto;
	};
	
	public int getPk(){
		return this.pk;
	}
	public int getFkProduto(){
		return this.fkproduto;
	}
	public int getFkCategoria(){
		return this.fkcategoria;
	}
	public void setPk(int pk){
		this.pk = pk;
	}
	public void setFkProduto(int fk){
		this.fkproduto = fk;
	}
	public void setFkCategoria(int fk){
		this.fkcategoria = fk;
	}
}
