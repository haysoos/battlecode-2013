package team000.robots;

import team000.common.Constants;
import team000.pathing.BugPathingAlgorithm;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

public class SoldierRobot extends BaseRobot {

	private MapLocation target;

	public SoldierRobot(RobotController rc) {
		super(rc);
		target = rc.senseEnemyHQLocation();
		this.setPathingAlgorithm(new BugPathingAlgorithm(this));
	}

	@Override
	public void runProgram() {
		while (true) {
			listenToBroadcasts();
			if (isEnemyInRange()) {
				RobotInfo weakestEnemy = getWeakestEnemyInRange(senseNearbyEnemyRobots());
				enemyInSightAction(weakestEnemy);
			}
			moveTowardsTarget(target);
			rc.yield();
		}
	}

	@Override
	public void enemyInSightAction(RobotInfo robotInfo) {

		MapLocation enemyLocation = robotInfo.location;
		int distanceToEnemy = getRobotController().getLocation().distanceSquaredTo(enemyLocation);
	
		if(distanceToEnemy > Constants.MAXIMUM_DISTANCE_TO_ENGAGE_ENEMY){
			return;
		}
		
		if(distanceToEnemy < Constants.MINIMUM_ENEMY_VISIBLE_RANGE ){
			
			if(distanceToEnemy > Constants.STOP_MOVING_AND_ATTACK_DISTANCE){
				moveTowardsTarget(enemyLocation);
			}
		} else {
			moveTowardsTarget(enemyLocation);
		}

	}
}
