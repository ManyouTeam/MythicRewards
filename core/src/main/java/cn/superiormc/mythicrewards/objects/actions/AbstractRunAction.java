package cn.superiormc.mythicrewards.objects.actions;

import cn.superiormc.mythicrewards.listeners.TrackerResult;
import cn.superiormc.mythicrewards.managers.ErrorManager;
import org.bukkit.entity.Player;

public abstract class AbstractRunAction {

    private final String type;

    private String[] requiredArgs;

    public AbstractRunAction(String type) {
        this.type = type;
    }

    protected void setRequiredArgs(String... requiredArgs) {
        this.requiredArgs = requiredArgs;
    }

    public void runAction(ObjectSingleAction singleAction, Player player, TrackerResult result) {
        if (requiredArgs != null) {
            for (String arg : requiredArgs) {
                if (!singleAction.getSection().contains(arg)) {
                    ErrorManager.errorManager.sendErrorMessage("§cError: Your action missing required arg: " + arg + ".");
                    return;
                }
            }
        }
        onDoAction(singleAction, player, result);
    }

    protected abstract void onDoAction(ObjectSingleAction singleAction, Player player, TrackerResult result);

    public String getType() {
        return type;
    }

    protected int parseMinPlayers(String msg) {
        if (msg == null || !msg.startsWith("@")) {
            return 0;
        }
        int idx = msg.indexOf(" ");
        if (idx == -1) {
            return 0;
        }
        try {
            return Integer.parseInt(msg.substring(1, idx));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
