/**
 * 
 */
package team001;

import team001.robots.ArtilleryRobot;
import team001.robots.HQRobot;
import team001.robots.SoldierRobot;
import battlecode.common.RobotController;

/**
 * @author Jesus Medrano
 * <a href="jesus.i.medrano@gmail.com">jesus.i.medrano@gmail.com</a>
 */
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
					new HQRobot(rc);
					break;
				case SOLDIER:
					new SoldierRobot(rc);
					break;
				case ARTILLERY:
					new ArtilleryRobot(rc);
					break;
				default:
					//throw new Exception("Unknown type " + rc.getType());
					//System.out.println("Not yet implemented solution for Robot type: " + rc.getType());
				}
			} catch (Exception e){
				e.printStackTrace();
			}
			// End turn
			rc.yield();
		}
	}
}