package de.cyzetlc.utils.manager;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import de.cyzetlc.Main;
import de.cyzetlc.utils.Data;
import de.cyzetlc.utils.mysql.stats.SQLStats;

public class RandomFactsManager 
{
	private Main plugin;
	private boolean isRunning;
	private String[] facts;
	
	public RandomFactsManager(Main plugin)
	{
		this.plugin = plugin;
		this.isRunning = false;
		
		this.facts = new String[] {
			"§7Wusstest du, dass schon §e" + SQLStats.getAllPlayers() + "§7 Leute §aUHC §7hier gespielt haben?",
			"§7Hast du schon gewusst, dass §aUHC §7am §e10.03.21 §7veröffentlicht wurde?",
			"§7Wenn du es nocht gewusst hast§8: §7Bei uns gibt es 3 §averschiedene Varianten §7von UHC."
		};
	}
	
	public void start()
	{
		this.isRunning = true;
		
		new BukkitRunnable()
		{

			@Override
			public void run() 
			{
				if (!isRunning)
				{
					cancel();
					return;
				}
				Bukkit.broadcastMessage(Data.PREFIX + getRandomFact());
			}
			
		}.runTaskTimer(this.plugin, 20*20, 20*60*5);
	}
	
	public void stop()
	{
		isRunning = false;
	}
	
	public String getRandomFact()
	{
		return this.facts[new Random().nextInt(this.facts.length)];
	}
}
