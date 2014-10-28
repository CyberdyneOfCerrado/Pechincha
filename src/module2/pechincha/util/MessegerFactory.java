package module2.pechincha.util;

import javax.websocket.Session;

import module2.pechincha.enumeration.MsgTypes;

import org.json.JSONObject;

public class MessegerFactory {
	public static Messeger createMesseger( String JsonObjectString){
		JSONObject o = new JSONObject(JsonObjectString); 
		Messeger m = null;
		
		switch ( MsgTypes.valueOf(o.get("TipoMSG").toString())){
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
		m.setMsg(o.get("Msg").toString());
		m.setTipoMsg(MsgTypes.valueOf(o.get("TipoMSG").toString()));
		
		return m;
	}
}
