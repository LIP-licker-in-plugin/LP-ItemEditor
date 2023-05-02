package com.licker2689.lie.functions;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Iterator;

public class LIEFunction {

    public static String getColoredText(String[] args, int line) {
        StringBuilder s = new StringBuilder();
        args = Arrays.copyOfRange(args, line, args.length);
        Iterator<String> i = Arrays.stream(args).iterator();
        while (i.hasNext()) {
            s.append(i.next()).append(" ");
        }
        // delete last space
        if(s.charAt(s.length()-1) == ' ') {
            s.deleteCharAt(s.length()-1);
        }
        return ChatColor.translateAlternateColorCodes('&', s.toString());
    }
}
