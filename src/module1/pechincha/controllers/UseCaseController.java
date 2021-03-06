package module1.pechincha.controllers;

import java.lang.reflect.Method;
import java.util.Hashtable;

import module1.pechincha.useCases.AmbienteLeilao;
import module1.pechincha.useCases.GerenciarLeilao;
import module1.pechincha.useCases.Home;
import module1.pechincha.useCases.ManterGalinha;
import module1.pechincha.useCases.ManterProdutos;
import module1.pechincha.useCases.ManterUsuario;
import module1.pechincha.util.ActionDone;
import module1.pechincha.util.DoAction;

//Esta classe procura pela classe de UseCase mais adequada para atuar na requisi��o
public class UseCaseController {
	Hashtable<String,ModelController> listUserCase;
	
	public UseCaseController(){
		listUserCase = new Hashtable<>(); 
		
		//Casos de uso novos devem ser adicionados aqui.
		listUserCase.put("ambienteLeilao", new AmbienteLeilao());
		listUserCase.put("manterGalinha", new ManterGalinha());
		listUserCase.put("gerenciarLeilao", new GerenciarLeilao());
		listUserCase.put("manterProdutos", new ManterProdutos());
		listUserCase.put("manterUsuario", new ManterUsuario());
		listUserCase.put("home", new Home());
		
	};
	
	@SuppressWarnings("unchecked")
	public ActionDone chooseUserCase(DoAction doAction){
		//1: procurar o caso de uso na hashtable;
		//2: procurar a a��o no caso de uso;
		//3: procurar o m�todo que executar� o doAction;
		//4: invocar o m�todo passando o doAction.
		
		//Validado se a requisi��o chegou nula.
		if(doAction.getUseCase() == null){
			return copy(doAction);
		}
		ModelController useCase = listUserCase.get(doAction.getUseCase());
		
		//Validando exist�ncia do caso de uso.
		if( useCase == null ){
			//Gerar um actionDone
			return copy(doAction);
		}
		//Fim validando caso de uso.
		
		String[] actions = useCase.getActions();
		
		//Validando exist�ncia da a��o
		boolean temp = false;
		for(String a : actions)if( a.equals(doAction.getAction())) temp = true;
		if( !temp ){
			//A a��o informada n�o existe.
			return copy(doAction);
		}
		//Fim da valida��o das a��es.
		
		//----Atuando com reflex�o------
		@SuppressWarnings("rawtypes")
		Class classe = useCase.getClass();//convertendo o UC em tipo class
		ActionDone actionDone = null;
		try {
			Method m   = classe.getMethod(doAction.getAction(),DoAction.class);//nome do m�todo e par�metros do m�todo
			actionDone = (ActionDone) m.invoke(useCase,doAction);//casting p/ action done de um object (resultado do invoke)
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Ocorreu um erro na igni��o do m�todo.");
		}
		return actionDone;
	};
	
	private ActionDone copy(DoAction da){
		return new ActionDone(da.getUseCase(),da.getAction());
	};
}
