package com.gamerking195.dev.thirst.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import com.gamerking195.dev.thirst.Main;
import com.gamerking195.dev.thirst.Thirst;
import com.gamerking195.dev.thirst.YAMLConfig.ThirstItem;

public class PlayerItemConsumeListener 
implements Listener
{
	@EventHandler
	public void onPlayerItemConsume(PlayerItemConsumeEvent event)
	{
			ThirstItem item = Main.getInstance().getYAMLConfig().new ThirstItem(Main.getInstance().getYAMLConfig().ThirstQuenchingItem);
			
			Material type = Material.valueOf(item.getItem());
			int quenchPercent = item.getQuenchPercent();

			if (type == event.getItem().getType())
			{
				Thirst.getThirst().setThirst(event.getPlayer(), Thirst.getThirst().getPlayerThirst(event.getPlayer())+quenchPercent);
			}
	}
}
