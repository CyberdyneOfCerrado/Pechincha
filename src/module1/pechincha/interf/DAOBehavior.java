package module1.pechincha.interf;

import java.util.List;

public abstract class DAOBehavior <T>{
	public abstract void insert ( T arg );
	public abstract void delete (int pk );
	public abstract List<T> list(); 
	public abstract T select( int pk );
	public abstract void update ( T arg ); 
}
