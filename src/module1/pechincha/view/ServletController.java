package module1.pechincha.view;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import pedidos.control.UseCaseController;
import pedidos.util.ActionDone;
import pedidos.util.DoAction;
import pedidos.viewUseCases.ManterAdmView;
import pedidos.viewUseCases.ManterClienteView;
import pedidos.viewUseCases.ManterPedidoView;
import pedidos.viewUseCases.ManterProdutoView;
import pedidos.viewUseCases.SecurityView;
import biz.source_code.miniTemplator.MiniTemplator;
import biz.source_code.miniTemplator.MiniTemplator.TemplateSyntaxException;

//Cria o pacote geral de comunicação da aplicação 'DoAction' e gerencia as classes geradoreas de conteúdo.
public class ServletController {
	private String servletContext;
	private UseCaseController ucc;
	private Hashtable<String,ViewController> listViews;
	String separador;
	
	public ServletController( String servletContext){
        separador = System.getProperty("file.separator");
		this.servletContext = servletContext+"templates"+separador;
		this.ucc = new UseCaseController();
		this.listViews = new Hashtable<>();
		initViews();
	};
	
	//Adicionar todas as classes de gerenciamento de conteúdo aqui.
	private void initViews(){
		listViews.put("manterCliente", new ManterClienteView(servletContext,"manterCliente"));
		listViews.put("manterProduto", new ManterProdutoView(servletContext,"manterProduto"));
		listViews.put("manterPedido", new ManterPedidoView(servletContext,"manterPedido"));
		listViews.put("manterAdm", new ManterAdmView(servletContext,"manterAdm"));
		listViews.put("security", new SecurityView(servletContext,"security"));
	};
	
	private String init() throws TemplateSyntaxException, IOException{
		MiniTemplator index = null;
		index = new MiniTemplator(servletContext+"index.html");
		index.setVariable("conteudo"," ");
		return index.generateOutput();
	};	
	
	//Processa os dados da Servlet.
	public String process(HttpServletRequest request) throws TemplateSyntaxException, IOException{
		DoAction   da = makeDoAction(request); //convertendo o Request em DoAction
		ActionDone ad = null;
		if(da.getAction()==null){
			return init();//cria uma tela vazia
		}else{
		//Se ação informada for do tipo 'redirect=true' os dados não devem ser enviados p/ o UseCaseController
			if( da.getData("redirect").equals("false")){
				ad = ucc.chooseUserCase(da);
				ad.setData("redirect","false");
			}else{
				ad = new ActionDone(da.getUseCase(),da.getAction(),da.getHashtable());//copiando o DA p/ o AC
			}
		}
		return readActionDone(ad);//abre o pacote de ação concluída e o manda p/ classe especialista
	};
	
	//Cria um pacote DoAction
	private DoAction makeDoAction(HttpServletRequest request){
		//1 pegando o nome do caso de uso e a respectiva ação.
		//useCase e Action são atributos estáticos em qualquer formulário
		String useCase = request.getParameter("useCase");
		String action   = request.getParameter("action");
		String security = (String) request.getAttribute("security");
		DoAction da;
		
		//Casou houver alguma restrição de segurança
		if(security.equals("true")){
			da = new DoAction("security","acessBlock");
			da.setData("redirect","true"); //informa se deve pegar o DA e processá-lo, falsa = processa, true = apenas redireciona
			return da;
		}
				
		da = new DoAction(useCase,action);
		
		//Pegando todos os parâmetros adicionados, exceto pelo useCase e action;
		Enumeration<String> valuesName = request.getParameterNames();
		
		while(valuesName.hasMoreElements()){
			String temp = valuesName.nextElement();
			if( !temp.equals("useCase") && !temp.equals("action")){
				da.setData(temp,request.getParameter(temp));//setando o nome do parametro como chave na hashtable
															//setando o nome do parametro como valor
			}
		}
		//Pegando dados de Sessão
		da.setData("Session", request.getSession());
		
		return da;
	};
	
	//Interpreta os dados do pacote ActionDone.
	private String readActionDone(ActionDone ad){
		
		String conteudo = null;
		//gerando o conteúdo.
		 ViewController view = listViews.get(ad.getUseCase());
		
		 conteudo  = view.choose(ad);
		
		//Fixando conteúdo na index.
		MiniTemplator index = null;
		try {
			System.out.println(servletContext+"index.html");
			index = new MiniTemplator(servletContext+"index.html");
		} catch (TemplateSyntaxException | IOException e) {
			e.printStackTrace();
		}
		index.setVariable("conteudo",conteudo);
		return index.generateOutput();
	};
}
