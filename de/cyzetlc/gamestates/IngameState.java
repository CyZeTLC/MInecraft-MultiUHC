package de.cyzetlc.gamestates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import de.cyzetlc.Main;
import de.cyzetlc.ui.ScoreBoard;
import de.cyzetlc.utils.Data;
import de.cyzetlc.utils.GameStateBuilder;
import de.cyzetlc.utils.MapReset;
import de.cyzetlc.utils.inv.ItemBuilder;
import de.cyzetlc.utils.kit.Kit;
import de.cyzetlc.utils.manager.BorderManager;
import de.cyzetlc.utils.manager.GameStateManager;
import de.cyzetlc.utils.manager.LocationManager;
import de.cyzetlc.utils.mysql.stats.SQLStats;
import de.cyzetlc.utils.team.Team;

public class IngameState implements GameStateBuilder
{

	private static int COUNTDOWN_TIME = 35;
	public static int FALL_SECONDS = 15;
	
	public static boolean isPvPAllowed = false;
	public static boolean isFallDamage = false;
	
	public List<Block> blocks = new ArrayList<Block>();
	private BlockFace[] faces = new BlockFace[] 
	{
			BlockFace.DOWN, BlockFace.UP,
			BlockFace.WEST, BlockFace.SOUTH,
			BlockFace.EAST, BlockFace.NORTH
	};
	
	public int taskID = 10;
	public static int seconds = COUNTDOWN_TIME, minutes = 0;
	public boolean isRunning = false;
	
	public static String displayMinutes = "10";
	public static String displaySeconds = "00";
	
	public GameStateManager gs = new GameStateManager(Main.getPlugin());
	
	public static HashMap<Team, Integer> teams = new HashMap<>();

	
	public static World world = null;
	
	private BukkitTask brn;
	
	@Override
	public void start() 
	{
		startWating();
		
		for (Player all : Bukkit.getOnlinePlayers())
		{
			Data.kills.put(all, 0);
		}
		
		for (Player all : Bukkit.getOnlinePlayers())
		{
			Team team = null;
			
			for (Team t : Data.allTeams)
			{
				if (t.getPlayers().contains(all))
				{
					team = t;
				}
			}
			
			if (LocationManager.isLocationExists(team.getName()))
			{
				all.teleport(LocationManager.getLocation(team.getName()));
			}
			else
			{
				all.sendMessage(Data.TEAM_SPAWN_NOT_SET);
			}
			
			ScoreBoard.update(Main.getPlugin(), all);
			
			all.getInventory().clear();
			all.setGameMode(GameMode.SURVIVAL);
			
			if (Data.MODI == "Meetup")
			{
				all.getInventory().setContents(Kit.getKitInventory(Kit.getActiveKit()));
				all.getInventory().setArmorContents(Kit.getKitArmor(Kit.getActiveKit()));
			}
			else if (Data.MODI == "SpeedUHC")
			{
				COUNTDOWN_TIME = 00;
				seconds = COUNTDOWN_TIME;
				minutes = 10;
				
				all.getInventory().addItem(new ItemBuilder(Material.IRON_PICKAXE).addEnchantment(Enchantment.DIG_SPEED, 0).addEnchantment(Enchantment.DURABILITY, 1).addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 2).create());
				all.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0), true);
				all.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 0), true);
			}
			else if (Data.MODI == "UHC")
			{
				COUNTDOWN_TIME = 00;
				seconds = COUNTDOWN_TIME;
				minutes = 10;
			}
			
			for (Team t : Data.allTeams)
			{
				teams.put(t, t.getPlayers().size());
			}
									
			world = all.getWorld();
		}
			
		for (Player all : Bukkit.getOnlinePlayers())
		{
			ScoreBoard.update(Main.getPlugin(), all);
			if (all.getLocation().subtract(0,-1,0).getBlock().getType() == Material.GLASS)
			    addAttachedOres(all.getLocation().subtract(0, -1, 0).getBlock(), Material.GLASS);
			else if (all.getLocation().subtract(0,-2,0).getBlock().getType() == Material.GLASS)
			    addAttachedOres(all.getLocation().subtract(0, -2, 0).getBlock(), Material.GLASS);
			else if (all.getLocation().subtract(0,-3,0).getBlock().getType() == Material.GLASS)
			    addAttachedOres(all.getLocation().subtract(0, -3, 0).getBlock(), Material.GLASS);
			else if (all.getLocation().subtract(0,0,0).getBlock().getType() == Material.GLASS)
			    addAttachedOres(all.getLocation().subtract(0, 0, 0).getBlock(), Material.GLASS);
			SQLStats.addPlayed(all.getUniqueId().toString(), 1);
		}
		
		if (!MapReset.worldSaved(world.getName()))
		{
			MapReset.saveWorld(world);
		}
		
		BorderManager b = new BorderManager(world);
		b.load();
		b.start();
	}
	
	public void addAttachedOres(Block source, Material m) 
	{
		if (!(blocks.contains(source)) && source.getType() == m)
		{
			blocks.add(source);
			for (BlockFace face : faces) 
			{
				addAttachedOres(source.getRelative(face), m);
			}
		}
	}
	
	@Override
	public void stop() 
	{
		stopIdle();
	}
	
	@Override
	public void startIdle()
	{
		isRunning = true;
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() 
		{
			@Override
			public void run()
			{		
				if (minutes > 0)
				{
					if (seconds == 0)
					{
						minutes--;
						seconds = 60;
					}
					
					displayMinutes = "0" + minutes;
					
					if (seconds < 10)
					{
						displaySeconds = "0" + seconds;
					}
					else
					{
						displaySeconds = ""+seconds;
					}
					
					seconds--;
					return;
				}
				
				if (seconds < 10)
				{
					displaySeconds = "0" + seconds;
				}
				else
				{
					displaySeconds = ""+seconds;
				}
				
				switch(seconds)
				{
				case 31:
					for (Block b : blocks)
					{
						b.setType(Material.GLASS);
					}
					break;
				case 30: case 15: case 10: case 5: case 4: case 3: case 2: case 1:
					for(Player all : Bukkit.getOnlinePlayers())
					{
                 	    all.playSound(all.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 1);
					}
					Bukkit.broadcastMessage(String.format(Data.SAVE_TIME_COUNTDOWN, String.valueOf(seconds)));
					break;
				case 0:
					isPvPAllowed = true;
					isFallDamage = false;
					FALL_SECONDS = 15;
					startWating();
					for (Player all : Bukkit.getOnlinePlayers())
					{
						all.removePotionEffect(PotionEffectType.FAST_DIGGING);
						all.removePotionEffect(PotionEffectType.SPEED);
						
						Team team = Team.getTeamOfPlayer(all);
						
						if (LocationManager.isLocationExists(team.getName()))
						{
							if (Data.MODI != "Meetup")
							{
								all.teleport(LocationManager.getLocation(team.getName()));
							}
						}
						else
						{
							all.sendMessage(Data.TEAM_SPAWN_NOT_SET);
						}
					}
				    break;
				default:
			     	break;
				}
				seconds--;
			}
			
		}, 0, 20);
	}

	@Override
	public void stopIdle()
	{
		
	}
	
	public void stopWating() 
	{

	}

	@Override
	public boolean isRunning()
	{
		return isRunning;
	}

	@Override
	public void startWating() 
	{
		new BukkitRunnable()
		{

			@Override
			public void run() 
			{
				switch (FALL_SECONDS) 
				{
				case 15: case 10: case 5: case 4: case 3: case 2: case 1:
					Bukkit.broadcastMessage(String.format(Data.PLAY_TIME_STARTS, String.valueOf(FALL_SECONDS)));
					break;
				case 0:
					for (Block b : blocks)
					{
						b.setType(Material.AIR);
						for (Player all : Bukkit.getOnlinePlayers())
						{
							all.playEffect(b.getLocation(), Effect.LAVA_POP, 1);
						}
					}
					startIdle();
					break;
				case -10:
					isFallDamage = true;
					cancel();
					break;
		        default:
		        	break;
				}
				FALL_SECONDS--;
			}
			
		}.runTaskTimer(Main.getPlugin(), 0, 20);
	}

}
