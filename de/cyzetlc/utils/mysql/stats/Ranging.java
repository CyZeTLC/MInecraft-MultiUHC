package de.cyzetlc.utils.mysql.stats;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SkullType;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import de.cyzetlc.Main;
import de.cyzetlc.utils.manager.LocationManager;
import de.cyzetlc.utils.mysql.MySQL;

public class Ranging 
{
	
    static HashMap<Integer, String> rang = new HashMap<>();
	
	public static void init()
	{	
		ResultSet rs = Main.mySQL.query("SELECT UUID FROM meetup ORDER BY points DESC LIMIT 5");
		
		int in = 0;
		
		try
		{
			while(rs.next())
			{
				in++;
				rang.put(in, rs.getString("UUID"));
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		Location loc = LocationManager.getLocation("S1");
		Location loc1 = LocationManager.getLocation("S2");
		Location loc2 = LocationManager.getLocation("S3");
		Location loc3 = LocationManager.getLocation("S4");
		Location loc4 = LocationManager.getLocation("S5");
		
		List<Location> LOC = new ArrayList<>();
		
		LOC.add(loc);
		LOC.add(loc1);
		LOC.add(loc2);
		LOC.add(loc3);
		LOC.add(loc4);
		
		for(int i = 0; i < LOC.size(); i++) 
		{
			
			int id = i+1;
			
			Skull s = (Skull) LOC.get(i).getBlock().getState();
			
			s.setSkullType(SkullType.PLAYER);
				
			String name = "";
			
			try 
			{
				if(rang.get(i) != null)
				{
					name = Bukkit.getOfflinePlayer(UUID.fromString(rang.get(id))).getName();
				}
				else 
				{
					name = "???";
				}
			}
			catch (Exception e)
			{
				name = "???";
			}
			
			Location newloc = new Location(LOC.get(i).getWorld(), LOC.get(i).getBlockX(), LOC.get(i).getBlockY() -1, LOC.get(i).getBlockZ());
			
			if(newloc.getBlock().getState() instanceof Sign)
			{
				BlockState b = newloc.getBlock().getState();
				
				Sign S = (Sign) b;
				
				try 
				{			
					
					s.setRotation(BlockFace.EAST);
					
					System.out.println(name);
					
					if(name == "???")
					{
						s.setOwner("MHF_Question");
						s.update();
					}
					else 
					{
						s.setOwner(name);
						s.update();
					}
						
					S.setLine(0, "§c- §rPlatz #" + id + " §c-");
					S.setLine(1, "§e" + name);
					S.setLine(2, "§6" + SQLStats.getWins(rang.get(id)) + " Siege");
					S.setLine(3, "§6" + SQLStats.getPoints(rang.get(id)) + " Punkte");
					S.update();
				}
				catch (Exception e)
				{

				}
			}
			
		}
	
	}

    public static String getName(String uuid)
    {
        String url = "https://api.mojang.com/user/profiles/"+uuid.replace("-", "")+"/names";

        try {
            String nameJson = IOUtils.toString(new URL(url));           
            JSONArray nameValue = (JSONArray) JSONValue.parseWithException(nameJson);
            String playerSlot = nameValue.get(nameValue.size()-1).toString();
            JSONObject nameObject = (JSONObject) JSONValue.parseWithException(playerSlot);
            return nameObject.get("name").toString();
        } catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }
        return "???";
    }

}
