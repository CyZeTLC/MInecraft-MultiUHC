package de.cyzetlc.utils.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import de.cyzetlc.Main;
import de.cyzetlc.utils.Data;
import de.cyzetlc.utils.GameState;

@SuppressWarnings({ })
public class RankFileManager
{
  private Main plugin;
  private File file;
  private FileConfiguration rankFile;
  private FileConfiguration teamFile;
  private HashMap<Player, String> rank;
  public HashMap<Player, String> prefix;
  public ArrayList<String> ranks;
  
  public RankFileManager(Main plugin)
  {
    this.plugin = plugin;
    this.file = new File(plugin.getDataFolder(), "ranks.yml");
    this.teamFile = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "teams.yml"));
    if (!this.file.exists())
    {
      FileConfiguration defaultLoader = YamlConfiguration.loadConfiguration(this.file);
      defaultLoader.set("Spieler.Prefix", "&7Spieler &8➥ &7");
      defaultLoader.set("Spieler.Permission", "rank.spieler");
      defaultLoader.set("Spieler.DefaultRank", Boolean.valueOf(true));
      defaultLoader.set("Spieler.ChatFormat", "&7Spieler &8➥ &7 %p% §8» &7%msg%");
      try
      {
        defaultLoader.save(this.file);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    this.rankFile = YamlConfiguration.loadConfiguration(this.file);
    this.rank = new HashMap();
    this.prefix = new HashMap();
    
    saveRankFile();
    this.ranks = new ArrayList();
	for (de.cyzetlc.utils.team.Team t : Data.allTeams)
	{
		this.ranks.add(t.getName());
	}
	this.ranks.addAll(this.rankFile.getKeys(false));
  }
  
  public void registerRanks(Scoreboard scoreboard)
  {
    ArrayList<String> ranks = new ArrayList();
    for (de.cyzetlc.utils.team.Team t : Data.allTeams)
    {
    	ranks.add(t.getName());
    }
    ranks.addAll(this.rankFile.getKeys(false));
    for (int i = 0; i < ranks.size(); i++)
    {
      String name;
      if (i < 10) {
        name = "0" + i + (String)ranks.get(i);
      } else {
        name = i + (String)ranks.get(i);
      }
      if (de.cyzetlc.utils.team.Team.getTeamByName(ranks.get(i)) != null)
      {
    	  de.cyzetlc.utils.team.Team t = de.cyzetlc.utils.team.Team.getTeamByName(ranks.get(i));
          scoreboard.registerNewTeam(name).setPrefix(this.teamFile.getString("TEAM.PREFIX").replace("%prefix%", t.getPrefix()).replace("%teamname%", t.getName()).replace("&", "§"));
      }
      else
      {
          scoreboard.registerNewTeam(name).setPrefix(ChatColor.translateAlternateColorCodes('&', this.rankFile.getString((String)ranks.get(i) + ".Prefix")));
      }
    }
  }
  
  public void setRankPrefix(Player player, Scoreboard scoreboard)
  {
    ArrayList<String> ranks = new ArrayList();
    for (de.cyzetlc.utils.team.Team t : Data.allTeams)
    {
    	ranks.add(t.getName());
    }
    ranks.addAll(this.rankFile.getKeys(false));
    for (int i = 0; i < ranks.size(); i++) {
      if ((de.cyzetlc.utils.team.Team.getTeamByName(ranks.get(i)) == null))
      {
    	if (player.hasPermission(this.rankFile.getString((String)ranks.get(i) + ".Permission")))
    	{
        	String name;
            if (i < 10) {
              name = "0" + i + (String)ranks.get(i);
            } else {
              name = i + (String)ranks.get(i);
            }
            scoreboard.getTeam(name).addEntry(player.getName());
            player.setDisplayName(scoreboard.getTeam(name).getPrefix() + player.getName());
            this.rank.put(player, ranks.get(i));
            this.prefix.put(player, this.rankFile.getString((String)ranks.get(i) + ".Prefix"));
            return;
    	}
      }
      if (de.cyzetlc.utils.team.Team.getTeamByName(ranks.get(i)) != null && de.cyzetlc.utils.team.Team.isPlayerInTeam(player) && de.cyzetlc.utils.team.Team.getTeamOfPlayer(player).equals(de.cyzetlc.utils.team.Team.getTeamByName(ranks.get(i))))
      {
          String name;
          if (i < 10) {
            name = "0" + i + (String)ranks.get(i);
          } else {
            name = i + (String)ranks.get(i);
          }
          scoreboard.getTeam(name).addEntry(player.getName());
          player.setDisplayName(scoreboard.getTeam(name).getPrefix() + player.getName());
          this.rank.put(player, ranks.get(i));
          this.prefix.put(player, de.cyzetlc.utils.team.Team.getTeamByName(ranks.get(i)).getPrefix());
          return;
      }
    }
    if ((getDefaultTeam() != null) || (getDefaultRankNumber() != -1))
    {
      String name;
      if (getDefaultRankNumber() < 10) {
        name = "0" + getDefaultRankNumber() + getDefaultTeam();
      } else {
        name = getDefaultRankNumber() + getDefaultTeam();
      }
      scoreboard.getTeam(name).addEntry(player.getName());
      player.setDisplayName(scoreboard.getTeam(name).getPrefix() + player.getName());
      this.rank.put(player, getDefaultTeam());
      this.prefix.put(player, this.rankFile.getString((String)ranks.get(getDefaultRankNumber()) + ".Prefix"));
    }
  }
  
  public String getDefaultTeam()
  {
    ArrayList<String> ranks = new ArrayList();
	for (de.cyzetlc.utils.team.Team t : Data.allTeams)
	{
		ranks.add(t.getName());
	}
	ranks.addAll(this.rankFile.getKeys(false));
    for (int i = 16; i < ranks.size(); i++) {
      if (this.rankFile.getBoolean((String)ranks.get(i) + ".DefaultRank")) {
        return (String)ranks.get(i);
      }
    }
    return null;
  }
  
  public int getDefaultRankNumber()
  {
	ArrayList<String> ranks = new ArrayList();
    for (de.cyzetlc.utils.team.Team t : Data.allTeams)
    {
		ranks.add(t.getName());
	}
	ranks.addAll(this.rankFile.getKeys(false));  
    for (int i = 16; i < ranks.size(); i++) {
      if (this.rankFile.getBoolean((String)ranks.get(i) + ".DefaultRank")) {
        return i;
      }
    }
    return -1;
  }
  
  public int getRankNumber(Player player)
  {
    for (int i = 0; i < this.ranks.size(); i++) {
      if (((String)this.ranks.get(i)).equals(this.rank.get(player))) {
        return i;
      }
    }
    return -1;
  }
  
  public String getDisplayName(Player player)
  {
    return ChatColor.translateAlternateColorCodes('&', (String)this.prefix.get(player)) + player.getName();
  }
  
  public String getChatFormat(Player player)
  {
    if (!this.plugin.gsManager.getCurrentGameState().equals(GameState.LOBBY) || de.cyzetlc.utils.team.Team.isPlayerInTeam(player))
	{
    	de.cyzetlc.utils.team.Team te = null;
      for (de.cyzetlc.utils.team.Team t : Data.allTeams)
      {
    	  if (t.getPlayers().contains(player))
    	  {
    		  te = t;
    	  }
      }
    	
	  return ChatColor.translateAlternateColorCodes('&', this.teamFile.getString("TEAM.CHAT").replace("%teamprefix%", te.getPrefix() + te.getName()));
	}
    return ChatColor.translateAlternateColorCodes('&', this.rankFile.getString((String)this.rank.get(player) + ".ChatFormat"));
  }
  
  public void saveRankFile()
  {
    try
    {
      this.rankFile.save(this.file);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
