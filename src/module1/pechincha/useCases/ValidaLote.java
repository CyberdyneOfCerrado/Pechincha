package module1.pechincha.useCases;

public class ValidaLote {
		public boolean validar(String[] quantidadeLote,String[] precoLote,String[] idproduto,String[] quantidade,String[] precoProd, float valorPerson,String[] adicionado){
			int x=0;
			boolean resul=false;
			for(String qtd:quantidadeLote){
				int tempqtd=Integer.parseInt(qtd);
				int tempquantidade=Integer.parseInt(quantidade[x]);
				x++;
				if(tempqtd<=tempquantidade)resul=true;else return false;
			}
			x=0;
			for(String Lote:precoLote){
				float tempPrecoLote=Float.parseFloat(Lote);
				int tempquantidade=Integer.parseInt(quantidadeLote[x]);
				float tempPrecoProd=Float.parseFloat(precoProd[x]);
				x++;
				if(tempquantidade*tempPrecoProd==tempPrecoLote)resul=true;else return false;
				}
			if(valorPerson>=0 && valorPerson<=10000)resul=true;else return false;
			for(String add:adicionado){
				if(add.equals("true")){
					resul=true;
					break;
				}else resul=false;
			}
			return resul;
			}
	}
