package de.cyzetlc.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.cyzetlc.Main;
import de.cyzetlc.utils.Data;
import de.cyzetlc.utils.GameState;

public class Cmd_Start implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) 
	{
		if (!(s instanceof Player))
		{
			return true;
		}
		
		Player p = (Player) s;
		
		if (!p.hasPermission(Data.MEETUP_START))
		{
			p.sendMessage(Data.NO_PERMS);
			return true;
		}
		
		if (!Main.getPlugin().gsManager.getCurrentGameState().equals(GameState.LOBBY))
		{
			p.sendMessage(Data.COMMAND_START_WAITING);
			return true;
		}
		
		if (Data.players.size() < 2)
		{
			p.sendMessage(Data.COMMAND_START_PLAYERS);
			return true;
		}
		
		if(Main.getPlugin().ls.isWating)
		{
			Main.getPlugin().ls.stopWating();
		}
		if(!(Main.getPlugin().ls.isRunning()))
		{
			Main.getPlugin().ls.stopIdle();
			Main.getPlugin().ls.startIdle();
		}
		
		return false;
	}

}
