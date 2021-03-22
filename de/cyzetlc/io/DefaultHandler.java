package de.cyzetlc.io;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import de.cyzetlc.Main;
import de.cyzetlc.gamestates.IngameState;
import de.cyzetlc.ui.ActionBar;
import de.cyzetlc.utils.Data;
import de.cyzetlc.utils.GameState;
import de.cyzetlc.utils.team.Team;

public class DefaultHandler implements Listener
{
	@EventHandler
	public void handleMove(PlayerMoveEvent e)
	{
		if (Main.getPlugin().currentGS.equals(GameState.INGAME))
		{
			Player p = e.getPlayer();
			Team playerTeam = null;
			int distance = 1000;

			for (Team team : Data.allTeams) 
			{
				if (team.getPlayers().contains(p)) 
				{
					playerTeam = team;
				}
			}

			for (Player all : Bukkit.getOnlinePlayers())
			{
				Team otherTeam = null;

				for (Team team : Data.allTeams)
				{
					if (team.getPlayers().contains(all)) 
					{
						otherTeam = team;
					}
				}

				if (!playerTeam.equals(otherTeam) && !all.equals(p)) 
				{
					if (all.getLocation().distance(p.getLocation()) < distance)
					{
						distance = (int) all.getLocation().distance(p.getLocation());
					}
				}
			}
		
			if (Data.MODI != "Meetup")
			{
				if (!IngameState.isPvPAllowed)
				{
					ActionBar.sendActionbar(0, 20, 0, p, "§7Schutzzeit: §e" + IngameState.displayMinutes + ":" + IngameState.displaySeconds + "s");
					return;
				}
			}
			
			ActionBar.sendActionbar(0, 20, 0, p, "§7Nächster Spieler in §e" + distance + "m");
		}
	}
}
