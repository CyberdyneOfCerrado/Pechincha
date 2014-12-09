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
    			}else{
    				document.getElementById("modal2").click();
    			}
    		});
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
 
/*    $("#mascara").click( function(){
        $(this).hide();
        $(".window").hide();
    });*/
 
    $('.fechar').click(function(ev){
        ev.preventDefault();
        $("#mascara").hide();
        $(".window").hide();
    });
});
