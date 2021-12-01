package com.darksoldier1404.die;

import com.darksoldier1404.die.commands.DIECommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ItemEditor extends JavaPlugin {
    public static String prefix = "§f[ §6DIE §f] ";
    private static ItemEditor plugin;
    public static List<String> materials = new ArrayList<>();
    public static List<String> enchants = new ArrayList<>();

    public static ItemEditor getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        getCommand("die").setExecutor(new DIECommand());
        Bukkit.getScheduler().runTask(plugin, this::initMaterialList);
    }

    public void initMaterialList() {
        for (Material m : Material.values()) {
            materials.add(m.name());
        }
        for (Enchantment e : Enchantment.values()) {
            enchants.add(e.getName());
        }
    }
}
