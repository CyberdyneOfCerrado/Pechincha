package module1.pechincha.model;

import module1.pechincha.util.ReflectiveModel;

public class LoteProduto extends ReflectiveModel {
	private int fkleilao;
	private int fkproduto;
	private int unidades;	
	
	public LoteProduto(Produto produto,int unidades,float precoLote, int fkleilao,int fkproduto){
		this.unidades=unidades;
		this.fkleilao=fkleilao;
		this.fkproduto=fkproduto;
	}
	
	public LoteProduto(){}
	
	public int getFkleilao() {
		return fkleilao;
	}

	public void setFkleilao(int fkleilao) {
		this.fkleilao = fkleilao;
	}

	public int getFkproduto() {
		return fkproduto;
	}

	public void setFkproduto(int fkproduto) {
		this.fkproduto = fkproduto;
	}

	public int getUnidades() {
		return unidades;
	}
	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}
}
