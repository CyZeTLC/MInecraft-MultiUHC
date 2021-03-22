package de.cyzetlc.utils.mysql.coins;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.cyzetlc.Main;
import de.cyzetlc.utils.mysql.MySQL;

public class CoinsAPI
{
  public static MySQL mySQL = Main.mySQL;
  
  public static int getCoins(String uuid)
  {
    try
    {
      PreparedStatement statement = mySQL.connection.prepareStatement("SELECT COINS FROM coins WHERE UUID = ?");
      statement.setString(1, uuid);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        return resultSet.getInt("COINS");
      }
    }
    catch (SQLException throwables)
    {
      throwables.printStackTrace();
    }
    return -1;
  }
  
  public static void setCoins(String uuid, int amount)
  {
    if(getCoins(uuid) == -1) {
      try
      {
        PreparedStatement statement = mySQL.connection.prepareStatement("INSERT INTO coins (UUID,COINS) VALUES (?,?)");
        statement.setString(1, uuid);
        statement.setInt(2, amount);
        statement.executeUpdate();
      }
      catch (SQLException throwables)
      {
        throwables.printStackTrace();
      }
    } else {
      try
      {
        PreparedStatement statement = mySQL.connection.prepareStatement("UPDATE coins SET COINS = ? WHERE UUID = ?");
        statement.setString(2, uuid);
        statement.setInt(1, amount);
        statement.executeUpdate();
      }
      catch (SQLException throwables)
      {
        throwables.printStackTrace();
      }
    }
  }
  
  public static void addCoins(String uuid, int amount)
  {
    setCoins(uuid, getCoins(uuid) + amount);
  }
  
  public static void removeCoins(String uuid, int amount)
  {
    if (getCoins(uuid) >= amount) {
      setCoins(uuid, getCoins(uuid) - amount);
    }
  }
}
