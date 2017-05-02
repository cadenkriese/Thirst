package com.gamerking195.dev.thirst.placeholder;

import com.gamerking195.dev.thirst.Main;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.gamerking195.dev.thirst.Thirst;

import me.clip.placeholderapi.external.EZPlaceholderHook;

public class ClipPlaceholders
extends EZPlaceholderHook
{
	public ClipPlaceholders(Plugin plugin)
	{
		super(plugin, "gkthirst");
	}

	@Override
	public String onPlaceholderRequest(Player player, String identifier)
	{
		switch (identifier) {
			case "thirstmessage":
				return Thirst.getThirst().getThirstString(player);
			case "thirstbar":
				return Thirst.getThirst().getThirstBar(player);
			case "thirstpercent":
				return Thirst.getThirst().getThirstPercent(player, true);
			case "thirstremovalspeed":
				return String.format(String.valueOf(Thirst.getThirst().getThirstData(player).getSpeed() / 1000), "%.3f");
			case "thirstremovalamount":
				return String.valueOf(Main.getInstance().getYAMLConfig().removeThirst);
		}
		return null;
	}
}
