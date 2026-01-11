package cn.superiormc.mythicrewards.objects.actions;

import cn.superiormc.mythicrewards.MythicRewards;
import cn.superiormc.mythicrewards.listeners.TrackerResult;
import org.bukkit.entity.Player;

public class ActionActionBar extends AbstractRunAction {

    public ActionActionBar() {
        super("action_bar");
        setRequiredArgs("message");
    }

    @Override
    protected void onDoAction(ObjectSingleAction singleAction, Player player, TrackerResult result) {
        String msg = singleAction.getString("message", player, result);
        MythicRewards.methodUtil.sendActionBar(player, msg);
    }
}
