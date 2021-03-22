package de.cyzetlc.cmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.cyzetlc.io.ForbiddenHandler;
import de.cyzetlc.utils.Data;
import de.cyzetlc.utils.manager.LocationManager;
import de.cyzetlc.utils.team.Team;

public class Cmd_Setspawn implements CommandExecutor, TabCompleter
{

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) 
	{
		if (!(s instanceof Player))
		{
			return false;
		}
		
		Player p = (Player) s;
		
		if (!p.hasPermission(Data.MEETUP_SETSPAWN))
		{
			p.sendMessage(Data.NO_PERMS);
			return true;
		}
		
		if (args.length == 0)
		{
			p.sendMessage(Data.ALL_COMMANDS);
			return true;
		}
		
		if (args.length == 1)
		{
			if (args[0].equalsIgnoreCase("holo"))
			{
				Location loc = p.getLocation();
				String name = "Holo";
							
				if (LocationManager.isLocationExists(name))
				{
					p.sendMessage(Data.CHANGE_HOLO_LOC);
					LocationManager.setLocation(loc, name);
					return true;
				}
				else
				{
					p.sendMessage(Data.SET_HOLO_LOC);
					LocationManager.setLocation(loc, name);
					return true;
				}
			}
			
			if (args[0].equalsIgnoreCase("lobby"))
			{
				Location loc = p.getLocation();
				String name = "Lobby";
							
				if (LocationManager.isLocationExists(name))
				{
					p.sendMessage(Data.CHANGE_LOBBY_LOC);
					LocationManager.setLocation(loc, name);
					return true;
				}
				else
				{
					p.sendMessage(Data.SET_LOBBY_LOC);
					LocationManager.setLocation(loc, name);
					return true;
				}
			}
			else
			{
				p.chat("/setspawn");
				return true;
			}
		}
		
		if (args.length == 2)
		{
			if (args[0].equalsIgnoreCase("skull"))
			{
				int i = 0;
				
				try
				{
					i = Integer.valueOf(args[1]);
					
					if (i > 5)
					{
						p.sendMessage(Data.PREFIX + "§7Gib eine Zahl zwischen 1 und 5 an!");
						return true;
					}
				}
				catch (Exception e)
				{
					p.sendMessage(Data.PREFIX + "§7Gib eine Zahl zwischen 1 und 5 an!");
					return true;
				}
				
				ForbiddenHandler.setup.put(p, i);
				
				p.sendMessage(Data.PREFIX + "§7Linksklicke nun den Block wo der Kopf sein soll ( im GameMode 1 )");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("team"))
			{
				Location loc = p.getLocation();
				String name = args[1];
				boolean isVailedTeam = false;
				
				for (Team t : Data.allTeams)
				{
					if (t.getName().equals(args[1]))
					{
						isVailedTeam = true;
						break;
					}
				}
				
				if (!isVailedTeam)
				{
					p.sendMessage(Data.TEAM_NOT_EXSISTS);
					return true;
				}
				
				if (LocationManager.isLocationExists(name))
				{
					p.sendMessage(String.format(Data.CHANGE_TEAM_LOC, "#" + args[1]));
					LocationManager.setLocation(loc, name);
					return true;
				}
				else
				{
					p.sendMessage(String.format(Data.SET_TEAM_LOC, "#" + args[1]));
					LocationManager.setLocation(loc, name);
					return true;
				}
			}
			else
			{
				p.chat("/setspawn");
				return true;
			}
		}
		else
		{
			p.chat("/setspawn");
			return true;
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String l, String[] args) 
	{
		final List<String> list = new ArrayList<String>();
		
		if (args.length == 1)
		{
			list.add("team");
			list.add("lobby");
			list.add("holo");
			list.add("skull");
		}
		
		if (args.length == 2)
		{
			if (args[0].equalsIgnoreCase("team"))
			{
				for (Team t : Data.allTeams)
				{
					list.add(t.getName());
				}
			}
		}
		
		return list;
	}
	
}
