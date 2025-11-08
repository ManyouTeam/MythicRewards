package cn.superiormc.mythicrewards.listeners;

import cn.superiormc.mythicrewards.managers.ConfigManager;
import cn.superiormc.mythicrewards.objects.rule.ObjectSingleRule;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void onBossDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity boss)) {
            return;
        }

        Player player = null;
        if (event.getDamager() instanceof Player p) {
            player = p;
        } else if (event.getDamager() instanceof Projectile projectile) {
            if (projectile.getShooter() instanceof Player shooter) {
                player = shooter;
            }
        }

        if (player == null) {
            return;
        }

        ObjectSingleRule singleRule = ConfigManager.configManager.getEntityMatchRule(boss);
        if (singleRule == null) {
            return;
        }

        singleRule.addDamage(boss, player, event.getDamage());
    }

    @EventHandler
    public void onBossDeath(EntityDeathEvent event) {
        LivingEntity boss = event.getEntity();

        if (!ConfigManager.configManager.containsEntityMatchRuleCache(boss)) {
            return;
        }

        ObjectSingleRule singleRule = ConfigManager.configManager.getEntityMatchRule(boss);
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

        ConfigManager.configManager.removeEntityMatchMap(boss);
    }
}
