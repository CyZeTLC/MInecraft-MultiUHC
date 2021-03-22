package de.cyzetlc.utils;

import java.io.File;
import java.util.ArrayList;
 
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
 
import com.google.common.io.Files;

import de.cyzetlc.Main;

public class MapReset {

	 public static void kickPlayers(World world) 
	 {
	        for(Player pl : world.getPlayers()) 
	        {
	            pl.teleport(Bukkit.getWorld("world").getSpawnLocation());
	        }
	    }
	   
	  public static Boolean worldSaved(String worldName)
	  {
	    File worldFolder = new File(Main.getPlugin().getDataFolder(), "worlds/" + worldName);
	    if (worldFolder.exists()) 
	    {
	      return Boolean.valueOf(true);
	    }
	    return Boolean.valueOf(false);
	  }
	 
	  public static void deleteWorldSave(String worldName)
	  {
	    File worldFolder = new File(Main.getPlugin().getDataFolder(), "worlds/" + worldName);
	    if (worldFolder.exists())
	      deleteFolder(worldFolder);
	  }
	 
	  public static void saveWorld(World world)
	  {
	    if (world != null) 
	    {
	      world.save();
	      File worldFolder = new File(Main.getPlugin().getDataFolder(), "worlds/" + world.getName());
	      File srcWorldFolder = new File(world.getName());
	      if (worldFolder.exists())
	      {
	        deleteFolder(worldFolder);
	      }
	      copyWorldFolder(srcWorldFolder, worldFolder);
	      FileConfiguration settings = YamlConfiguration.loadConfiguration(new File(worldFolder, "WorldSettings.yml"));
	      settings.set("World.Seed", Long.valueOf(world.getSeed()));
	      settings.set("World.Environment", world.getEnvironment().toString());
	      settings.set("World.Structures", Boolean.valueOf(world.canGenerateStructures()));
	      settings.set("World.Generator", getChunkGeneratorAsName(world));
	      settings.set("World.Type", world.getWorldType().toString());
	      try 
	      {
	        settings.save(new File(worldFolder, "WorldSettings.yml"));
	      }
	      catch (Exception e)
	      {
	        e.printStackTrace();
	        System.out.println("Es konnte kein Weltsave File erstellt werden!");
	      }
	    }
	  }
	 
	  public static void resetWorld(World world)
	  {
	    File srcWorldFolder = new File(Main.getPlugin().getDataFolder(), "worlds/" + world.getName());
	    File worldFolder = new File(world.getName());
	    if ((srcWorldFolder.exists()) &&
	      (worldFolder.exists()))
	      if (world.getName().equals("world"))
	      {
	        System.out.println("Die Hautpwelt kann nicht resettet werden!");
	      }
	      else
	      {
	        kickPlayers(world);
	        Boolean saveSett = Boolean.valueOf(false);
	        Long seed = null;
	        World.Environment environment = null;
	        Boolean structures = null;
	        String generator = null;
	        WorldType worldType = null;
	        File settingsFile = new File(srcWorldFolder, "WorldSettings.yml");
	        FileConfiguration settings = YamlConfiguration.loadConfiguration(settingsFile);
	        if ((settingsFile.exists()) && (settings.get("World.Seed") != null)) {
	          seed = Long.valueOf(settings.getLong("World.Seed"));
	          environment = World.Environment.valueOf(settings.getString("World.Environment"));
	          structures = Boolean.valueOf(settings.getBoolean("World.Structures"));
	          generator = settings.getString("World.Generator");
	          worldType = WorldType.valueOf(settings.getString("World.Type"));
	        } 
	        else
	        {
	          seed = Long.valueOf(world.getSeed());
	          environment = world.getEnvironment();
	          structures = Boolean.valueOf(world.canGenerateStructures());
	          generator = getChunkGeneratorAsName(world);
	          worldType = world.getWorldType();
	          settings.set("World.Seed", Long.valueOf(world.getSeed()));
	          settings.set("World.Environment", world.getEnvironment().toString());
	          settings.set("World.Structures", Boolean.valueOf(world.canGenerateStructures()));
	          settings.set("World.Generator", getChunkGeneratorAsName(world));
	          settings.set("World.Type", world.getWorldType().toString());
	          saveSett = Boolean.valueOf(true);
	        }
	       
	        Bukkit.getServer().unloadWorld(world, true);
	 
	        WorldCreator w = new WorldCreator(world.getName());
	        deleteFolder(worldFolder);
	        copyWorldFolder(srcWorldFolder, worldFolder);
	        if (saveSett.booleanValue())
	        {
	          try 
	          {
	            settings.save(settingsFile);
	          }
	          catch (Exception e)
	          {
	            e.printStackTrace();
	            System.out.println("Der WorldSettings File konnte nicht erstellt werden!");
	          }
	        }
	        w.seed(seed.longValue());
	        w.environment(environment);
	        w.generateStructures(structures.booleanValue());
	        w.generator(generator);
	        w.type(worldType);
	      }
	  }
	 
	  private static void deleteFolder(File folder)
	  {
	    File[] files = folder.listFiles();
	    if (files != null) 
	    {
	      for (File file : files) 
	      {
	        if (file.isDirectory())
	          deleteFolder(file);
	        else 
	        {
	          file.delete();
	        }
	      }
	    }
	    folder.delete();
	  }
	 
	  private static String getChunkGeneratorAsName(World world)
	  {
	    String generator = null;
	    for (Plugin plugin : Bukkit.getPluginManager().getPlugins())
	    {
	      WorldCreator wc = new WorldCreator("ThisMapWillNeverBeCreated");
	      wc.generator(plugin.getName());
	      if ((wc.generator() != null) && (world.getGenerator() != null) && (wc.generator().getClass().getName().equals(world.getGenerator().getClass().getName())))
	      {
	        generator = plugin.getName();
	      }
	    }
	    return generator;
	  }
	 
	  private static void copyWorldFolder(File from, File to) 
	  {
	    try {
	      ArrayList<String> ignore = new ArrayList<>();
	      ignore.add("session.dat");
	      ignore.add("session.lock");
	      ignore.add("WorldSettings.yml");
	      if (!ignore.contains(from.getName()))
	        if (from.isDirectory())
	        {
	          if (!to.exists()) 
	          {
	            to.mkdirs();
	          }
	          String[] files = from.list();
	          for (String file : files) 
	          {
	            File srcFile = new File(from, file);
	            File destFile = new File(to, file);
	            copyWorldFolder(srcFile, destFile);
	          }
	        } 
	        else 
	        {
	          Files.copy(from, to);
	        }
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	  }

}
