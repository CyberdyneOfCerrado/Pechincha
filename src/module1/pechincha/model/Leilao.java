package module1.pechincha.model;

import module1.pechincha.util.ReflectiveModel;

public class Leilao extends ReflectiveModel {
	private int id;

	public Leilao(int id){
		this.id = id;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	} 
	
}
