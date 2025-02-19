package studio.magemonkey.fabled.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import studio.magemonkey.codex.util.AttributeUT;
import studio.magemonkey.codex.util.StringUT;
import studio.magemonkey.fabled.Fabled;
import studio.magemonkey.fabled.api.CastData;
import studio.magemonkey.fabled.api.classes.FabledClass;
import studio.magemonkey.fabled.api.player.PlayerAccounts;
import studio.magemonkey.fabled.api.player.PlayerClass;
import studio.magemonkey.fabled.api.player.PlayerData;
import studio.magemonkey.fabled.api.player.PlayerSkill;
import studio.magemonkey.fabled.api.util.FlagData;
import studio.magemonkey.fabled.api.util.FlagManager;
import studio.magemonkey.fabled.dynamic.DynamicSkill;
import studio.magemonkey.fabled.hook.PlaceholderAPIHook;
import studio.magemonkey.fabled.hook.PluginChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlaceholderUtil {
    private static Pattern regex = Pattern.compile("%fabled_(.*?)%");

    @NotNull
    public static String colorizeAndReplace(@NotNull String str, OfflinePlayer player) {
        // If we have PlaceholderAPI, use it, otherwise we can default
        // to our internal supplied placeholders
        if (PluginChecker.isPlaceholderAPIActive()) {
            return StringUT.color(PlaceholderAPIHook.format(str, player.getPlayer()));
        }

        String  formattedLine = str;
        Matcher matcher       = regex.matcher(str);
        while (matcher.find()) {
            String match = matcher.group(1);
            String value = replace(player, match);
            if (value != null) {
                formattedLine = formattedLine.replace("%fabled_" + match + "%", value);
            }
        }
        return StringUT.color(formattedLine);
    }

    @NotNull
    public static List<String> colorizeAndReplace(List<String> list, OfflinePlayer player) {
        return list.stream()
                .map(line -> colorizeAndReplace(line, player))
                .collect(Collectors.toList());
    }

    public static String replace(OfflinePlayer player, String identifier) {
        PlayerData playerData = Fabled.getData(player);

        if (identifier.startsWith("group_")) {
            if (!Fabled.getClasses().isEmpty()) {
                Set<String> groups = Fabled.getClasses()
                        .values()
                        .stream()
                        .map(clazz -> clazz.getGroup().toLowerCase())
                        .collect(Collectors.toSet());
                for (String groupName : groups) {
                    PlayerClass playerClass = playerData.getClass(groupName);
                    if (playerClass == null || playerClass.getData() == null) continue;

                    if (identifier.equals("group_" + groupName + "_basehealth")) {
                        return String.valueOf(playerClass.getData().getBaseHealth());
                    }
                    if (identifier.equals("group_" + groupName + "_sbasehealth")) {
                        return String.valueOf((int) playerClass.getData().getBaseHealth());
                    }
                    if (identifier.equals("group_" + groupName + "_basemana")) {
                        return String.valueOf(playerClass.getData().getBaseMana());
                    }
                    if (identifier.equals("group_" + groupName + "_sbasemana")) {
                        return String.valueOf((int) playerClass.getData().getBaseMana());
                    }
                    if (identifier.startsWith("group_" + groupName + "_healthat:")) {
                        String[] idSplit = identifier.split(":");
                        try {
                            int lvl = Integer.parseInt(idSplit[1]);
                            return String.valueOf(playerClass.getData().getHealth(lvl));
                        } catch (NumberFormatException e) {
                            return "0";
                        }
                    }
                    if (identifier.startsWith("group_" + groupName + "_shealthat:")) {
                        String[] idSplit = identifier.split(":");
                        try {
                            int lvl = Integer.parseInt(idSplit[1]);
                            return String.valueOf((int) playerClass.getData().getHealth(lvl));
                        } catch (NumberFormatException e) {
                            return "0";
                        }
                    }
                    if (identifier.equals("group_" + groupName + "_healthscale")) {
                        return String.valueOf(playerClass.getData().getHealthScale());
                    }
                    if (identifier.startsWith("group_" + groupName + "_manaat:")) {
                        String[] idSplit = identifier.split(":");
                        try {
                            int lvl = Integer.parseInt(idSplit[1]);
                            return String.valueOf(playerClass.getData().getMana(lvl));
                        } catch (NumberFormatException e) {
                            return "0";
                        }
                    }
                    if (identifier.startsWith("group_" + groupName + "_smanaat:")) {
                        String[] idSplit = identifier.split(":");
                        try {
                            int lvl = Integer.parseInt(idSplit[1]);
                            return String.valueOf((int) playerClass.getData().getMana(lvl));
                        } catch (NumberFormatException e) {
                            return "0";
                        }
                    }
                    if (identifier.equals("group_" + groupName + "_mananame")) {
                        return String.valueOf(playerClass.getData().getManaName());
                    }
                    if (identifier.equals("group_" + groupName + "_smananame")) {
                        return String.valueOf(ChatColor.stripColor(playerClass.getData().getManaName()));
                    }
                    if (identifier.equals("group_" + groupName + "_manaregen")) {
                        return String.valueOf(playerClass.getData().getManaRegen());
                    }
                    if (identifier.equals("group_" + groupName + "_manascale")) {
                        return String.valueOf(playerClass.getData().getManaScale());
                    }
                    if (identifier.equals("group_" + groupName + "_maxlevel")) {
                        return String.valueOf(playerClass.getData().getMaxLevel());
                    }
                    if (identifier.equals("group_" + groupName + "_parent")) {
                        if (playerClass.getData().getParent() != null) {
                            return String.valueOf(playerClass.getData().getParent().getName());
                        } else {
                            return "0";
                        }
                    }
                    if (identifier.equals("group_" + groupName + "_prefix")) {
                        return String.valueOf(playerClass.getData().getPrefix());
                    }
                    if (identifier.equals("group_" + groupName + "_sprefix")) {
                        return String.valueOf(ChatColor.stripColor(playerClass.getData().getPrefix()));
                    }
                    if (identifier.startsWith("group_" + groupName + "_reqexpat:")) {
                        String[] idSplit = identifier.split(":");
                        try {
                            int lvl = Integer.parseInt(idSplit[1]);
                            return String.valueOf(playerClass.getData().getRequiredExp(lvl));
                        } catch (NumberFormatException e) {
                            return "0";
                        }
                    }

                    if (identifier.startsWith("group_" + groupName + "_children")) {
                        if (Fabled.getClass(groupName) == null) {
                            return "0";
                        }

                        ArrayList<String> childList = new ArrayList<String>();

                        for (FabledClass classes : Fabled.getClasses().values()) {
                            String classname = classes.getName().toLowerCase();

                            if (classes.hasParent()) {
                                if (classes.getParent().getName().equalsIgnoreCase(groupName)) {
                                    childList.add(classname);
                                }
                            }
                        }
                        return childList.toString();
                    }

                    if (identifier.startsWith("group_" + groupName + "_schildren")) {
                        if (Fabled.getClass(groupName) == null) {
                            return "0";
                        }

                        ArrayList<String> childList = new ArrayList<String>();

                        for (FabledClass classes : Fabled.getClasses().values()) {
                            String classname = classes.getName().toLowerCase();

                            if (classes.hasParent()) {
                                if (classes.getParent().getName().equalsIgnoreCase(groupName)) {
                                    childList.add(classname);
                                }
                            }
                        }
                        String finalList = childList.toString().replaceAll("(^\\[|\\]$)", "");
                        if (finalList.equals("") || finalList == null) {
                            return "0";
                        } else {
                            return finalList;
                        }

                    }

                }
            }
        }

        String[] args = identifier.split("_");
        if ((args.length == 3 && args[0].equals("default")) || (args.length == 4 && args[0].equals("player")
                && !args[1].equals("account"))) {
            // Another player
            String playerName = args[args.length - 1];
            UUID   uuid       = null;
            try {
                uuid = UUID.fromString(playerName);
            } catch (IllegalArgumentException ignored) {
            }
            player = uuid == null ? Bukkit.getOfflinePlayer(playerName) : Bukkit.getOfflinePlayer(uuid);
            identifier = identifier.substring(0, identifier.length() - playerName.length() - 1);
        }

        if (player == null) {
            return "0";
        }

        if (!Fabled.hasPlayerData(player)) {
            return "0";
        }

        PlayerData data = Fabled.getData(player);

        if (data == null) {
            return "0";
        }

        if (!data.hasClass()) {
            return "0";
        }

        if (identifier.startsWith("default_")) {
            if (identifier.equals("default_currentlevel")) {
                return String.valueOf(data.getMainClass().getLevel());
            }
            if (identifier.equals("default_currentmaxlevel")) {
                return String.valueOf(data.getMainClass().getData().getMaxLevel());
            }
            if (identifier.equals("default_currentmaxmana")) {
                return String.valueOf(data.getMainClass().getPlayerData().getMaxMana());
            }
            if (identifier.equals("default_scurrentmaxmana")) {
                return String.valueOf((int) data.getMainClass().getPlayerData().getMaxMana());
            }
            if (identifier.equals("default_currentmana")) {
                return String.valueOf(data.getMainClass().getPlayerData().getMana());
            }
            if (identifier.equals("default_scurrentmana")) {
                return String.valueOf((int) data.getMainClass().getPlayerData().getMana());
            }
            if (identifier.equals("default_currentmaxhealth")) {
                double maxHP = player.getPlayer().getAttribute(AttributeUT.resolve("MAX_HEALTH")).getBaseValue();
                return String.valueOf(maxHP);
            }
            if (identifier.equals("default_scurrentmaxhealth")) {
                double maxHP = player.getPlayer().getAttribute(AttributeUT.resolve("MAX_HEALTH")).getBaseValue();
                return String.valueOf((int) maxHP);
            }
            if (identifier.equals("default_currenthealth")) {
                double currentHP = player.getPlayer().getHealth();
                return String.valueOf(currentHP);
            }
            if (identifier.equals("default_scurrenthealth")) {
                double currentHP = player.getPlayer().getHealth();
                return String.valueOf((int) currentHP);
            }
            if (identifier.equals("default_currentmananame")) {
                return String.valueOf(data.getMainClass().getData().getManaName());
            }
            if (identifier.equals("default_scurrentmananame")) {
                return String.valueOf(ChatColor.stripColor(data.getMainClass().getData().getManaName()));
            }
            if (identifier.equals("default_currentmanaregen")) {
                return String.valueOf(data.getMainClass().getData().getManaRegen());
            }
            if (identifier.equals("default_scurrentmanaregen")) {
                return String.valueOf((int) data.getMainClass().getData().getManaRegen());
            }
            if (identifier.equals("default_currentgroupname")) {
                return String.valueOf(data.getMainClass().getData().getGroup());
            }
            if (identifier.equals("default_currentclassname")) {
                return String.valueOf(data.getMainClass().getData().getName());
            }
            if (identifier.equals("default_currentavailableattributepoints")
                    || identifier.equals("default_attributepoints")) {
                return String.valueOf(data.getMainClass().getPlayerData().getAttributePoints());
            }
            if (identifier.equals("default_currentavailableskillpoints")
                    || identifier.equals("default_skillpoints")
                    || identifier.equals("default_currentlyavailablesuperawesomeultramegagigaamazingskillpoints")) {
                return String.valueOf(data.getMainClass().getPoints());
            }
            if (identifier.equals("default_currentprefix")) {
                return String.valueOf(data.getMainClass().getData().getPrefix());
            }
            if (identifier.equals("default_scurrentprefix")) {
                return String.valueOf(ChatColor.stripColor(data.getMainClass().getData().getPrefix()));
            }
            if (identifier.equals("default_currentexp")) {
                return String.valueOf(data.getMainClass().getExp());
            }
            if (identifier.equals("default_scurrentexp")) {
                return String.valueOf((int) data.getMainClass().getExp());
            }
            if (identifier.equals("default_currentrequiredexp")) {
                return String.valueOf(data.getMainClass().getRequiredExp());
            }
            if (identifier.equals("default_scurrentrequiredexp")) {
                return String.valueOf(data.getMainClass().getRequiredExp());
            }
            if (identifier.startsWith("default_value_")) {
                return data.getPersistentData(identifier.substring(14)).toString();
            }
        }

        if (identifier.startsWith("player_")) {
            if (identifier.startsWith("player_account_")) {
                PlayerAccounts accounts = Fabled.getPlayerAccounts(player);
                Pattern        pattern  = Pattern.compile("player_account_(\\d+)");
                Matcher        matcher  = pattern.matcher(identifier);
                if (matcher.find()) {
                    int accNum = Integer.parseInt(matcher.group(1));
                    data = accounts.getData(accNum);

                    if (data == null) return ChatColor.GRAY + "Not Professed";

                    identifier = identifier.replace("player_account_" + accNum + "_", "");
                    return getPlaceholder(player, identifier, data.getMainClass());
                }
            } else if (!data.getClasses().isEmpty()) {
                for (PlayerClass group : data.getClasses()) {
                    String      groupName   = group.getData().getGroup();
                    PlayerClass playerClass = playerData.getClass(groupName);
                    if (!identifier.startsWith("player_" + groupName)) continue;

                    identifier = identifier.replace("player_" + groupName + "_", "");
                    return getPlaceholder(player, identifier, playerClass);
                }
            }
        }

        if (identifier.startsWith("dynamic_")) {
            if (!player.isOnline()) return "0";
            LivingEntity caster = player.getPlayer();
            String[]     ident  = identifier.split("_", 3);
            switch (ident[1]) {
                case "value" -> {
                    CastData castData = DynamicSkill.getCastData(caster);
                    if (ident.length < 3) return "0";
                    if (castData == null) return "0";
                    return castData.getOrDefault(ident[2], "0");
                }
                case "flags" -> {
                    FlagData flagData = FlagManager.getFlagData(caster);
                    if (flagData == null) return "0";
                    Stream<String> stream = flagData.flagList().stream();
                    if (ident.length > 2) stream = stream
                            .filter(f -> f.startsWith(ident[2]))
                            .map(f -> f.replaceFirst(ident[2], ""));
                    return stream.collect(Collectors.joining(" "));
                }
                case "flagremain" -> {
                    if (ident.length < 3) return "0";
                    return String.valueOf(FlagManager.getTimeLeft(caster, ident[2]));
                }
                case "cooldown" -> {
                    if (ident.length < 3) return "0";
                    PlayerSkill skill = data.getSkill(ident[2]);
                    if (skill == null) return "0";
                    return String.valueOf(skill.getCooldownLeft());
                }
            }
        }

        return null;
    }

    private static String getPlaceholder(OfflinePlayer player, String identifier, PlayerClass playerClass) {
        if (identifier.startsWith("attribute:")) {
            String[] idSplit = identifier.split(":");
            try {
                return String.valueOf(playerClass.getPlayerData().getAttribute(idSplit[1]));
            } catch (Exception e) {
                return "0";
            }
        }
        if (identifier.equals("availableattributepoints")
                || identifier.equals("attributepoints")) {
            return String.valueOf(playerClass.getPlayerData().getAttributePoints());
        }
        if (identifier.equals("availableskillpoints")
                || identifier.equals("skillpoints")
                || identifier.equals("availablesuperawesomeultramegagigaamazingskillpoints")) {
            return String.valueOf(playerClass.getPoints());
        }
        if (identifier.startsWith("investedattributepoints:")) {
            String[] idSplit = identifier.split(":");
            try {
                return String.valueOf(playerClass.getPlayerData().getInvestedAttribute(idSplit[1]));
            } catch (Exception e) {
                return "0";
            }
        }
        if (identifier.equals("mainclass")) {
            return String.valueOf(playerClass.getPlayerData().getMainClass().getData().getName());
        }
        if (identifier.equals("class")) {
            return String.valueOf(playerClass.getData().getName());
        }

        if (identifier.equals("currentexp")) {
            return String.valueOf(playerClass.getExp());
        }
        if (identifier.equals("requiredexp")) {
            return String.valueOf(playerClass.getRequiredExp());
        }
        if (identifier.equals("scurrentexp")) {
            return String.valueOf((int) playerClass.getExp());
        }
        if (identifier.equals("srequiredexp")) {
            return String.valueOf((int) playerClass.getRequiredExp());
        }
        if (identifier.equals("level")) {
            return String.valueOf(playerClass.getLevel());
        }
        if (identifier.equals("currentmana")) {
            return String.valueOf(playerClass.getPlayerData().getMana());
        }
        if (identifier.equals("maxmana")) {
            return String.valueOf(playerClass.getPlayerData().getMaxMana());
        }
        if (identifier.equals("scurrentmana")) {
            return String.valueOf((int) playerClass.getPlayerData().getMana());
        }
        if (identifier.equals("smaxmana")) {
            return String.valueOf((int) playerClass.getPlayerData().getMaxMana());
        }
        if (identifier.equals("scurrenthealth")) {
            double currentHP = player.getPlayer().getHealth();
            return String.valueOf((int) currentHP);
        }
        if (identifier.equals("smaxhealth")) {
            double maxHP = player.getPlayer().getAttribute(AttributeUT.resolve("MAX_HEALTH")).getBaseValue();
            return String.valueOf((int) maxHP);
        }
        if (identifier.startsWith("skillevel:")) {
            String[] idSplit = identifier.split(":");
            try {
                return String.valueOf(playerClass.getPlayerData().getSkillLevel(idSplit[1]));
            } catch (Exception e) {
                return "0";
            }
        }
        return null;
    }
}
