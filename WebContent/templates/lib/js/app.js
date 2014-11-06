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
			var temp = new Messeger(idEmissor,idLeilao,LANCE, userName,lanceCorrente);
			 websocketConnection.msg(temp);
	   });
	  
	  //Clicando no botão de aumentar
/*	  $('#botao-aumentar').click(function(){
			incrementLance();
	  });
*/		$('#botao-aumentar').click(incrementLance);
	  
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
/*	  
	  $('html').click(function(){
		  alertaDown();
	  });
*/	  $('html').click(alertaDown);

	  alertaDown();
	  //Seção de loops.
//	  setInterval(manterConexao,1000);
	  setInterval(updateHora,1000 * 60 );
});

var handshake = false;

function implementsWebSocket(){
	websocketConnection.onopen = function(){
		console.log('Tô dentro');
		console.log('Status: '+websocketConnection.readyState+" conectado: "+handshake);
		if (!handshake){
			var temp = new Messeger(idEmissor,idLeilao,HANDSHAKE,userName,' ');
			websocketConnection.msg(temp);
			handshake = true;
			alertaDown();
		}
	}

	websocketConnection.onclose = function(){
		console.log('Tô fora');
		initWebSocket();
		implementsWebSocket(); 
		handshake = false;
	}

	websocketConnection.onerror = function(error){
		console.log('Deu treta');
		if (websocketConnection.readyState > 1){
			initWebSocket();
			implementsWebSocket(); 
			handshake = false;
		}
	}
	
	websocketConnection.onmessage = function(e){
	   resolverMessage(JSON.parse(e.data)); 
	}
}
/*
function manterConexao(){
	console.log('Status: '+websocketConnection.readyState+" conectado: "+handshake);
	
	if( websocketConnection.readyState != 1){
		initWebSocket();
		implementsWebSocket(); 
		handshake = false;
	 }
	
	 if(websocketConnection.readyState == 1 && !handshake){
		var temp = new Messeger(idEmissor,idLeilao,HANDSHAKE,userName,' ');
		websocketConnection.msg(temp);
		handshake = true;
		alertaDown();
	 }
}
*/
function validaMensagem(){
	var mensagem = $('#msg').val().trim();
	
	if( mensagem.length <=  0){
		alertaUp('Você não pode mandar mensagem em branco.');
		$('#msg').val('');
		return;
	}
	
	var temp = new Messeger(idEmissor,idLeilao,MENSAGEM, userName,mensagem);
	 websocketConnection.msg(temp);
	$('#msg').val('');
}

