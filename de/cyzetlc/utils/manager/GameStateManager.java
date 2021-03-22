package de.cyzetlc.utils.manager;

import de.cyzetlc.Main;
import de.cyzetlc.utils.GameState;
import de.cyzetlc.utils.GameStateBuilder;

public class GameStateManager {

	private Main plugin;
	
	public GameStateManager(Main plugin)
	{
		this.plugin = plugin;
	}
	
	public GameState getCurrentGameState()
	{
		return plugin.currentGS;
	}
	
	public GameStateManager loadGameState(GameStateBuilder builder)
	{
		plugin.builder.stop();
		plugin.builder = builder;
		plugin.builder.start();
		return this;
	}
	
	public GameStateManager setCurrentGameState(GameState gs)
	{	
		plugin.currentGS = gs;
		return this;
	}

}
