package de.cyzetlc.utils.inv;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class InvManager
{
	public static Inventory getNewInventory(String invName, InvItem[] items, int size)
	{
		Inventory inv = Bukkit.createInventory(null, size, invName);
		
		for (int i = 0; i < inv.getSize(); i++)
		{
			inv.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setName("§c").create());
		}
		
		for (InvItem item : items)
		{
			inv.setItem(item.getSlot(), item.getItem());
		}
		
		return inv;
	}
}
