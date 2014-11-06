package module1.pechincha.model;

import module1.pechincha.util.ReflectiveModel;

public class Produto extends ReflectiveModel {
	private int pk = 0;
	private String titulo = null;
	private String descricao = null;
	private float preco = 0;
	private int quantidade = 0;
	private int fkusuario = 0;
	
	public Produto(){
		
	}
	public Produto(String titulo, String descricao, float preco, int quantidade, int fkusuario){
		this.titulo = titulo; 
		this.descricao = descricao;
		this.preco = preco;
		this.quantidade = quantidade;
		this.fkusuario = fkusuario;
	};
	
	public int getPk(){
		return this.pk;
	}
	
	public String getTitulo() {
		return this.titulo;
	}
	
	public String getDescricao() {
		return this.descricao;
	}

	public float getPreco() {
		return this.preco;
	}
	public int getQuantidade() {
		return this.quantidade;
	}
	public int getFkUsuario(){
		return this.fkusuario;
	}
	public void setPk(int pk){
		this.pk = pk;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setPreco(float preco) {
		this.preco = preco;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	public void setFkUsuario(int fk){
		this.fkusuario = fk;
	}
}
