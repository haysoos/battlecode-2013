/**
 * 
 */
package team001.strategy.soldier;

import java.util.ArrayList;
import java.util.List;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.GameObject;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;
import team001.common.Constants;
import team001.robots.BasePlayer;
import team001.robots.SoldierRobot;
import team001.strategy.Strategies;

/**
 * @author Jesus Medrano
 * <a href='jesus.i.medrano@gmail.com'>jesus.i.medrano@gmail.com</a>
 */
public class SoldierResourceStrategy extends SoldierStrategy {

	private RobotController rc;
	private BasePlayer robot;

	/* (non-Javadoc)
	 * @see team001.strategy.Strategy#runStrategy(team000.robots.BasePlayer)
	 */
	@Override
	public void runStrategy(SoldierRobot robotProgram) throws Exception {

		MapLocation targetResource = robotProgram.getTarget();
		// Find closest resource.
		this.robot = robotProgram;
		rc = robot.getRobotController();

		if(robot.seekAndDestroy()){
			return;
		}

		robotProgram.readBroadcasts();

		if(targetResource == null){
			targetResource = robot.getClosestLocation(rc.senseEncampmentSquares(rc.getLocation(), 1000, Team.NEUTRAL));		

			if(targetResource == null){
				robot.setStrategy(Strategies.soldierDefenseStrategy);
			}
		}


		rc.setIndicatorString(0, this.getClass().getSimpleName() + " heading towards " + targetResource);

		if(rc.getLocation().distanceSquaredTo(targetResource) < 1){
			while(!rc.isActive()){
				rc.yield();
			}

			try{
				
			
			if(rc.getTeamPower() > rc.senseCaptureCost()){
				rc.setIndicatorString(Constants.INDICATOR_MODE, "Capturing Supplier Encamptment " + targetResource + " Team Power: " + rc.getTeamPower() + " capture cost " + rc.senseCaptureCost());
				if(Clock.getRoundNum() < 200){
					rc.captureEncampment(RobotType.SUPPLIER);
				} else {
					if((Clock.getRoundNum() % 2) == 1){
						rc.captureEncampment(RobotType.GENERATOR);
					} else {
						rc.captureEncampment(RobotType.SUPPLIER);
					}

				}
			} else {
				rc.setIndicatorString(Constants.INDICATOR_MODE, "Could not capture Encamptment " + targetResource);
			}
			} catch(GameActionException e){
				//If here then I tried to capture a map location that isn't an encampment;
				targetResource = robot.getClosestLocation(rc.senseEncampmentSquares(rc.getLocation(), 10000, Team.NEUTRAL));
			}

		}
		else {
			if(rc.getLocation().distanceSquaredTo(targetResource) < 2){
				GameObject go = rc.senseObjectAtLocation(targetResource);
				if(go != null && go.getTeam().equals(rc.getTeam())){
					rc.setIndicatorString(Constants.INDICATOR_DEBUG, "Someone is in my spot " + go);
					List<MapLocation> ignore = new ArrayList<MapLocation>();
					ignore.add(targetResource);
					targetResource = robot.getClosestLocation(rc.senseEncampmentSquares(rc.getLocation(), 10000, Team.NEUTRAL), ignore);
					return;
				}
			}
			robot.moveTowards(targetResource);
		}

	}

}
