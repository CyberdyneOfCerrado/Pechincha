//Constantes
const LANCE                = 'LANCE'; 
const FINALIZAR            = 'FINALIZAR'; 
const HANDSHAKE            = 'HANDSHAKE';
const ONLINE               = 'ONLINE';
const OFFLINE              = 'OFFLINE';
const MENSAGEM             = 'MENSAGEM';
const MENSAGEM_MAIOR_LANCE = 'MENSAGEM_MAIOR_LANCE';
const MENSAGEM_LEILOEIRO   = 'MENSAGEM_LEILOEIRO';
const LANCE_INVALIDO       = 'LANCE_INVALIDO';
const CALLBACK             = 'CALLBACK';

const WS_SOCKET            = 'wss://'+ location.hostname +':'+location.port+'/Pechinchas/server';
//'ws://localhost:8080/Pechinchas/server'
//Variáveis de execução
var idEmissor; 
var idLeilao; 
var userName;

var tempoRestante; 
var lanceCorrente; 

var websocketConnection; 
/*-----------Seção de Objetos------------*/
//Objeto messeger
function Messeger( idEmissor, idLeilao, tipoMsg, userName, msg){
	this.idEmissor = idEmissor; 
	this.idLeilao  = idLeilao; 
	this.tipoMsg   = tipoMsg; 
	this.msg       = msg;
	this.userName  = userName;
}

/*-----------Seção de Funções------------*/

//Obtém informações essenciais nos inputs hidden do documento html.
function getInformations(){
	userName  = $('#userName').val();
	idEmissor = $('#idEmissor').val();
	idLeilao  = $('#idLeilao').val();
	console.log( userName + idEmissor + idLeilao);
}

//Incicia o WebSocket do programa. 
function initWebSocket(){
	websocketConnection = new WebSocket(WS_SOCKET);
}

//Converte Um JSON para String.
function JSONtoString(json){
	return JSON.stringify(json);
}

//Inseri uma mensagem no box-chat.
function putmessage( message ){
	var iconLeiloeiro  = '<i class="fa fa-user fa-1x "</i>'; 
	var iconMaiorLance = '<i class="fa fa-trophy fa-1x "></i>'; 
	var ul =  $('#ul-chat');
	var temp = '<li>';
	var classe;
	switch(message.tipoMsg){
		case MENSAGEM_MAIOR_LANCE:
			classe = 'nicknameMaiorLance'; 
		break;
		case MENSAGEM_LEILOEIRO:
			classe = 'nicknameLeiloeiro';
		break;
		case MENSAGEM:
			classe = 'nickname';
		break;
		case ONLINE:
			classe = 'online';
		break;
		case OFFLINE:
			classe = 'offline';
		break;
	}	
	temp += '<span class="fixFontChat '+classe+' " >'+ message.userName;
	switch(message.tipoMsg){
		case MENSAGEM_MAIOR_LANCE:
			temp += iconMaiorLance +'   </span>';
		break;
		case MENSAGEM_LEILOEIRO:
			temp += iconLeiloeiro+'   </span>';
		break;
		case MENSAGEM:
			temp += '   </span>';
		break;
		case ONLINE:
			temp += '   </span>';
		break;
		case OFFLINE:
			temp += '   </span>';
		break;
	}
	var d = new Date();
	temp +='<small>'+d.getHours()+'h:'+d.getMinutes()+'min    :'+'</small>';
	temp += '<span class="fixFontChat" >'+ message.msg+'</span>';
	temp +='</li>';
	ul.append(temp);
	
	$('#top-chat').animate({                  
        scrollTop: $('#top-chat').prop("scrollHeight")  
    },1000); 
	console.log($('#top-chat').prop("scrollHeight")); 
}

//Transforma segundos e horas e minutos. 
function formatarHora( segundos ){
	segundos = parseInt(segundos);
	var horas   = Math.floor(segundos / 3600);
	var minutos = Math.floor(segundos % 3600);
	minutos = Math.floor( minutos / 60  );
		
	var retorno = ( horas < 10   ) ?  '0'+horas   : horas;
	retorno   += 'h:';  
	retorno   +=  ( minutos < 10 ) ? '0'+minutos  : minutos;
	retorno   += 'min';
	return  retorno;
}

//Inicializa os componentes do ambiente de leilão conforme uma message do tipo CALLBACK; 
function inicializarAmbiente( message ){
		//Lista de coisas que eu devo pegar no callback
		//1 TempoCorrente; 
		//2 Etiqueta; 
		//3 Quantidade de pessoas online; 
		//4 LanceCorrente; 
		//5 Nome do usuário com o maior lance; 
		//6 Nome do leiloeiro;
	var string = new String(message);
	var data = string.split(';'); 
	
	tempoRestante = parseInt(data[0]); 
	lanceCorrente = parseFloat(data[3]);
	
	$('#valor-lance').text('Lance: '+lanceCorrente+'R$');
	
	$('#tempo').text(formatarHora( data[0])); 
	$('#etiqueta').text(data[1]);
	$('#online').text(data[2]);
	$('#maiorLanceMontante').text(data[3]+'R$');
	$('#maiorLance').text('Maior lance: '+data[4]);
	$('#leiloeiro').text('Leiloeiro: '+data[5]);
}

//Invoca o função mais adequada para o tipo de mensagem em específico.
function resolverMessage( message ){
	switch(message.tipoMsg){
		case CALLBACK:
			inicializarAmbiente(message.msg);
		break;
		case MENSAGEM:
			putmessage(message); 
		break;
		case ONLINE:
			feedOnline(message);
		break;
		case OFFLINE:
			feedOffline(message);
		break;
		case LANCE:
			updateLance(message);
		break;
	}
}

function feedOnline(message){
	var size = parseInt(message.msg);
	message.msg = 'Um usuário acaba de entrar.';
	putmessage(message);
	$('#online').text(size);
}

function feedOffline(message){
	var size = parseInt(message.msg);
	message.msg = 'Um usuário acaba de sair.';
	putmessage(message);
	$('#online').text(size);
}

function updateLance(message){
	//Elementos:
	//1 Valor do novo lance;
	//2 Nome do usuário com o maior lance.

	var string = new String(message.msg);
	var data = string.split(';'); 
	
	lanceCorrente = parseFloat(data[0]);
	
	console.log( data[0] + data[0] );
	
	$('#valor-lance').text('Lance: '+lanceCorrente+'R$');
	$('#maiorLanceMontante').text(lanceCorrente+'R$');
	$('#maiorLance').text('Maior lance: '+data[1]);
}

function updateHora(){
	if( tempoRestante > 0 ) tempoRestante -= 60; 
	$('#tempo').text(formatarHora( tempoRestante) ); 
}

function incrementLance( ){
	//Tem uma regra de negócio que rege isso. 
	lanceCorrente += 0.5;
	$('#valor-lance').text('Lance: '+lanceCorrente+'R$');
}
//Lixos
