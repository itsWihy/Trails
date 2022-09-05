package wihy.trails.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static wihy.trails.Utils.*;

public class MainMenu implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if(command.getName().equalsIgnoreCase("trails")) {
            player.hasPermission("trails.use");
            player.openInventory(Bukkit.createInventory(null, 54, c("Trails")));

            Integer[] i = {0,1, 2, 3, 4, 5, 6, 7, 8, 45, 46, 47, 48, 49, 50, 51, 52, 53};
            setSlots(player, i, rename(mat("LIGHT_BLUE_STAINED_GLASS_PANE"), "&b"));

            setSlot(player, 21, "EMERALD", "&BCreate trail");
            setSlot(player, 23, "SPECTRAL_ARROW", "&bSelect trail");
        }
        return true;
    }


}
