package de.cyzetlc.gamestates;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import de.cyzetlc.Main;
import de.cyzetlc.ui.ScoreBoard;
import de.cyzetlc.ui.TitleAPI;
import de.cyzetlc.utils.Data;
import de.cyzetlc.utils.GameState;
import de.cyzetlc.utils.GameStateBuilder;
import de.cyzetlc.utils.manager.GameStateManager;
import de.cyzetlc.utils.team.Team;

public class LobbyState implements GameStateBuilder
{
	private static final int COUNTDOWN_TIME = 60, IDLE_TIME = 15;
	
	public int taskID = 10, idleID = 9;
	public static int seconds = COUNTDOWN_TIME;
	public boolean isRunning = false;
	public boolean isWating = false;
	public GameStateManager gs = new GameStateManager(Main.getPlugin());
	
	private BukkitTask brn;
	
	@Override
	public void start() 
	{
		File f = new File(Main.getPlugin().getDataFolder(), "teams.yml");
		File f1 = new File(Main.getPlugin().getDataFolder(), "config.yml");

		FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		FileConfiguration Plcfg = YamlConfiguration.loadConfiguration(f1);
		
		try
		{
			if (!f.exists())
			{
				f.createNewFile();
				cfg.set("TEAM.PREFIX", "%prefix%%teamname% | ");
				cfg.set("TEAM.CHAT", "%teamprefix% &7%p% &8➥ &7 %msg%");
				cfg.set("MAX_PLAYERS", 2);
				cfg.set("TEAM_COUNT", 16);
				cfg.save(f);
			}
			
			if (!f1.exists())
			{
				f1.createNewFile();
				Plcfg.set("MIN_PLAYERS", 4);
				Plcfg.set("MAX_PLAYERS", 32);
				Plcfg.save(f1);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		Data.MIN_PLAYERS = Plcfg.getInt("MIN_PLAYERS");
		Data.MAX_PLAYERS = Plcfg.getInt("MAX_PLAYERS");
		
		if (cfg.getInt("TEAM_COUNT") > 32)
		{
			Data.TEAM_COUNT = 32;
		}
		else
		{
			Data.TEAM_COUNT = cfg.getInt("TEAM_COUNT");
		}
		
		Data.initTeams();
		
		for (Team t : Data.allTeams)
		{
			t.setMaxPlayers(cfg.getInt("MAX_PLAYERS"));
		}
	}

	@Override
	public void startWating()
	{
		if(isWating) return;
		
		isWating = true;
		
		brn = new BukkitRunnable() 
		{

			@Override
			public void run() 
			{
				if(!isRunning)
				{
					Bukkit.broadcastMessage(String.format(Data.GAME_PLAYER_IDLE, String.valueOf(Data.MIN_PLAYERS - Data.players.size())));
				}
				else
				{
					isWating = false;
					cancel();
				}
			}
			
		}.runTaskTimer(Main.getPlugin(), 0, 20 * IDLE_TIME);
	}
	
	@Override
	public void stop() 
	{
		stopIdle();
	}

	@Override
	public void startIdle()
	{
		Bukkit.getScheduler().cancelTask(idleID);
		stopWating();
		
		isRunning = true;
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() 
		{
			
			@Override
			public void run()
			{		
				for(Player all : Bukkit.getOnlinePlayers())
				{
					if(seconds > 0)
					{
						all.setLevel(seconds);
						all.setExp((float) (seconds) / (float) (COUNTDOWN_TIME));
					}
				}
				
				switch(seconds)
				{
				case 60: case 30: case 15: case 10: case 5: case 4: case 3: case 2: case 1:
					for(Player all : Bukkit.getOnlinePlayers())
					{
					    TitleAPI.sendTitle(all, 20, 20, 20, "§7» §c" + seconds, null);
                 	    all.playSound(all.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 1);
					}
					Bukkit.broadcastMessage(String.format(Data.GAME_START_COUNTDOWN, String.valueOf(seconds)));
					break;
				case 0:
   			         
   			         Team.fillTeams();
   			         
   			         for(Player all : Bukkit.getOnlinePlayers())
   			         {
   			        	 all.setLevel(0);
   			        	 all.setExp(0.0F);
   			        	 all.playSound(all.getLocation(), Sound.LEVEL_UP, 1, 1);
   			         }
   			         
   			         gs.setCurrentGameState(GameState.INGAME).loadGameState(new IngameState());
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
		if(isRunning)
		{
			isRunning = false;
			Bukkit.getScheduler().cancelTask(taskID);
			seconds = COUNTDOWN_TIME;
		}
	}
	
	public void stopWating() 
	{
		isWating = false;
	}

	@Override
	public boolean isRunning()
	{
		return isRunning;
	}

}
