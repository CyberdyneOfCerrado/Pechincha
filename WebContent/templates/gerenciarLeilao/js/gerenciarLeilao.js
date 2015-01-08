function enviar(Idleilao,Idleiloeiro,UseCase,Action,Redirect){
	document.getElementById("modal3").click();
	$.post("q",{
			idleilao : Idleilao,
			idleiloeiro : Idleiloeiro,
			useCase : UseCase,
			action : Action,
			redirect : Redirect
			}).done(
    		function(resposta){
    			$('.fechar').click();
    			if(resposta=="ok"){
    				document.getElementById("modal1").click();
					 $("#mascara").click( function(){
						$(this).hide();
						$(".window").hide();
					});
    			}else{
    				document.getElementById("modal2").click();
					 $("#mascara").click( function(){
						$(this).hide();
						$(".window").hide();
					});
    			}
    		});
}

function criarLeilao(Etapa,Redirect,UserCase,Action,Idleiloeiro){
$.post("q",{
    etapa : Etapa,
    redirect : Redirect,
    useCase : UserCase,
    action : Action,
    idleiloeiro : Idleiloeiro,
    etiqueta : document.getElementById("etiqueta").value,
    tempolimite : document.getElementById("tempolimite").value,
    descricao : document.getElementById("descricao").value
}).done(
    function(resposta){
        try{
        var obj = jQuery.parseJSON(resposta);
        document.getElementById("modal1").click();
        $("#mensagem").text(obj.erro);
        $("#mascara").click( function(){
             $(this).hide();
             $(".window").hide();
             focar(obj);
         });
        $('.fecharLeilaop0').click(function(ev){
             ev.preventDefault();
             $("#mascara").hide();
             $(".window").hide();
             focar(obj);
         });
        }catch(ex){
            document.write(resposta);
            location.reload();
        }
    });
}

function postar(acao){
	document.getElementById(acao).submit();
}

function focar(objResposta){
        switch(objResposta.tipo){
            case "1":
                document.getElementById("1").className = "control-group error";
                document.getElementById("etiqueta").select();
                document.getElementById("etiqueta").focus();
            break;
            case "2":
                document.getElementById("2").className = "control-group error";
                document.getElementById("descricao").select();
                document.getElementById("descricao").focus();
            break;
            case "3":
                document.getElementById("3").className = "control-group error";
                document.getElementById("tempolimite").select();
                document.getElementById("tempolimite").focus();
            break;
        }
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
