package module1.pechincha.security;

import java.util.Hashtable;
import java.util.Random;

public class VerificationCodeFactory {
	private static Hashtable<String, String> map;

	public VerificationCodeFactory() {
		
	}

	public static synchronized String create(String email) {
		// O código será alfanumérico, contendo 5 dígitos gerados de forma
		// aleatória.
		instanceMap(); 
		char code[] = new char[5];
		Random rand = new Random();

		for (int a = 0; a < code.length; a++) {
			code[a] = (char) ((char) rand.nextInt(76) + 49);
		}
		map.put(new String(code), email);
		return new String(code);

	};

	public static synchronized String verify(String code) {
		// Se o código existir, o respectivo email será retornado, caso
		// contrário, será retornado null.
		instanceMap(); 
		
		if (null != map.get(code)) {
			String temp = map.get(code);
			map.remove(code);
			return temp;
		}
		return null;
	};

	private static void instanceMap(){
		if(map == null) map = new Hashtable<>();
	};
}
