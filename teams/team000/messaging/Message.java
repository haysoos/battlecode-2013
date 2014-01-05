package team000.messaging;

import battlecode.common.MapLocation;

/**
 * Message handling between robots.<br />
 * @author Jesus Medrano
 * <a href="jesus.medrano@yahoo.com">jesus.medrano@yahoo.com</a>
 */
public class Message implements Comparable<Message> {

	private MapLocation mapLocation;
	private MessageIntent intent;
	private static final int SIZE_OF_COORDINATE = 7;
	private static final int SIZE_OF_MESSAGE_TYPE = 4;
	private static final int Y_MASK = (1 << SIZE_OF_COORDINATE) - 1;
	private static final int X_MASK = Y_MASK << SIZE_OF_COORDINATE;
	private static final int MESSAGE_TYPE_MASK = ((1 << SIZE_OF_MESSAGE_TYPE) - 1) 
			<< (SIZE_OF_COORDINATE * 2);

	
	public MessageIntent getIntent() {
		return intent;
	}

	public MapLocation getMapLocation() {
		return mapLocation;
	}

	public void setMapLocation(MapLocation mapLocation) {
		this.mapLocation = mapLocation;
	}
	
	public void setMessageIntent(MessageIntent intent) {
		this.intent = intent;
	}

	/**
	 * @return <code>int</code> representation of this message.
	 */
	public int encode() {
		int code = intent.ordinal() << (SIZE_OF_COORDINATE * 2) 
				| mapLocation.x << SIZE_OF_COORDINATE 
				| mapLocation.y;
		return code;
	}
	
	/**
	 * Static method to decode a message sent as an integer.
	 * @param encodedMessage
	 * @return Message Object
	 */
	public static Message decode(int encodedMessage) {
		Message message = new Message();
		MapLocation location = extractMapLocation(encodedMessage);
		message.setMapLocation(location);

		MessageIntent intent = extractMessageIntent(encodedMessage);
		message.setMessageIntent(intent);
	
		return message;
	}

	private static MessageIntent extractMessageIntent(int encodedMessage) {
		int messageTypeOrdinal;
		messageTypeOrdinal = (encodedMessage & MESSAGE_TYPE_MASK) >> (2 * SIZE_OF_COORDINATE);
	
		for(MessageIntent intent : MessageIntent.values()){
			if(intent.ordinal() == messageTypeOrdinal){
				return intent;
			}
		}
		
		return null;
	}

	private static MapLocation extractMapLocation(int encodedMessage) {
		int x;
		int y;


		y = encodedMessage & Y_MASK;
		x = (encodedMessage & X_MASK) >> SIZE_OF_COORDINATE;
	
	MapLocation location = new MapLocation(x, y);
		return location;
	}

	@Override
	public int compareTo(Message o) {
		return this.intent.ordinal() - o.intent.ordinal();
	}

	@Override
	public String toString() {
		return "Message [mapLocation=" + mapLocation + ", intent=" + intent
				+ "]";
	}

}
