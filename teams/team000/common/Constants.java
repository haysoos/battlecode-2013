package team000.common;

/**
 * General constants used.
 * @author Jesus Medrano
 * <a href="jesus.medrano@yahoo.com">jesus.medrano@yahoo.com</a>
 */
public interface Constants {

	/**
	 * The channel to broadcast all messages.
	 */
	public static final int BROADCAST_CHANNEL = 1337;
	
	/**
	 * Enable to add robot controller indicator information.
	 * Leave this to false to reduce byte code usage.
	 */
	public static final boolean VERBOSE = true;

	/**
	 * Line used for Message in the robot controller indicator
	 */
	public static final int INDICATOR_GENERAL = 0;
	
	
	/**
	 * Line used for Message in the robot controller indicator
	 */
	public static final int INDICATOR_PATHING = 1;
	
	
	/**
	 * Line used for Message in the robot controller indicator
	 */
	public static final int INDICATOR_MESSAGE = 2;

	public static final int MINIMUM_ENEMY_VISIBLE_RANGE = 63;
	public static final double MINIMUM_ENERGY_TO_SPAWN = 180;
	public static final int STOP_MOVING_AND_ATTACK_DISTANCE = 3;

	public static final int MAXIMUM_DISTANCE_TO_ENGAGE_ENEMY = 10;
	
}
