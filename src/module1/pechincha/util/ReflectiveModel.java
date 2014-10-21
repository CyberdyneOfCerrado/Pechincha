package module1.pechincha.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;



public class ReflectiveModel {

	
	protected <T>String getTableName(T classe){
		Class<? extends Class> c = (Class<? extends Class>) classe.getClass();
		String name = c.getCanonicalName().substring(c.getCanonicalName().lastIndexOf(".")+1);
		return name;
	};
	
	protected <T>String getColumnName(T classe ){
		Class<? extends Class> c = (Class<? extends Class>) classe.getClass();
		Field[] f = c.getDeclaredFields();
		String result="";
		
		for( Field a : f){
			if(!a.getName().contains("pk"))result +=","+a.getName();
		}
		
		return result.substring(1,result.length());
	};
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <T>String getColumnValues(T classe ){
		Converter con = new Converter();
		Class<? extends Class> c = (Class<? extends Class>) classe.getClass();
		String[] variables = getColumnName(classe).split(","); 
		Method[] m = c.getDeclaredMethods();
		String result="";
		
		for( int a = 0 ; a < variables.length ; a++ ){
			String temp = "get"+variables[a];
			for( int b = 0 ; b < m.length ; b++ ){
				if(temp.equalsIgnoreCase(m[b].getName())){
					Type t = m[b].getGenericReturnType();
					try {
						 
						 if(!m[b].getName().contains("pk")){
							 Object concat = m[b].invoke(classe, null);
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
	
	//inicia um Objeto a partir dos dados de uma doAction :|__|-+--[
	protected <T>void buildObject(T classe,DoAction doAction){
		Set keys = doAction.getHashtable().keySet();
		Converter con = new Converter();
		
		Class c = classe.getClass();
		Method[] m = c.getDeclaredMethods();
		
		for(int a = 0 ; a < m.length ; a++){
			Iterator<String> i = keys.iterator();
			while(i.hasNext()){
				String key = i.next();
				if(m[a].getName().equalsIgnoreCase("set"+key)){
					try {
						Type p[] = m[a].getGenericParameterTypes();
						Object o = con.convert(doAction.getData(key), p[0].toString());
						m[a].invoke(classe,o);
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						e.printStackTrace();
						e.getCause();
					}
				}
			}
		}
	};
}
