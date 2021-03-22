package de.cyzetlc.utils.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import de.cyzetlc.Main;
import de.cyzetlc.utils.szenarien.Szenarie;

public class SZVoteManager 
{
	
	public static HashMap<String, Integer> mapVotes;
	public static HashMap<Player, String> votePlayer;
	
	private Main plugin;

	public SZVoteManager(Main plugin) 
	{
		this.plugin = plugin;
		this.mapVotes = new HashMap<>();
		this.votePlayer = new HashMap<>();
		
		for(String maps : getSzenarien())
		{
			this.mapVotes.put(maps, 0);
		}
	}
	
	public static Integer getVotes(String map)
	{
		 return mapVotes.get(map);
	}
	
	public static boolean addVote(String map)
	{
		mapVotes.put(map, mapVotes.get(map)+1);
		return true;
	}
	
	public static boolean removeVote(String map)
	{
		int mapVote = mapVotes.get(map);
		int newVotes = mapVote -1;
		mapVotes.put(map, newVotes);
		return true;
	}
	
	public static String getPlayerVote(Player p)
	{
		 return votePlayer.get(p);
	}
	
	public static boolean removePlayer(Player p)
	{
		votePlayer.remove(p);
		return true;
	}
	
	public static String getSZWithMostVotes()
	{
		int max = 0;
		String map = "";
		for (String maps : getSzenarien())
		{
			int i = ((Integer) mapVotes.get(maps)).intValue();
			if (i > max) 
			{
				max = i;
			}
		}
		for (String s : mapVotes.keySet()) 
		{
			if (((Integer) mapVotes.get(s)).intValue() == max) 
			{
				map = s;
			}
		}
		return map;
	}
 
	public static List<String> getSzenarien()
	{
		List<String> back = new ArrayList<>();
		for (Szenarie sz : Szenarie.values())
		{
			back.add(sz.name());
		}
		return back;
	}
	
}
