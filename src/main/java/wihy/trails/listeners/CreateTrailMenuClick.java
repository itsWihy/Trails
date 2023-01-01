package wihy.trails.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static wihy.trails.Utils.*;

public class CreateTrailMenuClick implements Listener {

    private final Plugin plugin;

    public CreateTrailMenuClick(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) throws IOException {
        if(event.getWhoClicked().getOpenInventory().getTitle().contains("Create Trail")) {
            event.setCancelled(true);

            final int slot = event.getSlot();
            final Player player = (Player) event.getWhoClicked();
            final String name = strip(player.getOpenInventory().getTitle().replace("Create Trail ", ""));
            final File file = new File(plugin.getDataFolder(), name + ".yml");

            setShiny(player, 8, 26, slot);
            setShiny(player, 29, 33, slot);
            setShiny(player, 36, 44, slot);

            if(slot == 49 && file.delete()) player.performCommand("trails");

            if(slot == 53) {
                player.sendMessage(color("&b&lTRAILS&a This trail was saved! :)"));
                FileWriter fileWriter = new FileWriter(file);

                fileWriter.append("&b&lNAME&7 ").append(name).append("\n")
                .append("&b&lCOLOR&7 ").append(strip(getDisplayName(getShiny(player, 8, 26)))).append("\n")
                .append("&b&lHEIGHT&7 ").append(strip(getDisplayName(getShiny(player, 29, 33)))).append("\n")
                .append("&b&lSHAPE&7 ").append(strip(getDisplayName(getShiny(player, 36, 44))));

                fileWriter.close();
                player.performCommand("trails");

            }
        }
    }
}
