package wihy.trails.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.bukkit.plugin.Plugin;


import static wihy.trails.Utils.*;

public class TrailCommand implements CommandExecutor {
    private final Plugin plugin;

    public TrailCommand(Plugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        if(command.getName().equalsIgnoreCase("start") && hasPerm(player, "admin")) {
            if (player.hasMetadata("trails")) {
                player.removeMetadata("trails", plugin);
                player.sendMessage(color("&b&lTRAILS&color Trails are now disabled."));
                return true;
            }

            player.setMetadata("trails", new FixedMetadataValue(plugin, "true"));
            player.sendMessage(color("&b&lTRAILS&a Trails are now enabled."));

            float height;

            switch (args[0].toLowerCase()) {
                case "heightofthehead" -> height = 1.9f;
                case "heightoftheabdomen" -> height = 0.9f;
                default -> height = 0f;
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    if(!player.hasMetadata("trails")) cancel();

                    Location loc = player.getLocation().clone();

                    switch (args[2].toLowerCase()) {
                        case "outlinecircle" -> {
                            for (double t = 0; t < 70; t += 0.5) {
                                generateParticle(loc.clone().add((float) Math.sin(t), height, (float) Math.cos(t)), args[1]);
                            }
                        }

                        case "fullcircle" -> {
                            for (float r = 0.1f; r < 1; r += 0.1f) {
                                for (double t = 0; t < 70; t += 0.5) {

                                    float x = r * (float) Math.sin(t);
                                    float z = r * (float) Math.cos(t);

                                    generateParticle(loc.clone().add(x, height, z), args[1]);
                                }
                            }
                        }

                        case "straightline" -> {
                            for (int i = 0; i < 5; i++) {
                                generateParticle(loc.clone().add(0, height, 0), args[1]);
                            }
                        }

                        case "outlinesquare" -> {
                            Location loc1 = loc.clone().add(-1.25, height, -1.25);
                            Location loc2 = loc.clone().add(-1.25, height, -1.25);

                            for (int one = 0; one < 2; one++) {
                                for (float two = 0; two < 2.5; two += 0.1) {
                                    generateParticle(loc1, args[1]);
                                    loc1.add(0, 0, 0.1);
                                }
                                loc1.add(2.5, 0, -2.5);
                            }

                            for (int three = 0; three < 2; three++) {
                                for (float four = 0; four < 2.5; four += 0.1) {
                                    generateParticle(loc2, args[1]);
                                    loc2.add(0.1, 0, 0);
                                }
                                loc2.add(-2.5, 0, 2.5);
                            }
                        }

                        case "fullsquare" -> {
                            loc.add(-1.25, 0, -1.25);
                            float i, j;
                            for (i = 0; i < 2.5; i += 0.1) {
                                generateParticle(loc.clone().add(0, height, 0), args[1]);
                                for (j = 0; j < 2.5; j += 0.1) {
                                    generateParticle(loc.clone().add(0, height, 0), args[1]);
                                    loc.add(0, 0, 0.1);
                                }
                                loc.add(0, 0, -2.6);
                                loc.add(0.1, 0, 0);
                                generateParticle(loc.clone().add(0, height, 0), args[1]);
                            }
                        }

                    }

                }
            }.runTaskTimer(plugin, 1L, 3L);


        }
        return false;
    }
}
