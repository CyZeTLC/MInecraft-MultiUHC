package de.cyzetlc.utils.manager;

import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.scheduler.BukkitRunnable;

import de.cyzetlc.Main;
import de.cyzetlc.gamestates.IngameState;

public class BorderManager 
{
	private World w;
	private WorldBorder b;
	
	public BorderManager(World w)
	{
		this.w = w;
	}
	
	public void load()
	{
		this.b = this.w.getWorldBorder();
		this.b.setCenter(LocationManager.getLocation("T1"));
		this.b.setSize(1000);
		this.b.setWarningDistance(100);
		this.b.setDamageAmount(5);
		this.b.setDamageBuffer(0);
	}
	
	public void start()
	{
		new BukkitRunnable()
		{

			@Override
			public void run() 
			{
				if (b.getSize() > 50)
				{
					if (IngameState.isPvPAllowed)
					{
						b.setSize(b.getSize() - 0.05);
					}
				}
			}
			
		}.runTaskTimer(Main.getPlugin(), 0, 2);
	}
	
	public void setWorld(World w)
	{
		this.w = w;
	}
	
	public World getWorld() 
	{
		return this.w;
	}
}
