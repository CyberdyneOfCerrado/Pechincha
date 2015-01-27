$.getScript("templates/manterUsuario/js/md5.js");

function excluir(){
	var md5Encripted = md5($("#senha").val());
	$.post("q",{
        useCase : "manterUsuario",
        action : "excluirConta",
        etapa : "check",
        id : $("#user").val(),
        senha : md5Encripted
    }).done(
    		function(resposta){
            try{
                var obj = jQuery.parseJSON(resposta);
    			if(obj.estado===true){
                    $("#mensagem2").text(obj.erro);
                    document.getElementById("modal2").click();
                    $('.fechar').click(function(ev){
                         ev.preventDefault();
                         $("#mascara").hide();
                         $(".window").hide();
                         $("#cadastro").submit();
                     });
    			}else{
    				$('.fechar').click();
    				document.getElementById("modal1").click();
					 $("#mascara").click( function(){
						$(this).hide();
						$(".window").hide();
					});
    			}
            }catch(ex){
                console.log(resposta);
            }
    	}
	);
}

function cancel(){
    document.getElementById("cancela").submit();
}

$(document).ready(function(){
    $("a[rel=modal]").click( function(ev){
        ev.preventDefault();
 
        var id = $(this).attr("href");
 
        var alturaTela = $(document).height();
        var larguraTela = $(window).width();
     
        //colocando o fundo preto
        $('#mascara').css({'width':larguraTela,'height':alturaTela});
        $('#mascara').fadeIn(1000);
        $('#mascara').fadeTo("slow",0.8);
 
        var left = ($(window).width() /2) - ( $(id).width() / 2 );
        var top = ($(window).height() / 2) - ( $(id).height() / 2 );
     
        $(id).css({'top':top,'left':left});
        $(id).show();  
    });
 
    $('.fechar').click(function(ev){
        ev.preventDefault();
        $("#mascara").hide();
        $(".window").hide();
    });
});
