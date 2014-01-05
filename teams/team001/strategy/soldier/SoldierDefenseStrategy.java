/**
 * 
 */
package team001.strategy.soldier;

import battlecode.common.Direction;
import battlecode.common.RobotController;
import battlecode.common.Team;
import team001.robots.SoldierRobot;
import team001.strategy.soldier.SoldierStrategy;

/**
 * @author Jesus Medrano
 * <a href='jesus.i.medrano@gmail.com'>jesus.i.medrano@gmail.com</a>
 */
public class SoldierDefenseStrategy extends SoldierStrategy {

	private RobotController rc;

	/**
	 * 
	 * @param robotProgram
	 * @throws Exception
	 */
	public void runStrategy(SoldierRobot robotProgram) throws Exception {

		rc = robotProgram.getRobotController();
		robotProgram.isDefensive = true;

		robotProgram.readBroadcasts();

		if(rc.getLocation().distanceSquaredTo(rc.senseHQLocation()) > 50){
			robotProgram.moveTowards(rc.senseHQLocation());
		}

		if(robotProgram.seekAndDestroy()){
			return;
		}

		Direction dir = rc.getLocation().directionTo(rc.senseHQLocation()).opposite();
		if(rc.canMove(dir) && rc.getLocation().distanceSquaredTo(rc.senseHQLocation().add(dir)) < 2){
			while(!rc.isActive()){
				rc.yield();
			}
			Team mine = rc.senseMine(rc.getLocation().add(dir));
			if(mine != null && !mine.equals(rc.getTeam())){
				rc.defuseMine(rc.getLocation().add(dir));
				rc.yield();
				return;
			}

			if(rc.canMove(dir)){
				rc.move(dir);
			}

		} else {
			Team mine = rc.senseMine(rc.getLocation());
			if((mine == null 
					|| !mine.equals(rc.getTeam()))
					&& rc.getLocation().distanceSquaredTo(rc.senseHQLocation().add(dir)) < 50){
				while(!rc.isActive()){
					rc.yield();
				}
				rc.layMine();	
			} else {
				rc.yield();
			}

		}



	}

}
