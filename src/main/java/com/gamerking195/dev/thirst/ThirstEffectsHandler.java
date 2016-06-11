package com.gamerking195.dev.thirst;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ThirstEffectsHandler
{
	public ThirstEffectsHandler() {}
	public static ThirstEffectsHandler instance = new ThirstEffectsHandler();
	public static ThirstEffectsHandler getThirstEffects() {	return instance;	}

	private HashMap<Player, Integer> playerEffects = new HashMap<Player, Integer>();

	public HashMap<Player, Integer> getPlayerEffects() 
	{
		return playerEffects;
	}

	public void setEffect(Player p , int tier)
	{
		if (tier > 5) tier = 5;
		if (tier < 0) tier = 1;

		if (p.getGameMode() == GameMode.CREATIVE && Main.getInstance().getYAMLConfig().IgnoreCreative) return;
		if (p.isOp() && Main.getInstance().getYAMLConfig().IgnoreOP) return;
		if (p.hasPermission("thirst.ignore") || p.hasPermission("thirst.*")) return;
		
		if (tier == 0)
		{
			for (PotionEffect effect : p.getActivePotionEffects())
			{
				if (effect.getDuration() > 500)
				{
					if (effect.getType() == PotionEffectType.SLOW || effect.getType() == PotionEffectType.CONFUSION || effect.getType() == PotionEffectType.HUNGER || effect.getType() == PotionEffectType.WITHER)
					{
						p.removePotionEffect(effect.getType());
					}
				}
			}
			return;
		}
		else if (tier == 1)
		{
			for (PotionEffect effect : p.getActivePotionEffects())
			{
				if (effect.getDuration() > 500)
				{
					if (effect.getType() == PotionEffectType.SLOW || effect.getType() == PotionEffectType.CONFUSION || effect.getType() == PotionEffectType.HUNGER || effect.getType() == PotionEffectType.WITHER)
					{
						p.removePotionEffect(effect.getType());
					}
				}
			}
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 0));
		}
		else if (tier == 2)
		{
			for (PotionEffect effect : p.getActivePotionEffects())
			{
				if (effect.getDuration() > 500)
				{
					if (effect.getType() == PotionEffectType.SLOW || effect.getType() == PotionEffectType.CONFUSION || effect.getType() == PotionEffectType.HUNGER || effect.getType() == PotionEffectType.WITHER)
					{
						p.removePotionEffect(effect.getType());
					}
				}
			}
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 99999, 0));
		}
		else if (tier == 3)
		{
			for (PotionEffect effect : p.getActivePotionEffects())
			{
				if (effect.getDuration() > 500)
				{
					if (effect.getType() == PotionEffectType.SLOW || effect.getType() == PotionEffectType.CONFUSION || effect.getType() == PotionEffectType.HUNGER || effect.getType() == PotionEffectType.WITHER)
					{
						p.removePotionEffect(effect.getType());
					}
				}
			}
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 99999, 1));
		}
		else if (tier == 4)
		{
			for (PotionEffect effect : p.getActivePotionEffects())
			{
				if (effect.getDuration() > 500)
				{
					if (effect.getType() == PotionEffectType.SLOW || effect.getType() == PotionEffectType.CONFUSION || effect.getType() == PotionEffectType.HUNGER || effect.getType() == PotionEffectType.WITHER)
					{
						p.removePotionEffect(effect.getType());
					}
				}
			}
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 99999, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 3));
			p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 99999, 2));
		}
		else if (tier == 5)
		{
			for (PotionEffect effect : p.getActivePotionEffects())
			{
				if (effect.getDuration() > 500)
				{
					if (effect.getType() == PotionEffectType.SLOW || effect.getType() == PotionEffectType.CONFUSION || effect.getType() == PotionEffectType.HUNGER || effect.getType() == PotionEffectType.WITHER)
					{
						p.removePotionEffect(effect.getType());
					}
				}
			}
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 99999, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 99999, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 99999, 2));
		}

		playerEffects.put(p, tier);
	}

	public void removePlayer(Player p)
	{
		playerEffects.remove(p); 
	}
}
