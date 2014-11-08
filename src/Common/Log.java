package Common;


public class Log {

	private static final boolean debug = true;
	private static final boolean info = true;
	private static final boolean alert = true;

	public static void debug(String message) {
		if (!debug) return;
		System.out.println(message);
	}
	
	public static void info(String message) {
		if (!info) return;
		System.out.println(message);
	}
	
	public static void alert(String message) {
		if (!alert) return;
		System.out.println(message);
	}

}
