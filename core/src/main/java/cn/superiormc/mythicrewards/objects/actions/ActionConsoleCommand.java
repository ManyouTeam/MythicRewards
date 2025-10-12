package cn.superiormc.mythicrewards.objects.actions;

import cn.superiormc.mythicrewards.MythicRewards;
import cn.superiormc.mythicrewards.listeners.TrackerResult;
import org.bukkit.entity.Player;

public class ActionConsoleCommand extends AbstractRunAction {

    public ActionConsoleCommand() {
        super("console_command");
        setRequiredArgs("command");
    }

    @Override
    protected void onDoAction(ObjectSingleAction singleAction, Player player, TrackerResult result) {
        MythicRewards.methodUtil.dispatchCommand(singleAction.getString("command", player, result));
    }
}
