package wihy.trails;

import org.bukkit.plugin.java.JavaPlugin;
import wihy.trails.commands.MainMenu;
import wihy.trails.commands.StartTrail;
import wihy.trails.listeners.CreateTrailMenuClick;
import wihy.trails.listeners.MainMenuClick;
import wihy.trails.listeners.SelectTrailClick;

public final class Trails extends JavaPlugin {

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        getCommand("trails").setExecutor(new MainMenu(this));
        getCommand("startthetrail").setExecutor(new StartTrail(this));

        getServer().getPluginManager().registerEvents(new MainMenuClick(this), this);
        getServer().getPluginManager().registerEvents(new CreateTrailMenuClick(this), this);
        getServer().getPluginManager().registerEvents(new SelectTrailClick(this), this);
    }
}
