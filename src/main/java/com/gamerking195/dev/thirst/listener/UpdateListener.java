package com.gamerking195.dev.thirst.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
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
                if (!Thirst.getThirst().validatePlayer(p)) continue;
                if (!Main.getInstance().getYAMLConfig().alwaysShowActionBar) continue;

                Thirst.getThirst().displayThirst(p);
            }
        }

        else if (event.getType() == UpdateType.CENTI_SECOND)
        {
            for (String s : Thirst.getThirst().getThirstDataMap().keySet())
            {
                Player p = Bukkit.getServer().getPlayer(UUID.fromString(s));

                if (p == null)
                {
                    Thirst.getThirst().getThirstDataMap().remove(s);
                    continue;
                }

                if (!Thirst.getThirst().validatePlayer(p)) continue;

                if (System.currentTimeMillis() >= Thirst.getThirst().getThirstData(p).getTime())
                {
                    if (!Main.getInstance().getYAMLConfig().removeAFK) {
                        if (p.getLocation().getBlockX() != Thirst.getThirst().getThirstData(p).getLastLocation().getBlockX() || p.getLocation().getBlockY() != Thirst.getThirst().getThirstData(p).getLastLocation().getBlockY() || p.getLocation().getBlockZ() != Thirst.getThirst().getThirstData(p).getLastLocation().getBlockZ()) {
                            Thirst.getThirst().removeThirst(p);

                            Thirst.getThirst().getThirstData(p).setLastLocation(p.getLocation());
                        }
                    }
                    else
                        Thirst.getThirst().removeThirst(p);
                }
            }
        }

        else if (event.getType() == UpdateType.DAMAGE)
        {
            for (Player p : Bukkit.getOnlinePlayers())
            {
                if (!Thirst.getThirst().validatePlayer(p)) continue;

                if (Thirst.getThirst().getPlayerThirst(p) <= Main.getInstance().getYAMLConfig().getDamagePercent())
                {
                    p.damage(Main.getInstance().getYAMLConfig().getDamageAmount());
                }
            }
        }
    }
}
