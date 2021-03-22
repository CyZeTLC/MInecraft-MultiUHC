package de.cyzetlc.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

import de.cyzetlc.Main;
import de.cyzetlc.gamestates.IngameState;
import de.cyzetlc.ui.ActionBar;
import de.cyzetlc.utils.Data;
import de.cyzetlc.utils.GameState;
import de.cyzetlc.utils.manager.LocationManager;
import de.cyzetlc.utils.team.Team;

public class ForbiddenHandler implements Listener
{
	public static final List<Player> build = new ArrayList<Player>();
	public static final HashMap<Player, ItemStack[]> buildinv = new HashMap<Player, ItemStack[]>();
	public static final HashMap<Player, GameMode> buildgm = new HashMap<Player, GameMode>();

	private final Main plugin;
	
	public static HashMap<Player, Integer> setup = new HashMap<Player, Integer>();
	
	public ForbiddenHandler(Main plugin) 
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void handleWeather(WeatherChangeEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void handleRegain(EntityRegainHealthEvent e)
	{
		if (Data.MODI == "SpeedUHC")
		{
			if (e.getRegainReason().equals(RegainReason.REGEN))
			     e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void handleHunger(FoodLevelChangeEvent e)
	{
		if (plugin.gsManager.getCurrentGameState().equals(GameState.INGAME))
		{
			if (IngameState.isPvPAllowed) return;
		}
		e.setCancelled(true);
	}
	
	@EventHandler
	public void handleDrop(PlayerDropItemEvent e)
	{
		if (plugin.gsManager.getCurrentGameState().equals(GameState.INGAME)) return;
		e.setCancelled(true);
	}
	
	@EventHandler
	public void handleDamage(EntityDamageEvent e)
	{
		if (plugin.gsManager.getCurrentGameState().equals(GameState.INGAME))
		{
			if (e.getEntity() instanceof Player)
			{
				Player p = (Player) e.getEntity();

				if (IngameState.isPvPAllowed && IngameState.isFallDamage)
				{
					if (e instanceof EntityDamageByEntityEvent)
					{
						EntityDamageByEntityEvent e1 = (EntityDamageByEntityEvent) e;

						if (e1.getDamager() instanceof Arrow)
						{
							Arrow ar = (Arrow) e.getEntity();
							
							if (ar.getShooter() instanceof Player)
							{
								Player t = (Player) ar.getShooter();
								for (Team team : Data.allTeams)
								{
									if (team.getPlayers().contains(p) && team.getPlayers().contains(t))
									{
										e.setCancelled(true);
										return;
									}
								}
							}
							return;
						}
						
						Team t = null;
						Team t1 = null;
						
						for (Team team : Data.allTeams)
						{
							if (team.getPlayers().contains(e1.getDamager()))
							{
								t = team;
							}
							if (team.getPlayers().contains(p))
							{
								t1 = team;
							}
						}
						
						if (t != null)
						{
							if (t.equals(t1))
							{
								e.setCancelled(true);
								return;
							}
						}
					}
					return;
				}
				else
				{
					if (Data.MODI != "Meetup" && IngameState.isFallDamage)
					{
						if (e.getEntity() instanceof Player)
						{
							if (e instanceof EntityDamageByEntityEvent)
							{
								EntityDamageByEntityEvent e1 = (EntityDamageByEntityEvent) e;
								
								if (e1.getDamager() instanceof Player)
								{
									e.setCancelled(true);
								}
							}
						}
						else
						{
							e.setCancelled(false);
						}
						return;
					}
				}
				e.setCancelled(true);
			}
		}
		else
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void handleInteract(PlayerInteractEvent e)
	{
		if (build.contains(e.getPlayer())) return;
		if (plugin.gsManager.getCurrentGameState().equals(GameState.INGAME)
				|| setup.containsKey(e.getPlayer())) return;
		e.setCancelled(true);
	}
	
	@EventHandler
	public void handleBuild(BlockPlaceEvent e)
	{
		if (e.getPlayer().getLocation().getY() > 100)
		{
			e.setCancelled(true);
		}
		
		if (build.contains(e.getPlayer())) return;
		if (!Data.MODI.equals("Meetup")) return;
		if (plugin.gsManager.getCurrentGameState().equals(GameState.INGAME) && IngameState.isPvPAllowed) return;
		e.setCancelled(true);
		e.getPlayer().sendMessage(String.format(Data.SAVE_TIME_ACTIVE, e.getBlock().getType()));
	}
	
	@EventHandler
	public void handleBreak(BlockBreakEvent e)
	{
		if (e.getBlock().getType() == Material.GLASS)
		{
			if (e.getPlayer().getLocation().getY() > 100)
			{
				e.setCancelled(true);
			}
		}
		
		if (build.contains(e.getPlayer())) return;
		if (!Data.MODI.equals("Meetup")) return;
		if (setup.containsKey(e.getPlayer()))
		{
			e.setCancelled(true);
			
			LocationManager.setLocation(e.getBlock().getLocation(), "S" + setup.get(e.getPlayer()));
			
			e.getPlayer().sendMessage(String.format(Data.SKULL_SET, setup.get(e.getPlayer())));

			setup.remove(e.getPlayer());
						
			return;
		}
		
		if (plugin.gsManager.getCurrentGameState().equals(GameState.INGAME) && IngameState.isPvPAllowed) return;
		e.setCancelled(true);
		e.getPlayer().sendMessage(String.format(Data.SAVE_TIME_ACTIVE, e.getBlock().getType()));
	}
}
