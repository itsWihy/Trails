package wihy.trails.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import wihy.trails.Trails;

import static wihy.trails.Utils.c;
import static wihy.trails.Utils.generateParticle;

public class StartTrail implements CommandExecutor {
    Trails plugin;

    public StartTrail(Trails plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if(command.getName().equalsIgnoreCase("startthetrail")) {
            if(player.hasPermission("progamer")) {
                if(!(player.hasMetadata("trails"))) {
                    player.setMetadata("trails", new FixedMetadataValue(plugin, "true"));
                    player.sendMessage(c("&b&lTRAILS&a Trails are now enabled."));

                    float height;
                    if(args[0].equalsIgnoreCase("Heightofthehead")) {
                        height = 1.9f;
                    } else if(args[0].equalsIgnoreCase("Heightoftheabdomen")) {
                        height = 0.9f;
                    } else {
                        height = 0;
                    }
                    @NotNull BukkitTask runnable = new BukkitRunnable() {
                        @Override
                        public void run() {

                            if (player.hasMetadata("trails")) {
                                Location loc = player.getLocation().clone();
                                if(args[2].equals("Outlinecircle")) {
                                    for (double t = 0; t < 70; t += 0.5) {
                                        float x = (float) Math.sin(t);
                                        float z = (float) Math.cos(t);
                                        generateParticle(loc.clone().add(x, height, z), args[1]);
                                    }
                                } else if (args[2].equals("Fullcircle")) {
                                    for (float r = 0.1f; r < 1; r += 0.1f) {
                                        for (double t = 0; t < 70; t += 0.5) {

                                            float x = r * (float) Math.sin(t);
                                            float z = r * (float) Math.cos(t);

                                            generateParticle(loc.clone().add(x, height, z), args[1]);
                                        }
                                    }
                                } else if (args[2].equals("Straightline")) {
                                    for(int i = 0; i < 5; i++) {
                                        generateParticle(loc.clone().add(0, height, 0), args[1]);
                                    }

                                } else if (args[2].equals("Outlinesquare")) {

                                    Location loc1 = loc.clone().add(-1.25, height, -1.25);
                                    Location loc2 = loc.clone().add(-1.25, height, -1.25);

                                    for(int one = 0; one < 2; one++) {
                                        for(float two = 0; two < 2.5; two+=0.1) {
                                            generateParticle(loc1, args[1]);
                                            loc1.add(0, 0, 0.1);
                                        }
                                        loc1.add(2.5, 0, -2.5);
                                    }
                                    for(int three = 0; three < 2; three++) {
                                        for(float four = 0; four < 2.5; four+=0.1) {
                                            generateParticle(loc2, args[1]);
                                            loc2.add(0.1, 0, 0);
                                        }
                                        loc2.add(-2.5, 0, 2.5);
                                    }

                                } else if (args[2].equals("Fullsquare")) {
                                    loc.add(-1.25, 0, -1.25);
                                    float i, j;
                                    for(i = 0; i < 2.5; i+=0.1) {
                                        generateParticle(loc.clone().add(0, height, 0), args[1]);
                                        for(j = 0; j < 2.5; j+=0.1) {
                                            generateParticle(loc.clone().add(0, height, 0), args[1]);
                                            loc.add(0,0,0.1);
                                        }
                                        loc.add(0,0,-2.6);
                                        loc.add(0.1,0,0);
                                        generateParticle(loc.clone().add(0, height, 0), args[1]);
                                    }
                                }
                            } else {
                                cancel();
                            }

                        }
                    }.runTaskTimer(plugin, 1L, 3L);
                } else {
                    player.removeMetadata("trails", plugin);
                    player.sendMessage(c("&b&lTRAILS&c Trails are now disabled."));
                }
            }
        }
        return false;
    }
}
