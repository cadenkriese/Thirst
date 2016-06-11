package com.gamerking195.dev.thirst.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import com.gamerking195.dev.thirst.Main;
import com.gamerking195.dev.thirst.Thirst;

public class PlayerRespawnListener 
implements Listener
{
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		final Player p = event.getPlayer();

		if (Main.getInstance().getYAMLConfig().IgnoreCreative && p.getGameMode() == GameMode.CREATIVE) return;
		if (Main.getInstance().getYAMLConfig().IgnoreOP && p.isOp()) return;

		Thirst.getThirst().setThirst(p, 100);
	}
}
