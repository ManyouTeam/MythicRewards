package cn.superiormc.mythicrewards.listeners;

import cn.superiormc.mythicrewards.MythicRewards;
import cn.superiormc.mythicrewards.managers.ConfigManager;
import cn.superiormc.mythicrewards.objects.rule.ObjectSingleRule;
import cn.superiormc.mythicrewards.utils.CommonUtil;
import cn.superiormc.mythicrewards.utils.SchedulerUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class DamageListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBossDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity boss)) {
            return;
        }

        Player player = CommonUtil.getDamager(event.getDamager());
        if (player == null) {
            return;
        }

        if (MythicRewards.isFolia) {
            SchedulerUtil.runSync(boss, () -> {
                ObjectSingleRule singleRule = ConfigManager.configManager.getEntityMatchRule(boss);
                if (singleRule == null) {
                    return;
                }
                singleRule.addDamage(boss, player, event.getDamage());
            });
        } else {
            ObjectSingleRule singleRule = ConfigManager.configManager.getEntityMatchRule(boss);
            if (singleRule == null) {
                return;
            }
            singleRule.addDamage(boss, player, event.getDamage());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBossDeath(EntityDeathEvent event) {
        LivingEntity boss = event.getEntity();

        if (!ConfigManager.configManager.containsEntityMatchRuleCache(boss)) {
            return;
        }

        ObjectSingleRule singleRule = ConfigManager.configManager.getEntityMatchRuleCache(boss);
        if (singleRule == null) {
            return;
        }
        singleRule.startGiveAction(boss);
        if (singleRule.isPreventVanillaDrops()) {
            event.getDrops().clear();
        }
        if (singleRule.getDropExp() > 0) {
            event.setDroppedExp(singleRule.getDropExp());
        }


        if (MythicRewards.isFolia) {
            SchedulerUtil.runSync(boss, () -> {
                ConfigManager.configManager.removeEntityMatchMap(boss);
            });
        } else {
            ConfigManager.configManager.removeEntityMatchMap(boss);
        }

    }
}
