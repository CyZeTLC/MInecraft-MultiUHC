package de.cyzetlc.utils.inv;

import org.bukkit.inventory.ItemStack;

public class InvItem 
{
	private int slot = 0;
	private final ItemBuilder item;
	
	public InvItem(ItemBuilder ib, int slot)
	{
		this.item = ib;
		this.slot = slot;
	}
	
	public int getSlot() 
	{
		return slot;
	}
	
	public ItemStack getItem() 
	{
		return item.create();
	}
}
