package com.gamerking195.dev.thirst.listener;

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
		Player player = event.getEntity();
		boolean damageEffects = false;
		for (String potion : Main.getInstance().getYAMLConfig().potions)
		{
			if (potion.contains("DAMAGE") && event.getDeathMessage().equalsIgnoreCase(player.getName()+" died"))
			{
				damageEffects = true;
			}
		}
		
		if (Thirst.getThirst().getPlayerThirst(player) <= Main.getInstance().getYAMLConfig().getDamagePercent() && damageEffects)
		{
			event.setDeathMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().thirstDeathMessage.replace("%player%", player.getName())));

			Thirst.getThirst().setThirst(player, 100);
		}
	}
}