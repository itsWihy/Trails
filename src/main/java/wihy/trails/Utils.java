package wihy.trails;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Utils {
    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String strip(String string) {
        return ChatColor.stripColor(string);
    }

    public static ItemStack rename(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();

        if(meta == null) return item;

        meta.setDisplayName(color(name));
        item.setItemMeta(meta);
        return item;
    }

    public static String getDisplayName(ItemStack item) {
        if(item == null || item.getType() == Material.AIR || item.getItemMeta() == null) return "";
        return item.getItemMeta().getDisplayName();
    }

    public static boolean hasPerm(Player player, String perm) {
        if(!player.hasPermission("trails." + perm)) {
            player.sendMessage(color("&b&lTRAILS &aYou don't have the required permission to do so."));
            return false;
        }

        return true;

    }
    public static ItemStack mat(String material) {
        return new ItemStack(Material.valueOf(material.toUpperCase().replace(" ", "_")));
    }

    public static void setSlots(Player player, Integer[] slots, ItemStack itemStack) {
        for (Integer slot : slots) {
            player.getOpenInventory().setItem(slot, itemStack);
        }
    }

    public static void setSlots(Player player, String slots, String itemStacks, String names) {
        String[] items = itemStacks.split(",");

        for (int i = 0; i < slots.split(",").length; i++) {
            player.getOpenInventory().setItem(Integer.parseInt(slots.split(",")[i]), rename(mat(items[i]), names.split(",")[i]));
        }
    }

    public static Color fromName(String name) throws ReflectiveOperationException {
        return (Color) Color.class.getDeclaredField(name).get(null);
    }
    public static void generateParticle(Location loc, String color) {
        try {
            if(loc.getWorld() == null) return;

            loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, new Particle.DustOptions(fromName(color), 1));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
    public static void setSlot(Player player, Integer slot, String itemStack, String name) {
        player.getOpenInventory().setItem(slot, rename(mat(itemStack.toUpperCase()), name));
    }

    public static void setSlot(Player player, Integer slot, String itemStack, String name, String lore) {
        player.getOpenInventory().setItem(slot, addLore(rename(mat(itemStack.toUpperCase()), name), lore));
    }

    public static ItemStack selected(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        if(meta == null) return item;

        meta.addEnchant(Enchantment.CHANNELING, 2, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return addLore(item, " ,&a&lSELECTED");
    }

    public static ItemStack unShiny(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        if(meta == null) return item;

        meta.removeEnchant(Enchantment.CHANNELING);
        meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setLore(null);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack addLore(ItemStack item, String toLore) {
        toLore = color(toLore);
        String[] splitLore = toLore.split(",");
        ArrayList<String> lore = new ArrayList<>(Arrays.asList(splitLore));

        ItemMeta meta = item.getItemMeta();

        if(meta == null) return item;

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static void setShiny(Player player, int pos1, int pos2, int loc) {
        ItemStack item = player.getOpenInventory().getItem(loc);

        if(item == null || item.getType() == Material.AIR) return;

        if(loc > pos1 && loc < pos2) {
            for(int i = 0; i < pos2 - pos1; i++) {
                player.getOpenInventory().setItem(i + pos1 + 1, unShiny(Objects.requireNonNull(player.getOpenInventory().getItem(i + pos1 + 1))));
            }

            if(player.getOpenInventory().getItem(loc) != null && !(Objects.requireNonNull(player.getOpenInventory().getItem(loc)).getType().equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE))) {
                player.getOpenInventory().setItem(loc, selected(rename(mat(item.getType().name()), getDisplayName(item))));
            }

        }
    }
    public static ItemStack getShiny(Player player, int pos1, int pos2) {
        ItemStack item = mat("BARRIER");
        for (int i = 0; i < pos2 - pos1; i++) {
            ItemStack itemStack = player.getOpenInventory().getItem(i + pos1 + 1);

            if (itemStack != null && itemStack.getItemMeta() != null && itemStack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
                item = player.getOpenInventory().getItem(i + pos1 + 1);
            }

        }
        return item;
    }
    public static String getLoreByLine(ItemStack item, int line) {
        ItemMeta meta = item.getItemMeta();

        if(meta == null) return "";

        List<String> lore = meta.getLore();
        return lore != null ? lore.get(line) : "";
    }


    private static final List<Material> ITEMS;
    static {
        ITEMS = Arrays.stream(Material.values()).filter(Material::isItem).collect(Collectors.toList());
    }

    public static Material getRandomItem() {
        return ITEMS.get(ThreadLocalRandom.current().nextInt(ITEMS.size()));
    }
}
