package de.cyzetlc;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.cyzetlc.cmd.Cmd_Build;
import de.cyzetlc.cmd.Cmd_Ec;
import de.cyzetlc.cmd.Cmd_Kit;
import de.cyzetlc.cmd.Cmd_SZ;
import de.cyzetlc.cmd.Cmd_Setspawn;
import de.cyzetlc.cmd.Cmd_Start;
import de.cyzetlc.cmd.Cmd_Stats;
import de.cyzetlc.cmd.Cmd_To;
import de.cyzetlc.gamestates.IngameState;
import de.cyzetlc.gamestates.LobbyState;
import de.cyzetlc.io.ConnectionHandler;
import de.cyzetlc.io.DeathHandler;
import de.cyzetlc.io.ForbiddenHandler;
import de.cyzetlc.io.LobbyHandler;
import de.cyzetlc.io.SzenarienHandler;
import de.cyzetlc.utils.GameState;
import de.cyzetlc.utils.GameStateBuilder;
import de.cyzetlc.utils.MapReset;
import de.cyzetlc.utils.kit.Kit;
import de.cyzetlc.utils.manager.GameStateManager;
import de.cyzetlc.utils.manager.LocationManager;
import de.cyzetlc.utils.manager.RandomFactsManager;
import de.cyzetlc.utils.manager.RankFileManager;
import de.cyzetlc.utils.manager.SZVoteManager;
import de.cyzetlc.utils.mysql.MySQL;
import de.cyzetlc.utils.mysql.stats.Ranging;

public class Main extends JavaPlugin
{
	/*
	 * Managers
	 */
	private static RankFileManager rank;
	private static Main plugin;
	
	/*
	 * MySQL
	 */
	public static MySQL mySQL;
	
	/*
	 * Gamestates
	 */
	public static GameStateBuilder builder;
	public static GameState currentGS;
	public static GameStateManager gsManager;
	public static LobbyState ls;
	
	public void onEnable()
	{		
		this.plugin = this;
		this.gsManager = new GameStateManager(this);
		this.currentGS = GameState.LOBBY;
		this.ls = new LobbyState();
		this.mySQL = MySQL.initMySQL();
		this.builder = this.ls;
		this.builder.start();
		this.rank = new RankFileManager(this);
		
		new LocationManager();
		new SZVoteManager(this);
		new Kit();
		new RandomFactsManager(this).start();
		
		this.getCommand("setspawn").setExecutor(new Cmd_Setspawn());
		this.getCommand("setspawn").setTabCompleter(new Cmd_Setspawn());
		this.getCommand("kit").setExecutor(new Cmd_Kit());
		this.getCommand("stats").setExecutor(new Cmd_Stats());
		this.getCommand("to").setExecutor(new Cmd_To());
		this.getCommand("sz").setExecutor(new Cmd_SZ());
		this.getCommand("start").setExecutor(new Cmd_Start());
		this.getCommand("build").setExecutor(new Cmd_Build());
		this.getCommand("ec").setExecutor(new Cmd_Ec());
		this.getCommand("bp").setExecutor(new Cmd_Ec());
		this.getCommand("backpack").setExecutor(new Cmd_Ec());
		
		Bukkit.getPluginManager().registerEvents(new ConnectionHandler(this), this);
		Bukkit.getPluginManager().registerEvents(new LobbyHandler(), this);
		Bukkit.getPluginManager().registerEvents(new ForbiddenHandler(this), this);
		Bukkit.getPluginManager().registerEvents(new DeathHandler(), this);
		Bukkit.getPluginManager().registerEvents(new SzenarienHandler(), this);
		
		this.mySQL.update("CREATE TABLE IF NOT EXISTS meetup(UUID varchar(64), KILLS int, DEATHS int, WINS int, PLAYED int, POINTS int);");
		this.mySQL.update("DELETE FROM `meetup` WHERE UUID = \"null\"");
		if (LocationManager.isLocationExists("S1") && LocationManager.isLocationExists("S5"))
		{
			Ranging.init();
		}
		this.sendMSG("§2aktiviert");
	}
	
	public void onDisable()
	{
		try
		{
			if (IngameState.world != null)
			{
				MapReset.resetWorld(IngameState.world);
			}
		} catch (Exception e) {}
		this.plugin = null;
		this.mySQL.close();
		this.sendMSG("§cdeaktiviert");
	}
	
	public void sendMSG(String msg)
	{
		Bukkit.getConsoleSender().sendMessage("§2");
		Bukkit.getConsoleSender().sendMessage("§2");
		Bukkit.getConsoleSender().sendMessage("§2");
		Bukkit.getConsoleSender().sendMessage("§8------------------------------------------------------------");
		Bukkit.getConsoleSender().sendMessage("§2                     §bMultiUHC §ePlugin §7by");
		Bukkit.getConsoleSender().sendMessage("§2                          §7CyZe TLC");
		Bukkit.getConsoleSender().sendMessage("§2");
		Bukkit.getConsoleSender().sendMessage("§2     §7Plugin " + msg);
		Bukkit.getConsoleSender().sendMessage("§2     §7Diese ist das Meetup/UHC/SpeedUHC Plugin von §eCyZe TLC");
		Bukkit.getConsoleSender().sendMessage("§2");
		Bukkit.getConsoleSender().sendMessage("§2     §7Nuetzliche Domains:");
		Bukkit.getConsoleSender().sendMessage("§2        §8- §7Webseite: www.cyzetlc.tk");
		Bukkit.getConsoleSender().sendMessage("§2        §8- §7SocialMedia: @CyZeTLC");
		Bukkit.getConsoleSender().sendMessage("§2");
		Bukkit.getConsoleSender().sendMessage("§2     §cDas Plugin wurde von §eCyZe TLC §cfuer");
		Bukkit.getConsoleSender().sendMessage("§2     §bVyson.eu §cgeschrieben!");
		Bukkit.getConsoleSender().sendMessage("§8------------------------------------------------------------");
		Bukkit.getConsoleSender().sendMessage("§2");
		Bukkit.getConsoleSender().sendMessage("§2");
		Bukkit.getConsoleSender().sendMessage("§2");
	}
	
	public static RankFileManager getRankFileManager() 
	{
		return rank;
	}
	
	public static Main getPlugin()
	{
		return plugin;
	}
}
