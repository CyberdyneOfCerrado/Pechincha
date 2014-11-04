//Inciando Aplicação. 
$('document').ready(function(){
	getInformations(); 
	initWebSocket();
	implementsWebSocket(); 
	
	
	//Tratando Eventos de teclado.
	$( '#msg' ).keyup(function( event ) {
		  if ( event.which == 13 ) {
			  validaMensagem();
		  }
	  });
	  
	  //Clicando no botão de dar lance
	  $('#botao-lance').click(function(){
			var temp = JSONtoString(new Messeger(idEmissor,idLeilao,LANCE, userName,lanceCorrente));
			 websocketConnection.send(temp);
	   });
	  
	  //Clicando no botão de aumentar
	  $('#botao-aumentar').click(function(){
			incrementLance();
	  });
		
	  $('html').hover(function(){
		  //Em foco
		  console.log('Foco');
		  onFocoPage = true;
		  msgUnRead();
	  },function(){
		  //Fora de foco
		  console.log('Fora de foco');
		  onFocoPage = false;
	  });
	  
	  $('html').click(function(){
		  alertaDown();
	  });
	  
	  alertaDown();
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


var handshake = false;
function manterConexao(){
	console.log('Status: '+websocketConnection.readyState+" conectado: "+handshake);
	
	if( websocketConnection.readyState != 1){
		initWebSocket();
		implementsWebSocket(); 
		handshake = false;
	 }
	
	 if(websocketConnection.readyState == 1 && !handshake){
		var temp = JSONtoString(new Messeger(idEmissor,idLeilao,HANDSHAKE,userName,' '));
		websocketConnection.send(temp);
		handshake = true;
		alertaDown();
	 }
}

function validaMensagem(){
	var mensagem = $('#msg').val();
	mensagem = mensagem.trim();
	
	if( mensagem.length <=  0){
		alertaUp('Você não pode mandar mensagem em branco.');
		$('#msg').val('');
		return;
	}
	
	var temp = JSONtoString(new Messeger(idEmissor,idLeilao,MENSAGEM, userName,mensagem))
	 websocketConnection.send(temp);
	$('#msg').val('');
}

