package team001.strategy;

import team001.robots.BasePlayer;

/**
 * Strategy Interface
 * @author Jesus Medrano
 * <a href="jesus.i.medrano@gmail.com">jesus.i.medrano@gmail.com</a>
 */
public interface Strategy {

	/**
	 * Runs the strategy for the given Robot Program.
	 * @param robot 
	 * @throws Exception 
	 */
	public <T extends BasePlayer> void runStrategy(T robot) throws Exception;
	
}
