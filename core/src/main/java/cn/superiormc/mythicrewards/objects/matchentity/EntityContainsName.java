package cn.superiormc.mythicrewards.objects.matchentity;

import cn.superiormc.mythicrewards.MythicRewards;
import cn.superiormc.mythicrewards.utils.TextUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;

public class EntityContainsName extends AbstractMatchEntityRule {

    public EntityContainsName() {
        super();
    }

    @Override
    public boolean getMatch(ConfigurationSection section, LivingEntity entity) {
        for (String mobID : section.getStringList("entity-contains-name")) {
            if (TextUtil.clear(MythicRewards.methodUtil.getEntityName(entity)).contains(TextUtil.clear(mobID))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean configNotContains(ConfigurationSection section) {
        return !section.contains("entity-contains-name");
    }
}
