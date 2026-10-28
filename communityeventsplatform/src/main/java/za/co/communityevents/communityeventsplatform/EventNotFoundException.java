package za.co.communityevents.communityeventsplatform;

public class EventNotFoundException extends RuntimeException {
	public EventNotFoundException(String message) {
		super(message);
	}
}