package de.cyzetlc.utils.mysql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.cyzetlc.Main;

public class MySQL
{
  private String HOST = "";
  private String DATABASE = "";
  private String USER = "";
  private String PASSWORD = "";
  public static Connection connection;
  
  public static MySQL initMySQL()
  {
	  File f = new File(Main.getPlugin().getDataFolder(), "mysql.yml");
	  FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
	  
	  if (!f.exists())
	  {
		try 
		{
			f.createNewFile();
			cfg.set("host", "127.0.0.1");
			cfg.set("user", "root");
			cfg.set("password", "root-pw");
			cfg.set("database", "Meetup");
			cfg.save(f);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	  }
	  
	  return new MySQL(cfg.getString("host"), cfg.getString("database"), cfg.getString("user"), cfg.getString("password"));
  }
  
  public MySQL(String host, String database, String user, String password)
  {
    this.HOST = host;
    this.DATABASE = database;
    this.USER = user;
    this.PASSWORD = password;
    
    connect();
  }
  
  public MySQL(String host, String database, String user)
  {
    this.HOST = host;
    this.DATABASE = database;
    this.USER = user;
    
    connect();
  }
  
  public void connect()
  {
    try
    {
      connection = DriverManager.getConnection("jdbc:mysql://" + this.HOST + ":3306/" + this.DATABASE + "?autoReconnect=true&serverTimezone=CET&characterEncoding=latin1", this.USER, this.PASSWORD);
      System.out.println("[MySQL] Verbindung mit MySQL hergestellt.");
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
  
  public void close()
  {
    if (this.connection != null) {
      try
      {
        this.connection.close();
        System.out.println("[MySQL] Verbindung mit MySQL geschlossen.");
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
  }
  
  public void update(String qry)
  {
    try
    {
      Statement statement = this.connection.createStatement();
      statement.executeUpdate(qry);
      statement.close();
    }
    catch (SQLException e)
    {
      connect();
      e.printStackTrace();
    }
  }
  
  public static int getRank(String uuid)
  {
    int rank = 0;
    try
    {
      PreparedStatement ps = connection
        .prepareStatement("SELECT * FROM meetup ORDER BY POINTS DESC");
      ResultSet result = ps.executeQuery();
      while (result.next())
      {
        rank++;
        String uuid2 = result.getString("UUID");
        if (uuid2.equalsIgnoreCase(uuid)) {
          return rank;
        }
      }
      result.close();
      ps.close();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return rank;
  }
  
  public ResultSet query(String qry)
  {
    ResultSet resultSet = null;
    try
    {
      Statement statement = this.connection.createStatement();
      resultSet = statement.executeQuery(qry);
    }
    catch (SQLException e)
    {
      connect();
      e.printStackTrace();
    }
    return resultSet;
  }
}
