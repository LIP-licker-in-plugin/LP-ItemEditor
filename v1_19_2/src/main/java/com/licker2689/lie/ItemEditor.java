package com.licker2689.lie;

import com.licker2689.lie.commands.LIECommand;
import com.licker2689.lpc.LPCore;
import com.licker2689.lpc.lang.LLang;
import com.licker2689.lpc.utils.ConfigUtils;
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
    private LPCore core;
    public static String prefix = "§f[ §6DIE §f] ";
    private static ItemEditor plugin;
    public static YamlConfiguration config;
    public static LLang lang;
    public static List<String> materials = new ArrayList<>();
    public static List<String> enchants = new ArrayList<>();

    public static ItemEditor getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        Plugin pl = getServer().getPluginManager().getPlugin("LP-Core");
        if(pl == null) {
            getLogger().warning("LP-Core 플러그인이 설치되어있지 않습니다.");
            getLogger().warning("LP-Core plugin is not installed.");
            plugin.setEnabled(false);
            return;
        }
        core = (LPCore) pl;
        config = ConfigUtils.loadDefaultPluginConfig(plugin);
        lang = new LLang(config.getString("Settings.Lang") == null ? "English" : config.getString("Settings.Lang"), plugin);
        getCommand("die").setExecutor(new LIECommand());
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
