/**
 * 
 */
package team001.strategy;

import team001.strategy.hq.BuildUpStrategy;
import team001.strategy.hq.HQInitialStrategy;
import team001.strategy.hq.HQStrategy;
import team001.strategy.soldier.SoldierAwaitOrdersStrategy;
import team001.strategy.soldier.SoldierDefenseStrategy;
import team001.strategy.soldier.SoldierResourceStrategy;
import team001.strategy.soldier.SoldierSmarterStrategy;
import team001.strategy.soldier.SoldierStrategy;

/**
 * @author Jesus Medrano
 * <a href="jesus.i.medrano@gmail.com">jesus.i.medrano@gmail.com</a>
 */
public class Strategies {

	public static HQStrategy buildUpStrategy = new BuildUpStrategy();
	public static HQStrategy initialHQStrategy = new HQInitialStrategy();
	
	public static SoldierStrategy soldierResourceStrategy = new SoldierResourceStrategy();
	public static SoldierStrategy soldierOffensiveStrategy = new SoldierSmarterStrategy();
	public static SoldierStrategy soldierDefenseStrategy = new SoldierDefenseStrategy();
	public static SoldierStrategy soldierAwaitOrdersStrategy = new SoldierAwaitOrdersStrategy();
	
}
