package de.cyzetlc.cmd;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.cyzetlc.Main;
import de.cyzetlc.utils.Data;
import de.cyzetlc.utils.team.Team;

public class Cmd_To implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args)
	{
		if (!(s instanceof Player))
		{
			return true;
		}
		
		Player p = (Player) s;
		
		if (!p.hasPermission(Data.MEETUP_TO))
		{
			p.sendMessage(Data.NO_PERMS);
			return true;
		}
		
		if (args.length == 0)
		{
			p.sendMessage(Data.ALL_COMMANDS_TO);
			return true;
		}
		
		if (args.length == 1)
		{
			int max = 0;
			
			try
			{
				max = Integer.valueOf(args[0]);
			}
			catch (NumberFormatException e)
			{
				p.sendMessage(Data.ONLY_NUMBERS);
				return true;
			}
			
			File f = new File(Main.getPlugin().getDataFolder(), "teams.yml");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
			
			try
			{
				cfg.set("MAX_PLAYERS", max);
				cfg.save(f);
				
				for (Team t : Data.allTeams)
				{
					t.setMaxPlayers(max);
				}
				
				p.sendMessage(Data.SETTINGS_SAVED);
				return true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return true;
		}
		else
		{
			p.chat("/to");
			return true;
		}
	}
}
