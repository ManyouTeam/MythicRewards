package cn.superiormc.mythicrewards.objects.actions;

import cn.superiormc.mythicrewards.MythicRewards;
import cn.superiormc.mythicrewards.listeners.TrackerResult;
import org.bukkit.entity.Player;

public class ActionTitle extends AbstractRunAction {

    public ActionTitle() {
        super("title");
        setRequiredArgs("main-title", "sub-title", "fade-in", "stay", "fade-out");
    }

    @Override
    protected void onDoAction(ObjectSingleAction singleAction, Player player, TrackerResult result) {
        MythicRewards.methodUtil.sendTitle(player,
                singleAction.getString("main-title", player, result),
                singleAction.getString("sub-title", player, result),
                singleAction.getInt("fade-in"),
                singleAction.getInt("stay"),
                singleAction.getInt("fade-out"));
    }
}
