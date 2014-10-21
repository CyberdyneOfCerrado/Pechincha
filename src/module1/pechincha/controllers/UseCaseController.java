package module1.pechincha.controllers;

import java.lang.reflect.Method;
import java.util.Hashtable;

import pedidos.useCases.ManterAdm;
import pedidos.useCases.ManterCliente;
import pedidos.useCases.ManterPedido;
import pedidos.useCases.ManterProduto;
import pedidos.util.ActionDone;
import pedidos.util.DoAction;

//Esta classe procura pela classe de UseCase mais adequada para atuar na requisição
public class UseCaseController {
	Hashtable<String,ModelController> listUserCase;
	
	public UseCaseController(){
		listUserCase = new Hashtable<>(); 
		
		//Casos de uso novos devem ser adicionados aqui.
		 listUserCase.put("manterCliente", new ManterCliente());
		 listUserCase.put("manterProduto", new ManterProduto());
		 listUserCase.put("manterPedido", new ManterPedido());
		 listUserCase.put("manterAdm", new ManterAdm());
	};
	
	@SuppressWarnings("unchecked")
	public ActionDone chooseUserCase(DoAction doAction){
		//1: procurar o caso de uso na hashtable;
		//2: procurar a ação no caso de uso;
		//3: procurar o método que executará o doAction;
		//4: invocar o método passando o doAction.
		
		//Validado se a requisição chegou nula.
		if(doAction.getUseCase() == null){
			return copy(doAction);
		}
		ModelController useCase = listUserCase.get(doAction.getUseCase());
		
		//Validando existência do caso de uso.
		if( useCase == null ){
			//Gerar um actionDone
			return copy(doAction);
		}
		//Fim validando caso de uso.
		
		String[] actions = useCase.getActions();
		
		//Validando existência da ação
		boolean temp = false;
		for(String a : actions)if( a.equals(doAction.getAction())) temp = true;
		if( !temp ){
			//A ação informada não existe.
			return copy(doAction);
		}
		//Fim da validação das ações.
		
		//----Atuando com reflexão------
		@SuppressWarnings("rawtypes")
		Class classe = useCase.getClass();//convertendo o UC em tipo class
		ActionDone actionDone = null;
		try {
			Method m   = classe.getMethod(doAction.getAction(),DoAction.class);//nome do método e parâmetros do método
			actionDone = (ActionDone) m.invoke(useCase,doAction);//casting p/ action done de um object (resultado do invoke)
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Ocorreu um erro na ignição do método.");
		}
		return actionDone;
	};
	
	private ActionDone copy(DoAction da){
		return new ActionDone(da.getUseCase(),da.getAction());
	};
}
