package de.cyzetlc.utils.team;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.cyzetlc.Main;
import de.cyzetlc.ui.ScoreBoard;
import de.cyzetlc.utils.Data;

public class Team 
{
	private int MAX_PLAYERS = 2;
	
	private final String name;
	private final String prefix;
	private final List<Player> players = new ArrayList<Player>();
	
	private ItemStack[] items;
	private Inventory inv;
	
	public Team(String name, String prefix)
	{
		this.name = name;
		this.prefix = prefix;
		this.items = new ItemStack[9*3];
		this.inv = Bukkit.createInventory(null,9*3,this.prefix+"#"+this.name);
	}
	
	public boolean isTeamEmpty()
	{
		if (this.players.isEmpty())
		{
			return true;
		}
		return false;
	}
	
	public Inventory getInv() 
	{
		return this.inv;
	}
	
	public void setMaxPlayers(int max)
	{
		this.MAX_PLAYERS = max;
	}
	
	public int getMaxPlayers() 
	{
		return MAX_PLAYERS;
	}
	
	public String getPrefix() 
	{
		return prefix;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean isTeamFull()
	{
		if (this.players.size() == this.MAX_PLAYERS)
		{
			return true;
		}
		return false;
	}
	
	public void removePlayer(Player p)
	{
		this.players.remove(p);
	}
	
	public void addPlayer(Player p)
	{
		Data.teamName.put(p, this.getPrefix() + this.getName());
		this.players.add(p);
	}
	
	public List<Player> getPlayers()
	{
		return this.players;
	}
	
	public static boolean isPlayerInTeam(Player p)
	{
		boolean isPlayerInTeam = false;
		for (Team t : Data.allTeams)
		{
			if (t.getPlayers().contains(p))
			{
				isPlayerInTeam = true;
				break;
			}
		}
		return isPlayerInTeam;
	}
	
	public static Team getTeamOfPlayer(Player p)
	{
		Team team = null;
		for (Team t : Data.allTeams)
		{
			if (t.getPlayers().contains(p))
			{
				team = t;
				break;
			}
		}
		return team;
	}
	
	public static Team getTeamByName(String name)
	{
		Team team = null;
		for (Team t : Data.allTeams)
		{
			if (String.valueOf(t.getName()).equals(name))
			{
				team = t;
				break;
			}
		}
		return team;
	}
	
	public static Team getTeamByFullName(String name)
	{
		Team team = null;
		for (Team t : Data.allTeams)
		{
			if (String.valueOf(t.getPrefix() + t.getName()).equals(name))
			{
				team = t;
				break;
			}
		}
		return team;
	}
	
	public static void fillTeams()
	{
		for (Player all : Bukkit.getOnlinePlayers())
		{
			if (isPlayerInTeam(all))
			{
				continue;
			}
			
			for (Team t : Data.allTeams)
			{
				if (Data.players.size() == 2)
				{
					if (t.isTeamEmpty())
					{
						if (!isPlayerInTeam(all))
						{
							t.addPlayer(all);
						}
					}
				}
				
				if (!t.isTeamFull())
				{
					if (!isPlayerInTeam(all))
					{
						t.addPlayer(all);
					}
				}
			}
			ScoreBoard.update(Main.getPlugin(), all);
		}
	}
}
