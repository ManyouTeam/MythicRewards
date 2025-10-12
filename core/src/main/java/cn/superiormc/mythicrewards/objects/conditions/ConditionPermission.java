package cn.superiormc.mythicrewards.objects.conditions;


import cn.superiormc.mythicrewards.listeners.TrackerResult;
import org.bukkit.entity.Player;

public class ConditionPermission extends AbstractCheckCondition {

    public ConditionPermission() {
        super("permission");
        setRequiredArgs("permission");
    }

    @Override
    protected boolean onCheckCondition(ObjectSingleCondition singleCondition, Player player, TrackerResult result) {
        return player.hasPermission(singleCondition.getString("permission"));
    }
}
