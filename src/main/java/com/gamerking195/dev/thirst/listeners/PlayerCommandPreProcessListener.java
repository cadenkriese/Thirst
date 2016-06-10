package com.gamerking195.dev.thirst.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.gamerking195.dev.thirst.Thirst;

public class PlayerCommandPreProcessListener
implements Listener
{
	@EventHandler
	public void onCommandPreProcess(PlayerCommandPreprocessEvent event)
	{
		if (event.getMessage().equalsIgnoreCase("/reload"))
		{
			for (Player p : Bukkit.getServer().getOnlinePlayers())
			{
				Thirst.getThirst().playerLeave(p);
			}
		}
	}
}
