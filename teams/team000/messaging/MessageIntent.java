package team000.messaging;

/**
 * Tye type of message being sent.
 * @author Jesus Medrano
 * <a href="jesus.medrano@yahoo.com">jesus.i.medrano@yahoo.com</a>
 */
public enum MessageIntent {
	REQUEST,
	CONFIRMATION, 
	RESOURCE,
	ATTACK,
	DEFEND,
	PROMOTE,
	ENEMYAT, 
	RETURN_TO_BASE,
}
