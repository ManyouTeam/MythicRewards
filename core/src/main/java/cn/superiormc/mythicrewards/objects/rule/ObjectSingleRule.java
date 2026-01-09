package cn.superiormc.mythicrewards.objects.rule;
import cn.superiormc.mythicrewards.MythicRewards;
import cn.superiormc.mythicrewards.listeners.DamageTracker;
import cn.superiormc.mythicrewards.listeners.TrackerResult;
import cn.superiormc.mythicrewards.managers.MatchEntityManager;
import cn.superiormc.mythicrewards.objects.ObjectAction;
import cn.superiormc.mythicrewards.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ObjectSingleRule implements Comparable<ObjectSingleRule> {

    private final String id;

    private final YamlConfiguration config;

    private final ObjectAction generalActions;

    private final ObjectAction allActions;

    private Collection<ObjectAction> packActions;

    private final DamageTracker damageTracker;

    private final long timeOutTicks;

    private final boolean preventVanillaDrops;

    private final int dropExp;

    public ObjectSingleRule(String id, YamlConfiguration config) {
        this.id = id;
        this.config = config;
        this.generalActions = new ObjectAction(config.getConfigurationSection("general-actions"));
        this.allActions = new ObjectAction(config.getConfigurationSection("all-actions"));
        this.damageTracker = new DamageTracker(this);
        this.timeOutTicks = config.getLong("time-out-ticks", 6000);
        this.preventVanillaDrops = config.getBoolean("prevent-vanilla-drops");
        this.dropExp = config.getInt("drop-exp", -1);
        initPackActions();
        //this.condition = new ObjectCondition(config.getConfigurationSection("conditions"));
    }

    private void initPackActions() {
        packActions = new ArrayList<>();
        ConfigurationSection packActionsSection = config.getConfigurationSection("pack-actions");
        if (packActionsSection == null) {
            return;
        }
        for (String key : packActionsSection.getKeys(false)) {
            if (key.equals("amount")) {
                continue;
            }
            if (packActionsSection.isConfigurationSection(key)) {
                ConfigurationSection actionSection = packActionsSection.getConfigurationSection(key);
                if (actionSection != null && !actionSection.contains("type")) {
                    ObjectAction action = new ObjectAction(actionSection);
                    packActions.add(action);
                }
            }
        }
        if (packActions.isEmpty()) {
            ObjectAction tempVal1 = new ObjectAction(config.getConfigurationSection("pack-actions"));
            packActions.add(tempVal1);
        }
        TextUtil.sendMessage(null, TextUtil.pluginPrefix() + " §fPack actions enabled for rule: " + id + ", total action amount: " + packActions.size() + ".");

    }

    public boolean getMatchEntity(LivingEntity entity) {
        ConfigurationSection section = config.getConfigurationSection("match-entity");
        return MatchEntityManager.matchEntityManager.getMatch(section, entity);
    }

    public void addDamage(LivingEntity entity, Player player, double damage) {
        damageTracker.addDamage(entity, player, damage);
    }

    public void startGiveAction(LivingEntity entity) {
        TrackerResult trackerResult = new TrackerResult(damageTracker, entity);
        List<Player> allPlayers = trackerResult.getAllPlayers();

        for (Player tempVal1 : allPlayers) {
            generalActions.runAllActions(tempVal1, trackerResult);
        }

        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        for (Player tempVal2 : players) {
            allActions.runAllActions(tempVal2, trackerResult);
        }

        for (ObjectAction action : packActions) {
            int amount = action.getAmount();
            if (amount <= 0 || allPlayers.isEmpty()) {
                continue;
            }

            List<Player> availablePlayers = new ArrayList<>(allPlayers);

            for (int i = 0; i < amount && !availablePlayers.isEmpty(); i++) {
                Player selected = weightedRandomPick(availablePlayers, trackerResult);
                if (selected != null) {
                    action.runAllActions(selected, trackerResult);

                    availablePlayers.remove(selected);
                }
            }
        }
        clearDamage(entity);
    }

    private Player weightedRandomPick(List<Player> players, TrackerResult trackerResult) {
        double totalWeight = 0.0;
        for (Player p : players) {
            totalWeight += trackerResult.getPlayerPercentage(p);
        }
        if (totalWeight <= 0) return null;

        double rand = new Random().nextDouble() * totalWeight;
        double cumulative = 0.0;

        for (Player p : players) {
            cumulative += trackerResult.getPlayerPercentage(p);
            if (rand <= cumulative) {
                return p;
            }
        }

        // 万一没有选中，返回最后一个
        return players.get(players.size() - 1);
    }

    public void clearDamage(LivingEntity entity) {
        damageTracker.clearDamage(entity);
    }

    public String getId() {
        return id;
    }

    public int getWeight() {
        return config.getInt("weight", 0);
    }

    public long getTimeOutTicks() {
        return timeOutTicks;
    }

    public boolean isPreventVanillaDrops() {
        return preventVanillaDrops;
    }

    public int getDropExp() {
        return dropExp;
    }

    @Override
    public int compareTo(@NotNull ObjectSingleRule otherPrefix) {
        if (getWeight() == otherPrefix.getWeight()) {
            int len1 = getId().length();
            int len2 = otherPrefix.getId().length();
            int minLength = Math.min(len1, len2);

            for (int i = 0; i < minLength; i++) {
                char c1 = getId().charAt(i);
                char c2 = otherPrefix.getId().charAt(i);

                if (c1 != c2) {
                    if (Character.isDigit(c1) && Character.isDigit(c2)) {
                        // 如果字符都是数字，则按照数字大小进行比较
                        return Integer.compare(Integer.parseInt(getId().substring(i)), Integer.parseInt(otherPrefix.getId().substring(i)));
                    } else {
                        // 否则，按照字符的unicode值进行比较
                        return c1 - c2;
                    }
                }
            }

            return len1 - len2;
        }
        return getWeight() - otherPrefix.getWeight();
    }

    @Override
    public String toString() {
        return getId();
    }
}
