//Inciando Aplicação. 
$('document').ready(
		function() {
			getInformations();
			initWebSocket();
			implementsWebSocket();

			// Tratando Eventos de teclado.
			$('#msg').keyup(function(event) {
				if (event.which == 13) {
					validaMensagem();
				}
			});

			// Clicando no botão de dar lance
			$('#botao-lance').click(
					function() {
						var temp = new Messeger(idEmissor, idLeilao, LANCE,
								userName, lanceCorrente);
						websocketConnection.msg(temp);
					});

			$('#finalizar').click(
					function() {
						var temp = new Messeger(idEmissor, idLeilao, FINALIZAR,
								userName, ' ');
						websocketConnection.msg(temp);
					});

			$('#botao-aumentar').click(incrementLance);

			$('html').hover(function() {
				// Em foco
				console.log('Foco');
				onFocoPage = true;
				msgUnRead();
			}, function() {
				// Fora de foco
				console.log('Fora de foco');
				onFocoPage = false;
			});

			$('html').click(alertaDown);

			$('#owl').owlCarousel({

				autoPlay : 3000,
				stopOnHover : true,
				navigation : true,
				paginationSpeed : 1000,
				goToFirstSpeed : 2000,
				singleItem : true,
				autoHeight : true,
				transitionStyle : 'fade',
				pagination : false
			});

			alertaDown();
			setInterval(updateHora, 1000 * 60);

			// Área reservada para Tooltips
			$('#botao-aumentar').tooltip({
				delay : {
					show : 1500,
					hide : 100
				}
			});

			$('#botao-lance').tooltip({
				delay : {
					show : 1500,
					hide : 100
				}
			});

			$('#qtd-online').tooltip({
				delay : {
					show : 500,
					hide : 100
				}
			});

			$('#tempo-restante').tooltip({
				delay : {
					show : 500,
					hide : 100
				}
			});

		});

var handshake = false;

function implementsWebSocket() {
	websocketConnection.onopen = function() {
		console.log('Tô dentro');
		console.log('Status: ' + websocketConnection.readyState
				+ " conectado: " + handshake);
		if (!handshake) {
			var temp = new Messeger(idEmissor, idLeilao, HANDSHAKE, userName,
					' ');
			websocketConnection.msg(temp);
			handshake = true;
			alertaDown();
		}
	}

	websocketConnection.onclose = function() {
		console.log('Tô fora');
		initWebSocket();
		implementsWebSocket();
		handshake = false;
	}

	websocketConnection.onerror = function(error) {
		console.log('Deu treta');
		if (websocketConnection.readyState > 1) {
			initWebSocket();
			implementsWebSocket();
			handshake = false;
		}
	}

	websocketConnection.onmessage = function(e) {
		resolverMessage(JSON.parse(e.data));
	}
}

function validaMensagem() {
	var mensagem = $('#msg').val().trim();

	if (mensagem.length <= 0) {
		alertaUp('Você não pode mandar mensagem em branco.');
		$('#msg').val('');
		return;
	}

	var temp = new Messeger(idEmissor, idLeilao, MENSAGEM, userName, mensagem);
	websocketConnection.msg(temp);
	$('#msg').val('');
}
