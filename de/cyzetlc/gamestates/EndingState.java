package de.cyzetlc.gamestates;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import de.cyzetlc.Main;
import de.cyzetlc.utils.Data;
import de.cyzetlc.utils.GameState;
import de.cyzetlc.utils.GameStateBuilder;
import de.cyzetlc.utils.manager.GameStateManager;
import de.cyzetlc.utils.manager.LocationManager;
import de.cyzetlc.utils.mysql.stats.SQLStats;

public class EndingState implements GameStateBuilder
{

	private static final int COUNTDOWN_TIME = 25;
	public static boolean isPvPAllowed = false;
	
	public int taskID = 10;
	public static int seconds = COUNTDOWN_TIME;
	public boolean isRunning = false;
	public GameStateManager gs = new GameStateManager(Main.getPlugin());
	
	public static World world = null;
	
	private BukkitTask brn;
	
	@Override
	public void start() 
	{
		startIdle();
		
		for (Player all : Bukkit.getOnlinePlayers())
		{
			all.teleport(LocationManager.getLocation("Lobby"));
			all.getInventory().clear();
			for (Player p : Bukkit.getOnlinePlayers())
			{
				all.showPlayer(p);
			}
			all.setGameMode(GameMode.SPECTATOR);
		}
	}
	
	@Override
	public void stop() 
	{
		stopIdle();
	}

	
	public static void endGame()
	{
    	Main.getPlugin().gsManager.setCurrentGameState(GameState.ENDING).loadGameState(new EndingState());
    	Player last = null;
    	
    	for (Player p1 : Data.players)
    	{
    		last = p1;
    	}
    	
	    de.cyzetlc.utils.team.Team team = null;
	    
	    for (de.cyzetlc.utils.team.Team t : Data.allTeams)
	    {
	    	if (t.getPlayers().contains(last))
	    	{
	    		team = t;
	    		break;
	    	}
	    }
	    
	    int livingPlayers = 0;
    	for (Player a : team.getPlayers())
    	{
    		if (Data.players.contains(a))
    		{
    			livingPlayers++;
    		}
    	}
    	
    	if (livingPlayers > 1)
    	{
    		for (Player p : team.getPlayers())
    		{
    			SQLStats.addWins(p.getUniqueId().toString(), 1);
    		}
    		Bukkit.broadcastMessage(String.format(Data.TEAM_WIN, team.getPrefix() + "#" + team.getName()));
    	}
    	else
    	{
    		SQLStats.addWins(last.getUniqueId().toString(), 1);
    		Bukkit.broadcastMessage(String.format(Data.PLAYER_WIN, last.getName()));
    	}
	}
	
	@Override
	public void startIdle()
	{
		isRunning = true;
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() 
		{
			
			@Override
			public void run()
			{		
				switch(seconds)
				{
				case 25:
					for (Player all : Bukkit.getOnlinePlayers())
					{
						all.teleport(LocationManager.getLocation("Lobby"));
						all.setGameMode(GameMode.ADVENTURE);
						all.getInventory().clear();
						all.getInventory().setArmorContents(null);
						
						for (Player p : Bukkit.getOnlinePlayers())
						{
							all.showPlayer(p);
						}
					}
					break;
				case 20: case 15: case 10: case 5: case 4: case 3: case 2: case 1:
					for(Player all : Bukkit.getOnlinePlayers())
					{
                 	    all.playSound(all.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 1);
					}
					Bukkit.broadcastMessage(String.format(Data.STOP_TIME_COUNTDOWN, String.valueOf(seconds)));
					break;
				case 0:
					for (Player all : Bukkit.getOnlinePlayers())
					{
						all.kickPlayer(Data.PREFIX + "§7Die Runde ist vorbei!");
					}
					Bukkit.shutdown();
					break;
				     
				default:
			     	break;
				}
				seconds--;
			}
			
		}, 0, 20);
	}

	@Override
	public void stopIdle()
	{
		
	}
	
	public void stopWating() 
	{

	}

	@Override
	public boolean isRunning()
	{
		return isRunning;
	}

	@Override
	public void startWating() {
		// TODO Auto-generated method stub
		
	}

}
