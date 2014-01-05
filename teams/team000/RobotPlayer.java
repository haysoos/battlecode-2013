package team000;

import team000.robots.ArtilleryRobot;
import team000.robots.HQRobot;
import team000.robots.SoldierRobot;
import battlecode.common.RobotController;

public class RobotPlayer {
	
	/**
	 * Start point.
	 * @param rc
	 */
	public static void run(RobotController rc) {
		while (true) {
			try {
				switch(rc.getType()){
				case HQ:
					new HQRobot(rc).runProgram();
					break;
				case SOLDIER:
					new SoldierRobot(rc).runProgram();
					break;
				case ARTILLERY:
					new ArtilleryRobot(rc).runProgram();
					break;
				default:
					
				}
			} catch (Exception e){
				e.printStackTrace();
			}
			// End turn
			rc.yield();
		}
	}
}
