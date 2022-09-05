package wihy.trails.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;

import static wihy.trails.Trails.getPlugin;
import static wihy.trails.Utils.*;

public class SelectTrailClick implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(player.getOpenInventory().getTitle().equalsIgnoreCase("Trail List")) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            if(event.getRawSlot() < 45 && event.getRawSlot() > 8) {
                try {
                    if(item.getItemMeta().getLore().size() > 0) {
                        String height;

                        if(getLoreByLine(item, 3).contains("abdomen")) {
                            height = "Heightoftheabdomen";
                        } else if (getLoreByLine(item, 3).contains("head")) {
                            height = "Heightofthehead";
                        } else {
                            height = "g";
                        }
                        String color = (ChatColor.stripColor(getLoreByLine(item, 2).replace("COLOR", "").replace(" ", ""))).replace("Color", "");
                        String shape = (ChatColor.stripColor(getLoreByLine(item, 4).replace("SHAPE", "").replace(" ", "")));
                        player.performCommand("startthetrail " + height + " " + color + " " + shape);
                    }
                } catch (NullPointerException ignored) {
                }

            }
            if(event.getRawSlot() == 49) {
                player.performCommand("trails");
            }
            if(event.getRawSlot() == 53) {
                int i = 0;
                for(File file : getPlugin().getDataFolder().listFiles()) {
                    if(!(file.getName().equalsIgnoreCase("config.yml"))) {
                        file.delete();
                        i++;
                    }
                }
                player.sendMessage(c("&b&lTRAIL&c Deleted " + i + " &cfiles"));
                player.performCommand("trails");
            }
        }
    }
}
