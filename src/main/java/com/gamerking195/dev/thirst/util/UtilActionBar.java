package com.gamerking195.dev.thirst.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class UtilActionBar {
    private UtilActionBar() {}
    private static UtilActionBar instance = new UtilActionBar();
    public static UtilActionBar getInstance() {
        return instance;
    }

    //Fairly long method to call repetadly ¯\_(ツ)_/¯
    public void sendActionBar(Player player, String message) {
        if (player != null)
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
    }
}
