package team001.strategy.soldier;

import team001.robots.BasePlayer;
import team001.robots.SoldierRobot;
import team001.strategy.Strategy;

/**
 * Soldier Strategy Interface
 * @author Jesus Medrano
 * <a href="jesus.i.medrano@gmail.com">jesus.i.medrano@gmail.com</a>
 */
public abstract class SoldierStrategy implements Strategy {

	/**
	 * Runs the strategy for the given Robot Program.
	 * @param robot 
	 * @throws Exception 
	 */
	public abstract void runStrategy(SoldierRobot robot) throws Exception;
	
	@Override
	public <T extends BasePlayer> void runStrategy(T robot) throws Exception {
		runStrategy((SoldierRobot)robot);	
	}
	
}
