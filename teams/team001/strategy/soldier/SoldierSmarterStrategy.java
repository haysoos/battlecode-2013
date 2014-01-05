/**
 * 
 */
package team001.strategy.soldier;

import team001.common.Constants;
import team001.robots.SoldierRobot;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;

/**
 * @author Jesus Medrano
 * <a href="jesus.i.medrano@gmail.com">jesus.i.medrano@gmail.com</a>
 */
public class SoldierSmarterStrategy extends SoldierStrategy {

	private RobotController rc;

	/* (non-Javadoc)
	 * @see team001.strategy.Strategy#runStrategy(team000.robots.RobotProgram)
	 */
	@Override
	public void runStrategy(SoldierRobot robot) throws Exception {

		rc = robot.getRobotController();
		robot.isDefensive = false;

		if(robot.seekAndDestroy()){
			return;
		}

		robot.readBroadcasts();
		
		MapLocation closestEncampment = robot.getClosestLocation(rc.senseEncampmentSquares(rc.getLocation(), 10000, Team.NEUTRAL));

		if(rc.getEnergon() < 20d){
			if(rc.getLocation().distanceSquaredTo(closestEncampment) < 10){
				
				if(rc.getLocation().distanceSquaredTo(closestEncampment) > 0){
					robot.moveTowards(closestEncampment);
					return;
				}
				
				while(!rc.isActive()){
					rc.yield();
				}
				if(rc.getTeamPower() > rc.senseCaptureCost()){
					rc.setIndicatorString(Constants.INDICATOR_MODE, "Trying to capture Artillery encampment at " + closestEncampment);
					rc.captureEncampment(RobotType.ARTILLERY);
				} else {
					rc.setIndicatorString(Constants.INDICATOR_MODE, "Could not capture Artillery Encamptment " + closestEncampment);
				}
			}
			else {
				robot.moveTowards(closestEncampment);
			}
		} else {
	
				robot.moveTowards(rc.senseEnemyHQLocation());			
		}

	}

}
