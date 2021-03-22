package de.cyzetlc.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.cyzetlc.Main;
import de.cyzetlc.ui.ScoreBoard;
import de.cyzetlc.utils.Data;
import de.cyzetlc.utils.szenarien.Szenarie;
import de.cyzetlc.utils.szenarien.SzenarieManager;

public class Cmd_SZ implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) 
	{
		if (!(s instanceof Player))
		{
			return true;
		}
		
		Player p = (Player) s;
		
		if (!p.hasPermission(Data.MEETUP_SZ))
		{
			p.sendMessage(Data.NO_PERMS);
			return true;
		}
		
		if (args.length == 0)
		{
			p.sendMessage(Data.ALL_COMMANDS_SZ);
			return true;
		}
		
		if (args.length == 1)
		{
			String sz = args[0];
			
			if (Szenarie.valueOf(sz) == null)
			{
				p.sendMessage(Data.SZ_NOT_FOUND);
				return true;
			}
			
			try
			{
				SzenarieManager.setCurrentSzenarie(Szenarie.valueOf(sz));
			}
			catch (Exception e)
			{
				p.sendMessage(Data.SZ_NOT_FOUND);
				return true;
			}
			
			for (Player all : Bukkit.getOnlinePlayers())
			{
				ScoreBoard.update(Main.getPlugin(), all);
			}
			
			p.sendMessage(Data.SZ_NEW_SET);
			return true;
		}
		else
		{
			p.chat("/sz");
			return true;
		}
	}

}
