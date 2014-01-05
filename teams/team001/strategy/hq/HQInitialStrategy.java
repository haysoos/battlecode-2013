/**
 * 
 */
package team001.strategy.hq;

import battlecode.common.Upgrade;
import team001.robots.HQRobot;
import team001.strategy.Strategies;

/**
 * @author Jesus Medrano
 * <a href="jesus.i.medrano@gmail.com">jesus.i.medrano@gmail.com</a>
 */
public class HQInitialStrategy extends HQStrategy {

	public void runStrategy(HQRobot robot) throws Exception {
		
		robot.researchUpgrades();
		
		if(robot.getRobotController().hasUpgrade(Upgrade.DEFUSION)
				&& robot.getRobotController().hasUpgrade(Upgrade.PICKAXE)){
			robot.setStrategy(Strategies.buildUpStrategy);
		}
		
	}

}
