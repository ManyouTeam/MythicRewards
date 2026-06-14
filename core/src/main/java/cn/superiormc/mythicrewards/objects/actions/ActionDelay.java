package cn.superiormc.mythicrewards.objects.actions;

import cn.superiormc.mythicrewards.listeners.TrackerResult;
import cn.superiormc.mythicrewards.objects.ObjectAction;
import cn.superiormc.mythicrewards.utils.SchedulerUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ActionDelay extends AbstractRunAction {

    public ActionDelay() {
        super("delay");
        setRequiredArgs("time", "actions");
    }

    @Override
    protected void onDoAction(ObjectSingleAction singleAction, Player player, TrackerResult result) {
        ConfigurationSection chanceSection = singleAction.getSection().getConfigurationSection("actions");
        if (chanceSection == null) {
            return;
        }
        long time = singleAction.getSection().getLong("time");
        ObjectAction action = new ObjectAction(chanceSection);
        UUID playerId = player.getUniqueId();
        SchedulerUtil.runTaskLater(() -> {
            Player onlinePlayer = Bukkit.getPlayer(playerId);
            if (onlinePlayer != null && onlinePlayer.isOnline()) {
                action.runAllActions(onlinePlayer, result);
            }
        }, time);
    }
}
