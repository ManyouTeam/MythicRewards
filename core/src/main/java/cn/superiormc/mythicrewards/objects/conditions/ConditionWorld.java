package cn.superiormc.mythicrewards.objects.conditions;

import cn.superiormc.mythicrewards.listeners.TrackerResult;
import org.bukkit.entity.Player;

public class ConditionWorld extends AbstractCheckCondition {

    public ConditionWorld() {
        super("world");
        setRequiredArgs("world");
    }

    @Override
    protected boolean onCheckCondition(ObjectSingleCondition singleCondition, Player player, TrackerResult result) {
        return player.getWorld().getName().equals(singleCondition.getString("world", player, result));
    }
}
