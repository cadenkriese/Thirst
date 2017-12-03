package com.gamerking195.dev.thirst.command;

import com.gamerking195.dev.thirst.ThirstManager;
import com.gamerking195.dev.thirst.listener.PlayerMoveListener;
import com.gamerking195.dev.thirst.util.UtilActionBar;
import com.gamerking195.dev.thirst.util.UtilSQL;
import com.gamerking195.dev.thirst.util.UtilUpdater;
import net.cubespace.Yamler.Config.InvalidConfigurationException;
import net.md_5.bungee.api.ChatColor;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.gamerking195.dev.thirst.Thirst;
import com.gamerking195.dev.thirst.ThirstData;
import com.gamerking195.dev.thirst.config.DataConfig;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ThirstCommand
        implements CommandExecutor
{

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (label.equalsIgnoreCase("thirst"))
        {
            if (args.length == 0)
            {
                if (sender instanceof Player)
                {
                    Player player = (Player) sender;
                    if (!player.hasPermission("thirst.command") && !player.hasPermission("thirst.*"))
                    {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Thirst.getInstance().getYAMLConfig().noPermissionMessage));
                        return true;
                    }
                }

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&m-----------------------"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&1&lThirst &fV"+ Thirst.getInstance().getDescription().getVersion()+" &bby &f"+ Thirst.getInstance().getDescription().getAuthors().toString().replace("[", "").replace("]", "")));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bSpigot: &fhttps://www.spigotmc.org/resources/thirst.24610/"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bGitHub: &fhttps://github.com/GamerKing195/Thirst"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bHelp: &f/thirst help"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&m-----------------------"));
                return true;
            }
            else
            {
                if (args[0].equalsIgnoreCase("view"))
                {
                    if (args.length >= 2)
                    {
                        if (sender instanceof Player)
                        {
                            Player player = (Player) sender;
                            if (!player.hasPermission("thirst.command.view.other") && !player.hasPermission("thirst.*"))
                            {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Thirst.getInstance().getYAMLConfig().noPermissionMessage));
                                return true;
                            }
                        }

                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                        if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore())
                        {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Thirst.getInstance().getYAMLConfig().invalidCommandMessage));
                            return true;
                        }
                        else if (offlinePlayer.hasPlayedBefore())
                        {
                            //if they run /thirst view %player%

                            if (offlinePlayer.getPlayer() != null && sender instanceof Player && offlinePlayer.getPlayer() == sender) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Thirst.getInstance().getYAMLConfig().thirstViewMessage
                                                                                                       .replace("%player%", offlinePlayer.getName())
                                                                                                       .replace("%bar%", ThirstManager.getThirst().getThirstBar(offlinePlayer))
                                                                                                                                 .replace("%percent%", ThirstManager.getThirst().getThirstPercent(offlinePlayer, true))
                                                                                                                                                               .replace("%thirstmessage%", ThirstManager.getThirst().getThirstString(offlinePlayer))
                                                                                                                                                                                                   .replace("%removespeed%", String.valueOf(ThirstManager.getThirst().getThirstData(offlinePlayer).getSpeed()/1000))));
                                return true;
                            }

                            String thirstViewPlayerMessage;

                            if (offlinePlayer.isOnline()) {
                                thirstViewPlayerMessage = Thirst.getInstance().getYAMLConfig().thirstViewPlayerMessage
                                                                  .replace("%player%", offlinePlayer.getName())
                                                                  .replace("%bar%", ThirstManager.getThirst().getThirstBar(offlinePlayer))
                                                                                            .replace("%percent%", ThirstManager.getThirst().getThirstPercent(offlinePlayer, true))
                                                                                                                          .replace("%thirstmessage%", ThirstManager.getThirst().getThirstString(offlinePlayer))
                                                                                                                                                              .replace("%removespeed%", String.valueOf(ThirstManager.getThirst().getThirstData(offlinePlayer).getSpeed()/1000));
                            } else {
                                thirstViewPlayerMessage = Thirst.getInstance().getYAMLConfig().thirstViewPlayerMessage
                                                                  .replace("%player%", offlinePlayer.getName())
                                                                  .replace("%bar%", ThirstManager.getThirst().getThirstBar(offlinePlayer))
                                                                                            .replace("%percent%", ThirstManager.getThirst().getThirstPercent(offlinePlayer, true))
                                                                                                                          .replace("%thirstmessage%", ThirstManager.getThirst().getThirstString(offlinePlayer))
                                                                                                                                                              .replace("%removespeed%", "");
                            }

                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', thirstViewPlayerMessage.replace("%thirstmessage%", ThirstManager.getThirst().getThirstString(offlinePlayer))));
                            return true;
                        }
                    }
                    else
                    {
                        if (sender instanceof Player)
                        {
                            Player player = (Player) sender;
                            if (!player.hasPermission("thirst.command.view") && !player.hasPermission("thirst.*"))
                            {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Thirst.getInstance().getYAMLConfig().noPermissionMessage));
                                return true;
                            }

                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Thirst.getInstance().getYAMLConfig().thirstViewMessage
                                                                                                   .replace("%player%", player.getName())
                                                                                                   .replace("%bar%", ThirstManager.getThirst().getThirstBar(player))
                                                                                                                             .replace("%percent%", ThirstManager.getThirst().getThirstPercent(player, true))
                                                                                                                                                           .replace("%thirstmessage%", ThirstManager.getThirst().getThirstString(player))
                                                                                                                                                                                               .replace("%removespeed%", String.valueOf(ThirstManager.getThirst().getThirstData(player).getSpeed()/1000))));

                            return true;
                        }
                        else
                        {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&1Thirst&8] &bYou must be a player to execute that command!"));
                            return true;
                        }
                    }
                }
                else if (args[0].equalsIgnoreCase("help"))
                {
                    if (sender instanceof Player)
                    {
                        Player player = (Player) sender;
                        if (!player.hasPermission("thirst.command.help") && !player.hasPermission("thirst.*"))
                        {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Thirst.getInstance().getYAMLConfig().noPermissionMessage));
                            return true;
                        }
                    }

                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&m-----------------------"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&1&lThirst &fV"+ Thirst.getInstance().getDescription().getVersion()+" &bby &f"+ Thirst.getInstance().getDescription().getAuthors().toString().replace("[", "").replace("]", "")));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/thirst | &fDisplays basic plugin information."));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/thirst help | &fDisplays this help message."));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/thirst view | &fDisplays your thirst."));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/thirst view [PLAYER] | &fDisplays the thirst of the specified player."));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/thirst list [BIOMES:FOOD:EFFECTS] | &fLists all possible config nodes for the specified category."));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/thirst set [PLAYER] [INT] | &fSet a players thirst."));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/thirst add [PLAYER] [INT] | &fAdd thirst to a player."));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/thirst remove [PLAYER] [INT] | &fRemove thirst from a player."));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/thirst reload | &fReloads the configuration and data files."));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/thirst update | &fAutomatically updates the plugin to the latest version."));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&m-----------------------"));
                }
                else if (args[0].equalsIgnoreCase("update")) {
                    if (sender.isOp() || sender.hasPermission("thirst.command.update") || sender.hasPermission("thirst.*")) {
                        if (sender instanceof Player)
                            UtilActionBar.getInstance().sendActionBar((Player) sender, "&1&lTHIRST &f&lCHECKING FOR UPDATES...");

                        UtilUpdater.getInstance().checkForUpdate();

                        if (UtilUpdater.getInstance().isUpdateAvailable()) {
                            if (sender instanceof Player) {
                                UtilUpdater.getInstance().update((Player) sender);
                            } else {
                                UtilUpdater.getInstance().update(null);
                            }
                        }
                        else {
                            if (sender instanceof Player)
                                UtilActionBar.getInstance().sendActionBar((Player) sender, "&c&lNO UPDATES AVAILABLE.");
                            else
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&1Thirst&8] &cYour version, V"+ Thirst.getInstance().getDescription().getVersion()+" is up to date!"));
                        }

                    }
                    else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Thirst.getInstance().getYAMLConfig().noPermissionMessage));
                    }
                }
                else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
                    if (sender.hasPermission("thirst.command.reload") || sender.hasPermission("thirst.*")) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            ThirstData data = ThirstManager.getThirst().getThirstData(player);

                            if (data.getBar() != null) {
                                data.getBar().removePlayer(player);
                            }
                        }

                        try {
                            boolean previouslyEnabled = Thirst.getInstance().getYAMLConfig().enableSQL;

                            Thirst.getInstance().getYAMLConfig().reload();

                            Thirst.getInstance().getYAMLConfig().load();

                            Thirst.getInstance().getYAMLConfig().save();

                            if (Thirst.getInstance().getYAMLConfig().enableSQL) {
                                if (!previouslyEnabled) {
                                    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                                        if (Thirst.getInstance().getYAMLConfig().displayType.equalsIgnoreCase("SCOREBOARD"))
                                            player.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());

                                        DataConfig.getConfig().writeThirstToFile(player.getUniqueId(), ThirstManager.getThirst().getPlayerThirst(player));
                                    }

                                    DataConfig.getConfig().saveFile();

                                    UtilSQL.getInstance().init();

                                    HashMap<String, Integer> currentFile = DataConfig.getConfig().getFile();

                                    Iterator<Map.Entry<String, Integer>> iterator = currentFile.entrySet().iterator();

                                    StringBuilder statement = new StringBuilder("INSERT INTO TABLENAME (uuid, thirst) VALUES ");

                                    int i = 0;

                                    while (iterator.hasNext()) {
                                        Map.Entry<String, Integer> current = iterator.next();

                                        statement.append("('").append(current.getKey()).append("', ").append(current.getValue()).append(")");

                                        i++;

                                        if (i % 100 == 0) {
                                            UtilSQL.getInstance().runStatement(statement.toString());

                                            statement = new StringBuilder("INSERT INTO TABLENAME (uuid, thirst) VALUES ");
                                        } else if (iterator.hasNext() || i + 1 % 100 == 0) {
                                            statement.append(", ");
                                        }
                                    }

                                    if (i % 100 != 0)
                                        UtilSQL.getInstance().runStatement(statement.toString());
                                }
                            } else {
                                if (previouslyEnabled) {
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            ResultSet rs = UtilSQL.getInstance().runQuery("SELECT * FROM TABLENAME");

                                            HashMap<String, Integer> thirstData = new HashMap<>();

                                            if (rs != null) {
                                                try {
                                                    while (rs.next()) {
                                                        thirstData.put(rs.getString("uuid"), rs.getInt("thirst"));
                                                    }

                                                    rs.close();
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                }
                                            }

                                            UtilSQL.getInstance().runStatementSync("DROP TABLE IF EXISTS TABLENAME CASCADE");

                                            //Switch back to main thread.
                                            new BukkitRunnable() {
                                                @Override
                                                public void run() {
                                                    DataConfig.getConfig().setFile(thirstData);
                                                }
                                            }.runTask(Thirst.getInstance());
                                        }
                                    }.runTaskAsynchronously(Thirst.getInstance());
                                }
                            }

                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&1Thirst&8] &bconfig.yml & thirst_data.yml successfully reloaded!"));
                        } catch (InvalidConfigurationException ex) {
                            Thirst.getInstance().printError(ex, "Error occurred while reloading thirst");

                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&1Thirst&8] &bconfig.yml reloading failed! Check the console for more info."));
                            return true;
                        }

                        PlayerMoveListener.reload();

                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            ThirstManager.getThirst().displayThirst(p);
                        }
                        return true;
                    }
                    else
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Thirst.getInstance().getYAMLConfig().noPermissionMessage));
                }
                else if (args[0].equalsIgnoreCase("list"))
                {
                    if (sender instanceof Player)
                    {
                        Player p = (Player) sender;
                        if (!p.hasPermission("thirst.command.list") && !p.hasPermission("thirst.*"))
                        {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Thirst.getInstance().getYAMLConfig().noPermissionMessage));
                            return true;
                        }
                    }

                    if (args.length < 2)
                    {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Thirst.getInstance().getYAMLConfig().invalidCommandMessage));
                        return true;
                    }

                    if (args[1].equalsIgnoreCase("effects"))
                    {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&m-----------------------"));
                        for (PotionEffectType effect : PotionEffectType.values())
                        {
                            if (effect != null)
                            {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b"+effect.getName()));
                            }
                        }
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&m-----------------------"));
                    }
                    else if (args[1].equalsIgnoreCase("food"))
                    {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&m-----------------------"));
                        for (Material mat : Material.values())
                        {
                            if (mat != null && mat.isEdible())
                            {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b"+mat.toString()));
                            }
                        }
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&m-----------------------"));
                    }
                    else if (args[1].equalsIgnoreCase("biomes"))
                    {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&m-----------------------"));
                        for (Biome biome : Biome.values())
                        {
                            if (biome != null)
                            {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b"+biome.toString()));
                            }
                        }
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&m-----------------------"));
                    }
                    else if (args[1].equalsIgnoreCase("armor"))
                    {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&m-----------------------"));
                        for(Material mat : Material.values())
                        {
                            if (mat != null)
                            {
                                if(mat.toString().contains("LEATHER") || mat.toString().contains("GOLD") || mat.toString().contains("CHAINMAIL") || mat.toString().contains("IRON") || mat.toString().contains("GOLD"))
                                {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b"+mat.toString()));
                                }
                            }
                        }
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&m-----------------------"));
                    }
                }
                else if (args[0].equalsIgnoreCase("set") && args.length > 2) {
                    if (Bukkit.getPlayer(args[1]) != null) {
                        if (NumberUtils.isNumber(args[2])) {
                            if (sender.hasPermission("thirst.*") || sender.hasPermission("thirst.command.set")) {
                                ThirstManager.getThirst().setThirst(Bukkit.getPlayer(args[1]), Integer.valueOf(args[2]));
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&1Thirst&8] &bSet &f"+Bukkit.getPlayer(args[1]).getName()+"'s&b thirst to &f"+Integer.valueOf(args[2])+"&b."));
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Thirst.getInstance().getYAMLConfig().noPermissionMessage));
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Thirst.getInstance().getYAMLConfig().invalidCommandMessage));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&1Thirst&8] &bThat player is not online."));
                    }
                }
                else if (args[0].equalsIgnoreCase("add") && args.length > 2) {
                    if (Bukkit.getPlayer(args[1]) != null) {
                        if (NumberUtils.isNumber(args[2])) {
                            if (sender.hasPermission("thirst.*") || sender.hasPermission("thirst.command.set")) {
                                Player player = Bukkit.getPlayer(args[1]);

                                ThirstManager.getThirst().setThirst(player, ThirstManager.getThirst().getPlayerThirst(player)+Integer.valueOf(args[2]));
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&1Thirst&8] &bAdded &f"+Integer.valueOf(args[2])+" thirst to &f"+player.getName()+"&b."));
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Thirst.getInstance().getYAMLConfig().noPermissionMessage));
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Thirst.getInstance().getYAMLConfig().invalidCommandMessage));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&1Thirst&8] &bThat player is not online."));
                    }
                }
                else if (args[0].equalsIgnoreCase("remove") && args.length > 2) {
                    if (Bukkit.getPlayer(args[1]) != null) {
                        if (NumberUtils.isNumber(args[2])) {
                            if (sender.hasPermission("thirst.*") || sender.hasPermission("thirst.command.set")) {
                                Player player = Bukkit.getPlayer(args[1]);

                                ThirstManager.getThirst().setThirst(player, ThirstManager.getThirst().getPlayerThirst(player)-Integer.valueOf(args[2]));
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&1Thirst&8] &bRemoved &f"+Integer.valueOf(args[2])+" thirst from &f"+player.getName()+"&b."));
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Thirst.getInstance().getYAMLConfig().noPermissionMessage));
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Thirst.getInstance().getYAMLConfig().invalidCommandMessage));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&1Thirst&8] &bThat player is not online."));
                    }
                }
                else
                {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Thirst.getInstance().getYAMLConfig().invalidCommandMessage));
                    return true;
                }
            }
        }
        return true;
    }
}
