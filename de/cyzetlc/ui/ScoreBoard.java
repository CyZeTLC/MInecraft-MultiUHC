package de.cyzetlc.ui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import de.cyzetlc.Main;
import de.cyzetlc.utils.Data;
import de.cyzetlc.utils.GameState;
import de.cyzetlc.utils.szenarien.Szenarie;
import de.cyzetlc.utils.szenarien.SzenarieManager;

public class ScoreBoard {

	  public static void update(Main plugin, Player player)
	  {
	    if (player.getScoreboard().getObjective("aaa") != null)
	    {
	      player.getScoreboard().getObjective("aaa").unregister();
	    }
	    if (player.getScoreboard().getObjective("count") != null)
	    {
	    	player.getScoreboard().getObjective("count").unregister();
	    }
	    Objective objective = player.getScoreboard().registerNewObjective("aaa", "bbb");
	    Objective obj = player.getScoreboard().registerNewObjective("count", "img");
        obj.setDisplayName("§4☠");
        obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
        obj.getScore(player).setScore((int) 0);
        
	    for (Team team : player.getScoreboard().getTeams())
	    {
	      team.unregister();
	    }
	    plugin.getRankFileManager().registerRanks(player.getScoreboard());
	    for (Player all : Bukkit.getOnlinePlayers()) 
	    {
	      plugin.getRankFileManager().setRankPrefix(all, player.getScoreboard());
	    }
	    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	    objective.setDisplayName("§8•§7●§b§lUHC§7●§8•");
	    
	    if (plugin.gsManager.getCurrentGameState().equals(GameState.LOBBY))
	    {	    	
		    objective.getScore("§1").setScore(13);
		    objective.getScore("§f§lOnline").setScore(12);
		    objective.getScore("§8➥ §e" + Bukkit.getOnlinePlayers().size()).setScore(11);
		    objective.getScore("§2").setScore(10);
		    objective.getScore("§f§lSzenarien").setScore(9);
		    objective.getScore("§8➥ §e" + SzenarieManager.getCurrentSzenarie()).setScore(8);
		    objective.getScore("§3").setScore(7);
		    objective.getScore("§f§lSpiel Variante").setScore(6);
		    objective.getScore("§8➥ §e" + Data.MODI).setScore(5);
		    objective.getScore("§4").setScore(4);
		    objective.getScore("§f§lDein Team ").setScore(3);
		    objective.getScore("§8➥ §e" + Data.teamName.get(player)).setScore(2);
		    objective.getScore("§5").setScore(1);
	    }
	    
	    if (plugin.gsManager.getCurrentGameState().equals(GameState.INGAME))
	    {
		    int teamKills = Data.kills.get(player);
	        obj.getScore(player).setScore((int) Data.kills.get(player));
		    de.cyzetlc.utils.team.Team playerTeam = null;
		    
		    for (de.cyzetlc.utils.team.Team t : Data.allTeams)
		    {
		    	if (t.getPlayers().contains(player))
		    	{
		    		playerTeam = t;
		    		break;
		    	}
		    }
		    
		    for (Player p : Bukkit.getOnlinePlayers())
		    {
		    	de.cyzetlc.utils.team.Team team = null;
		    	for (de.cyzetlc.utils.team.Team t : Data.allTeams)
		    	{
		    		if (t.getPlayers().contains(p))
		    		{
		    			team = t;
		    		}
		    	}
		    	
		    	if (team.equals(playerTeam))
		    	{
		    		if (!p.equals(player))
		    		{
		    			teamKills += Data.kills.get(p);
		    		}
		    	}
		    }
	    	
		    objective.getScore("§1").setScore(13);
		    objective.getScore("§f§lLebende Spieler").setScore(12);
		    objective.getScore("§8➥ §e" + Data.players.size()).setScore(11);
		    objective.getScore("§2").setScore(10);
		    objective.getScore("§f§lKills").setScore(9);
		    objective.getScore("§8➥ §e" + Data.kills.get(player) + " §7(§c" + teamKills + "§7)").setScore(8);
		    objective.getScore("§3").setScore(7);

		    if (Data.MODI != "SpeedUHC")
		    {
			    objective.getScore("§f§lSzenarien ").setScore(6);
			    objective.getScore("§8➥ §e" + SzenarieManager.getCurrentSzenarie()).setScore(5);
			    objective.getScore("§4").setScore(4);
		    }
		    
		    objective.getScore("§f§lSpiel Variante").setScore(3);
		    objective.getScore("§8➥ §e" + Data.MODI).setScore(2);
		    objective.getScore("§5").setScore(1);
		    
		    player.setPlayerListName(player.getDisplayName() + " §8[§e" + Data.kills.get(player) + "§8]");
	    }
	    
	    player.setScoreboard(objective.getScoreboard());
	  }
	  
	  public static void sendScoreboard(Player player)
	  {
	    Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	    
	    player.setScoreboard(scoreboard);
	  }

}
