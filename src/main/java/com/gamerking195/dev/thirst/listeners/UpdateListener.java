package com.gamerking195.dev.thirst.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.gamerking195.dev.thirst.Main;
import com.gamerking195.dev.thirst.Thirst;

import me.gamerzking.core.updater.UpdateType;
import me.gamerzking.core.updater.event.UpdateEvent;

public class UpdateListener
implements Listener
{
	@EventHandler
	public void onUpdate(UpdateEvent event)
	{
		if (event.getType() == UpdateType.SECOND)
		{
			for (Player p : Bukkit.getOnlinePlayers())
			{
				if (p.getGameMode() == GameMode.CREATIVE && Main.getInstance().getYAMLConfig().IgnoreCreative) continue;
				if (p.isOp() && Main.getInstance().getYAMLConfig().IgnoreOP) continue;
				if (p.hasPermission("thirst.ignore") || p.hasPermission("thirst.*")) continue;
				
				if (Main.getInstance().getYAMLConfig().DisplayType.equalsIgnoreCase("ACTION")) ActionBarAPI.sendActionBar(p, Thirst.getThirst().getThirstString(p));
				else if (Main.getInstance().getYAMLConfig().DisplayType.equalsIgnoreCase("SCOREBOARD")) Thirst.getThirst().displayThirst(p);
			}
		}
		if (event.getType() == UpdateType.CONFIG)
		{
			for (Player p : Bukkit.getOnlinePlayers())
			{
				if (p.getGameMode() == GameMode.CREATIVE && Main.getInstance().getYAMLConfig().IgnoreCreative) continue;
				if (p.isOp() && Main.getInstance().getYAMLConfig().IgnoreOP) continue;
				if (p.hasPermission("thirst.ignore") || p.hasPermission("thirst.*")) continue;

				Thirst.getThirst().setThirst(p, Thirst.getThirst().getPlayerThirst(p)-Main.getInstance().getYAMLConfig().RemoveThirst);
			}
		}
		if (event.getType() == UpdateType.DAMAGE)
		{
			for (Player p : Bukkit.getOnlinePlayers())
			{
				if (p.getGameMode() == GameMode.CREATIVE && Main.getInstance().getYAMLConfig().IgnoreCreative) continue;
				if (p.isOp() && Main.getInstance().getYAMLConfig().IgnoreOP) continue;
				if (p.hasPermission("thirst.ignore") || p.hasPermission("thirst.*")) continue;
				
				if (Thirst.getThirst().getPlayerThirst(p) <= Main.getInstance().getYAMLConfig().getDamagePercent())
				{
					p.damage(Main.getInstance().getYAMLConfig().getDamageAmount());
				}
			}
		}
	}
}
