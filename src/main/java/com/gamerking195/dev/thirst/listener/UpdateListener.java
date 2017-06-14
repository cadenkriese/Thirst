package com.gamerking195.dev.thirst.listener;

import java.util.UUID;

import com.gamerking195.dev.thirst.Thirst;
import com.gamerking195.dev.thirst.ThirstManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
                if (!ThirstManager.getThirst().validatePlayer(player)) continue;
                if (!Thirst.getInstance().getYAMLConfig().alwaysShowActionBar) continue;

                ThirstManager.getThirst().displayThirst(player);
            }
        }

        else if (event.getType() == UpdateType.CENTI_SECOND)
        {
            for (String key : ThirstManager.getThirst().getThirstDataMap().keySet())
            {
                Player player = Bukkit.getServer().getPlayer(UUID.fromString(key));

                if (player == null)
                {
                    ThirstManager.getThirst().getThirstDataMap().remove(key);
                    continue;
                }

                if (!ThirstManager.getThirst().validatePlayer(player)) continue;

                //Don't put in validatePlayer because then thirst doesn't display properly.
                if ((player.getEyeLocation().getBlock().getType() == Material.WATER || player.getEyeLocation().getBlock().getType() == Material.STATIONARY_WATER) && !Thirst.getInstance().getYAMLConfig().removeThirstSubmerged) continue;


                if (System.currentTimeMillis() >= ThirstManager.getThirst().getThirstData(player).getTime())
                {
                    if (!Thirst.getInstance().getYAMLConfig().removeAFK) {
                        if (player.getLocation().getBlockX() != ThirstManager.getThirst().getThirstData(player).getLastLocation().getBlockX() || player.getLocation().getBlockY() != ThirstManager.getThirst().getThirstData(player).getLastLocation().getBlockY() || player.getLocation().getBlockZ() != ThirstManager.getThirst().getThirstData(player).getLastLocation().getBlockZ()) {
                            ThirstManager.getThirst().removeThirst(player);

                            ThirstManager.getThirst().getThirstData(player).setLastLocation(player.getLocation());
                        }
                    }
                    else
                        ThirstManager.getThirst().removeThirst(player);
                }
            }
        }

        else if (event.getType() == UpdateType.DAMAGE)
        {
            for (Player player : Bukkit.getOnlinePlayers())
            {
                if (!ThirstManager.getThirst().validatePlayer(player)) continue;

                if (ThirstManager.getThirst().getPlayerThirst(player) <= Thirst.getInstance().getYAMLConfig().getDamagePercent())
                {
                    player.damage(Thirst.getInstance().getYAMLConfig().getDamageAmount());
                }
            }
        }
    }
}
