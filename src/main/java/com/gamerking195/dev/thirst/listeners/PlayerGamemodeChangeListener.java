package com.gamerking195.dev.thirst.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.gamerking195.dev.thirst.Main;
import com.gamerking195.dev.thirst.Thirst;
import com.gamerking195.dev.thirst.ThirstEffectsHandler;

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
		}
		else if (event.getNewGameMode() == GameMode.CREATIVE && Main.getInstance().getYAMLConfig().IgnoreCreative)
		{
			if (ThirstEffectsHandler.getThirstEffects().getPlayerEffects().containsKey(p))
			{
				switch (ThirstEffectsHandler.getThirstEffects().getPlayerEffects().get(p))
				{
				case 0:
					break;
				case 1: case 2: case 3: case 4: case 5:
					for (PotionEffect effect : p.getActivePotionEffects())
					{
						if (effect.getType() == PotionEffectType.SLOW || effect.getType() == PotionEffectType.CONFUSION || effect.getType() == PotionEffectType.HUNGER || effect.getType() == PotionEffectType.WITHER)
						{
							p.removePotionEffect(effect.getType());
						}
					}
					break;
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
			else
			{
				return;
			}
		}
	}
}
