package module1.pechincha.view;

import java.io.IOException;

import module1.pechincha.util.ActionDone;
import biz.source_code.miniTemplator.MiniTemplator;
import biz.source_code.miniTemplator.MiniTemplator.TemplateSyntaxException;
//Classe com o comportamento genérico de uma classe de controle de visão de caso de uso.

public abstract class ViewController {
	private String sevletContext;
	private String useCase;
	private String separador;
	
	//Pega o contexto da servlet e cria um caminho de diretório com base no nome do caso de uso.
	public ViewController(String sevletContext,String useCase ){
		separador = System.getProperty("file.separator");
		this.sevletContext = sevletContext;
		this.useCase = useCase+separador;
	};
	
	//Método genérico para instanciar o MiniTemplator.
	protected MiniTemplator startMiniTemplator(String path){
		MiniTemplator mi = null;
		try {
			System.out.println(path);
			mi = new MiniTemplator(path);
		} catch (TemplateSyntaxException | IOException e) {
			e.printStackTrace();
		}
		return mi;
	};
	
	//Método que devolve o caminho total de template.
	protected String getTemplate (ActionDone ad){
		String temp = getSevletContext()+getUseCase()+ad.getAction()+".html";
		System.out.println(temp);
		return temp;
	};
	
	public String getSevletContext() {
		return sevletContext;
	}

	public String getUseCase() {
		return useCase;
	}

	public String getSeparador() {
		return separador;
	}
	
	//Comportamento que deve ser abstraído para as classes filhas para garantir a interatividade com
	//a classe ServletController
	//escolhe a o cenário a ser executado para cada visão específica
	public abstract String choose( ActionDone ad );
}
