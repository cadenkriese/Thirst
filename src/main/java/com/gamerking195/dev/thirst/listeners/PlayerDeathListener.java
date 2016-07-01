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
		boolean damageEffects = false;
		for (String s : Main.getInstance().getYAMLConfig().Effects)
		{
			if (s.contains("DAMAGE") && event.getDeathMessage().equalsIgnoreCase(p.getName()+" died"))
			{
				damageEffects = true;
			}
		}
		
		if (Thirst.getThirst().getPlayerThirst(p) <= Main.getInstance().getYAMLConfig().getDamagePercent() && damageEffects)
		{
			event.setDeathMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().ThirstDeathMessage.replace("%player%", p.getName())));

			Thirst.getThirst().setThirst(p, 100);
		}
	}
}