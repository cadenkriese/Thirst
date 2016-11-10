package com.gamerking195.dev.thirst.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.potion.PotionEffect;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.gamerking195.dev.thirst.Main;
import com.gamerking195.dev.thirst.Thirst;
import com.gamerking195.dev.thirst.ThirstData;

public class PlayerGamemodeChangeListener
implements Listener
{
	@EventHandler
	public void onGamemodeChange(PlayerGameModeChangeEvent event)
	{
		Player p = event.getPlayer();

		if (event.getNewGameMode() != GameMode.CREATIVE && Main.getInstance().getYAMLConfig().IgnoreCreative)
		{
			Thirst.getThirst().displayThirst(p);
			return;
		}
		else if (event.getNewGameMode() == GameMode.CREATIVE && Main.getInstance().getYAMLConfig().IgnoreCreative)
		{
			for (PotionEffect effect : p.getActivePotionEffects())
			{
				p.removePotionEffect(effect.getType());
			}
		}

		if (Main.getInstance().getYAMLConfig().DisplayType.equalsIgnoreCase("scoreboard"))
		{
			p.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
		}
		else if (Main.getInstance().getYAMLConfig().DisplayType.equalsIgnoreCase("action"))
		{
			ActionBarAPI.sendActionBar(p, "");
		}
		else if (Main.getInstance().getYAMLConfig().DisplayType.equalsIgnoreCase("bossbar"))
		{
			ThirstData data = Thirst.getThirst().getThirstData(p);
			
			if (data.getBar() != null)
			{
				data.getBar().removePlayer(p);
			}
		}
		else
		{
			return;
		}
	}
}
