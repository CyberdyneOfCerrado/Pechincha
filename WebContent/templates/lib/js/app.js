//Inciando Aplicação. 
$('document').ready(function(){
	getInformations(); 
	initWebSocket();
	implementsWebSocket(); 
	
	//Tratando Eventos de teclado.
	$( '#msg' ).keyup(function( event ) {
		  if ( event.which == 13 ) {
			 var temp = JSONtoString(new Messeger(idEmissor,idLeilao,MENSAGEM, userName, $('#msg').val()))
			 websocketConnection.send(temp);
			 $('#msg').val('');
		  }
	  });
	  
	  //Clicando no botão de dar lance
	  $('#botao-lance').click(function(){
			var temp = JSONtoString(new Messeger(idEmissor,idLeilao,LANCE, userName,lanceCorrente));
			 websocketConnection.send(temp);
	   });
	  
	  //Clicando no botão de aumentar
	  $('#botao-aumentar').click(function(){
			incrementLance( );
	  });
		
	  //Seção de loops.
	  setInterval(function(){manterConexao()},1000);
	  setInterval(function(){updateHora()},1000 * 60 );
});


function implementsWebSocket(){
	websocketConnection.onopen = function(){
		console.log('Tô dentro');
	}

	websocketConnection.onclose = function(){
		console.log('Tô fora');
	}

	websocketConnection.onerror = function(error){
		console.log('Deu treta');
	}
	
	websocketConnection.onmessage = function(e){
	   resolverMessage(JSON.parse(e.data)); 
	}
}


var conectado = false;
function manterConexao(){
	 if(websocketConnection.readyState != 0 && !conectado){
		console.log("Efetuando Aperto de mão.");
		var temp = JSONtoString(new Messeger(idEmissor,idLeilao,HANDSHAKE,userName,' '));
		websocketConnection.send(temp);
		conectado = true;
	 }
	 if(websocketConnection.readyState == 3 ){
		initWebSocket();
		implementsWebSocket(); 
		conection = false;
	 }
}



