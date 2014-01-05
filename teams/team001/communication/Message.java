/**
 * 
 */
package team001.communication;

import battlecode.common.MapLocation;

/**
 * Class to handle message handling between robots.<br />
 * @author Jesus Medrano
 * <a href="jesus.i.medrano@gmail.com">jesus.i.medrano@gmail.com</a>
 */
public class Message {

	private MapLocation location;
	private int robotID;
	private MessageType messageType;
	private static final int SIZE_OF_COORDINATE = 7;
	private static final int SIZE_OF_MESSAGE_TYPE = 4;
	private static final int Y_MASK = (1 << SIZE_OF_COORDINATE) - 1;
	private static final int X_MASK = Y_MASK << SIZE_OF_COORDINATE;
	private static final int MESSAGE_TYPE_MASK = ((1 << SIZE_OF_MESSAGE_TYPE) - 1) 
			<< (SIZE_OF_COORDINATE * 2);
	
	/**
	 * Default Message Constructor.
	 */
	public Message(){
		
	}
	
	/**
	 * Static method to decode a message sent as an integer.
	 * @param cipher
	 * @return Message object
	 */
	public static Message decodeMessage(int cipher){
		
		Message message = new Message();
		int x, y, messageTypeOrdinal;
		
		y = cipher & Y_MASK;
		x = (cipher & X_MASK) >> SIZE_OF_COORDINATE;
		messageTypeOrdinal = (cipher & MESSAGE_TYPE_MASK) >> (2 * SIZE_OF_COORDINATE);
		
		MapLocation location = new MapLocation(x, y);
		
		message.setLocation(location);
		
		for(MessageType type : MessageType.values()){
			if(type.ordinal() == messageTypeOrdinal){
				message.setMessageType(type);
			}
		}
		
		return message;
		
	}
	
	/**
	 * @return <code>int</code> representation of this message.
	 */
	public int toCode(){
		
		int code = messageType.ordinal() << (SIZE_OF_COORDINATE * 2) 
				| location.x << SIZE_OF_COORDINATE 
				| location.y;
		
		return code;
	}
	
	/**
	 * @param robotID
	 */
	public void setRobotID(int robotID){
		this.robotID = robotID;
	}
	
	/**
	 * @param messageType
	 */
	public void setMessageType(MessageType messageType){
		this.messageType = messageType;
	}
	
	/**
	 * @param location
	 */
	public void setLocation(MapLocation location){
		this.location = location;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("Robot ID: ");
		sb.append(robotID);
		
		if(messageType != null){
			sb.append(", MessageType: ");
			sb.append(messageType);
		}
		
		if(location != null){
			sb.append(", Location: ");
			sb.append(location);
		}
		
		sb.append(", Coded: ");
		sb.append(this.toCode());
		
		return sb.toString();
	}

	/**
	 * @return RobotID or Channel this message will be sent on.
	 */
	public int getRobotID() {
		return robotID;
	}

	/**
	 * @return Type of message
	 * @see MessageType
	 */
	public MessageType getMessageType() {
		return messageType;
	}

	/**
	 * @return Location associated with this message.
	 */
	public MapLocation getLocation() {
		return location;
	}
	
}
