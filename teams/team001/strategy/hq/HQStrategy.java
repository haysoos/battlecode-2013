package team001.strategy.hq;

import team001.robots.BasePlayer;
import team001.robots.HQRobot;
import team001.strategy.Strategy;

/**
 * HQ Robot Strategy Interface
 * @author Jesus Medrano
 * <a href="jesus.i.medrano@gmail.com">jesus.i.medrano@gmail.com</a>
 */
public abstract class HQStrategy implements Strategy {

	/**
	 * Runs the strategy for the given Robot Program.
	 * @param robot 
	 * @throws Exception 
	 */
	public abstract void runStrategy(HQRobot robot) throws Exception;
	
	@Override
	public <T extends BasePlayer> void runStrategy(T robot) throws Exception {
		runStrategy((HQRobot) robot);	
	}
	
}
