package me.liskoh.events.utils;

import me.liskoh.events.EventsPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class YmlMaker {
    public String fileName;
    public File ConfigFile;
    EventsPlugin Plugin;
    private JavaPlugin plugin;
    private FileConfiguration Configuration;

    public YmlMaker(EventsPlugin Plugin) {
        this.Plugin = Plugin;
    }

    public YmlMaker(JavaPlugin plugin, String fileName) {
        if(plugin == null) {
            throw new IllegalArgumentException("plugin cannot be null");
        }
        this.plugin = plugin;
        this.fileName = fileName;
        File dataFolder = plugin.getDataFolder();
        if(dataFolder == null) {
            throw new IllegalStateException();
        }
        this.ConfigFile = new File(dataFolder.toString() + File.separatorChar + this.fileName);
    }

    @SuppressWarnings("deprecation")
	public void reloadConfig() {
        try {
            this.Configuration = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(this.ConfigFile), "UTF-8"));
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        java.io.InputStream defConfigStream = this.plugin.getResource(this.fileName);
        if(defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            this.Configuration.setDefaults(defConfig);
        }
    }

    public FileConfiguration getConfig() {
        if(this.Configuration == null) {
            reloadConfig();
        }
        return this.Configuration;
    }

    public void saveConfig() {
        if((this.Configuration == null) || (this.ConfigFile == null)) {
            return;
        }
        try {
            getConfig().save(this.ConfigFile);
        } catch(IOException ex) {
        }
    }

    public void saveDefaultConfig() {
        if(! this.ConfigFile.exists()) {
            this.plugin.saveResource(this.fileName, false);
        }
    }
}
