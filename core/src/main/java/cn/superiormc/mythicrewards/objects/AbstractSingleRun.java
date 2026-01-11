package cn.superiormc.mythicrewards.objects;

import cn.superiormc.mythicrewards.listeners.TrackerResult;
import cn.superiormc.mythicrewards.managers.ConfigManager;
import cn.superiormc.mythicrewards.utils.CommonUtil;
import cn.superiormc.mythicrewards.utils.MathUtil;
import cn.superiormc.mythicrewards.utils.TextUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSingleRun {

    protected ConfigurationSection section;

    protected String rankSetting;

    public AbstractSingleRun(ConfigurationSection section) {
        this.section = section;
        rankSetting = section.getString("rank-limit", null);
    }

    protected String replacePlaceholder(String content, Player player, TrackerResult result) {
        if (content == null) {
            return "";
        }

        content = CommonUtil.modifyString(player, content,
                "world", player.getWorld().getName(),
                "player-x", String.valueOf(player.getLocation().getX()),
                "player-y", String.valueOf(player.getLocation().getY()),
                "player-z", String.valueOf(player.getLocation().getZ()),
                "player-pitch", String.valueOf(player.getLocation().getPitch()),
                "player-yaw", String.valueOf(player.getLocation().getYaw()),
                "player", player.getName(),
                "entity-x", String.valueOf(result.getEntity().getLocation().getX()),
                "entity-y", String.valueOf(result.getEntity().getLocation().getY()),
                "entity-z", String.valueOf(result.getEntity().getLocation().getZ()),
                "entity-pitch", String.valueOf(result.getEntity().getLocation().getPitch()),
                "entity-yaw", String.valueOf(result.getEntity().getLocation().getYaw()),
                "entity", result.getEntityName(),
                "entity-health", String.valueOf(result.getEntity().getHealth()),
                "total-damage", String.format(ConfigManager.configManager.getString("placeholders.result.damage-format"), result.getTotalDamage()),
                "rank", String.valueOf(result.getPlayerRank(player)),
                "damage", String.format(ConfigManager.configManager.getString("placeholders.result.damage-format"), result.getPlayerDamage(player)),
                "percentage", String.format(ConfigManager.configManager.getString("placeholders.result.percentage-format"), result.getPlayerPercentage(player))
        );

        content = TextUtil.withPAPI(result.parseResultPlaceholders(content), player);
        return content;
    }

    public String getString(String path) {
        return section.getString(path);
    }

    public List<String> getStringList(String path, Player player, TrackerResult result) {
        List<String> rawList = section.getStringList(path);
        if (rawList.isEmpty()) {
            return rawList;
        }

        List<String> tempVal1 = new ArrayList<>();
        for (String line : rawList) {
            tempVal1.add(replacePlaceholder(line, player, result));
        }
        return tempVal1;
    }

    public int getInt(String path) {
        return section.getInt(path);
    }

    public int getInt(String path, int defaultValue) {
        return section.getInt(path, defaultValue);
    }

    public double getDouble(String path) {
        return MathUtil.doCalculate(section.getString(path));
    }

    public double getDouble(String path, Player player, TrackerResult result) {
        return MathUtil.doCalculate(replacePlaceholder(section.getString(path), player, result));
    }

    public boolean getBoolean(String path, boolean defaultValue) {
        return section.getBoolean(path, defaultValue);
    }

    public String getString(String path, Player player, TrackerResult result) {
        return replacePlaceholder(section.getString(path), player, result);
    }

    public ConfigurationSection getSection() {
        return section;
    }

    public boolean meetRankLimit(int rank) {
        if (rank == -1) {
            return false;
        }
        if (rankSetting == null) {
            return true;
        }
        if (rankSetting.contains("~")) {
            String[] parts = rankSetting.split("~");
            int min = Integer.parseInt(parts[0]);
            int max = Integer.parseInt(parts[1]);
            return rank >= min && rank <= max;
        } else if (rankSetting.startsWith("<=")) {
            int max = Integer.parseInt(rankSetting.substring(2));
            return rank <= max;
        } else if (rankSetting.startsWith(">=")) {
            int min = Integer.parseInt(rankSetting.substring(2));
            return rank >= min;
        } else {
            return rank == Integer.parseInt(rankSetting);
        }
    }
}
