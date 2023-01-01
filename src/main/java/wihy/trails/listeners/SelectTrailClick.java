package wihy.trails.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Objects;

import static wihy.trails.Utils.strip;
import static wihy.trails.Utils.color;
import static wihy.trails.Utils.getLoreByLine;

public class SelectTrailClick implements Listener {
    private final Plugin plugin;

    public SelectTrailClick(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(player.getOpenInventory().getTitle().equalsIgnoreCase("Trail List")) {
            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();

            if(event.getRawSlot() < 45 && event.getRawSlot() > 8) {
                try {
                    if(item == null || item.getItemMeta() == null || item.getItemMeta().getLore() == null || item.getItemMeta().getLore().size() < 1) return;

                    String height;

                    if(getLoreByLine(item, 3).contains("abdomen")) {
                        height = "Heightoftheabdomen";
                    } else if (getLoreByLine(item, 3).contains("head")) {
                        height = "Heightofthehead";
                    } else {
                        height = "g";
                    }

                    String color = (strip(getLoreByLine(item, 2).replace("COLOR", "").replace(" ", ""))).replace("Color", "");
                    String shape = (strip(getLoreByLine(item, 4).replace("SHAPE", "").replace(" ", "")));

                    player.performCommand("start " + height + " " + color + " " + shape);

                } catch (NullPointerException ignored) {

                }

            }

            if(event.getRawSlot() == 49)
                player.performCommand("trails");

            if(event.getRawSlot() == 53) {
                int i = 0;

                for(File file : Objects.requireNonNull(plugin.getDataFolder().listFiles())) {
                    if(file.getName().equalsIgnoreCase("config.yml") || !file.delete())
                        continue;

                    i++;
                }

                player.sendMessage(color("&b&lTRAIL&color Deleted " + i + " &cfiles"));
                player.performCommand("trails");
            }
        }
    }
}
