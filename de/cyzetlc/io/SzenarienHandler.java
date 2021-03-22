package de.cyzetlc.io;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.cyzetlc.utils.Data;
import de.cyzetlc.utils.inv.ItemBuilder;
import de.cyzetlc.utils.szenarien.Szenarie;
import de.cyzetlc.utils.szenarien.SzenarieManager;

public class SzenarienHandler implements Listener
{
	private int x = 0;
	private int count = 0;
	private int timber = 0;
	
	private List<Block> ores = new ArrayList<>();
	private List<Block> leaves = new ArrayList<>();

	private BlockFace[] faces = new BlockFace[] 
	{
			BlockFace.DOWN, BlockFace.UP,
			BlockFace.WEST, BlockFace.SOUTH,
			BlockFace.EAST, BlockFace.NORTH
	};
	
	@EventHandler
	public void handleTimber(BlockBreakEvent e)
	{		
		if (SzenarieManager.getCurrentSzenarie().equals(Szenarie.Timber) 
				|| Data.MODI == "SpeedUHC")
		{
			if (e.getBlock().getType() == Material.LOG || e.getBlock().getType() == Material.LOG_2)
			{
				e.getBlock().getDrops().clear();
				e.getBlock().getDrops().add(new ItemStack(Material.LOG));
				leaves.clear();
				dropTree(e.getBlock().getLocation());
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void handleCut(BlockBreakEvent e)
	{
		if (SzenarieManager.getCurrentSzenarie().equals(Szenarie.CutClean)
				|| Data.MODI == "SpeedUHC")
		{
			if (e.getBlock().getType() == Material.IRON_ORE)
			{
				e.getBlock().getDrops().clear();
				e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation(), new ItemBuilder(Material.IRON_INGOT).create());
			}
			if (e.getBlock().getType() == Material.GOLD_ORE)
			{
				e.getBlock().getDrops().clear();
				e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation(), new ItemBuilder(Material.GOLD_INGOT).create());
			}
		}
	}
	
	@EventHandler
	public void handleSpeedUHC(BlockBreakEvent e)
	{
		if (Data.MODI == "SpeedUHC")
		{
			if (e.getPlayer() != null)
			{
				count = 0;
				ores.clear();
			}
			
			if (e.getBlock().getType() == Material.IRON_ORE 
					|| e.getBlock().getType() == Material.DIAMOND_ORE 
					|| e.getBlock().getType() == Material.COAL_ORE 
					|| e.getBlock().getType() == Material.LAPIS_ORE
					|| e.getBlock().getType() == Material.REDSTONE_ORE
					|| e.getBlock().getType() == Material.EMERALD_ORE)
			{
				addAttachedOres(e.getBlock(), e.getBlock().getType());
				breakOres(e.getPlayer());
				e.setExpToDrop(100);
			}
		}
		
		if (e.getBlock().getType() == Material.GRAVEL)
		{
			e.setExpToDrop(0);
			e.setCancelled(true);
			e.getBlock().setType(Material.AIR);
			
			if (new Random().nextInt(10) < 8)
			{
				e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation(), new ItemBuilder(Material.ARROW).setCount(2).create());
			}
			else
			{
				e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation(), new ItemBuilder(Material.FLINT).create());
			}
		}
	}
	
	@EventHandler
	public void handleClean(EntityDeathEvent e)
	{
		if (SzenarieManager.getCurrentSzenarie().equals(Szenarie.CutClean)
				|| Data.MODI == "SpeedUHC")
		{
			if (e.getEntity() instanceof Player) return;
			e.getDrops().clear();
			
			if (e.getEntity() instanceof Cow)
			{
				e.getDrops().add(new ItemStack(Material.COOKED_BEEF));
			}
			if (e.getEntity() instanceof Pig)
			{
				e.getDrops().add(new ItemStack(Material.GRILLED_PORK));
			}
			if (e.getEntity() instanceof Rabbit)
			{
				e.getDrops().add(new ItemStack(Material.COOKED_RABBIT));
			}
			if (e.getEntity() instanceof Sheep)
			{
				e.getDrops().add(new ItemStack(Material.COOKED_MUTTON));
			}
			if (e.getEntity() instanceof Zombie)
			{
				if (new Random().nextInt(10) < 8)
				{
					e.getDrops().add(new ItemStack(Material.GOLDEN_APPLE));
				}
				e.getDrops().add(new ItemStack(Material.ROTTEN_FLESH));
			}
		}
	}
	
	@EventHandler
	public void handleDoubleGold(BlockBreakEvent e)
	{
		if (SzenarieManager.getCurrentSzenarie().equals(Szenarie.DoubleGold))
		{
			if (e.getBlock().getType() == Material.GOLD_ORE)
			{
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
				e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation(), new ItemBuilder(Material.GOLD_INGOT).setCount(2).create());
			}
		}
	}
	
	@EventHandler
	public void handleRodLess(PlayerInteractEvent e)
	{
		try
		{
			if (SzenarieManager.getCurrentSzenarie().equals(Szenarie.Rodless))
			{
				if (e.getItem().getType() == Material.FISHING_ROD)
				{
					e.setCancelled(true);
				}
			}
		}
		catch (Exception e1)
		{
			
		}
	}
	
	@EventHandler
	public void handleAntiFall(EntityDamageEvent e)
	{
		if (SzenarieManager.getCurrentSzenarie().equals(Szenarie.NoFall))
		{
			if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL))
			{
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void handleBowless(EntityDamageByEntityEvent e)
	{
		if (SzenarieManager.getCurrentSzenarie().equals(Szenarie.Bowless))
		{
			if (e.getDamager() instanceof Arrow)
			{
				e.setCancelled(true);
			}
		}
	}
	  
	@EventHandler
	public void handleDiamondless(BlockBreakEvent e)
	{
		if (SzenarieManager.getCurrentSzenarie().equals(Szenarie.Diamondless))
		{
			if (e.getBlock().getType().equals(Material.DIAMOND_ORE))
			{
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
			}
		}
	}
	
	@EventHandler
	public void handleFireless(EntityDamageEvent event) 
	{
		if (SzenarieManager.getCurrentSzenarie().equals(Szenarie.Fireless)) 
		{
			if ((event.getEntity() instanceof Player)) 
			{
				if ((event.getCause() == EntityDamageEvent.DamageCause.FIRE)
						|| (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK)
						|| (event.getCause() == EntityDamageEvent.DamageCause.LAVA)) 
				{
					this.x += 1;
					event.setCancelled(true);

					if (this.x >= 200) 
					{
						this.x = 0;
					}
				}
			}
		}
	}
	
    public void addAttachedOres(Block source, Material m)
    {
        if(!(ores.contains(source)) && source.getType() == m)
        {	
            ores.add(source);
            for(BlockFace face: faces)
            {
                addAttachedOres(source.getRelative(face), m);
            }
        }
    }
	
	private void breakOres(Player p) 
	{
		for (Block block : ores) 
		{
			for (Player all : Bukkit.getOnlinePlayers())
			{
				all.playSound(block.getLocation(), Sound.DIG_STONE, 1, 1);
			}
			
			if (block.getType() == Material.IRON_ORE)
			{
				block.getDrops().clear();
				block.getLocation().getWorld().dropItem(p.getLocation(), new ItemBuilder(Material.IRON_INGOT).create());
			}
			else
			{
				block.breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE));
				for (ItemStack is : block.getDrops())
				{
					block.getLocation().getWorld().dropItem(p.getLocation(), is);
				}
				block.getDrops().clear();
			}
		}
	}
	
    public void addAttachedLeaves(Block source, Material m, Material m2)
    {
        if(!(leaves.contains(source)) && (source.getType() == m || source.getType() == m2))
        {
        	if (timber == 32)
        	{
        		return;
        	}
        	
            leaves.add(source);
            timber++;
            for(BlockFace face: faces)
            {
            	addAttachedLeaves(source.getRelative(face), m, m2);
            }
        }
    }
	
	private void dropTree(final Location location) 
	{
		List<Block> blocks = new LinkedList<>();
		Location old = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());

		for (int i = location.getBlockY(); i < location.getWorld().getHighestBlockYAt(location.getBlockX(), location.getBlockZ()); i++) 
		{
			Location l = location.add(0, 1, 0);
			if (l.getBlock().getType() == Material.LOG || l.getBlock().getType() == Material.LOG_2)
				blocks.add(l.getBlock());
			else
				break;

			l = null;
		}
		
		for (int i = old.getBlockY(); i > 0; i--) 
		{
			Location l = old;
			l.setY(i);
			if (l.getBlock().getType() == Material.LOG || l.getBlock().getType() == Material.LOG_2)
				blocks.add(l.getBlock());
			else
				break;

			l = null;
		}
		
		Location topBlock = new Location (Bukkit.getWorld("wolrd"),0,0,0);
		
		for (Block block : blocks) 
		{
			block.setType(Material.LOG);
			for (Player all : Bukkit.getOnlinePlayers())
			{
				all.playSound(block.getLocation(), Sound.DIG_WOOD, 1, 1);
			}
			
			if (block.getY() >= topBlock.getY())
			{
				topBlock = block.getLocation();
			}
			
			block.breakNaturally(new ItemStack(Material.DIAMOND_AXE));
		}
				
		addAttachedLeaves(topBlock.add(0,1,0).getBlock(), Material.LEAVES, Material.LEAVES_2);
		timber = 0;
		
	    boolean breaks = false;
	    
		for (Block block : leaves) 
		{
			for (Player all : Bukkit.getOnlinePlayers())
			{
				all.playSound(block.getLocation(), Sound.DIG_GRASS, 1, 1);
			}
			block.breakNaturally(new ItemStack(Material.SHEARS));
			
			if (!breaks)
			{
				if (new Random().nextInt(10) < 8)
				{
					block.getWorld().dropItem(block.getLocation(), new ItemBuilder(Material.APPLE).create());
				}
				else if (new Random().nextInt(10) > 2)
				{
					block.getWorld().dropItem(block.getLocation(), new ItemBuilder(Material.GOLDEN_APPLE).create());
				}
				breaks = true;
			}
		}
		
		leaves.clear();
		
		blocks = null;
	}
}
