package com.gamerking195.dev.thirst.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
			        if (!p.isOnline()) continue;
				if (p.getGameMode() == GameMode.CREATIVE && Main.getInstance().getYAMLConfig().IgnoreCreative) continue;
				if (p.isOp() && Main.getInstance().getYAMLConfig().IgnoreOP) continue;
				if (p.hasPermission("thirst.ignore") || p.hasPermission("thirst.*")) continue;
				
				Thirst.getThirst().displayThirst(p);
			}
		}
		
		if (event.getType() == UpdateType.CENTI_SECOND)
		{
			for (String s : Thirst.getThirst().thirstRemovalSpeed.keySet())
			{
				Player p = Bukkit.getServer().getPlayer(UUID.fromString(s));
				
				if (!p.isOnline()) continue;
				if (p.getGameMode() == GameMode.CREATIVE && Main.getInstance().getYAMLConfig().IgnoreCreative) continue;
				if (p.isOp() && Main.getInstance().getYAMLConfig().IgnoreOP) continue;
				if (p.hasPermission("thirst.ignore") || p.hasPermission("thirst.*")) continue;
				
				if (System.currentTimeMillis() >= Thirst.getThirst().getThirstData(p).getTime())
				{
					Thirst.getThirst().removeThirst(p);
				}
			}
		}
		
		if (event.getType() == UpdateType.DAMAGE)
		{
			for (Player p : Bukkit.getOnlinePlayers())
			{
			        if (!p.isOnline()) continue;
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
