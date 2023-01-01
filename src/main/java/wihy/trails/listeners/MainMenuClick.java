package wihy.trails.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

import static wihy.trails.Utils.*;

public class MainMenuClick implements Listener {
    private final Plugin plugin;

    public MainMenuClick(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) throws IOException {
        if(event.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase("Trails")) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();

            if(event.getSlot() == 21) {
                player.setMetadata("Editor", new FixedMetadataValue(plugin, true));
                player.closeInventory();
                player.sendMessage(color("&b&lTRAILS&A Enter a name for the trail."));
            }

            if(event.getSlot() == 23) {
                int i = 0, integer = 0;

                final Integer[] ints = {0,1, 2, 3, 4, 5, 6, 7, 8, 45, 46, 47, 48, 49, 50, 51, 52, 53};

                player.openInventory(Bukkit.createInventory(null, 54, ("Trail List")));

                setSlots(player, ints, rename(mat("LIGHT_BLUE_STAINED_GLASS_PANE"), "&b"));
                setSlot(player, 49, "ARROW", "&BGo back");
                setSlot(player, 53, "BARRIER", "&bDelete all trails");

                String line, strippedName, name = "", color = "", height = "", shape = "";

                BufferedReader bufferedReader;

                for(File file : Objects.requireNonNull(plugin.getDataFolder().listFiles())) {
                    if(file.getName().equalsIgnoreCase("config.yml")) continue;

                    bufferedReader = new BufferedReader(new FileReader(file));

                    while((line = bufferedReader.readLine())!= null) {
                        integer++;

                        switch (integer) {
                            case 1 -> name = line;
                            case 2 -> color = line;
                            case 3 -> height = line;
                            case 4 -> shape = line;
                        }
                    }

                    //todo fix not working (File creation>)

                    strippedName = strip(color(name));

                    if(strippedName.length() > 4) strippedName = strippedName.substring(5);

                    setSlot(player, i + 9, String.valueOf(getRandomItem()), "&7" + strippedName + " &a" + i, "," + name + "," + color + "," + height + "," + shape + ", ,&aClick to select");
                    bufferedReader.close();

                    i++;
                    integer = 0;
                }
            }
        }
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final String name = event.getMessage();

        if(!player.hasMetadata("Editor")) return;

        event.setCancelled(true);
        player.removeMetadata("Editor", plugin);

        File file = new File(plugin.getDataFolder(), name+".yml");

        final int[] extra = {0};
        Integer[] ints = {0,1, 2, 3, 4, 5, 6, 7, 8, 45, 46, 47, 48, 49, 50, 51, 52, 53};
        Integer[] ints2 = {18, 26, 27, 28, 29, 33, 34, 35, 36, 39, 41, 44};
        String[] colors = {"RED", "ORANGE", "YELLOW", "LIME", "GREEN", "LIGHT_BLUE", "BLUE", "CYAN", "PINK", "MAGENTA", "PURPLE", "GRAY", "LIGHT_GRAY", "WHITE", "BROWN", "BLACK"};

        if (!file.exists()) {
            System.out.println("Waw");
            try {
                if(file.createNewFile())
                    player.sendMessage(color("&b&lTRAILS&a New file created"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.openInventory(Bukkit.createInventory(null, 54, color("Create Trail &b" + event.getMessage())));

                    setSlots(player, ints, rename(mat("LIGHT_BLUE_STAINED_GLASS_PANE"), "&b"));
                    setSlots(player, ints2, rename(mat("LIGHT_GRAY_STAINED_GLASS_PANE"), "&b"));
                    player.getOpenInventory().setItem(49, addLore(rename(mat("ARROW"), "&bGo back"), " ,&7Clicking this will delete this trail.,  ,&eClick to go back"));

                    for(int i = 0; i < 16; i++) {
                        setSlot(player, i + 9 + extra[0], colors[i] + "_DYE", "&b" + colors[i] + " Color");
                        if(i + 9 == 17) {
                            extra[0]++;
                        }
                    }

                    setSlots(player,
                            "14,16,17,19,22,24,30,31,32,37,40,42,43,53",
                            "light blue dye,cyan dye,pink dye,magenta dye,light gray dye,brown dye,oak planks,oak stairs,oak slab,glass pane,allium,golden apple,enchanted golden apple,emerald",
                            "&bMAROON Color,&bTEAL Color,&bPURPLE Color,&bFUCHSIA Color,&bSILVER Color,&bOLIVE Color,&bHeight of the head,&bHeight of the abdomen,&bHeight of the feet,&bOutline square,&bStraight line,&bOutline circle,&bFull circle,&bClick to save");

                    player.getOpenInventory().setItem(38, selected(rename(mat("GLASS_PANE"), "&bFull square")));
                }
            }.runTask(plugin);

        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendMessage(color("&b&lTRAILS&C This trail already exists. Please delete it first or choose another name."));
                    player.performCommand("trails");
                }
            }.runTask(plugin);
        }

    }
}
