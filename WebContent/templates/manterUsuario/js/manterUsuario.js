function concluirCad(){
$.post("q",$("#cadastro").serialize()).done(
    function(resposta){
        try{
            var obj = jQuery.parseJSON(resposta);
            if(obj.estado===true){
                document.getElementById("etapa").value="cadastrar";
                document.getElementById("cadastro").submit();
            }else{
                    $("#mensagem").text(obj.erro);
                    document.getElementById("modal1").click();
                    $('.fechar').click(function(ev){
                         ev.preventDefault();
                         $("#mascara").hide();
                         $(".window").hide();
                         focarUs(obj);
                     });
            }
        }catch(ex){
            console.log(resposta);
        }
    });
}

function cancelarCad(){
    document.getElementById("cancelarCad").submit();
}

function focarUs(objResposta){
        switch(objResposta.tipo){
            case "1":
                document.getElementById("1").className = "control-group error";
                document.getElementById("nomeCompleto").select();
                document.getElementById("nomeCompleto").focus();
            break;
            case "2":
                document.getElementById("2").className = "control-group error";
                document.getElementById("senha").select();
                document.getElementById("senha").focus();
            break;
            case "3":
                document.getElementById("3").className = "control-group error";
                document.getElementById("confirmarSenha").select();
                document.getElementById("confirmarSenha").focus();
            break;
            case "4":
                document.getElementById("4").className = "control-group error";
                document.getElementById("nickname").select();
                document.getElementById("nickname").focus();
            break;
            case "5":
                document.getElementById("5").className = "control-group error";
                document.getElementById("dataDeNascimento").select();
                document.getElementById("dataDeNascimento").focus();
            break;
            case "6":
                document.getElementById("6").className = "control-group error";
                document.getElementById("email").select();
                document.getElementById("email").focus();
            break;
            case "7":
                document.getElementById("7").className = "control-group error";
                document.getElementById("emailAlternativo").select();
                document.getElementById("emailAlternativo").focus();
            break;
            case "8":
                document.getElementById("8").className = "control-group error";
                document.getElementById("skype").select();
                document.getElementById("skype").focus();
            break;
            case "9":
                document.getElementById("9").className = "control-group error";
                document.getElementById("telefoneCelular").select();
                document.getElementById("telefoneCelular").focus();
            break;
            case "10":
                document.getElementById("10").className = "control-group error";
                document.getElementById("telefoneFixo").select();
                document.getElementById("telefoneFixo").focus();
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
        var top = ($(document.getElementById("bloco")).height() / 2) - ( $(id).height() / 2 );
     
        $(id).css({'top':top,'left':left});
        $(id).show();  
    });
});