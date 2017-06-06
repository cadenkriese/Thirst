package com.gamerking195.dev.thirst.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.gamerking195.dev.thirst.Main;
import com.gamerking195.dev.thirst.Thirst;
import com.gamerking195.dev.thirst.config.YAMLConfig.ThirstItem;

public class PlayerItemConsumeListener 
implements Listener
{
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerItemConsume(PlayerItemConsumeEvent event)
	{		
		for (String itemString : Main.getInstance().getYAMLConfig().thirstQuenchingItems)
		{
			ThirstItem item = Main.getInstance().getYAMLConfig().new ThirstItem(itemString);
			
			ItemStack is = new ItemStack(Material.valueOf(item.getItem()), 1);
			
			//casting due to spigot api subject to change.
			is.setData(new MaterialData(is.getType(), (byte) item.getMetaData()));

			int quenchPercent = item.getQuenchPercent();
			
			if (is.getType() == event.getItem().getType() && event.getItem().getData().toString().equals(is.getData().toString()))
			{
				Thirst.getThirst().setThirst(event.getPlayer(), Thirst.getThirst().getPlayerThirst(event.getPlayer())+quenchPercent);
				return;
			}
		}
	}
}
