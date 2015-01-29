package module1.pechincha.useCases;

import java.text.DecimalFormat;

public class ValidaLote {
		public boolean validar(String[] quantidadeLote,String[] precoLote,String[] idproduto,String[] quantidade,String[] precoProd, float valorPerson,String[] adicionado){
			int x=0;
			boolean resul=false;
			if(quantidadeLote==null)return false;
			for(String qtd:quantidadeLote){
				int tempqtd=Integer.parseInt(qtd);
				int tempquantidade=Integer.parseInt(quantidade[x]);
				x++;
				if(tempqtd<=tempquantidade)resul=true;else return false;
			}
			x=0;
			if(precoLote==null)return false;
			for(String Lote:precoLote){
				double tempPrecoLote=Double.parseDouble(Lote);
				double precolote=converterDoubleDoisDecimais(tempPrecoLote);
				int tempquantidade=Integer.parseInt(quantidadeLote[x]);
				double tempPrecoProd=Double.parseDouble(precoProd[x]);
				x++;
				double temp=tempquantidade*tempPrecoProd;
				double preco=converterDoubleDoisDecimais(temp);
				if(preco==precolote)resul=true;else return false;
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
		  
		public double converterDoubleDoisDecimais(double precoDouble) {  
		    DecimalFormat fmt = new DecimalFormat("0.00");        
		    String string = fmt.format(precoDouble);  
		    String[] part = string.split("[,]");  
		    String string2 = part[0]+"."+part[1];  
		        double preco = Double.parseDouble(string2);  
		    return preco;  
		}  
	}
