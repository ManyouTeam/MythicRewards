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
        bossNameMap.put(boss.getUniqueId(), MythicRewards.methodUtil.getEntityName(boss));
        bossDamageMap
                .computeIfAbsent(boss.getUniqueId(), k -> new HashMap<>())
                .merge(player.getUniqueId(), damage, Double::sum);
        SchedulerUtil schedulerUtil = bossCacheClearMap.get(boss.getUniqueId());
        if (schedulerUtil != null) {
            schedulerUtil.cancel();
        }
        bossCacheClearMap.put(boss.getUniqueId(), SchedulerUtil.runTaskLater(boss, () -> {
            clearDamage(boss);
            bossCacheClearMap.remove(boss.getUniqueId());
            ConfigManager.configManager.removeEntityMatchMap(boss);
            if (ConfigManager.configManager.getBoolean("debug")) {
                MythicRewards.methodUtil.sendMessage(null, TextUtil.pluginPrefix() + " §fRemoved damage cache for entity: " +
                        getName(boss) + " (" + boss.getUniqueId().toString() + ")!");
                MythicRewards.methodUtil.sendMessage(null, TextUtil.pluginPrefix() + " §fNow damage cache amount: " + bossDamageMap.size() + "!");
            }
        }, singleRule.getTimeOutTicks()));
        if (ConfigManager.configManager.getBoolean("debug")) {
            MythicRewards.methodUtil.sendMessage(null, TextUtil.pluginPrefix() + " §fAdded damage cache for entity: " +
                    getName(boss) + " (" + boss.getUniqueId().toString() + "), damage value: " + damage + "!");
            MythicRewards.methodUtil.sendMessage(null, TextUtil.pluginPrefix() + " §fNow damage cache amount: " + bossDamageMap.size() + "!");
        }
    }

    public Map<UUID, Double> getDamageMap(LivingEntity boss) {
        return bossDamageMap.getOrDefault(boss.getUniqueId(), new HashMap<>());
    }

    public void clearDamage(LivingEntity boss) {
        bossNameMap.remove(boss.getUniqueId());
        bossDamageMap.remove(boss.getUniqueId());
    }

    public String getName(LivingEntity boss) {
        return bossNameMap.get(boss.getUniqueId());
    }
}
