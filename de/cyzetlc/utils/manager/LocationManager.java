package de.cyzetlc.utils.manager;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.cyzetlc.Main;

public class LocationManager 
{
	public static File f;
	public static FileConfiguration cfg;
	
	public LocationManager()
	{
		f = new File(Main.getPlugin().getDataFolder(), "locs.yml");
		cfg = YamlConfiguration.loadConfiguration(f);
		
		if (!f.exists())
		{
			try { f.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	public static void setLocation(Location loc, String name)
	{
		cfg.set(name + ".world", loc.getWorld().getName());
		cfg.set(name + ".x", loc.getX());
		cfg.set(name + ".y", loc.getY());
		cfg.set(name + ".z", loc.getZ());
		cfg.set(name + ".yaw", loc.getYaw());
		cfg.set(name + ".pitch", loc.getPitch());
		save();
	}
	
	public static Location getLocation(String name)
	{
		return new Location(Bukkit.getWorld(cfg.getString(name + ".world")),
				            cfg.getDouble(name + ".x"),
				            cfg.getDouble(name + ".y"),
				            cfg.getDouble(name + ".z"),
				            (float) cfg.getDouble(name + ".yaw"),
				            (float) cfg.getDouble(name + ".pitch"));
	}
	
	public static boolean isLocationExists(String name)
	{
		if (cfg.getString(name) != null)
		{
			return true;
		}
		return false;
	}
	
	private static void save()
	{
		try { cfg.save(f); } catch (Exception e) { e.printStackTrace(); }
	}
}
