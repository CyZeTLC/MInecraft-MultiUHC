package de.cyzetlc.utils.mysql.stats;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.cyzetlc.Main;

public class SQLStats
{
  public static boolean playerExists(String uuid)
  {
    try
    {
      ResultSet rs = Main.mySQL.query("SELECT * FROM meetup WHERE UUID= '" + uuid + "'");
      if (rs.next()) {
        return rs.getString("UUID") != null;
      }
      return false;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return false;
  }
  
  public static void createPlayer(String uuid)
  {
    if (!playerExists(uuid) && (uuid != null || uuid != "null"))
    {
          Main.mySQL.update("INSERT INTO meetup(UUID, KILLS, DEATHS, WINS, PLAYED, POINTS) VALUES ('" + uuid + 
            "', '0', '0', '0', '0', '0');");
    }
  }
  
  public static int getAllPlayers()
  {
	  int i = 0;
	  try
	  {
		  ResultSet rs = Main.mySQL.query("SELECT * FROM meetup");
		  while (rs.next())
		  {
			  i++;
		  }
	  }
	  catch (Exception e)
	  {
		  e.printStackTrace();
	  }
	  return i;
  }
  
  public static Integer getKills(String uuid)
  {
    Integer i = Integer.valueOf(0);
    if (playerExists(uuid))
    {
      try
      {
        ResultSet rs = Main.mySQL.query("SELECT * FROM meetup WHERE UUID= '" + uuid + "'");
        if (rs.next()) {
          Integer.valueOf(rs.getInt("KILLS"));
        }
        i = Integer.valueOf(rs.getInt("KILLS"));
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      createPlayer(uuid);
      getKills(uuid);
    }
    return i;
  }
  
  public static Integer getWins(String uuid)
  {
    Integer i = Integer.valueOf(0);
    if (playerExists(uuid))
    {
      try
      {
        ResultSet rs = Main.mySQL.query("SELECT * FROM meetup WHERE UUID= '" + uuid + "'");
        if (rs.next()) {
          Integer.valueOf(rs.getInt("WINS"));
        }
        i = Integer.valueOf(rs.getInt("WINS"));
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      createPlayer(uuid);
      getKills(uuid);
    }
    return i;
  }
   
  public static void setWins(final String uuid, Integer wins)
  {
    if (playerExists(uuid))
    {
      Thread thread = new Thread()
      {
        public void run()
        {
          Main.mySQL.update("UPDATE meetup SET WINS= '" + wins + "' WHERE UUID= '" + uuid + "';");
        }
      };
      thread.setPriority(1);
      thread.start();
    }
    else
    {
      createPlayer(uuid);
      setWins(uuid, wins);
    }
  }
  
  public static void addWins(String uuid, final Integer wins)
  {
    if (playerExists(uuid))
    {
      Thread thread = new Thread()
      {
        public void run()
        {
          SQLStats.setWins(uuid, Integer.valueOf(SQLStats.getWins(uuid).intValue() + wins.intValue()));
          SQLStats.addPoints(uuid, 10);
        }
      };
      thread.setPriority(1);
      thread.start();
    }
    else
    {
      createPlayer(uuid);
      setWins(uuid, wins);
    }
  }
  
  public static Integer getPlayed(String uuid)
  {
    Integer i = Integer.valueOf(0);
    if (playerExists(uuid))
    {
      try
      {
        ResultSet rs = Main.mySQL.query("SELECT * FROM meetup WHERE UUID= '" + uuid + "'");
        if (rs.next()) {
          Integer.valueOf(rs.getInt("PLAYED"));
        }
        i = Integer.valueOf(rs.getInt("PLAYED"));
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      createPlayer(uuid);
      getKills(uuid);
    }
    return i;
  }
  
  public static void setPlayed(final String uuid, Integer played)
  {
    if (playerExists(uuid))
    {
      Thread thread = new Thread()
      {
        public void run()
        {
          Main.mySQL.update("UPDATE meetup SET PLAYED= '" + played + "' WHERE UUID= '" + uuid + "';");
        }
      };
      thread.setPriority(1);
      thread.start();
    }
    else
    {
      createPlayer(uuid);
      setPlayed(uuid, played);
    }
  }
  
  public static void addPlayed(String uuid, final Integer played)
  {
    if (playerExists(uuid))
    {
      Thread thread = new Thread()
      {
        public void run()
        {
          SQLStats.setPlayed(uuid, Integer.valueOf(SQLStats.getPlayed(uuid).intValue() + played.intValue()));
        }
      };
      thread.setPriority(1);
      thread.start();
    }
    else
    {
      createPlayer(uuid);
      setPlayed(uuid, played);
    }
  }
  
  public static void setKills(final String uuid, Integer kills)
  {
    if (playerExists(uuid))
    {
      Thread thread = new Thread()
      {
        public void run()
        {
          Main.mySQL.update("UPDATE meetup SET KILLS= '" + kills + "' WHERE UUID= '" + uuid + "';");
        }
      };
      thread.setPriority(1);
      thread.start();
    }
    else
    {
      createPlayer(uuid);
      setKills(uuid, kills);
    }
  }
  
  public static void addKills(String uuid, final Integer kills)
  {
    if (playerExists(uuid))
    {
      Thread thread = new Thread()
      {
        public void run()
        {
          SQLStats.setKills(uuid, Integer.valueOf(SQLStats.getKills(uuid).intValue() + kills.intValue()));
          SQLStats.addPoints(uuid, 1);
        }
      };
      thread.setPriority(1);
      thread.start();
    }
    else
    {
      createPlayer(uuid);
      addKills(uuid, kills);
    }
  }
  
  public static void removeKills(String uuid, final Integer kills)
  {
    if (playerExists(uuid))
    {
      Thread thread = new Thread()
      {
        public void run()
        {
          SQLStats.setKills(uuid, Integer.valueOf(SQLStats.getKills(uuid).intValue() - kills.intValue()));
        }
      };
      thread.setPriority(1);
      thread.start();
    }
    else
    {
      createPlayer(uuid);
      removeKills(uuid, kills);
    }
  }
  
  public static Integer getDeaths(String uuid)
  {
    Integer i = Integer.valueOf(0);
    if (playerExists(uuid))
    {
      try
      {
        ResultSet rs = Main.mySQL.query("SELECT * FROM meetup WHERE UUID= '" + uuid + "'");
        if (rs.next()) {
          Integer.valueOf(rs.getInt("DEATHS"));
        }
        i = Integer.valueOf(rs.getInt("DEATHS"));
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      createPlayer(uuid);
      getDeaths(uuid);
    }
    return i;
  }
  
  public static void setDeaths(final String uuid, Integer deaths)
  {
    if (playerExists(uuid))
    {
      Thread thread = new Thread()
      {
        public void run()
        {
          Main.mySQL.update("UPDATE meetup SET DEATHS= '" + deaths + "' WHERE UUID= '" + uuid + "';");
        }
      };
      thread.setPriority(1);
      thread.start();
    }
    else
    {
      createPlayer(uuid);
      setDeaths(uuid, deaths);
    }
  }
  
  public static void removeDeaths(String uuid, final Integer deaths)
  {
    if (playerExists(uuid))
    {
      Thread thread = new Thread()
      {
        public void run()
        {
          SQLStats.setDeaths(uuid, Integer.valueOf(SQLStats.getDeaths(uuid).intValue() - deaths.intValue()));
        }
      };
      thread.setPriority(1);
      thread.start();
    }
    else
    {
      createPlayer(uuid);
      removeDeaths(uuid, deaths);
    }
  }
  
  public static void addDeaths(String uuid, final Integer deaths)
  {
    if (playerExists(uuid))
    {
      Thread thread = new Thread()
      {
        public void run()
        {
          SQLStats.setDeaths(uuid, Integer.valueOf(SQLStats.getDeaths(uuid).intValue() + deaths.intValue()));
        }
      };
      thread.setPriority(1);
      thread.start();
    }
    else
    {
      createPlayer(uuid);
      addDeaths(uuid, deaths);
    }
  }
  
  public static Integer getPoints(String uuid)
  {
    Integer i = Integer.valueOf(0);
    if (playerExists(uuid))
    {
      try
      {
        ResultSet rs = Main.mySQL.query("SELECT * FROM meetup WHERE UUID= '" + uuid + "'");
        if (rs.next()) {
          Integer.valueOf(rs.getInt("POINTS"));
        }
        i = Integer.valueOf(rs.getInt("POINTS"));
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      createPlayer(uuid);
      getPoints(uuid);
    }
    return i;
  }
  
  public static void setPoints(final String uuid, Integer points)
  {
    if (playerExists(uuid))
    {
      Thread thread = new Thread()
      {
        public void run()
        {
          Main.mySQL.update("UPDATE meetup SET POINTS= '" + points + "' WHERE UUID= '" + uuid + "';");
        }
      };
      thread.setPriority(1);
      thread.start();
    }
    else
    {
      createPlayer(uuid);
      setPoints(uuid, points);
    }
  }
  
  public static void addPoints(String uuid, final Integer points)
  {
    if (playerExists(uuid))
    {
      Thread thread = new Thread()
      {
        public void run()
        {
          SQLStats.setPoints(uuid, Integer.valueOf(SQLStats.getPoints(uuid).intValue() + points.intValue()));
        }
      };
      thread.setPriority(1);
      thread.start();
    }
    else
    {
      createPlayer(uuid);
      addPoints(uuid, points);
    }
  }
  
  public static void removePoints(String uuid, final Integer points)
  {
    if (playerExists(uuid))
    {
      Thread thread = new Thread()
      {
        public void run()
        {
          SQLStats.setPoints(uuid, Integer.valueOf(SQLStats.getPoints(uuid).intValue() - points.intValue()));
        }
      };
      thread.setPriority(1);
      thread.start();
    }
    else
    {
      createPlayer(uuid);
      removePoints(uuid, points);
    }
  }
}
