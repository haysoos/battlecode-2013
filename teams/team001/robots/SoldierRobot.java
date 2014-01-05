/**
 * 
 */
package team001.robots;

import team001.common.Constants;
import team001.strategy.Strategies;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

/**
 * @author Jesus Medrano
 * <a href="jesus.i.medrano@gmail.com">jesus.i.medrano@gmail.com</a>
 */
public class SoldierRobot extends BasePlayer {

	public boolean isDefensive = false;
	
	/**
	 * @param rc
	 */
	public SoldierRobot(RobotController rc){
		super(rc);	
		setStrategy(Strategies.soldierAwaitOrdersStrategy);
		super.startProgram();
	}

	/* (non-Javadoc)
	 * @see team001.robots.BasePlayer#attackEnemy(battlecode.common.MapLocation)
	 */
	@Override
	public boolean enemyInSight(MapLocation enemyLocation) {

		int distanceToEnemy = super.getRobotController().getLocation().distanceSquaredTo(enemyLocation);
		int distanceToBase = super.getRobotController().getLocation().distanceSquaredTo(super.getRobotController().senseHQLocation());
		
		if(isDefensive && distanceToEnemy > 10){
			//this.moveTowards(super.getRobotController().senseHQLocation());
			return false;
		}
		
		if(distanceToEnemy < Constants.MINIMUM_ENEMY_VISIBLE_RANGE ){
			
			if(distanceToEnemy > Constants.STOP_MOVING_AND_ATTACK_DISTANCE){
				this.moveTowards(enemyLocation);
			}
		} else {
			this.moveTowards(enemyLocation);
		}


		return true;
	}
	
	


}
