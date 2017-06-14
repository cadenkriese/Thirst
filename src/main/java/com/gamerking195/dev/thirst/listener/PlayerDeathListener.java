package com.gamerking195.dev.thirst.listener;

import com.gamerking195.dev.thirst.ThirstManager;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.gamerking195.dev.thirst.Thirst;

public class PlayerDeathListener 
implements Listener
{
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		Player player = event.getEntity();
		boolean damageEffects = false;
		for (String potion : Thirst.getInstance().getYAMLConfig().potions)
		{
			if (potion.contains("DAMAGE") && event.getDeathMessage().equalsIgnoreCase(player.getName()+" died"))
			{
				damageEffects = true;
			}
		}
		
		if (ThirstManager.getThirst().getPlayerThirst(player) <= Thirst.getInstance().getYAMLConfig().getDamagePercent() && damageEffects)
		{
			event.setDeathMessage(ChatColor.translateAlternateColorCodes('&', Thirst.getInstance().getYAMLConfig().thirstDeathMessage.replace("%player%", player.getName())));

			ThirstManager.getThirst().setThirst(player, 100);
		}
	}
}