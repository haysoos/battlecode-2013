/**
 * 
 */
package team001.strategy.artillery;

import team001.robots.ArtilleryRobot;
import team001.strategy.artillery.ArtilleryStrategy;

/**
 * @author Jesus Medrano
 * <a href='jesus.i.medrano@gmail.com'>jesus.i.medrano@gmail.com</a>
 */
public class ArtilleryOffensiveStrategy extends ArtilleryStrategy {

	/* (non-Javadoc)
	 * @see team001.strategy.Strategy#runStrategy(team000.robots.BasePlayer)
	 */
	@Override
	public void runStrategy(ArtilleryRobot robot) throws Exception {
		
		robot.seekAndDestroy();
		
	}

}
