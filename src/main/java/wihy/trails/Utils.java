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
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Utils {
    public static String c(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    public static ItemStack rename(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(c(name));
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack mat(String material) {
        return new ItemStack(Material.valueOf(material));
    }
    public static void setSlots(Player player, Integer[] slots, ItemStack itemStack) {
        for(int i = 0; i < slots.length; i++) {
            player.getOpenInventory().setItem(slots[i], itemStack);
        }
    }
    public static Color fromName(String name) throws ReflectiveOperationException {
        return (Color) Color.class.getDeclaredField(name).get(null);
    }
    public static void generateParticle(Location loc, String color) {
        try {
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
        meta.addEnchant(Enchantment.CHANNELING, 2, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return addLore(item, " ,&a&lSELECTED");
    }
    public static ItemStack unShiny(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.removeEnchant(Enchantment.CHANNELING);
        meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setLore(null);
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack addLore(ItemStack item, String toLore) {
        toLore = c(toLore);
        String[] splitLore = toLore.split(",");
        ArrayList<String> lore = new ArrayList<>(Arrays.asList(splitLore));

        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public static void setShiny(Player player, int pos1, int pos2, int loc) {
        ItemStack item = player.getOpenInventory().getItem(loc);
        if(loc > pos1 && loc < pos2) {
            for(int i = 0; i < pos2 - pos1; i++) {
                player.getOpenInventory().setItem(i + pos1 + 1, unShiny(player.getOpenInventory().getItem(i + pos1 + 1)));
            }
            if(!(player.getOpenInventory().getItem(loc).getType().equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE))) {
                player.getOpenInventory().setItem(loc, selected(rename(mat(item.getType().name()), item.getItemMeta().getDisplayName())));
            }
        }
    }
    public static ItemStack getShiny(Player player, int pos1, int pos2) {
        ItemStack item = mat("BARRIER");
        for (int i = 0; i < pos2 - pos1; i++) {
            if (player.getOpenInventory().getItem(i + pos1 + 1).getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
                item = player.getOpenInventory().getItem(i + pos1 + 1);
            }
        }
        return item;
    }
    public static String getLoreByLine(ItemStack item, int line) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        return lore.get(line);
    }


    private static final List<Material> ITEMS;
    static {
        ITEMS = Arrays.stream(Material.values()).filter(Material::isItem).collect(Collectors.toList());
    }

    public static Material getRandomItem() {
        return ITEMS.get(ThreadLocalRandom.current().nextInt(ITEMS.size()));
    }
}
