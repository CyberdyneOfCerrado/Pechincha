package module1.pechincha.model;

import module1.pechincha.util.ReflectiveModel;

public class Categoria extends ReflectiveModel {
	private int pk = 0;
	private String descricao = null;
	
	public Categoria(){
		
	}
	public Categoria(int pk, String descricao){
		this.pk = pk;
		this.descricao = descricao;
	};
	
	public int getPk(){
		return this.pk;
	}
	public String getDescricao(){
		return this.descricao;
	}
	public void setPk(int pk){
		this.pk = pk;
	}
	public void setDescricao(String descricao){
		this.descricao = descricao;
	}
}
