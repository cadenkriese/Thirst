package com.gamerking195.dev.thirst.command;

import com.gamerking195.dev.pluginupdater.PremiumUpdater;
import com.gamerking195.dev.pluginupdater.UpdateLocale;
import com.gamerking195.dev.thirst.util.UtilSQL;
import com.gamerking195.dev.thirst.util.UtilUpdater;
import net.cubespace.Yamler.Config.InvalidConfigurationException;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.gamerking195.dev.thirst.Main;
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
                    Player p = (Player) sender;
                    if (!p.hasPermission("thirst.command") && !p.hasPermission("thirst.*"))
                    {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().noPermissionMesage));
                        return true;
                    }
                }

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&m-----------------------"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&1&lThirst &fV"+Main.getInstance().getDescription().getVersion()+" &bby &f"+Main.getInstance().getDescription().getAuthors()));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bSpigot: &fhttps://www.spigotmc.org/resources/thirst.24610/"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bGithub: &fhttps://github.com/GamerKing195/Thirst"));
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
                            Player p = (Player) sender;
                            if (!p.hasPermission("thirst.command.view.other") && !p.hasPermission("thirst.*"))
                            {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().noPermissionMesage));
                                return true;
                            }
                        }

                        OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
                        if (op == null || !op.hasPlayedBefore())
                        {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().invalidCommandMessage));
                            return true;
                        }
                        else if (op.hasPlayedBefore())
                        {
                            //if they run /thirst view %player%
                            String thirstViewPlayerMessage = Main.getInstance().getYAMLConfig().thirstViewPlayerMessage
                                                                     .replace("%player%", op.getName())
                                                                     .replace("%bar%", Thirst.getThirst().getThirstBar(op)
                                                                                               .replace("%percent%", Thirst.getThirst().getThirstPercent(op, true)
                                                                                                                             .replace("%thirstmessage%", Thirst.getThirst().getThirstString(op)
                                                                                                                                                                 .replace("%removespeed%", String.valueOf(Thirst.getThirst().getThirstData(op).getSpeed()/1000)))));

                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', thirstViewPlayerMessage.replace("%thirstmessage%", Thirst.getThirst().getThirstString(op))));
                            return true;
                        }
                    }
                    else
                    {
                        if (sender instanceof Player)
                        {
                            Player p = (Player) sender;
                            if (!p.hasPermission("thirst.command.view") && !p.hasPermission("thirst.*"))
                            {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().noPermissionMesage));
                                return true;
                            }

                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().thirstViewMessage.replace("%player%", p.getName())+Thirst.getThirst().getThirstString(p)));
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
                        Player p = (Player) sender;
                        if (!p.hasPermission("thirst.command.help") && !p.hasPermission("thirst.*"))
                        {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().noPermissionMesage));
                            return true;
                        }
                    }

                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&m-----------------------"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&1&lThirst &fV"+Main.getInstance().getDescription().getVersion()+" &bby &f"+Main.getInstance().getDescription().getAuthors()));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/thirst | &fDisplays basic plugin information."));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/thirst help | &fDisplays this help message."));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/thirst view | &fDisplays your thirst."));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/thirst view [PLAYER] | &fDisplays the thirst of the specified player."));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/thirst list [BIOMES:FOOD:EFFECTS] | &fLists all possible config nodes for the specified category."));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/thirst reload | &fReloads the configuration and data files."));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/thirst update | &fAutomatically updates the plugin to the latest version."));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&m-----------------------"));
                }
                else if (args[0].equalsIgnoreCase("update") && Main.getInstance().getYAMLConfig().enableUpdater) {
                    if (sender.isOp() || sender.hasPermission("thirst.command.update") || sender.hasPermission("thirst.*")) {
                        if (UtilUpdater.getInstance().isUpdateAvailable()) {
                            if (sender instanceof Player) {
                                UtilUpdater.getInstance().update((Player) sender);
                            } else {
                                UtilUpdater.getInstance().update(null);
                            }
                        }
                        else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&1Thirst&8] &cYour version, V"+Main.getInstance().getDescription().getVersion()+" is up to date!"));
                        }

                    }
                    else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().noPermissionMesage));
                    }
                }
                else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl"))
                {
                    for (Player p : Bukkit.getOnlinePlayers())
                    {
                        ThirstData data = Thirst.getThirst().getThirstData(p);

                        if (data.getBar() != null)
                        {
                            data.getBar().removePlayer(p);
                        }
                    }

                    try
                    {
                        boolean previouslyEnabled = Main.getInstance().getYAMLConfig().enableSQL;

                        Main.getInstance().getYAMLConfig().reload();

                        Main.getInstance().getYAMLConfig().load();

                        Main.getInstance().getYAMLConfig().save();

                        if (Main.getInstance().getYAMLConfig().enableSQL) {
                            if (!previouslyEnabled) {
                                for (Player p : Bukkit.getServer().getOnlinePlayers())
                                {
                                    if (Main.getInstance().getYAMLConfig().displayType.equalsIgnoreCase("SCOREBOARD"))
                                        p.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());

                                    DataConfig.getConfig().writeThirstToFile(p.getUniqueId(), Thirst.getThirst().getPlayerThirst(p));
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
                                    }
                                    else if (iterator.hasNext() || i+1 % 100 == 0) {
                                        statement.append(", ");
                                    }
                                }

                                if (i % 100 != 0)
                                    UtilSQL.getInstance().runStatement(statement.toString());
                            }
                        }
                        else {
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
                                            } catch(Exception ex) {
                                                ex.printStackTrace();
                                            }
                                        }

                                        UtilSQL.getInstance().runStatementSync("DROP TABLE IF EXISTS TABLENAME CASCADE");

                                        //Switch back to main thread.
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                Bukkit.broadcastMessage("WRITING DATA TO FILE ("+thirstData.size()+")");
                                                DataConfig.getConfig().setFile(thirstData);
                                            }
                                        }.runTask(Main.getInstance());
                                    }
                                }.runTaskAsynchronously(Main.getInstance());
                            }
                        }

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&1Thirst&8] &bconfig.yml & thirst_data.yml sucsesfully reloaded!"));
                    }
                    catch (InvalidConfigurationException ex)
                    {
                        Main.getInstance().printError(ex, "WARNING, Thirst is now disabled.");

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&1Thirst&8] &bconfig.yml reloading failed! Check the console for more info."));

                        Main.getInstance().disable();
                        return true;
                    }

                    for (Player p : Bukkit.getServer().getOnlinePlayers())
                    {
                        Thirst.getThirst().displayThirst(p);
                    }
                    return true;
                }
                else if (args[0].equalsIgnoreCase("list"))
                {
                    if (sender instanceof Player)
                    {
                        Player p = (Player) sender;
                        if (!p.hasPermission("thirst.command.list") && !p.hasPermission("thirst.*"))
                        {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().noPermissionMesage));
                            return true;
                        }
                    }

                    if (args.length < 2)
                    {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().invalidCommandMessage));
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
                else if (args[0].equalsIgnoreCase("feather")) {
                    if (sender instanceof Player) {
                        UpdateLocale locale = new UpdateLocale();

                        locale.fileName = "FeatherBoard";

                        new PremiumUpdater((Player) sender, Main.getInstance(), 2691, new UpdateLocale(), false);
                    }
                }
                else
                {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().invalidCommandMessage));
                    return true;
                }
            }
        }
        return true;
    }
}
