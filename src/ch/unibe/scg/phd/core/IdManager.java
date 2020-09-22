package ch.unibe.scg.phd.core;

import java.security.SecureRandom;
import java.util.HashMap;

import org.glassfish.grizzly.websockets.WebSocket;

public class IdManager {

	private static HashMap<Long, WebSocket> _idRelation = new HashMap<>();
	
	public static long getUniqueId() {
		SecureRandom random = new SecureRandom();
		return random.nextLong();
	}
	
	public static void addEntry(long id, WebSocket socket) {
		_idRelation.put(id, socket);
	}
	
	public static void updateEntry(long idBefore, long idAfter) {
		WebSocket socket = _idRelation.get(idBefore);
		IdManager.addEntry(idAfter, socket);
		IdManager.removeEntry(idBefore);
	}
	
	public static void removeEntry(long id) {
		_idRelation.remove(id);
	}
	
	public static WebSocket getEntry(long id) {
		return _idRelation.get(id);
	}
	
}
