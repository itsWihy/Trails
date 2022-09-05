package wihy.trails;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import wihy.trails.commands.MainMenu;
import wihy.trails.commands.StartTrail;
import wihy.trails.listeners.CreateTrailMenuClick;
import wihy.trails.listeners.MainMenuClick;
import wihy.trails.listeners.SelectTrailClick;

public final class Trails extends JavaPlugin {
    static Plugin plugin;
    public static Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        getConfig().options().copyDefaults(true);
        saveConfig();

        getCommand("trails").setExecutor(new MainMenu());
        getCommand("startthetrail").setExecutor(new StartTrail());

        getServer().getPluginManager().registerEvents(new MainMenuClick(), this);
        getServer().getPluginManager().registerEvents(new CreateTrailMenuClick(), this);
        getServer().getPluginManager().registerEvents(new SelectTrailClick(), this);
    }
}
