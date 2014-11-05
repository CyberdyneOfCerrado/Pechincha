package module1.pechincha.model;

import module1.pechincha.util.ReflectiveModel;



public class Pessoa extends ReflectiveModel{
	private int id; 
	private String nome; 
	private int idade; 
	private double altura;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getIdade() {
		return idade;
	}
	public void setIdade(int idade) {
		this.idade = idade;
	}
	public double getAltura() {
		return altura;
	}
	public void setAltura(double altura) {
		this.altura = altura;
	} 
	
}
