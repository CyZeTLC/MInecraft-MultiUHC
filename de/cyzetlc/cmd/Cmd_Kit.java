package de.cyzetlc.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.cyzetlc.utils.Data;
import de.cyzetlc.utils.kit.Kit;

public class Cmd_Kit implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args)
	{
		if (!(s instanceof Player))
		{
			return true;
		}
		
		Player p = (Player) s;
		
		if (!p.hasPermission(Data.MEETUP_KIT))
		{
			p.sendMessage(Data.NO_PERMS);
			return true;
		}
		
		if (args.length == 0)
		{
			p.sendMessage(Data.ALL_COMMANDS_KIT);
			return true;
		}
		
		if (args.length == 2)
		{
			if (args[0].equalsIgnoreCase("create"))
			{
				String name = args[1];
				
				if (Kit.isKitExists(name))
				{
					p.sendMessage(Data.KIT_EXISTS);
					return true;
				}
				
				Kit.saveInventoryToKit(name, p.getInventory().getContents(), p.getInventory().getArmorContents());
				p.sendMessage(String.format(Data.KIT_CREATE_NEW, name));
				return true;
			}
			
			if (args[0].equalsIgnoreCase("set"))
			{
				String kit = args[1];
				
				if (!Kit.isKitExists(kit))
				{
					p.sendMessage(String.format(Data.KIT_NOT_EXISTS, kit));
					return true;
				}
				
				Kit.setActiveKit(kit);
				p.sendMessage(String.format(Data.DEFAULT_KIT_SET, kit));
				return true;
			}
			else
			{
				p.chat("/kit");
				return true;
			}
		}
		else
		{
			p.chat("/kit");
			return true;
		}
	}

}
