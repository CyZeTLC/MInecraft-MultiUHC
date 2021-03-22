package de.cyzetlc.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.cyzetlc.Main;
import de.cyzetlc.utils.Data;
import de.cyzetlc.utils.GameState;
import de.cyzetlc.utils.team.Team;

public class Cmd_Ec implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) 
	{
		Player p = (Player) s;
		
		if (!Main.getPlugin().currentGS.equals(GameState.INGAME))
		{
			p.sendMessage(Data.PREFIX + "§7Die Runde ist noch §cnicht §7gestartet!");
			return true;
		}
		
		if (!Team.isPlayerInTeam(p))
		{
			p.sendMessage(Data.PREFIX + "§7Du musst in einem Team sein!");
			return true;
		}
		
		
		p.openInventory(Team.getTeamOfPlayer(p).getInv());
		
		return false;
	}

}
