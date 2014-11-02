package module2.pechincha.util;

import javax.websocket.Session;

import module2.pechincha.enumeration.MsgTypes;

import org.json.JSONObject;

public class MessegerFactory {
	
	public static String MessegerToJSONString( Messeger messeger){
		JSONObject o = new JSONObject();
		o.put("idEmissor", messeger.getIdEmissor()); 
		o.put("idLeilao", messeger.getIdLeilao()); 
		o.put("msg", messeger.getMsg()); 
		o.put("userName", messeger.getUserName()); 
		o.put("tipoMsg", messeger.getTipoMsg()); 
		
		return o.toString();
	}; 
	
	public static Messeger createMesseger( String JsonObjectString){
		JSONObject o = new JSONObject(JsonObjectString); 
		Messeger m = null;
		System.out.println(o.get("tipoMsg"));
		switch ( MsgTypes.valueOf(o.get("tipoMsg").toString())){
		case MENSAGEM: 
			m = createMenasgem(o);
			break;
		case HANDSHAKE: 
			m = createMenasgem(o);
			break;
		case LANCE: 
			break;
		case FINALIZAR: 
			break;
		case LANCE_INVALIDO: 
			break;
		default:
			break;
		}
		return m;
	}; 
	
	private static Messeger createMenasgem(JSONObject o ){
		Messeger m = new Messeger();
		m.setIdEmissor(Integer.parseInt(o.get("idEmissor").toString())); 
		m.setIdLeilao(Integer.parseInt(o.get("idLeilao").toString()));
		m.setMsg(o.get("msg").toString());
		m.setTipoMsg(MsgTypes.valueOf(o.get("tipoMsg").toString()));
		m.setUserName(o.getString("userName").toString());
		return m;
	}
	
	public static Messeger createMessegerCallback(String data){
		Messeger m = new Messeger(-1,-1,"Pechincha",MsgTypes.CALLBACK, data);
		return m;
	};
	
	public static Messeger createMessegerOnline( int size){
		Messeger m = new Messeger(-1,-1,"Pechincha",MsgTypes.ONLINE, String.valueOf(size));
		return m;
	};
	
	public static Messeger createMessegerOffline(int size){
		Messeger m = new Messeger(-1,-1,"Pechincha",MsgTypes.OFFLINE,String.valueOf(size));
		return m;
	};
}
