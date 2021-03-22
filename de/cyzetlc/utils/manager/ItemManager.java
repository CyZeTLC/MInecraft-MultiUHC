package de.cyzetlc.utils.manager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import de.cyzetlc.utils.Data;
import de.cyzetlc.utils.inv.ItemBuilder;

public class ItemManager
{
	public static void addLobbyItems(Player p)
	{
		p.getInventory().setItem(0, new ItemBuilder(Material.BED).setName(Data.ITEM_TEAM_SELECTION).create());
		p.getInventory().setItem(4, new ItemBuilder(Material.REDSTONE_TORCH_ON).setName("§6Vote").create());
		
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte)3);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setDisplayName(Data.ITEM_LEAVE_GAME);
		meta.setOwner("MHF_ArrowRight");
		skull.setItemMeta(meta);
		
		p.getInventory().setItem(8, skull);
	}
}
