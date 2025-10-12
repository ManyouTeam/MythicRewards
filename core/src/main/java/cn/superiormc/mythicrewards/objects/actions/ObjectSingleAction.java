package cn.superiormc.mythicrewards.objects.actions;

import cn.superiormc.mythicrewards.listeners.TrackerResult;
import cn.superiormc.mythicrewards.managers.ActionManager;
import cn.superiormc.mythicrewards.objects.AbstractSingleRun;
import cn.superiormc.mythicrewards.objects.ObjectAction;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ObjectSingleAction extends AbstractSingleRun {

    private final ObjectAction action;

    public ObjectSingleAction(ObjectAction action, ConfigurationSection actionSection) {
        super(actionSection);
        this.action = action;
    }

    public void doAction(Player player, TrackerResult result) {
        if (meetRankLimit(result.getPlayerRank(player))) {
            ActionManager.actionManager.doAction(this, player, result);
        }
    }

    public ObjectAction getAction() {
        return action;
    }

}
