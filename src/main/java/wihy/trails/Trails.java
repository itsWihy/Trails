package wihy.trails;

import org.bukkit.plugin.java.JavaPlugin;
import wihy.trails.commands.OpenMenuCommand;
import wihy.trails.commands.TrailCommand;
import wihy.trails.listeners.CreateTrailMenuClick;
import wihy.trails.listeners.MainMenuClick;
import wihy.trails.listeners.SelectTrailClick;

import java.util.Objects;

public final class Trails extends JavaPlugin {

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        Objects.requireNonNull(getCommand("trails")).setExecutor(new OpenMenuCommand());
        Objects.requireNonNull(getCommand("start")).setExecutor(new TrailCommand(this));

        getServer().getPluginManager().registerEvents(new MainMenuClick(this), this);
        getServer().getPluginManager().registerEvents(new CreateTrailMenuClick(this), this);
        getServer().getPluginManager().registerEvents(new SelectTrailClick(this), this);
    }
}
