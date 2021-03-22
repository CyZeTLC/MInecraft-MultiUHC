package de.cyzetlc.ui;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_8_R3.PlayerConnection;

public class TitleAPI {
	
	public static void sendTitle(Player p, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle)
	{
		PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
		
		PacketPlayOutTitle PacketPlayOutTime = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, fadeIn.intValue(), stay.intValue(), fadeOut.intValue());
		connection.sendPacket(PacketPlayOutTime);
		
		if(subtitle != null) 
		{
			IChatBaseComponent TitleSub = ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
			PacketPlayOutTitle PacketPlayOutSubTitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, TitleSub);
			connection.sendPacket(PacketPlayOutSubTitle);
		}
		if(title != null) 
		{
			IChatBaseComponent Title = ChatSerializer.a("{\"text\": \"" + title + "\"}");
			PacketPlayOutTitle PacketPlayOutSubTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, Title);
			connection.sendPacket(PacketPlayOutSubTitle);
		}
	}

}
