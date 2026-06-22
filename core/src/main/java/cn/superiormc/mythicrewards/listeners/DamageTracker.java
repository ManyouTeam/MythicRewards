package cn.superiormc.mythicrewards.listeners;

import cn.superiormc.mythicrewards.MythicRewards;
import cn.superiormc.mythicrewards.managers.ConfigManager;
import cn.superiormc.mythicrewards.objects.rule.ObjectSingleRule;
import cn.superiormc.mythicrewards.utils.SchedulerUtil;
import cn.superiormc.mythicrewards.utils.TextUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DamageTracker {

    private final Map<UUID, Map<UUID, Double>> bossDamageMap = new HashMap<>();

    private final Map<UUID, SchedulerUtil> bossCacheClearMap = new HashMap<>();

    private final Map<UUID, String> bossNameMap = new HashMap<>();

    private final ObjectSingleRule singleRule;

    public DamageTracker(ObjectSingleRule singleRule) {
        this.singleRule = singleRule;
    }

    public void addDamage(LivingEntity boss, Player player, double damage) {
        UUID bossId = boss.getUniqueId();
        String bossName = MythicRewards.methodUtil.getEntityName(boss);
        bossNameMap.put(bossId, bossName);
        bossDamageMap
                .computeIfAbsent(bossId, k -> new HashMap<>())
                .merge(player.getUniqueId(), damage, Double::sum);
        SchedulerUtil schedulerUtil = bossCacheClearMap.remove(bossId);
        if (schedulerUtil != null) {
            schedulerUtil.cancel();
        }
        bossCacheClearMap.put(bossId, SchedulerUtil.runTaskLater(boss, () -> {
            clearDamage(bossId, false);
            ConfigManager.configManager.removeEntityMatchMap(bossId);
            if (ConfigManager.configManager.getBoolean("debug")) {
                TextUtil.sendMessage(null, TextUtil.pluginPrefix() + " §fRemoved damage cache for entity: " +
                        bossName + " (" + bossId + ")!");
                TextUtil.sendMessage(null, TextUtil.pluginPrefix() + " §fNow damage cache amount: " + bossDamageMap.size() + "!");
            }
        }, singleRule.getTimeOutTicks()));
        if (ConfigManager.configManager.getBoolean("debug")) {
            TextUtil.sendMessage(null, TextUtil.pluginPrefix() + " §fAdded damage cache for entity: " +
                    bossName + " (" + bossId + "), damage value: " + damage + "!");
            TextUtil.sendMessage(null, TextUtil.pluginPrefix() + " §fNow damage cache amount: " + bossDamageMap.size() + "!");
        }
    }

    public Map<UUID, Double> getDamageMap(LivingEntity boss) {
        return bossDamageMap.getOrDefault(boss.getUniqueId(), new HashMap<>());
    }

    public void clearDamage(LivingEntity boss) {
        clearDamage(boss.getUniqueId(), true);
    }

    private void clearDamage(UUID bossId, boolean cancelTask) {
        bossNameMap.remove(bossId);
        bossDamageMap.remove(bossId);
        SchedulerUtil schedulerUtil = bossCacheClearMap.remove(bossId);
        if (cancelTask && schedulerUtil != null) {
            schedulerUtil.cancel();
        }
    }

    public String getName(LivingEntity boss) {
        return bossNameMap.get(boss.getUniqueId());
    }
}
