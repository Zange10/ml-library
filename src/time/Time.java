package time;

public class Time {
	public static void wait(int timeInMs) {
		try {
			Thread.sleep(timeInMs);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
