package Common;


public class Log {

	private static final boolean debug = true;
	private static final boolean info = true;
	private static final boolean alert = true;

	public static void debug(String message) {
		if (!debug) return;
		System.out.println("[DEBUG] " + message);
	}
	
	public static void info(String message) {
		if (!info) return;
		System.out.println("[INFO] " + message);
	}
	
	public static void alert(String message) {
		if (!alert) return;
		System.out.println("[ALERT] " + message);
	}

	public static void warn(String message) {
		System.err.println("WARN " + message);
	}
	
	public static void error(String message) {
		System.err.println("[ERROR] " + message);
		System.exit(-1);
	}
	public static void error(Exception error) {
		System.err.println("[ERROR] " + error.getMessage());
		error.printStackTrace();
		System.exit(-1);
	}
} 
