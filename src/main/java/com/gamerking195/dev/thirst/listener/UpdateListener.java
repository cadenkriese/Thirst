package com.gamerking195.dev.thirst.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
            for (Player player : Bukkit.getOnlinePlayers())
            {
                if (!Thirst.getThirst().validatePlayer(player)) continue;
                if (!Main.getInstance().getYAMLConfig().alwaysShowActionBar) continue;

                Thirst.getThirst().displayThirst(player);
            }
        }

        else if (event.getType() == UpdateType.CENTI_SECOND)
        {
            for (String key : Thirst.getThirst().getThirstDataMap().keySet())
            {
                Player player = Bukkit.getServer().getPlayer(UUID.fromString(key));

                if (player == null)
                {
                    Thirst.getThirst().getThirstDataMap().remove(key);
                    continue;
                }

                if (!Thirst.getThirst().validatePlayer(player)) continue;

                //Don't put in validatePlayer because then thirst doesn't display properly.
                if ((player.getEyeLocation().getBlock().getType() == Material.WATER || player.getEyeLocation().getBlock().getType() == Material.STATIONARY_WATER) && !Main.getInstance().getYAMLConfig().removeThirstSubmerged) continue;


                if (System.currentTimeMillis() >= Thirst.getThirst().getThirstData(player).getTime())
                {
                    if (!Main.getInstance().getYAMLConfig().removeAFK) {
                        if (player.getLocation().getBlockX() != Thirst.getThirst().getThirstData(player).getLastLocation().getBlockX() || player.getLocation().getBlockY() != Thirst.getThirst().getThirstData(player).getLastLocation().getBlockY() || player.getLocation().getBlockZ() != Thirst.getThirst().getThirstData(player).getLastLocation().getBlockZ()) {
                            Thirst.getThirst().removeThirst(player);

                            Thirst.getThirst().getThirstData(player).setLastLocation(player.getLocation());
                        }
                    }
                    else
                        Thirst.getThirst().removeThirst(player);
                }
            }
        }

        else if (event.getType() == UpdateType.DAMAGE)
        {
            for (Player player : Bukkit.getOnlinePlayers())
            {
                if (!Thirst.getThirst().validatePlayer(player)) continue;

                if (Thirst.getThirst().getPlayerThirst(player) <= Main.getInstance().getYAMLConfig().getDamagePercent())
                {
                    player.damage(Main.getInstance().getYAMLConfig().getDamageAmount());
                }
            }
        }
    }
}
