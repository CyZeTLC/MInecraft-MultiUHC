package de.cyzetlc.io;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import de.cyzetlc.Main;
import de.cyzetlc.gamestates.EndingState;
import de.cyzetlc.ui.ScoreBoard;
import de.cyzetlc.utils.Data;
import de.cyzetlc.utils.GameState;
import de.cyzetlc.utils.Hologram;
import de.cyzetlc.utils.manager.ItemManager;
import de.cyzetlc.utils.manager.LocationManager;
import de.cyzetlc.utils.manager.SZVoteManager;
import de.cyzetlc.utils.mysql.MySQL;
import de.cyzetlc.utils.mysql.stats.SQLStats;
import de.cyzetlc.utils.szenarien.Szenarie;
import de.cyzetlc.utils.szenarien.SzenarieManager;
import de.cyzetlc.utils.team.Team;

public class ConnectionHandler implements Listener
{
	private final Main plugin;
	
	public ConnectionHandler(Main plugin) 
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void handleLogin(PlayerLoginEvent e)
	{
		if (Data.players.size() == Data.MAX_PLAYERS)
		{
			e.disallow(Result.KICK_FULL, Data.GAME_STARTED);
		}
		if (plugin.gsManager.getCurrentGameState().equals(GameState.LOBBY)) return;
		
		e.disallow(Result.KICK_OTHER, Data.GAME_STARTED);
	}
	
	@EventHandler
	public void handleJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		
		if (plugin.gsManager.getCurrentGameState().equals(GameState.INGAME))
		{
			e.setJoinMessage(null);
			p.getInventory().clear();
			p.getInventory().setArmorContents(null);
			p.setVelocity(new Vector(0,0,0));
			
			Data.spectators.add(p);
			
			p.setHealth(0.0D);
			return;
		}
		
		if (!plugin.gsManager.getCurrentGameState().equals(GameState.LOBBY)) return;
		
		Data.players.add(p);
		Data.teamName.put(p, "-");
		
		p.setLevel(60);
		p.setExp((float) (60) / (float) (60)); // oder halt einfach 1 xD
		
		checkLobbyStats();
		
		for (Player all : Bukkit.getOnlinePlayers())
		{
			all.hidePlayer(p);
			all.showPlayer(p);
		}
		
		ScoreBoard.sendScoreboard(p);
		
		for (Player all : Bukkit.getOnlinePlayers())
		{
			ScoreBoard.update(plugin, all);
			ScoreBoard.update(plugin, all);
		}
			
		if(!(p.hasPlayedBefore()))
		{
			new BukkitRunnable()
			{
				@Override
				public void run() 
				{
					if (LocationManager.isLocationExists("Lobby")) { p.teleport(LocationManager.getLocation("Lobby")); }
				}	
			}.runTaskLater(plugin, 20);
		}
		else
		{
			p.setHealth(20);
			p.setFoodLevel(20);
			p.setVelocity(new Vector(0,0,0));
			if (LocationManager.isLocationExists("Lobby")) { p.teleport(LocationManager.getLocation("Lobby")); }
		}
		
		p.setGameMode(GameMode.ADVENTURE);
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);	
		ItemManager.addLobbyItems(p);
				
		if (LocationManager.isLocationExists("Holo"))
		{
			int kills = SQLStats.getKills(p.getUniqueId().toString()).intValue();
			int deaths = SQLStats.getDeaths(p.getUniqueId().toString()).intValue();
			int wins = SQLStats.getWins(p.getUniqueId().toString()).intValue();
			int played = SQLStats.getPlayed(p.getUniqueId().toString()).intValue();
			int rank = MySQL.getRank(p.getUniqueId().toString());
			double kd = kills;
			if (deaths > 0)
			{
				kd = Math.round(kills / deaths);
			}
			
			String[] txt = new String[] {
					Data.STATS_FORMAT_HEADER.replace("%name%", p.getName()),
					Data.STATS_FORMAT_RANK.replace("%rank%", String.valueOf(rank)),
					Data.STATS_FORMAT_GAMES.replace("%gs%", String.valueOf(played)),
					Data.STATS_FORMAT_WINS.replace("%wins%", String.valueOf(wins)),
					Data.STATS_FORMAT_KILLS.replace("%Kills%", String.valueOf(kills)),
					Data.STATS_FORMAT_DEATHS.replace("%deaths%", String.valueOf(deaths)),
					Data.STATS_FORMAT_KD.replace("%kd%", String.valueOf(kd))
			};
			Hologram holo = new Hologram(txt, LocationManager.getLocation("Holo"));
			holo.showPlayer(p);
		}
		
		e.setJoinMessage(String.format(Data.PLAYER_JOIN, p.getName()));
	}
	
	@EventHandler
	public void handleQuit(PlayerQuitEvent e)
	{
		Player p = e.getPlayer();
		
		if (!Data.players.contains(p))
		{
			e.setQuitMessage(null);
			return;
		}
		
	    de.cyzetlc.utils.team.Team team = null;
	    
	    for (de.cyzetlc.utils.team.Team t : Data.allTeams)
	    {
	    	if (t.getPlayers().contains(p))
	    	{
	    		team = t;
	    		break;
	    	}
	    }
	    
	    if (team != null)
	    {
	    	team.removePlayer(p);
	    }
		
		e.setQuitMessage(String.format(Data.PLAYER_QUIT, p.getName()));
		
		if (plugin.gsManager.getCurrentGameState().equals(GameState.INGAME))
		{		    
		    if (team.getPlayers().size() == 0 && Data.players.contains(p))
		    {
		    	Bukkit.broadcastMessage(String.format(Data.TEAM_ELIMINATE, team.getPrefix() + "#" + team.getName()));
		    }
		    else if (team.getPlayers().size() >= 1 && Data.players.contains(p))
		    {
		    	Bukkit.broadcastMessage(Data.TEAM_LOSE_PLAYER.replace("%s", team.getPrefix() + team.getName()).replace("%c", String.valueOf(team.getPlayers().size())));
		    }
		    
		    if (Data.players.size() == 1)
		    {
		    	EndingState.endGame();
		    }
		    else
		    {
			    int livingTeams = 0;
			    
			    for (Team t : Data.allTeams)
			    {
			    	int livingPlayers = 0;
			    	for (Player a : t.getPlayers())
			    	{
			    		if (Data.players.contains(a))
			    		{
			    			livingPlayers++;
			    		}
			    	}
			    	
			    	if (livingPlayers > 0)
			    	{
			    		livingTeams++;
			    	}
			    }
			    
			    if (livingTeams == 1)
			    {
			    	EndingState.endGame();
			    }
		    }
		}
		
		if (Data.players.contains(p))
		{
			Data.players.remove(p);
		}
		
		
		for (Player all : Bukkit.getOnlinePlayers())
		{
			ScoreBoard.update(Main.getPlugin(), all);
		}
		
		if (plugin.gsManager.getCurrentGameState().equals(GameState.LOBBY))
		{
			if (SZVoteManager.votePlayer.containsKey(p))
			{
				SZVoteManager.removeVote(SZVoteManager.getPlayerVote(p));
				SZVoteManager.removePlayer(p);
				SzenarieManager.setCurrentSzenarie(Szenarie.valueOf(SZVoteManager.getSZWithMostVotes()));
				
				for (Player all : Bukkit.getOnlinePlayers())
				{
					ScoreBoard.update(Main.getPlugin(), all);
				}
			}
			
			if (Data.playerVotes.containsKey(p))
			{
				if (Data.playerVotes.get(p) == "SpeedUHC")
				{
					Data.votesSpeedUHC--;
				}
				else
				if (Data.playerVotes.get(p) == "Meetup")
				{
					Data.votesMeetup--;
				}
				else
				{
					Data.votesUHC--;
				}
				
				Data.setModi();
				
				for (Player all : Bukkit.getOnlinePlayers())
				{
					ScoreBoard.update(Main.getPlugin(), all);
				}
			}
			
			if (Data.players.size() < Data.MIN_PLAYERS)
			{
				if(plugin.ls.isRunning())
				{
					plugin.ls.stopIdle();
					plugin.ls.startWating();
				}
			}
		}
	}
	
	public void checkLobbyStats()
	{
		if(Data.players.size() == Data.MIN_PLAYERS)
		{
			if(plugin.ls.isWating)
			{
				plugin.ls.stopWating();
			}
			if(!(plugin.ls.isRunning()))
			{
				plugin.ls.stopIdle();
				plugin.ls.startIdle();
			}
		}
		else
		{
			if(!(plugin.ls.isWating))
			{
				plugin.ls.startWating();
			}
		}
	}
}
