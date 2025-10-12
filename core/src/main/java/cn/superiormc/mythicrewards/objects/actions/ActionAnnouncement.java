package cn.superiormc.mythicrewards.objects.actions;

import cn.superiormc.mythicrewards.MythicRewards;
import cn.superiormc.mythicrewards.listeners.TrackerResult;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public class ActionAnnouncement extends AbstractRunAction {

    public ActionAnnouncement() {
        super("announcement");
        setRequiredArgs("message");
    }

    @Override
    protected void onDoAction(ObjectSingleAction singleAction, Player player, TrackerResult result) {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        for (Player p : players) {
            List<String> messages = singleAction.getStringList("messages", p, result);
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

                    MythicRewards.methodUtil.sendMessage(p, msg);
                }
                return;
            }

            String message = singleAction.getString("message", p, result);
            if (message != null && !message.isEmpty()) {
                MythicRewards.methodUtil.sendMessage(p, message);
            }
        }
    }
}
