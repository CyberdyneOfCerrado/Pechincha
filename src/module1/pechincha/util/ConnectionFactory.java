package module1.pechincha.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	private final static String  user     = "postgres"; 
	private final static String  password = "122331"; 
	private final static String  bd       = "123434"; 
	private static Connection c; 
	
	public static Connection getConnection(){
		if( c == null){
			try{
				Class.forName("org.postgresql.Driver"); 
				return c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+bd,user,password);
			}catch(SQLException | ClassNotFoundException e){
				throw new RuntimeException("Erro ao retornar a conexão do banco de dados: Classe: ConnectionFactory", e);
			}
		}
		
		return c; 
	}
}
