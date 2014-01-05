/**
 * 
 */
package team001.robots;

import team001.common.Constants;
import team001.strategy.artillery.ArtilleryOffensiveStrategy;
import team001.communication.Message;
import team001.communication.MessageType;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;


/**
 * @author Jesus Medrano
 * <a href='jesus.i.medrano@gmail.com'>jesus.i.medrano@gmail.com</a>
 */
public class ArtilleryRobot extends BasePlayer{

	private RobotController rc;

	/**
	 * @param rc
	 */
	public ArtilleryRobot(RobotController rc) {
		super(rc);
		this.rc = rc;
		setStrategy(new ArtilleryOffensiveStrategy());
		super.startProgram();
	}

	/* (non-Javadoc)
	 * @see team001.robots.BasePlayer#attackEnemy(battlecode.common.MapLocation)
	 */
	@Override
	public boolean enemyInSight(MapLocation enemyLocation) {

		try {
			if(rc.isActive()){
				if(rc.getLocation().distanceSquaredTo(enemyLocation) < Constants.MINIMUM_ENEMY_VISIBLE_RANGE){
					rc.setIndicatorString(1, "I see enemy robot at " + enemyLocation);
					Message enemyat = new Message();
					enemyat.setLocation(enemyLocation);
					enemyat.setMessageType(MessageType.ENEMYAT);
					enemyat.setRobotID(Constants.BROADCAST_CHANNEL);
					sendMessage(enemyat);
					rc.attackSquare(enemyLocation);
					return true;
				} else {
					return false;
				}
			} else {
				rc.yield();
			}

		} catch (GameActionException e) {
			e.printStackTrace();
		}
		return false;
	}



}
