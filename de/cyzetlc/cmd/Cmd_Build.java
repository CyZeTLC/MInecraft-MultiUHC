package de.cyzetlc.cmd;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.cyzetlc.io.ForbiddenHandler;
import de.cyzetlc.utils.Data;

public class Cmd_Build implements CommandExecutor
{
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) 
	{
		
		if(!(s instanceof Player))
		{
			s.sendMessage("§cDu bist kein Spieler!");
			return true;
		}
		
		Player p = (Player) s;
		
		if(p.hasPermission("meetup.build")) 
		{
			if(args.length == 0)
			{
				if(!ForbiddenHandler.build.contains(p)) 
				{
					
					ForbiddenHandler.build.add(p);
					
					ForbiddenHandler.buildinv.put(p, p.getInventory().getContents());
					
					ForbiddenHandler.buildgm.put(p, p.getGameMode());
					
					p.setGameMode(GameMode.CREATIVE);
					
					p.getInventory().clear();
					
					p.sendMessage(Data.PREFIX + "§7 Du bist nun im Build Modus!");
					
				}
				else if(ForbiddenHandler.build.contains(p))
				{
					
					ForbiddenHandler.build.remove(p);
					
					p.setGameMode(ForbiddenHandler.buildgm.get(p));
					
					p.getInventory().clear();
					
					p.getInventory().setContents(ForbiddenHandler.buildinv.get(p));
					
					p.sendMessage(Data.PREFIX + "§7Du bist nun nicht mehr im Build Modus!");
					
				}
				
				return true;
			}
			
			if(args.length == 1) 
			{
				Player t = Bukkit.getPlayer(args[0]);
				
				if(t == null)
				{
					p.sendMessage(Data.PREFIX + "§c Der Spieler ist nicht online!");
					return true;
				}
				
				if(!ForbiddenHandler.build.contains(t)) 
				{
					
					ForbiddenHandler.build.add(t);
					
					ForbiddenHandler.buildinv.put(t, t.getInventory().getContents());
					
					ForbiddenHandler.buildgm.put(t, t.getGameMode());
					
					t.setGameMode(GameMode.CREATIVE);
					
					t.getInventory().clear();
					
					t.sendMessage(Data.PREFIX + "§7Du bist nun im Build Modus!");
					
					p.sendMessage(Data.PREFIX + "§7Der Spieler §a" + t.getPlayerListName() +  "§7 ist nun im BuildModus");
					
				}
				else if(ForbiddenHandler.build.contains(t)) 
				{
					
					ForbiddenHandler.build.remove(t);
					
					t.setGameMode(ForbiddenHandler.buildgm.get(t));
					
					t.getInventory().clear();
					
					t.getInventory().setContents(ForbiddenHandler.buildinv.get(t));
					
					t.sendMessage(Data.PREFIX + "§7Du bist nun nicht mehr im Build Modus!");
					
					p.sendMessage(Data.PREFIX + "§7Der Spieler §a" + t.getPlayerListName() +  "§7 ist nun nicht mehr im BuildModus");
					
				}
				
				return true;
			}
		}
		else
			p.sendMessage(Data.NO_PERMS);
		
		return true;
	}

}
