package de.cyzetlc.cmd;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.cyzetlc.utils.Data;
import de.cyzetlc.utils.mysql.MySQL;
import de.cyzetlc.utils.mysql.stats.SQLStats;

public class Cmd_Stats implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) 
	{
		if (!(s instanceof Player))
		{
			return true;
		}
		
		Player p = (Player) s;
		
		if (args.length == 0)
		{
			int kills = SQLStats.getKills(p.getUniqueId().toString()).intValue();
			int deaths = SQLStats.getDeaths(p.getUniqueId().toString()).intValue();
			int wins = SQLStats.getWins(p.getUniqueId().toString()).intValue();
			int played = SQLStats.getPlayed(p.getUniqueId().toString()).intValue();
			int rank = MySQL.getRank(p.getUniqueId().toString());
			double kd = kills;
			if (deaths > 0)
			{
				kd = Math.round(kills / deaths);
			}
			
			p.sendMessage(Data.STATS_FORMAT
					.replace("%name%", p.getName())
					.replace("%Kills%", String.valueOf(kills))
					.replace("%wins%", String.valueOf(wins))
					.replace("%gs%", String.valueOf(played))
					.replace("%deaths%", String.valueOf(deaths))
					.replace("%rank%", String.valueOf(rank))
					.replace("%kd%", String.valueOf(kd)));
			return true;
		}
		
		if (args.length > 0)
		{
			OfflinePlayer t = Bukkit.getOfflinePlayer(args[0]);
			
			if (!SQLStats.playerExists(t.getUniqueId().toString()))
			{
				p.sendMessage(Data.NO_STATS_FOUND);
				return true;
			}
			
			int kills = SQLStats.getKills(t.getUniqueId().toString()).intValue();
			int deaths = SQLStats.getDeaths(t.getUniqueId().toString()).intValue();
			int wins = SQLStats.getWins(t.getUniqueId().toString()).intValue();
			int played = SQLStats.getPlayed(t.getUniqueId().toString()).intValue();
			int rank = MySQL.getRank(t.getUniqueId().toString());
			double kd = kills;
			if (deaths > 0)
			{
				kd = Math.round(kills / deaths);
			}			
			p.sendMessage(Data.STATS_FORMAT
					.replace("%name%", t.getName())
					.replace("%Kills%", String.valueOf(kills))
					.replace("%wins%", String.valueOf(wins))
					.replace("%gs%", String.valueOf(played))
					.replace("%deaths%", String.valueOf(deaths))
					.replace("%rank%", String.valueOf(rank))
					.replace("%kd%", String.valueOf(kd)));
			return true;
		}
		
		return false;
	}

}
