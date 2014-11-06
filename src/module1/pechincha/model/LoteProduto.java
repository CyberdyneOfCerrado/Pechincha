package module1.pechincha.model;

import java.util.ArrayList;

import module1.pechincha.util.ReflectiveModel;

public class LoteProduto extends ReflectiveModel {
	private Produto produto;
	private int unidades;
	private float precoLote;
	
	public LoteProduto(Produto produto,int unidades,float precoLote){
		this.precoLote=precoLote;
		this.produto=produto;
		this.unidades=unidades;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public int getUnidades() {
		return unidades;
	}
	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}
	public float getPrecoLote() {
		return precoLote;
	}
	public void setPrecoLote(float precoLote) {
		this.precoLote = precoLote;
	}
}
