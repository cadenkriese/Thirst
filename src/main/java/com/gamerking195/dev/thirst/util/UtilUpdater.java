package com.gamerking195.dev.thirst.util;

import com.gamerking195.dev.pluginupdater.UpdateLocale;
import com.gamerking195.dev.pluginupdater.Updater;
import com.gamerking195.dev.thirst.Main;
import com.gamerking195.dev.thirst.Thirst;
import com.gamerking195.dev.thirst.ThirstData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilUpdater {
    private UtilUpdater() {}
    private static UtilUpdater instance = new UtilUpdater();
    public static UtilUpdater getInstance() {
        return instance;
    }

    private String latestVersion;
    private String updateInfo;
    private List<String> testedVersions;

    private boolean updateAvailable;
    private boolean updating;

    private Gson gson = new Gson();

    /*
     * UTILITIES
     */

    public void init() {
        if (Main.getInstance().getYAMLConfig().enableUpdater) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    checkForUpdate();

                    if (updateAvailable) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.isOp() || player.hasPermission("thirst.command.update") || player.hasPermission("thirst.*")) {
                                String currentVersion = Main.getInstance().getDescription().getVersion();
                                String mcVersion = Bukkit.getServer().getClass().getPackage().getName();
                                mcVersion = mcVersion.substring(mcVersion.lastIndexOf(".") + 1).substring(1, mcVersion.length()-3).replace("_", ".");

                                player.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&f&m------------------------------"));
                                player.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&1&lThirst &fV" + currentVersion + " &bby &f" + Main.getInstance().getDescription().getAuthors()));
                                player.sendMessage("");
                                player.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&bThere is a Thirst update available!"));
                                player.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&bVersion: &f" + latestVersion));
                                player.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&bUpdates: \n" + UtilUpdater.getInstance().getUpdateInfo()));
                                player.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&bSupported MC Versions: &f" + StringUtils.join(testedVersions, ", ")));
                                if (!testedVersions.contains(mcVersion))
                                    player.sendMessage(net.md_5.bungee.api.ChatColor.RED+"Warning your current version, "+mcVersion+", is not supported by this update, there may be unexpected bugs!");
                                player.sendMessage("");

                                TextComponent accept = new TextComponent("[CLICK TO UPDATE]");
                                accept.setColor(net.md_5.bungee.api.ChatColor.DARK_AQUA);
                                accept.setBold(true);
                                accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/thirst update"));
                                accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&1&lTHIRST &bV" + currentVersion + " &a&l» &bV" + latestVersion+"\n&b\n&b    CLICK TO UPDATE")).create()));

                                player.spigot().sendMessage(accept);

                                player.sendMessage("");
                                player.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&f&m------------------------------"));
                            }
                        }
                    }
                }
            }.runTaskTimer(Main.getInstance(), 0, 24000L);
        }
    }

    public void checkForUpdate() {
        try {
            //Latest version number.
            String latestVersionInfo = readFrom("https://api.spiget.org/v2/resources/24610/versions/latest");

            Type type = new TypeToken<JsonObject>() {
            }.getType();
            JsonObject object = gson.fromJson(latestVersionInfo, type);

            latestVersion = object.get("name").getAsString();
            updateAvailable = !latestVersion.equals(Main.getInstance().getDescription().getVersion());

            if (updateAvailable) {
                //Supported mc versions

                Type objectType = new TypeToken<JsonObject>(){}.getType();

                JsonObject pluginInfoObject = gson.fromJson(readFrom("https://api.spiget.org/v2/resources/24610/"), objectType);

                testedVersions = gson.fromJson(pluginInfoObject.get("testedVersions"), new TypeToken<List<String>>(){}.getType());

                //Update description

                JsonObject latestUpdateObject = gson.fromJson(readFrom("https://api.spiget.org/v2/resources/24610/updates/latest"), objectType);

                String descriptionBase64 = gson.fromJson(latestUpdateObject.get("description"), new TypeToken<String>(){}.getType());
                String decodedDescription = new String(Base64.getDecoder().decode(descriptionBase64));

                Pattern pat = Pattern.compile("<li>(.*)</li>");

                Matcher match = pat.matcher(decodedDescription);

                StringBuilder sb = new StringBuilder();

                while (match.find())
                    sb.append(ChatColor.WHITE).append(" - ").append(match.group(1)).append("\n");

                updateInfo = sb.toString();
            }
        } catch (Exception exception) {
            Main.getInstance().printError(exception, "Error occurred whilst pinging spiget.");
        }
    }

    public void update(Player initiator) {
        if (Main.getInstance().getYAMLConfig().enableUpdater && updateAvailable && !updating) {
            UtilActionBar.getInstance().sendActionBar(initiator, ChatColor.translateAlternateColorCodes('&', "&f&lUPDATING &1&lTHIRST &b&lV" + Main.getInstance().getDescription().getVersion() + " &a&l» &b&lV" + latestVersion + " &8[RETREIVING UPDATER]"));

            updating = true;
            boolean delete = true;
            try {
                if (!Bukkit.getPluginManager().isPluginEnabled("PluginUpdater")) {
                    delete = false;
                    //Download AutoUpdaterAPI
                    URL url = new URL("https://api.spiget.org/v2/resources/39719/download");
                    HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                    httpConnection.setRequestProperty("User-Agent", "SpigetResourceUpdater");
                    long completeFileSize = httpConnection.getContentLength();

                    BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
                    FileOutputStream fos = new java.io.FileOutputStream(new File(Main.getInstance().getDataFolder().getPath().substring(0, Main.getInstance().getDataFolder().getPath().lastIndexOf("/")) + "/PluginUpdater.jar"));
                    BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);

                    byte[] data = new byte[1024];
                    long downloadedFileSize = 0;
                    int x;
                    while ((x = in.read(data, 0, 1024)) >= 0) {
                        downloadedFileSize += x;

                        final int currentProgress = (int) ((((double) downloadedFileSize) / ((double) completeFileSize)) * 15);

                        final String currentPercent = String.format("%.2f", (((double) downloadedFileSize) / ((double) completeFileSize)) * 100);

                        String bar = "&a:::::::::::::::";

                        bar = bar.substring(0, currentProgress + 2) + "&c" + bar.substring(currentProgress + 2);

                        UtilActionBar.getInstance().sendActionBar(initiator, ChatColor.translateAlternateColorCodes('&', "&f&lUPDATING &1&lTHIRST &b&lV" + Main.getInstance().getDescription().getVersion() + " &a&l» &b&lV" + latestVersion + " &8&l| " + bar + " &8&l| &2" + currentPercent + "% &8[DOWNLAODING UPDATER]"));

                        bout.write(data, 0, x);
                    }

                    bout.close();
                    in.close();

                    UtilActionBar.getInstance().sendActionBar(initiator, ChatColor.translateAlternateColorCodes('&', "&f&lUPDATING &1&lTHIRST &b&lV" + Main.getInstance().getDescription().getVersion() + " &a&l» &b&lV" + latestVersion + " &8[RUNNING UPDATER]"));

                    Plugin target = Bukkit.getPluginManager().loadPlugin(new File(Main.getInstance().getDataFolder().getPath().substring(0, Main.getInstance().getDataFolder().getPath().lastIndexOf("/")) + "/PluginUpdater.jar"));
                    target.onLoad();
                    Bukkit.getPluginManager().enablePlugin(target);
                }


                //Save player data

                for (Player p : Bukkit.getOnlinePlayers()) {
                    ThirstData thirstData = Thirst.getThirst().getThirstData(p);

                    if (thirstData.getBar() != null) {
                        thirstData.getBar().removePlayer(p);
                    }
                }

                Main.getInstance().getYAMLConfig().save();

                UpdateLocale locale = new UpdateLocale();
                locale.fileName = "Thirst-" + latestVersion;

                new Updater(initiator, Main.getInstance(), 24610, locale, delete).update();
            } catch (Exception ex) {
                Main.getInstance().printError(ex, "Error occurred whilst downloading resource update.");
                UtilActionBar.getInstance().sendActionBar(initiator, ChatColor.translateAlternateColorCodes('&', "&f&lUPDATING &1&lTHIRST &b&lV" + Main.getInstance().getDescription().getVersion() + " &b&l» &1&lV" + latestVersion + " &8[&c&lUPDATE FAILED &7&o(Check Console)&8]"));
            }
        }
    }

    /*
     * GETTERS
     */

    public String getLatestVersion() {
        return latestVersion;
    }

    public List<String> getTestedVersions() {
        return testedVersions;
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    /*
     * PRIVATE UTILITIES
     */

    private String readFrom(String url) throws IOException
    {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }
    }
}
