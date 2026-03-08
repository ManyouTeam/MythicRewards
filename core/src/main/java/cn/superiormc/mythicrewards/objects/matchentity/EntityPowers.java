package cn.superiormc.mythicrewards.objects.matchentity;

import cn.superiormc.enchantedmobs.managers.EntityScannerManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class EntityPowers extends AbstractMatchEntityRule {

    public EntityPowers() {
        super();
    }

    @Override
    public boolean getMatch(ConfigurationSection section, LivingEntity entity) {
        List<String> powers = EntityScannerManager.entityScannerManager.getEntityPowerCache(entity);
        if (powers == null || powers.isEmpty()) {
            return false;
        }

        List<String> expectedPowers = getExpectedPowers(section);
        if (expectedPowers.isEmpty()) {
            return true;
        }

        if (isAllMode(section)) {
            for (String expectedPower : expectedPowers) {
                boolean found = false;
                for (String actualPower : powers) {
                    if (actualPower.equalsIgnoreCase(expectedPower)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return false;
                }
            }
            return true;
        }

        for (String expectedPower : expectedPowers) {
            for (String actualPower : powers) {
                if (actualPower.equalsIgnoreCase(expectedPower)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean configNotContains(ConfigurationSection section) {
        return getExpectedPowers(section).isEmpty();
    }

    private List<String> getExpectedPowers(ConfigurationSection section) {
        if (section.isList("entity-powers")) {
            return section.getStringList("entity-powers");
        }

        ConfigurationSection entityPowersSection = section.getConfigurationSection("entity-powers");
        if (entityPowersSection == null) {
            return new ArrayList<>();
        }
        return entityPowersSection.getStringList("powers");
    }

    private boolean isAllMode(ConfigurationSection section) {
        ConfigurationSection entityPowersSection = section.getConfigurationSection("entity-powers");
        if (entityPowersSection == null) {
            return false;
        }
        return entityPowersSection.getString("mode", "any").equalsIgnoreCase("all");
    }
}