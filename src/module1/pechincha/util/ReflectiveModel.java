package module1.pechincha.util;
import java.lang.reflect.*;



public class ReflectiveModel {
	
	public String getTableName(){
		Class<? extends ReflectiveModel> c = this.getClass();
		String name = c.getCanonicalName().substring(c.getCanonicalName().lastIndexOf(".")+1);
		return name;
	};
	
	public String getColumnName( ){
		Class<? extends ReflectiveModel> c = this.getClass();
		Field[] f = c.getDeclaredFields();
		String result="";
		
		for( Field a : f){
			if(!a.getName().contains("pk"))result +=","+a.getName();
		}
		
		return result.substring(1,result.length());
	};
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getColumnValues(){
		Converter con = new Converter();
		Class<? extends ReflectiveModel> c = this.getClass();
		String[] variables = getColumnName().split(","); 
		Method[] m = c.getDeclaredMethods();
		String result="";
		
		for( int a = 0 ; a < variables.length ; a++ ){
			String temp = "get"+variables[a];
			for( int b = 0 ; b < m.length ; b++ ){
				if(temp.equalsIgnoreCase(m[b].getName())){
					Type t = m[b].getGenericReturnType();
					try {
						 
						 if(!m[b].getName().contains("pk")){
							 Object concat = m[b].invoke(this, null);
							 concat = (concat == null)? " ":concat.toString();
							 if( t == Integer.TYPE){
								 result += ","+concat;
							 }else{
								 result += ","+"'"+concat+"'";
							 }
						 }
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException  e) {
						e.printStackTrace();
					}
				}
			}
		}
		return result.substring(1,result.length());
	};
	
}
