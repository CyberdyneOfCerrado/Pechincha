package module1.pechincha.util;

public class Converter {
 public Object convert ( Object temp, String type ){
	 Object o = null;
	 type =  type.substring(type.lastIndexOf(".")+1);
	 
	 switch( type ){
		 case "int": 
			  String number = (String) temp;
			  if(number.equals(""))return new Integer("0");
			  o = Integer.parseInt(number);
		 break;
		 case "String":
			 o = (String) temp ;
		 break;
	 }
	 return o;
 };
 
}
