package de.cyzetlc.utils.kit;

import java.io.File;
import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.cyzetlc.Main;

public class Kit 
{
	public static FileConfiguration cfg;
	public static File f;
	
	public Kit()
	{
		f = new File(Main.getPlugin().getDataFolder(), "kits.yml");
		cfg = YamlConfiguration.loadConfiguration(f);
		
		if (!f.exists())
		{
			try { f.createNewFile(); cfg.set("default", "Default"); cfg.save(f); } catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	public static void setActiveKit(String kit)
	{
		try
		{
			cfg.set("default", kit);
			cfg.save(f);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static String getActiveKit()
	{
		return cfg.getString("default");
	}
	
	public static boolean isKitExists(String kit)
	{
		if (cfg.getString(kit) != null)
		{
			return true;
		}
		return false;
	}
	
	public static void saveInventoryToKit(String kit, ItemStack[] items, ItemStack[] armor) 
	{
		ItemStack[] contents = items;
		for (int i = 0; i < 36; i++)
		{
			if ((contents[i] != null) && (contents[i].getType() != Material.AIR)) 
			{
				cfg.set(kit + ".items." + i, contents[i]);
			} 
			else 
			{
				cfg.set(kit + ".items." + i, null);
			}
		}
		try 
		{
			cfg.save(f);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		saveArmorToKit(kit, armor);
	}
	
	private static void saveArmorToKit(String kit, ItemStack[] items) 
	{
		ItemStack[] contents = items;
		for (int i = 0; i < 4; i++)
		{
			if ((contents[i] != null) && (contents[i].getType() != Material.AIR)) 
			{
				cfg.set(kit + ".armor." + i, contents[i]);
			} 
			else 
			{
				cfg.set(kit + ".armor." + i, null);
			}
		}
		try 
		{
			cfg.save(f);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public static ItemStack[] getKitArmor(String kit) 
	{
		ItemStack[] contents = new ItemStack[4];
		for (int i = 0; i < contents.length; i++)
		{
			ItemStack inv = cfg.getItemStack(kit + ".armor." + i, contents[i]);
			if ((inv != null) && (inv.getAmount() != 0)) 
			{
				contents[i] = inv;
			} else 
				if (inv != null)
			{
				if (inv.getAmount() == 0)
				{
					inv.setAmount(1);
					contents[i] = inv;
				}
			}
		}
		return contents;
	}
	
	public static ItemStack[] getKitInventory(String kit) 
	{
		ItemStack[] contents = new ItemStack[36];
		for (int i = 0; i < contents.length; i++)
		{
			ItemStack inv = cfg.getItemStack(kit + ".items." + i, contents[i]);
			if ((inv != null) && (inv.getAmount() != 0)) 
			{
				contents[i] = inv;
			} else 
				if (inv != null)
			{
				if (inv.getAmount() == 0)
				{
					inv.setAmount(1);
					contents[i] = inv;
				}
			}
		}
		return contents;
	}
}
