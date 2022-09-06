package wihy.trails.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import wihy.trails.Trails;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static wihy.trails.Utils.*;

public class CreateTrailMenuClick implements Listener {

    Trails plugin;

    public CreateTrailMenuClick(Trails plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) throws IOException {
        if(event.getWhoClicked().getOpenInventory().getTitle().contains("Create Trail")) {

            Player player = (Player) event.getWhoClicked();
            String name = player.getOpenInventory().getTitle().replace("Create Trail ", "");
            name = ChatColor.stripColor(name);
            File file = new File(plugin.getDataFolder(), name + ".yml");

            event.setCancelled(true);

            setShiny(player, 8, 26, event.getRawSlot());
            setShiny(player, 29, 33, event.getRawSlot());
            setShiny(player, 36, 44, event.getRawSlot());

            if(event.getRawSlot() == 49) {
                file.delete();
                player.performCommand("trails");
            }
            if(event.getRawSlot() == 53) {
                player.sendMessage(c("&b&lTRAILS&a This trail was saved! :)"));
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.append("&b&lNAME&7 ").append(name).append("\n");
                fileWriter.append("&b&lCOLOR&7 ").append(ChatColor.stripColor(getShiny(player, 8, 26).getItemMeta().getDisplayName())).append("\n");
                fileWriter.append("&b&lHEIGHT&7 ").append(ChatColor.stripColor(getShiny(player, 29, 33).getItemMeta().getDisplayName())).append("\n");
                fileWriter.append("&b&lSHAPE&7 ").append(ChatColor.stripColor(getShiny(player, 36, 44).getItemMeta().getDisplayName()));
                fileWriter.close();
                player.performCommand("trails");

            }
        }
    }
}
