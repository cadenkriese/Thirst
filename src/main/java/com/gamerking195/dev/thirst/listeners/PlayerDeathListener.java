package com.gamerking195.dev.thirst.listeners;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.gamerking195.dev.thirst.Main;
import com.gamerking195.dev.thirst.Thirst;

public class PlayerDeathListener 
implements Listener
{
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		Player p = event.getEntity();
		if (event.getDeathMessage().contains("withered away"))
		{
			if (Thirst.getThirst().getPlayerThirst(p) <= 0)
			{
				event.setDeathMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().ThirstDeathMessage.replace("%player%", p.getName())));
				
				Thirst.getThirst().setThirst(p, 100);
			}
		}
	}
}