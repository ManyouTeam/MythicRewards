package cn.superiormc.mythicrewards.objects.actions;

import cn.superiormc.mythicrewards.MythicRewards;
import cn.superiormc.mythicrewards.listeners.DamageTracker;
import cn.superiormc.mythicrewards.listeners.TrackerResult;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class ActionOPCommand extends AbstractRunAction {

    public ActionOPCommand() {
        super("op_command");
        setRequiredArgs("command");
    }

    @Override
    protected void onDoAction(ObjectSingleAction singleAction, Player player, TrackerResult result) {
        MythicRewards.methodUtil.dispatchOpCommand(player, singleAction.getString("command", player, result));
    }
}
