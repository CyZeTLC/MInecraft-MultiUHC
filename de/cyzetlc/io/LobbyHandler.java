package de.cyzetlc.io;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import de.cyzetlc.Main;
import de.cyzetlc.ui.ScoreBoard;
import de.cyzetlc.utils.Data;
import de.cyzetlc.utils.GameState;
import de.cyzetlc.utils.inv.InvItem;
import de.cyzetlc.utils.inv.InvManager;
import de.cyzetlc.utils.inv.ItemBuilder;
import de.cyzetlc.utils.manager.SZVoteManager;
import de.cyzetlc.utils.mysql.coins.CoinsAPI;
import de.cyzetlc.utils.szenarien.Szenarie;
import de.cyzetlc.utils.szenarien.SzenarieManager;
import de.cyzetlc.utils.team.Team;

public class LobbyHandler implements Listener
{
	private Inventory teamsel;
	private Inventory vote;
	private Inventory voteMenu;
	private Inventory voteModi;
	
	private List<Player> ggBonus = new ArrayList<>();
	
	public LobbyHandler()
	{
		teamsel = null;
		vote = null;
		voteMenu = null;
		voteModi = null;
	}
	
	@EventHandler
	public void handleInvClick(InventoryClickEvent e)
	{
		Player p = (Player) e.getWhoClicked();
		
		if (ForbiddenHandler.build.contains(p)) return;
		if (Main.getPlugin().gsManager.getCurrentGameState().equals(GameState.INGAME)) return;
		e.setCancelled(true);
		
		if (e.getInventory() != null)
		{
			if (e.getCurrentItem() != null)
			{
				if (e.getClickedInventory() != null)
				{
					if (e.getCurrentItem().hasItemMeta())
					{
						switch (e.getInventory().getTitle()) 
						{
						case "§6Votings":
							if (e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) return;

							if (e.getCurrentItem().getType() == Material.ARMOR_STAND)
							{
								loadVoteModiInv();
								p.openInventory(voteModi);
								return;
							}
							if(e.getCurrentItem().getType() == Material.MAP)
							{
								loadVoteInv();
								p.openInventory(vote);
								return;
							}
							break;
						case "§6Modi Voting":
							if (e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) return;
							
							if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Normal UHC"))
							{
								if (Data.playerVotes.containsKey(p))
								{
									p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 1);
									p.sendMessage(Data.PREFIX + "§7Du hast bereits abgestimmt!");
									return;
								}
								
								Data.playerVotes.put(p, "UHC");
								Data.votesUHC++;
							}
							
							if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Meetup"))
							{
								if (Data.playerVotes.containsKey(p))
								{
									p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 1);
									p.sendMessage(Data.PREFIX + "§7Du hast bereits abgestimmt!");
									return;
								}
								
								Data.playerVotes.put(p, "Meetup");
								Data.votesMeetup++;
							}
							
							if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Speed UHC"))
							{
								if (Data.playerVotes.containsKey(p))
								{
									p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 1);
									p.sendMessage(Data.PREFIX + "§7Du hast bereits abgestimmt!");
									return;
								}
								
								Data.playerVotes.put(p, "SpeedUHC");
								Data.votesSpeedUHC++;
							}
							
							Data.setModi();
							p.closeInventory();
							
							for (Player all : Bukkit.getOnlinePlayers())
							{
								ScoreBoard.update(Main.getPlugin(), all);
							}
							break;
						case "§6Szenarien Voting":
							if (e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) return;

							String clickedSzenarien = e.getCurrentItem().getItemMeta().getDisplayName().replace("§6", "");
							
							p.closeInventory();
							
							if (SZVoteManager.votePlayer.containsKey(p))
							{
								p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 1);
								p.sendMessage(Data.PREFIX + "§7Du hast bereits abgestimmt!");
								return;
							}
							
							SZVoteManager.votePlayer.put(p, clickedSzenarien);
							SZVoteManager.addVote(clickedSzenarien);
							SzenarieManager.setCurrentSzenarie(Szenarie.valueOf(SZVoteManager.getSZWithMostVotes()));
							
							for (Player all : Bukkit.getOnlinePlayers())
							{
								ScoreBoard.update(Main.getPlugin(), all);
							}
							break;
						case "§6Teamwahl":
							if (e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) return;
							
							Team clickedTeam = Team.getTeamByFullName(e.getCurrentItem().getItemMeta().getDisplayName());
							
							if (clickedTeam.getPlayers().contains(p))
							{
								p.sendMessage(Data.ALREADY_IN_SAME_TEAM);
								p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 1);
								p.closeInventory();
								break;
							}
							
							if (clickedTeam.isTeamFull())
							{
								p.sendMessage(Data.TEAM_IS_FULL);
								p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 1);
								p.closeInventory();
								for (Player all : Bukkit.getOnlinePlayers())
								{
									ScoreBoard.update(Main.getPlugin(), all);
								}
								break;
							}
							
							try
							{
								if (Team.isPlayerInTeam(p))
								{
									Team oldTeam = Team.getTeamOfPlayer(p);
									oldTeam.removePlayer(p);
								}
								
								clickedTeam.addPlayer(p);
								p.sendMessage(String.format(Data.TEAM_JOIN, clickedTeam.getName()));
								p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
								p.closeInventory();
							}
							catch (Exception e1) 
							{
								p.sendMessage(Data.TEAM_JOIN_ERROR);
							}
							
							for (Player all : Bukkit.getOnlinePlayers())
							{
								ScoreBoard.update(Main.getPlugin(), all);
							}
							
							break;

						default:
							break;
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void handleChat(PlayerChatEvent e)
	{
		Player p = e.getPlayer();
		
		e.setFormat(Main.getRankFileManager().getChatFormat(p).replace("%p%", p.getName()).replace("%msg%", e.getMessage()));
		
		if (Main.getPlugin().currentGS.equals(GameState.INGAME))
		{
			e.setCancelled(true);
			
			if (e.getMessage().startsWith("@a"))
			{
				for (Player all : Bukkit.getOnlinePlayers())
				{
					all.sendMessage("§7» §b§lGlobal §8§l● §r" + Main.getRankFileManager().getChatFormat(p).replace("%p%", p.getName()).replace("%msg%", e.getMessage().replace("@a ", "").replace("@a", "")));
				}
				return;
			}
			
			Team playerTeam = null;
			
			for (Team t : Data.allTeams)
			{
				if (t.getPlayers().contains(p))
				{
					playerTeam = t;
				}
			}
			
			for (Player all : Bukkit.getOnlinePlayers())
			{
				for (Team t : Data.allTeams)
				{
					if (t.getPlayers().contains(all))
					{
						if (t.equals(playerTeam))
						{
							all.sendMessage(Main.getRankFileManager().getChatFormat(p).replace("%p%", p.getName()).replace("%msg%", e.getMessage()));
						}
					}
				}
			}
		}
		
		if (Main.getPlugin().currentGS.equals(GameState.ENDING))
		{
			if (!ggBonus.contains(p))
			{
				if (e.getMessage().equalsIgnoreCase("gg")
						|| e.getMessage().equalsIgnoreCase("ggwp")
						|| e.getMessage().equalsIgnoreCase("wl"))
				{
					CoinsAPI.addCoins(p.getUniqueId().toString(), 30);
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
					p.sendMessage(Data.PREFIX + "§7+§e30 Coins");
					ggBonus.add(p);
				}
			}
		}
	}
	
	@EventHandler
	public void handleInteract(PlayerInteractEvent e)
	{
		try
		{
			if (e.getItem() != null)
			{
				if (e.getItem().hasItemMeta())
				{
					if (e.getAction() != null)
					{
						switch (e.getItem().getItemMeta().getDisplayName()) 
						{
						case "§6Spiel verlassen":
							e.getPlayer().kickPlayer("");
							break;
						case "§6Vote":
							loadVoteMenuInv();;
							ScoreBoard.update(Main.getPlugin(), e.getPlayer());
							e.getPlayer().openInventory(voteMenu);
							break;
						case "§6Teamwahl":
							loadTeamInv();
					        ScoreBoard.update(Main.getPlugin(), e.getPlayer());
							e.getPlayer().openInventory(teamsel);
							break;

						default:
							break;
						}
					}
				}
			}
		}
		catch (Exception e1) {}
	}
	
	public void loadTeamInv()
	{
		InvItem[] items = new InvItem[Data.allTeams.length];
		
		for (int i = 0; i < Data.allTeams.length; i++)
		{
			Team t = Data.allTeams[i];
	        ArrayList<String> lore = new ArrayList();
	        
	        for(int i2 = 0; i2 < t.getMaxPlayers(); ++i2)
            {
	           if (t.getPlayers().size() - 1 >= i2) 
	           {
	              lore.add("§7- §a" + ((Player)t.getPlayers().get(i2)).getName());
	           }
	           else 
	           {
	              lore.add("§7-");
	           }
	        }
	        
			ItemBuilder ib = new ItemBuilder(35, (short)5).setLore(lore);
			
			if (t.isTeamEmpty())
			{
				ib = new ItemBuilder(35,(short)0).setLore(lore);
			}
			
			if (t.isTeamFull())
			{
				ib = new ItemBuilder(35, (short)14).setLore(lore);
			}
			
			ib.setName(t.getPrefix() + t.getName());
			
			items[i] = new InvItem(ib, i);
		}
		
		if (Data.TEAM_COUNT > 45)
		{
			teamsel = InvManager.getNewInventory("§6Teamwahl", items, 9*6);
		}
		else
		if (Data.TEAM_COUNT > 36)
		{
			teamsel = InvManager.getNewInventory("§6Teamwahl", items, 9*5);
		}
		else
		if (Data.TEAM_COUNT > 27)
		{
			teamsel = InvManager.getNewInventory("§6Teamwahl", items, 9*4);
		}
		else
		if (Data.TEAM_COUNT > 18)
		{
			teamsel = InvManager.getNewInventory("§6Teamwahl", items, 9*3);
		}
		else
		if (Data.TEAM_COUNT < 18)
		{
			teamsel = InvManager.getNewInventory("§6Teamwahl", items, 9*2);
		}
	}
	
	public void loadVoteModiInv()
	{
		InvItem[] items = new InvItem[3];
		
		items[0] = new InvItem(new ItemBuilder(Material.ARMOR_STAND).setName("§6Normal UHC").setLore(new String[] {"§7Votes: §6" + Data.votesUHC}), 2);
		items[1] = new InvItem(new ItemBuilder(Material.ARMOR_STAND).setName("§6Speed UHC").setLore(new String[] {"§7Votes: §6" + Data.votesSpeedUHC}), 4);
		items[2] = new InvItem(new ItemBuilder(Material.ARMOR_STAND).setName("§6Meetup").setLore(new String[] {"§7Votes: §6" + Data.votesMeetup}), 6);
		
		voteModi = InvManager.getNewInventory("§6Modi Voting", items, 9*1);
	}
	
	public void loadVoteMenuInv()
	{
		InvItem[] items = new InvItem[2];
		
		items[0] = new InvItem(new ItemBuilder(Material.MAP).setName("§6Szenarien Voting"), 11);
		items[1] = new InvItem(new ItemBuilder(Material.ARMOR_STAND).setName("§6Modi Voting"), 15);
		
		voteMenu = InvManager.getNewInventory("§6Votings", items, 9*3);
	}
	
	public void loadVoteInv()
	{
		InvItem[] items = new InvItem[SZVoteManager.getSzenarien().size()];
		
		for (int i = 0; i < items.length; i++)
		{
			String sz = SZVoteManager.getSzenarien().get(i);
			ItemBuilder ib = new ItemBuilder(Material.MAP).setLore(new String[] {"§7Votes: §6" + SZVoteManager.getVotes(sz)});
			
			ib.setName("§6" + sz);
			items[i] = new InvItem(ib, i);
			
		}
		vote = InvManager.getNewInventory("§6Szenarien Voting", items, 9*2);
	}
}
