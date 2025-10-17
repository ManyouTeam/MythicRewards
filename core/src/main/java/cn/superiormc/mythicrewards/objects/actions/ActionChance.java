package cn.superiormc.mythicrewards.objects.actions;

import cn.superiormc.mythicrewards.listeners.TrackerResult;
import cn.superiormc.mythicrewards.objects.ObjectAction;
import org.apache.commons.lang3.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ActionChance extends AbstractRunAction {

    public ActionChance() {
        super("chance");
        setRequiredArgs("rate", "actions");
    }

    @Override
    protected void onDoAction(ObjectSingleAction singleAction, Player player, TrackerResult result) {
        ConfigurationSection chanceSection = singleAction.getSection().getConfigurationSection("actions");
        if (chanceSection == null) {
            return;
        }
        double rate = singleAction.getDouble("rate", player, result);
        if (RandomUtils.nextDouble(0, 100) <= rate) {
            ObjectAction action = new ObjectAction(chanceSection);
            action.runAllActions(player, result);
        }
    }
}
