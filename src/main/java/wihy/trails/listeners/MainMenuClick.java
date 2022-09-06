package wihy.trails.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.metadata.FixedMetadataValue;
import wihy.trails.Trails;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static wihy.trails.Utils.*;

public class MainMenuClick implements Listener {
    Trails plugin;

    public MainMenuClick(Trails plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) throws IOException {
        if(event.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase("Trails")) {
            event.setCancelled(true);
            if(event.getSlot() == 21) {
                event.getWhoClicked().setMetadata("Editor", new FixedMetadataValue(plugin, true));
                event.getWhoClicked().closeInventory();
                event.getWhoClicked().sendMessage(c("&b&lTRAILS&A Enter a name for the trail."));
            }
            if(event.getSlot() == 23) {
                int i = 0;
                int integer = 0;
                Integer[] ints = {0,1, 2, 3, 4, 5, 6, 7, 8, 45, 46, 47, 48, 49, 50, 51, 52, 53};
                event.getWhoClicked().openInventory(Bukkit.createInventory(null, 54, c("Trail List")));
                setSlots((Player) event.getWhoClicked(), ints, rename(mat("LIGHT_BLUE_STAINED_GLASS_PANE"), "&b"));
                setSlot((Player) event.getWhoClicked(), 49, "ARROW", "&BGo back");
                setSlot((Player) event.getWhoClicked(), 53, "BARRIER", "&bDelete all trails");

                String line;
                String name = "";
                String color = "";
                String height = "";
                String shape = "";

                String strippedName;
                BufferedReader bufferedReader;

                for(File file : plugin.getDataFolder().listFiles()) {
                    if(!(file.getName().equalsIgnoreCase("config.yml"))) {
                        bufferedReader = new BufferedReader(new FileReader(file));

                        while((line = bufferedReader.readLine())!= null) {
                            integer++;
                            if(integer == 1) {
                                name = line;
                            } else if (integer == 2) {
                                color = line;
                            }  else if (integer == 3) {
                                height = line;
                            }  else if (integer == 4) {
                                shape = line;
                            }
                        }
                        strippedName = ChatColor.stripColor(c(name));
                        strippedName = strippedName.substring(5);
                        setSlot((Player) event.getWhoClicked(), i + 9, String.valueOf(getRandomItem()), "&7" + strippedName + " &a" + i, "," + name + "," + color + "," + height + "," + shape + ", ,&aClick to select");
                        i++;
                        bufferedReader.close();
                        integer = 0;
                    }
                }
            }
        }
    }
    @EventHandler
    public void onChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String name = event.getMessage();
        if(player.hasMetadata("Editor")) {
            event.setCancelled(true);
            player.removeMetadata("Editor", plugin);

            File file = new File(plugin.getDataFolder(), name+".yml");

            int extra = 0;
            Integer[] ints = {0,1, 2, 3, 4, 5, 6, 7, 8, 45, 46, 47, 48, 49, 50, 51, 52, 53};
            Integer[] ints2 = {18, 26, 27, 28, 29, 33, 34, 35, 36, 39, 41, 44};
            String[] colors = {"RED", "ORANGE", "YELLOW", "LIME", "GREEN", "LIGHT_BLUE", "BLUE", "CYAN", "PINK", "MAGENTA", "PURPLE", "GRAY", "LIGHT_GRAY", "WHITE", "BROWN", "BLACK"};

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                player.openInventory(Bukkit.createInventory(null, 54, c("Create Trail &b" + event.getMessage())));

                setSlots(player, ints, rename(mat("LIGHT_BLUE_STAINED_GLASS_PANE"), "&b"));
                setSlots(player, ints2, rename(mat("LIGHT_GRAY_STAINED_GLASS_PANE"), "&b"));
                player.getOpenInventory().setItem(49, addLore(rename(mat("ARROW"), "&bGo back"), " ,&7Clicking this will delete this trail.,  ,&eClick to go back"));
                for(int i = 0; i < 16; i++) {
                    setSlot(player, i + 9 + extra, colors[i] + "_DYE", "&b" + colors[i] + " Color");
                    if(i + 9 == 17) {
                        extra++;
                    }
                }

                setSlot(player, 14, "LIGHT_BLUE_DYE", "&bMAROON Color");
                setSlot(player, 16, "CYAN_DYE", "&bTEAL Color");
                setSlot(player, 17, "PINK_DYE", "&bPURPLE Color");
                setSlot(player, 19, "MAGENTA_DYE", "&bFUCHSIA Color");
                setSlot(player, 22, "LIGHT_GRAY_DYE", "&bSILVER Color");
                setSlot(player, 24, "BROWN_DYE", "&bOLIVE Color");

                setSlot(player, 30, "OAK_PLANKS", "&bHeight of the head");
                setSlot(player, 31, "OAK_STAIRS", "&bHeight of the abdomen");
                setSlot(player, 32, "OAK_SLAB", "&bHeight of the feet");

                setSlot(player, 37, "GLASS_PANE", "&bOutline square");
                player.getOpenInventory().setItem(38, selected(rename(mat("GLASS_PANE"), "&bFull square")));

                setSlot(player, 40, "ALLIUM", "&bStraight line");

                setSlot(player, 42, "GOLDEN_APPLE", "&bOutline circle");
                setSlot(player, 43, "ENCHANTED_GOLDEN_APPLE", "&bFull circle");

                setSlot(player, 53, "EMERALD", "&bClick to save");
            } else {
                player.sendMessage(c("&b&lTRAILS&C This trail already exists. Please delete it first or choose another name."));
                player.performCommand("trails");
            }

        }
    }
}
