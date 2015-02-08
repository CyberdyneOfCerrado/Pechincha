/* jQuery POST/GET redirect method
   v.0.1
	modified by Miguel Galante,https://github.com/mgalante
   v.0.1
   made by Nemanja Avramovic, www.avramovic.info 
 */

function redirect(target, values, method) {

	method = (method && method.toUpperCase() == 'GET') ? 'GET' : 'POST';

	if (!values) {
		var obj = $.parse_url(target);
		target = obj.url;
		values = obj.params;
	}

	var form = $('<form>', {
		attr : {
			method : method,
			action : target
		}
	});

	for ( var i in values) {
		$('<input>', {
			attr : {
				type : 'hidden',
				name : i,
				value : values[i]
			}
		}).appendTo(form);

	}

	$('body').append(form);
	console.log(form);
	form.submit();
};

$.parse_url = function(url) {
	if (url.indexOf('?') == -1)
		return {
			url : url,
			params : {}
		}

	var parts = url.split('?');
	var url = parts[0];
	var query_string = parts[1];

	var return_obj = {};
	var elems = query_string.split('&');

	var obj = {};

	for ( var i in elems) {
		var elem = elems[i];
		var pair = elem.split('=');
		obj[pair[0]] = pair[1];
	}

	return_obj.url = url;
	return_obj.params = obj;

	return return_obj;
}

$('document').ready(function() {
	$('#confirmar').click(validarEnviar);
	$('#enviar').click(enviarSolicitacao)
});

function enviarSolicitacao(){
	var email = $('#email').val(); 
	if( email != ''){
		$.post("q", {
			useCase : 'manterUsuario',
			action : 'solicitacao',
			'email' : email,
			redirect : 'false'
		}).done(function(data){
			showMessage("alert alert-info",data+", verifique o seu e-mail.");
		})
		showMessage("alert alert-info","A sua solicitação foi encaminhada ao servidor, aguarde.");
	}
}
// Validando os campos preechidos pelo usuário
function validarEnviar() {
	if ($('#senhaConfirmacao').val() == '' || $('#senha').val() == ''
			|| $('#senhaConfirmacao').val() != $('#senha').val()) {
		showMessage("alert alert-error","Preencha os campos corretamente");
		return;
	} else if ($('#senha').val().length < 6 || $('#senha').val().length > 16) {
		showMessage("alert alert-error","O campo deverá conter entre 6 a 16 caracteres!");
		return;
	} else if ($('#codVerificacao').val() == '') {
		showMessage("alert alert-error",'Ensira um código de verificação');
		return;
	}
	enviarDados(md5($('#senha').val()),$('#codVerificacao').val());
}

function showMessage(type, message) {
	var html = "<div class='"+type+"'> <button type='button' class='close' data-dismiss='alert'>&times;</button> <strong>Atenção! </strong> "
			+ message + "</div>";
	$('#error').append(html);
}

function enviarDados(senha,code) {

	$.post("q", {
		useCase : 'manterUsuario',
		action : 'alterarSenha',
		'senha' : senha,
		'code' : code,
		redirect : 'false'
	}).done(function(data) {
		if(data == "true"){
			showMessage("alert alert-success",'Sua senha foi alterada com sucesso.');
		}else{
			showMessage("alert alert-error",'Dados inconsistentes. Tente solicitar um novo código de verificação.');
		}
	});
}