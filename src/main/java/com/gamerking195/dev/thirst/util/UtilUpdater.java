package com.gamerking195.dev.thirst.util;

import com.gamerking195.dev.pluginupdater.UpdateLocale;
import com.gamerking195.dev.pluginupdater.Updater;
import com.gamerking195.dev.thirst.Main;
import com.gamerking195.dev.thirst.Thirst;
import com.gamerking195.dev.thirst.ThirstData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

public class UtilUpdater {
    private UtilUpdater() {}
    private static UtilUpdater instance = new UtilUpdater();
    public static UtilUpdater getInstance() {
        return instance;
    }

    private String latestVersion;
    private List<String> testedVersions;

    private boolean updateAvailable;
    private boolean updating;

    /*
     * UTILITIES
     */

    public void init() {
        if (Main.getInstance().getYAMLConfig().enableUpdater) {
            //Initialize the latest version & tested version variables.
            Gson gson = new Gson();
            try {
                //Latest version number.
                String latestVersionInfo = readFrom("https://api.spiget.org/v2/resources/24610/versions/latest");

                Type type = new TypeToken<JsonObject>() {
                }.getType();
                JsonObject object = gson.fromJson(latestVersionInfo, type);

                latestVersion = object.get("name").getAsString();
                updateAvailable = !latestVersion.equals(Main.getInstance().getDescription().getVersion());

                //Tested minecraft versions.

                String basicInfo = readFrom("https://api.spiget.org/v2/resources/24610/");

                Type pluginInfoType = new TypeToken<JsonObject>() {
                }.getType();
                JsonObject pluginInfoObject = gson.fromJson(basicInfo, pluginInfoType);

                Type arrayType = new TypeToken<List<String>>(){}.getType();
                testedVersions = gson.fromJson(pluginInfoObject.get("testedVersions"), arrayType);
            } catch (Exception exception) {
                Main.getInstance().printError(exception, "Error occured whilst pinging spiget.");
            }
        }
    }

    public void update(Player initiater) {
        if (Main.getInstance().getYAMLConfig().enableUpdater && updateAvailable && !updating) {
            UtilActionBar.getInstance().sendActionBar(initiater, ChatColor.translateAlternateColorCodes('&', "&f&lUPDATING &1&lTHIRST &b&lV" + Main.getInstance().getDescription().getVersion() + " &a&l» &b&lV" + latestVersion + " &8[RETREIVING UPDATER]"));

            updating = true;
            try {
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

                    UtilActionBar.getInstance().sendActionBar(initiater, ChatColor.translateAlternateColorCodes('&', "&f&lUPDATING &1&lTHIRST &b&lV" + Main.getInstance().getDescription().getVersion() + " &a&l» &b&lV" + latestVersion + " &8&l| " + bar + " &8&l| &2" + currentPercent + "% &8[DOWNLAODING UPDATER]"));

                    bout.write(data, 0, x);
                }

                bout.close();
                in.close();

                UtilActionBar.getInstance().sendActionBar(initiater, ChatColor.translateAlternateColorCodes('&', "&f&lUPDATING &1&lTHIRST &b&lV" + Main.getInstance().getDescription().getVersion() + " &a&l» &b&lV" + latestVersion + " &8[RUNNING UPDATER]"));

                Plugin target = Bukkit.getPluginManager().loadPlugin(new File(Main.getInstance().getDataFolder().getPath().substring(0, Main.getInstance().getDataFolder().getPath().lastIndexOf("/")) + "/PluginUpdater.jar"));
                target.onLoad();
                Bukkit.getPluginManager().enablePlugin(target);

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

                new Updater(initiater, Main.getInstance(), 24610, locale, true).update();
            } catch (Exception ex) {
                Main.getInstance().printError(ex, "Error occured whilst downloading resource update.");
                UtilActionBar.getInstance().sendActionBar(initiater, ChatColor.translateAlternateColorCodes('&', "&f&lUPDATING &1&lTHIRST &b&lV" + Main.getInstance().getDescription().getVersion() + " &b&l» &1&lV" + latestVersion + " &8[&c&lUPDATE FAILED &7&o(Check Console)&8]"));
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
