package cn.superiormc.mythicrewards.objects.matchentity;

import cn.superiormc.enchantedmobs.managers.EntityScannerManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;

import java.util.List;

public class EntityPowersAmount extends AbstractMatchEntityRule {

    public EntityPowersAmount() {
        super();
    }

    @Override
    public boolean getMatch(ConfigurationSection section, LivingEntity entity) {
        List<String> powers = EntityScannerManager.entityScannerManager.getEntityPowerCache(entity);
        int size = powers == null ? 0 : powers.size();

        if (section.isList("entity-powers-amount")) {
            return section.getIntegerList("entity-powers-amount").contains(size);
        }

        String amountSetting = section.getString("entity-powers-amount", "0");
        if (amountSetting.contains("~")) {
            String[] parts = amountSetting.split("~");
            int min = Integer.parseInt(parts[0]);
            int max = Integer.parseInt(parts[1]);
            return size >= min && size <= max;
        } else if (amountSetting.startsWith("<=")) {
            int max = Integer.parseInt(amountSetting.substring(2));
            return size <= max;
        } else if (amountSetting.startsWith(">=")) {
            int min = Integer.parseInt(amountSetting.substring(2));
            return size >= min;
        } else if (amountSetting.startsWith("<")) {
            int max = Integer.parseInt(amountSetting.substring(1));
            return size < max;
        } else if (amountSetting.startsWith(">")) {
            int min = Integer.parseInt(amountSetting.substring(1));
            return size > min;
        }
        return size == Integer.parseInt(amountSetting);
    }

    @Override
    public boolean configNotContains(ConfigurationSection section) {
        return !section.contains("entity-powers-amount");
    }
}