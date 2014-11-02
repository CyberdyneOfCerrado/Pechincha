package module1.pechincha.model;

import module1.pechincha.util.ReflectiveModel;

public class Leilao extends ReflectiveModel {
	private int id;
	private String etiqueta; 
	private int tempoLimite;
	private int idLeiloeiro; 
	private float lanceInicial;
	
	public Leilao(int id, String etiqueta, int tempoLimite, int idLeiloeiro, float lanceInicial){
		this.id = id; 
		this.etiqueta = etiqueta;
		this.tempoLimite = tempoLimite; 
		this.setLanceInicial(lanceInicial);
		this.idLeiloeiro = idLeiloeiro; 
	};
	
	public Leilao(int id){
		this.id = id;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public int getIdLeiloeiro() {
		return idLeiloeiro;
	}
	public void setIdLeiloeiro(int idLeiloeiro) {
		this.idLeiloeiro = idLeiloeiro;
	}
	public int getTempoLimite() {
		return tempoLimite;
	}
	public void setTempoLimite(int tempoLimite) {
		this.tempoLimite = tempoLimite;
	}
	public String getEtiqueta() {
		return etiqueta;
	}
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public float getLanceInicial() {
		return lanceInicial;
	}

	public void setLanceInicial(float lanceInicial) {
		this.lanceInicial = lanceInicial;
	} 
}
