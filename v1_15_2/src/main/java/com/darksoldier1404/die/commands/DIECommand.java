package com.darksoldier1404.die.commands;

import com.darksoldier1404.die.ItemEditor;
import com.darksoldier1404.die.functions.DIEFunction;
import com.darksoldier1404.dppc.utils.NBT;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SuppressWarnings("all")
public class DIECommand implements CommandExecutor, TabCompleter {
    private final String prefix = ItemEditor.prefix;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + "게임내에서만 사용할 수 있습니다.");
            return false;
        }
        Player p = (Player) sender;
        if (!p.isOp()) {
            p.sendMessage(prefix + "권한이 없습니다.");
            return false;
        }
        if (args.length == 0) {
            p.sendMessage(prefix + "/die name <Display Name>");
            p.sendMessage(prefix + "/die lore add <lore>");
            p.sendMessage(prefix + "/die lore del <line>");
            p.sendMessage(prefix + "/die lore set <line> <lore>");
            p.sendMessage(prefix + "/die type <Material>");
            p.sendMessage(prefix + "/die enc add <enchant> <level>");
            p.sendMessage(prefix + "/die enc del <enchant>");
            p.sendMessage(prefix + "/die nbt del <key>");
            p.sendMessage(prefix + "/die nbt set <key> <value>");
            p.sendMessage(prefix + "/die nbt get <key>");
            p.sendMessage(prefix + "/die nbt list");
            return false;
        }
        if (p.getInventory().getItemInMainHand().getType() == Material.AIR) {
            p.sendMessage(prefix + "편집할 아이템을 손에 들고 있어야 합니다.");
            return false;
        }
        ItemStack item = p.getInventory().getItemInMainHand();
        ItemMeta im = item.getItemMeta();
        if (args[0].equals("name")) {
            if (args.length == 1) {
                p.sendMessage(prefix + "아이템의 이름을 입력해주세요.");
                return false;
            }
            im.setDisplayName(DIEFunction.getColoredText(args, 1));
            item.setItemMeta(im);
            return false;
        }
        if (args[0].equals("lore")) {
            if (args.length == 1) {
                p.sendMessage(prefix + "로어 옵션을 선택해주세요.");
                return false;
            }
            if (args[1].equals("add")) {
                if (!im.hasLore()) {
                    im.setLore(Collections.singletonList(DIEFunction.getColoredText(args, 2)));
                    item.setItemMeta(im);
                    return false;
                }
                List<String> lore = im.getLore();
                lore.add(DIEFunction.getColoredText(args, 2));
                im.setLore(lore);
                item.setItemMeta(im);
                return false;
            }
            if (args[1].equals("set")) {
                if (args.length == 2) {
                    p.sendMessage(prefix + "로어의 번호를 입력해주세요.");
                    return false;
                }
                if (args.length == 3) {
                    p.sendMessage(prefix + "로어를 입력해주세요.");
                    return false;
                }
                List<String> lore = im.getLore();
                lore.set(Integer.parseInt(args[2]), DIEFunction.getColoredText(args, 3));
                im.setLore(lore);
                item.setItemMeta(im);
                return false;
            }
            if (args[1].equals("del")) {
                if (args.length == 2) {
                    p.sendMessage(prefix + "삭제할 로어의 인덱스를 입력해주세요.");
                    return false;
                }
                if (!im.hasLore()) {
                    p.sendMessage(prefix + "로어가 없습니다.");
                    return false;
                }
                try {
                    List<String> lore = im.getLore();
                    assert lore != null;
                    lore.remove(Integer.parseInt(args[2]));
                    im.setLore(lore);
                    item.setItemMeta(im);
                } catch (Exception e) {
                    p.sendMessage(prefix + "잘못된 인덱스입니다.");
                    return false;
                }
                return false;
            }
        }
        if (args[0].equals("enc")) {
            if (args.length == 1) {
                p.sendMessage(prefix + "옵션을 선택해주세요.");
                return false;
            }
            if (args[1].equals("add")) {
                if (args.length == 2) {
                    p.sendMessage(prefix + "추가할 인첸트를 입력해주세요.");
                    return false;
                }
                if (args.length == 3) {
                    p.sendMessage(prefix + "추가할 인첸트의 레벨을 입력해주세요.");
                    return false;
                }
                try {
                    im.addEnchant(Enchantment.getByName(args[2]), Integer.parseInt(args[3]), true);
                    item.setItemMeta(im);
                    return false;
                } catch (Exception e) {
                    p.sendMessage(prefix + "잘못된 인첸트입니다.");
                    return false;
                }
            }
            if (args[1].equals("del")) {
                if (args.length == 2) {
                    p.sendMessage(prefix + "삭제할 인첸트를 입력해주세요.");
                    return false;
                }
                try {
                    im.removeEnchant(Enchantment.getByName(args[2]));
                    item.setItemMeta(im);
                    return false;
                } catch (Exception e) {
                    p.sendMessage(prefix + "잘못된 인첸트입니다.");
                }
            }
        }
        if (args[0].equals("type")) {
            if (args.length == 1) {
                p.sendMessage(prefix + "아이템의 종류를 입력해주세요.");
                return false;
            }
            try {
                Material m = Material.getMaterial(args[1]);
                item.setType(m);
            } catch (Exception e) {
                p.sendMessage(prefix + "잘못된 종류입니다.");
                return false;
            }
        }
        if (args[0].equals("nbt")) {
            if (args.length == 1) {
                p.sendMessage(prefix + "NBT 옵션을 선택해주세요.");
                return false;
            }
            if (args[1].equals("set")) {
                if (args.length == 2) {
                    p.sendMessage(prefix + "NBT 타입을 입력해주세요.");
                    return false;
                }
                if (args.length == 3) {
                    p.sendMessage(prefix + "NBT 키를 입력해주세요.");
                    return false;
                }
                if (args.length == 4) {
                    p.sendMessage(prefix + "NBT 값을 입력해주세요.");
                    return false;
                }
                if (args[2].equalsIgnoreCase("int")) {
                    try {
                        p.getInventory().setItemInMainHand(NBT.setIntTag(item, args[3], Integer.parseInt(args[4])));
                    } catch (Exception e) {
                        p.sendMessage(prefix + "잘못된 NBT 값입니다.");
                        return false;
                    }
                    return false;
                }
                if (args[2].equalsIgnoreCase("string")) {
                    try {
                        p.getInventory().setItemInMainHand(NBT.setStringTag(item, args[3], args[4]));
                    } catch (Exception e) {
                        p.sendMessage(prefix + "잘못된 NBT 값입니다.");
                        return false;
                    }
                    return false;
                }
                if (args[2].equalsIgnoreCase("double")) {
                    try {
                        p.getInventory().setItemInMainHand(NBT.setDoubleTag(item, args[3], Double.parseDouble(args[4])));
                    } catch (Exception e) {
                        p.sendMessage(prefix + "잘못된 NBT 값입니다.");
                    }
                    return false;
                }
                if (args[2].equalsIgnoreCase("float")) {
                    try {
                        p.getInventory().setItemInMainHand(NBT.setFloatTag(item, args[3], Float.parseFloat(args[4])));
                    } catch (Exception e) {
                        p.sendMessage(prefix + "잘못된 NBT 값입니다.");
                    }
                    return false;
                }
                if (args[2].equalsIgnoreCase("long")) {
                    try {
                        p.getInventory().setItemInMainHand(NBT.setLongTag(item, args[3], Long.parseLong(args[4])));
                    } catch (Exception e) {
                        p.sendMessage(prefix + "잘못된 NBT 값입니다.");
                    }
                    return false;
                }
                if (args[2].equalsIgnoreCase("byte")) {
                    try {
                        p.getInventory().setItemInMainHand(NBT.setByteTag(item, args[3], Byte.parseByte(args[4])));
                    } catch (Exception e) {
                        p.sendMessage(prefix + "잘못된 NBT 값입니다.");
                    }
                    return false;
                }
                if (args[2].equalsIgnoreCase("short")) {
                    try {
                        p.getInventory().setItemInMainHand(NBT.setShortTag(item, args[3], Short.parseShort(args[4])));
                    } catch (Exception e) {
                        p.sendMessage(prefix + "잘못된 NBT 값입니다.");
                    }
                    return false;
                }
                return false;
            }
            if (args[1].equals("get")) {
                if (args.length == 2) {
                    p.sendMessage(prefix + "NBT 키를 입력해주세요.");
                    return false;
                }
                if (args[2].equalsIgnoreCase("int")) {
                    p.sendMessage(prefix + NBT.getIntegerTag(item, args[3]));
                    return false;
                }
                if (args[2].equalsIgnoreCase("string")) {
                    p.sendMessage(prefix + NBT.getStringTag(item, args[3]));
                    return false;
                }
                if (args[2].equalsIgnoreCase("double")) {
                    p.sendMessage(prefix + NBT.getDoubleTag(item, args[3]));
                    return false;
                }
                if (args[2].equalsIgnoreCase("float")) {
                    p.sendMessage(prefix + NBT.getFloatTag(item, args[3]));
                    return false;
                }
                if (args[2].equalsIgnoreCase("long")) {
                    p.sendMessage(prefix + NBT.getLongTag(item, args[3]));
                    return false;
                }
                if (args[2].equalsIgnoreCase("byte")) {
                    p.sendMessage(prefix + NBT.getByteTag(item, args[3]));
                    return false;
                }
                if (args[2].equalsIgnoreCase("short")) {
                    p.sendMessage(prefix + NBT.getShortTag(item, args[3]));
                    return false;
                }
                return false;
            }
            if (args[1].equals("del")) {
                if (args.length == 2) {
                    p.sendMessage(prefix + "NBT 키를 입력해주세요.");
                    return false;
                }
                p.getInventory().setItemInMainHand(NBT.removeTag(item, args[2]));
                return false;
            }
            if (args[1].equals("list")) {
                String s = "";
                Map<String, String> tags = NBT.getAllStringTag(item);
                if (tags == null) {
                    p.sendMessage(prefix + "no nbt found.");
                    return false;
                }
                for (String key : tags.keySet()) {
                    if (key.equals("display")) continue;
                    String value = tags.get(key);
                    s += "key : " + key + " | value: " + value + "\n";
                }
                p.sendMessage(prefix + s);
                return false;
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + "게임내에서만 사용할 수 있습니다.");
            return new ArrayList<>();
        }
        Player p = (Player) sender;
        if (p.getInventory().getItemInMainHand().getType() == Material.AIR) {
            p.sendMessage(prefix + "아이템을 손에 들고 사용해주세요.");
            return new ArrayList<>();
        }
        ItemStack item = p.getInventory().getItemInMainHand();
        if (args.length == 1) {
            return Arrays.asList("name", "lore", "enc", "type", "nbt");
        }
        if (args[0].equals("enc")) {
            if (args.length == 2) {
                return Arrays.asList("add", "del");
            }
            if (args.length == 3) {
                return ItemEditor.enchants;
            }
        }
        if (args[0].equals("lore")) {
            if (args.length == 2) {
                return Arrays.asList("add", "del", "set");
            }
            if (args[1].equals("del") || args[1].equals("set")) {
                List<String> index = new ArrayList<>();
                List<String> lore = item.getItemMeta().getLore() == null ? new ArrayList<>() : item.getItemMeta().getLore();
                for (int i = 0; i < lore.size(); i++) {
                    index.add(i + "");
                }
                return index;
            }
        }
        if (args[0].equals("type")) {
            if (args.length == 2) {
                return ItemEditor.materials;
            }
        }
        if (args[0].equals("nbt")) {
            if (args.length == 2) {
                return Arrays.asList("set", "get", "del", "list");
            }
            if (args.length == 3) {
                return Arrays.asList("STRING", "INT", "FLOAT", "DOUBLE", "LONG", "BYTE", "SHORT");
            }
        }
        return new ArrayList<>();
    }
}
