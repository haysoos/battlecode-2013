package team000.messaging;

import battlecode.common.MapLocation;

public class Message implements Comparable<Message> {

	private int channel;
	private int intent;
	private MapLocation mapLocation;

	public int encode() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public int getIntent() {
		return intent;
	}

	public void setIntent(int intent) {
		this.intent = intent;
	}

	public MapLocation getMapLocation() {
		return mapLocation;
	}

	public void setMapLocation(MapLocation mapLocation) {
		this.mapLocation = mapLocation;
	}

	public static Message decode(int encodedMessage) {
		Message message = new Message();
		message.channel = encodedMessage;
		message.intent = Intent.ATTACK;
		message.mapLocation = getMapLocation(encodedMessage);
		return message;
	}

	private static MapLocation getMapLocation(int encodedMessage) {
		int x = 1;
		int y = 2;
		return new MapLocation(x, y);
	}

	@Override
	public int compareTo(Message o) {
		return this.intent - o.intent;
	}

}
