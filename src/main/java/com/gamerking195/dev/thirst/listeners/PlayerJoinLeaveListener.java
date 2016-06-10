package com.gamerking195.dev.thirst.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gamerking195.dev.thirst.Thirst;

public class PlayerJoinLeaveListener 
implements Listener
{
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Thirst.getThirst().playerJoin(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event)
	{
		Thirst.getThirst().playerLeave(event.getPlayer());
	}
}
