package de.cyzetlc.io;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import de.cyzetlc.Main;
import de.cyzetlc.gamestates.EndingState;
import de.cyzetlc.gamestates.IngameState;
import de.cyzetlc.ui.ScoreBoard;
import de.cyzetlc.utils.Data;
import de.cyzetlc.utils.Hologram;
import de.cyzetlc.utils.manager.LocationManager;
import de.cyzetlc.utils.mysql.stats.SQLStats;
import de.cyzetlc.utils.szenarien.Szenarie;
import de.cyzetlc.utils.szenarien.SzenarieManager;
import de.cyzetlc.utils.team.Team;

public class DeathHandler implements Listener
{
	public static HashMap<Player, Location> respawnLocs = new HashMap<Player, Location>();
	public static HashMap<Location, Integer> chests = new HashMap<Location, Integer>();
	
	@EventHandler
	public void handleDeath(PlayerDeathEvent e)
	{
		Player p = e.getEntity();
		
		if (Data.spectators.contains(p))
		{
			e.setDeathMessage(null);
			e.getDrops().clear();
			respawnLocs.put(p, LocationManager.getLocation("T1"));
			p.spigot().respawn();
			return;
		}
		
		if (p.getKiller() == null)
		{
			e.setDeathMessage(String.format(Data.PLAYER_DEATH_PUBLIC, p.getName()));
		}
		else
		{
			e.setDeathMessage(Data.PLAYER_DEATH_BY_PLAYER_PUBLIC.replace("%p", p.getName()).replace("%k", p.getKiller().getName()));
			p.sendMessage(Data.PLAYER_DEATH.replace("%k", p.getKiller().getName()).replace("%l", String.valueOf((int) p.getKiller().getHealth())));
			p.getKiller().sendMessage(Data.PLAYER_KILLED.replace("%p", p.getName()));
			SQLStats.addKills(p.getKiller().getUniqueId().toString(), 1);
			Data.kills.put(p.getKiller(), Data.kills.get(p.getKiller())+1);	
		}
		
		Data.players.remove(p);
		SQLStats.addDeaths(p.getUniqueId().toString(), 1);
		
		for (Player all : Bukkit.getOnlinePlayers())
		{
			all.playSound(all.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
			all.hidePlayer(p);
			ScoreBoard.update(Main.getPlugin(), all);
		}
		
		respawnLocs.put(p, p.getLocation());
		
		if (SzenarieManager.getCurrentSzenarie() != null)
		{
			if (SzenarieManager.getCurrentSzenarie().equals(Szenarie.TimeBomb))
			{
				p.getLocation().getBlock().setType(Material.CHEST);
				p.getLocation().subtract(1, 0, 0).getBlock().setType(Material.CHEST);
				Chest c = (Chest) p.getLocation().getBlock().getState();
				
				for (ItemStack is : e.getDrops())
				{
					c.getInventory().addItem(is);
				}
				
				Location loc = new Location(c.getLocation().getWorld(), c.getLocation().getX() - 1.0, c.getLocation().getY() - 1, c.getLocation().getZ() + 0.5);
				chests.put(loc, 45);
				
		        new BukkitRunnable()
		        {

					@Override
					public void run()
					{
						Integer sec = chests.get(loc);
						String[] txt = new String[] { String.valueOf("§6"+sec+"s") };
						Hologram holo = new Hologram(txt, loc);

						for (Player all : Bukkit.getOnlinePlayers())
						{
					        holo.showAllTemp(all, 20);
						}
						
				        if (sec == 0)
				        {
				        	chests.remove(loc);
				        	c.getInventory().clear();
				        	c.getLocation().subtract(1, 0, 0).getBlock().setType(Material.AIR);
				        	c.getLocation().getBlock().setType(Material.AIR);
				        	
				        	for (Player all : Bukkit.getOnlinePlayers())
				        	{
				        		if (all.getLocation().distance(c.getLocation()) < 10)
				        		{
				        			all.playSound(all.getLocation(), Sound.ITEM_PICKUP, 1, 1);
				        		}
				        	}
				        	
				        	cancel();
				        }
				        else
				        {
				        	sec--;
					        chests.put(loc, sec);
				        }
					}
		        	
		        }.runTaskTimer(Main.getPlugin(), 0, 20);
		          
				e.getDrops().clear();
			}
		}
		
	    if (Data.players.size() == 1)
	    {
	    	EndingState.endGame();
	    }
	    else
	    {
		    int livingTeams = 0;
		    
		    for (Team t : Data.allTeams)
		    {
		    	int livingPlayers = 0;
		    	for (Player a : t.getPlayers())
		    	{
		    		if (Data.players.contains(a))
		    		{
		    			livingPlayers++;
		    		}
		    	}
		    	
		    	if (livingPlayers > 0)
		    	{
		    		livingTeams++;
		    	}
		    }
		    
		    if (livingTeams == 1)
		    {
		    	EndingState.endGame();
		    }
	    }
		
		p.spigot().respawn();
	}
	
	@EventHandler
	public void handleRespawn(PlayerRespawnEvent e)
	{
		Player p = e.getPlayer();
		
		e.getPlayer().setVelocity(new Vector(0,0,0));
		e.getPlayer().getInventory().clear();
		e.getPlayer().setGameMode(GameMode.SPECTATOR);
		e.setRespawnLocation(respawnLocs.get(e.getPlayer()));
		
		if (Data.spectators.contains(p)) return;
		
		new BukkitRunnable()
		{

			@Override
			public void run() 
			{
				p.kickPlayer(Data.PREFIX + "§7Du bist gestorben!");
			}
			
		}.runTaskLater(Main.getPlugin(), 20*20);
	}
}
