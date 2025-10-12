package cn.superiormc.mythicrewards.objects.conditions;

import cn.superiormc.mythicrewards.listeners.TrackerResult;
import cn.superiormc.mythicrewards.managers.ConditionManager;
import cn.superiormc.mythicrewards.objects.AbstractSingleRun;
import cn.superiormc.mythicrewards.objects.ObjectCondition;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ObjectSingleCondition extends AbstractSingleRun {

    private final ObjectCondition condition;

    public ObjectSingleCondition(ObjectCondition condition, ConfigurationSection conditionSection) {
        super(conditionSection);
        this.condition = condition;
    }

    public boolean checkBoolean(Player player, TrackerResult result) {
        if (!meetRankLimit(result.getPlayerRank(player))) {
            return false;
        }
        return ConditionManager.conditionManager.checkBoolean(this, player, result);
    }

    public ObjectCondition getCondition() {
        return condition;
    }

}
