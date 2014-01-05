package team000.robots;

import team000.common.Constants;
import team000.messaging.Message;
import team000.messaging.MessageIntent;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

public class ArtilleryRobot extends BaseRobot {

	public ArtilleryRobot(RobotController rc) {
		super(rc);
	}

	@Override
	public void runProgram() {
		while (true) {
			listenToBroadcasts();
			if (isEnemyInRange()) {
				RobotInfo weakestEnemy = getWeakestEnemyInRange(senseNearbyEnemyRobots());
				enemyInSightAction(weakestEnemy);
			}
			rc.yield();
		}
	}

	@Override
	public void enemyInSightAction(RobotInfo robotInfo) {
		try {
			if(rc.isActive()){
				
				MapLocation enemyLocation = robotInfo.location;
				if(rc.getLocation().distanceSquaredTo(enemyLocation) < Constants.MINIMUM_ENEMY_VISIBLE_RANGE){
					setRobotIndicator(Constants.INDICATOR_GENERAL, "I see enemy robot at " + enemyLocation);
					Message enemyat = new Message();
					enemyat.setMapLocation(enemyLocation);
					enemyat.setMessageIntent(MessageIntent.ENEMYAT);
					this.broadcast(enemyat);
					rc.attackSquare(enemyLocation);

				} 
				
			} else {
				rc.yield();
			}

		} catch (GameActionException e) {
			printErrorMessage(e);
		}
	}	

}
