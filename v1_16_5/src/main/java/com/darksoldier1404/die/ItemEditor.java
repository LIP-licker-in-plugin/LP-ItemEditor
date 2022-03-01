package com.darksoldier1404.die;

import com.darksoldier1404.die.commands.DIECommand;
import com.darksoldier1404.dppc.DPPCore;
import com.darksoldier1404.dppc.lang.DLang;
import com.darksoldier1404.dppc.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class ItemEditor extends JavaPlugin {
    private DPPCore core;
    public static String prefix = "§f[ §6DIE §f] ";
    private static ItemEditor plugin;
    public static YamlConfiguration config;
    public static DLang lang;
    public static List<String> materials = new ArrayList<>();
    public static List<String> enchants = new ArrayList<>();

    public static ItemEditor getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        Plugin pl = getServer().getPluginManager().getPlugin("DPP-Core");
        if(pl == null) {
            getLogger().warning("DPP-Core 플러그인이 설치되어있지 않습니다.");
            getLogger().warning("DPP-Core plugin is not installed.");
            plugin.setEnabled(false);
            return;
        }
        core = (DPPCore) pl;
        config = ConfigUtils.loadDefaultPluginConfig(plugin);
        lang = new DLang(config.getString("Settings.Lang") == null ? "English" : config.getString("Settings.Lang"), plugin);
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
