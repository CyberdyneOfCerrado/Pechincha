function enviar(Idleilao,UseCase,Action,Redirect){
	document.getElementById("modal3").click();
	$.post("q",{
			idleilao : Idleilao,
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

function home(){
    document.getElementById("home").submit();
}

function criarLeilao(){
$.post("q",$("#passo01").serialize()).done(
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
            document.getElementById("passo01").submit();
        }
    });
}

function concluir(){
$.post("q",$("#done").serialize()).done(
    function(resposta){
        try{
            var obj = jQuery.parseJSON(resposta);
            if(obj.erro===false){
                document.getElementById("etapaFinal").value="concluir";
                document.getElementById("done").submit();
            }else{
            	document.getElementById("modal1").click();
                $("#mensagem").text(obj.erro);
                $("#mascara").click( function(){
                     $(this).hide();
                     $(".window").hide();
                 });
                $('.fechar').click(function(ev){
                     ev.preventDefault();
                     $("#mascara").hide();
                     $(".window").hide();
                 });
            }
        }catch(ex){
            console.log(resposta);
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

function valorLote(){
    $('#valorPersonalizado').val((document.getElementById('checkval').checked? 'true' : 'false'));
}

function somar(id){
    var temp="#valorTemp"+id+"";
    var temp1="qtd"+id+"";
    var temp2="#preco"+id+"";
    var temp3="#valor"+id+"";
    if($('#IDtemp').val()!==id && $("#adicionado"+id+"").val()==="true")$('#temp').val($(temp).text());
    $(temp).text(((document.getElementById(temp1).value)*($(temp2).val())).toFixed(2));
    $(temp3).val(((document.getElementById(temp1).value)*($(temp2).val())).toFixed(2));
    var valTemp=parseFloat(document.getElementById("temp").value);
    var valC=parseFloat($(temp).text());
    if(valTemp<valC){
        if($("#adicionado"+id+"").val()==="true"){
            var valorP = parseFloat($(temp).text());
            var valorT = parseFloat($("#valortotal").text());
            var pr= parseFloat($(temp2).val());
            valorT+=pr;
            $("#valortotal").text(valorT.toFixed(2));
            $("#valortotal").text(valorT.toFixed(2));
            $("#temp").val($(temp).text());
            $(temp3).val(valorP);
            $("#temp").val(valorP);
        }
    }
    if(valTemp>valC){
        if($("#adicionado"+id+"").val()==="true"){
            var valorP = parseFloat($(temp).text());
            var valorT = parseFloat($("#valortotal").text());
            var pr= parseFloat($(temp2).val());
            valorT-=pr;
            $("#valortotal").text(valorT.toFixed(2));
            $("#temp").val($(temp).text());
            $(temp3).val(valorP);
            $("#temp").val(valorP);
        }
    }
    $('#IDtemp').val(id);
}

function adicionar(id){
    var temp="#valorTemp"+id+"";
    $('#adicionado'+id+'').val((document.getElementById('checkpr'+id+'').checked? 'true' : 'false'));
    $('#IDtemp').val(id);
    $('#temp').val($(temp).text());
    var valorC = parseFloat($(temp).text());
    var valorT = parseFloat($("#valortotal").text());
    if($("#adicionado"+id+"").val()==="true"){
        valorT+=valorC;
        $("#valortotal").text(valorT.toFixed(2));
    }else{
        if(valorT>0){
        valorT-=valorC;
        $("#valortotal").text(valorT.toFixed(2));
        }
    }
}