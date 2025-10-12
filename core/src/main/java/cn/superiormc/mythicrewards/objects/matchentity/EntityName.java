package cn.superiormc.mythicrewards.objects.matchentity;

import cn.superiormc.mythicrewards.MythicRewards;
import cn.superiormc.mythicrewards.utils.TextUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class EntityName extends AbstractMatchEntityRule {

    public EntityName() {
        super();
    }

    @Override
    public boolean getMatch(ConfigurationSection section, LivingEntity entity) {
        for (String mobID : section.getStringList("entity-name")) {
            if (TextUtil.clear(MythicRewards.methodUtil.getEntityName(entity)).equalsIgnoreCase(TextUtil.clear(mobID))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean configNotContains(ConfigurationSection section) {
        return !section.contains("entity-name");
    }
}
