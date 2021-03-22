package de.cyzetlc.utils.inv;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {

	private ItemStack is;
	private ItemMeta im;
	private int ID = -1;
	
	@SuppressWarnings("deprecation")
	public ItemBuilder()
	{
		is = new ItemStack(0, 1);
		im = is.getItemMeta();		
		ID=ID+1;
	}
	
	
	@SuppressWarnings("deprecation")
	public ItemBuilder(int ID)
	{
		is = new ItemStack(ID, 1);
		im = is.getItemMeta();
		ID=ID+1;
	}
	
	public ItemBuilder(ItemStack is)
	{
		this.is = is;
		this.im = is.getItemMeta();
	}
	
	@SuppressWarnings("deprecation")
	public ItemBuilder(int ID, short subID)
	{
		is = new ItemStack(ID, 1, subID);
		im = is.getItemMeta();
		ID=ID+1;
	}
	
	public ItemBuilder(Material material)
	{
		is = new ItemStack(material, 1);
		im = is.getItemMeta();		
		ID=ID+1;
	}
	
	public ItemBuilder(Material material, short subID)
	{
		is = new ItemStack(material, 1, subID);
		im = is.getItemMeta();
		ID=ID+1;
	}
	
	public ItemBuilder setName(String name)
	{
		im.setDisplayName(name);
		return this;
	}
	
	public ItemBuilder setCount(int count)
	{
		is.setAmount(count);
		return this;
	}
	
	public ItemBuilder setLore(String... lore)
	{
		im.setLore(Arrays.asList(lore));
		return this;
	}
	
	public ItemBuilder setLore(ArrayList lore)
	{
		im.setLore(lore);
		return this;
	}
	
	public ItemBuilder setMeterial(Material material)
	{
		is.setType(material);
		return this;
	}
	
	public ItemBuilder setMeterial(Material material, short id)
	{
		is.setType(material);
		is = new ItemStack(material, id);
		im = is.getItemMeta();
		return this;
	}
	
	public ItemBuilder addEnchantment(Enchantment enchantment, int level)
	{
		im.addEnchant(enchantment, level, true);
		return this;
	}
	
	public int getID()
	{
		return ID;
	}
	
	public ItemStack create()
	{
		is.setItemMeta(im);
		return is;
	}
	
	public ItemBuilder valueOfItemStack(ItemStack is)
	{
		this.is = is;
		this.im = is.getItemMeta();
		return this;
	}
	
	public ItemBuilder valueOf(Object obj)
	{
		return this;
	}

}
