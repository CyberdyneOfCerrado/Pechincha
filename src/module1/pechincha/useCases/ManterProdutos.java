package module1.pechincha.useCases;

import module1.pechincha.controllers.ModelController;

public class ManterProdutos  extends ModelController{

	@Override
	public String[] getActions() {
		String[] actions = {
			"",
			"",
			"",
			""
		}; 
		return actions;
	}

	@Override
	public String getUserCase() {
		return "manterProdutos";
	}

}
