package cn.superiormc.mythicrewards.objects.actions;

import cn.superiormc.mythicrewards.listeners.TrackerResult;
import cn.superiormc.mythicrewards.utils.TextUtil;
import org.bukkit.entity.Player;

import java.util.List;

public class ActionMessage extends AbstractRunAction {

    public ActionMessage() {
        super("message");
    }

    @Override
    protected void onDoAction(ObjectSingleAction singleAction, Player player, TrackerResult result) {
        List<String> messages = singleAction.getStringList("messages", player, result);
        if (messages != null && !messages.isEmpty()) {
            for (String msg : messages) {
                int minPlayers = parseMinPlayers(msg);
                if (result.getAllPlayers().size() < minPlayers) {
                    continue;
                }

                if (msg.startsWith("@")) {
                    int idx = msg.indexOf(" ");
                    if (idx != -1) msg = msg.substring(idx + 1);
                }

                TextUtil.sendMessage(player, msg);
            }
            return;
        }

        String message = singleAction.getString("message", player, result);
        if (message != null && !message.isEmpty()) {
            TextUtil.sendMessage(player, message);
        }
    }
}