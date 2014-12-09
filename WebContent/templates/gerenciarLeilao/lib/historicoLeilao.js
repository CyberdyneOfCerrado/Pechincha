function enviar(Idleilao,Idleiloeiro,UseCase,Action,Redirect){
	$.post("q",{
			idleilao : Idleilao,
			idleiloeiro : Idleiloeiro,
			useCase : UseCase,
			action : Action,
			redirect : Redirect,
			index : false
			}).done(
    		function(resposta){
    			alert(resposta);
    			var resposta = JSON.parse(resposta);
    			alert(resposta);
    		}
    );
}