package module1.pechincha.useCases;

import module1.pechincha.controllers.ModelController;

public class GerenciarLeilao extends ModelController{

	@Override
	public String[] getActions() {
		String[] actions = {
				"criarLeilao",
				"reenviarEmail",
				"getHistorico",
				"getTodosLeiloes",
				"entrarLeilao",
				"enviarEmail",
				"pesquisarLeilao",
				"finalizarLeilao",
				"iniciarLeilao"
		   }; 
return actions;
	}

	@Override
	public String getUserCase() {
		return "gerenciarLeilao";
	}
	//os metodos vao abaixo
}
