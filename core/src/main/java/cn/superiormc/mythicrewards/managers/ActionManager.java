package cn.superiormc.mythicrewards.managers;


import cn.superiormc.mythicrewards.MythicRewards;
import cn.superiormc.mythicrewards.listeners.TrackerResult;
import cn.superiormc.mythicrewards.objects.LicenseType;
import cn.superiormc.mythicrewards.objects.actions.*;
import cn.superiormc.mythicrewards.utils.TextUtil;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ActionManager {

    public static ActionManager actionManager;

    private Map<String, AbstractRunAction> actions;

    public ActionManager() {
        actionManager = this;
        initActions();
    }

    private void initActions() {
        actions = new HashMap<>();
        registerNewAction("message", new ActionMessage());
        registerNewAction("title", new ActionTitle());
        registerNewAction("particle", new ActionParticle());
        registerNewAction("sound", new ActionSound());
        registerNewAction("effect", new ActionEffect());
        registerNewAction("console_command", new ActionConsoleCommand());
        registerNewAction("op_command", new ActionOPCommand());
        registerNewAction("player_command", new ActionPlayerCommand());
        registerNewAction("teleport", new ActionTeleport());
        registerNewAction("entity_spawn", new ActionEntitySpawn());
        registerNewAction("chance", new ActionChance());
        registerNewAction("delay", new ActionDelay());
        registerNewAction("any", new ActionAny());
        registerNewAction("conditional", new ActionConditional());
        registerNewAction("mythicmobs_spawn", new ActionMythicMobsSpawn());
        if (InitManager.initManager.getLicenseType() != LicenseType.ACTION_TYPE_LIMITED) {
            MythicRewards.methodUtil.sendMessage(null, TextUtil.pluginPrefix() + " §cLicense ATL do not allow you use give_item and drop_item actions, ignored those action in configs!");
            registerNewAction("drop_item", new ActionDropItem());
            registerNewAction("give_item", new ActionGiveItem());
        }
    }

    public void registerNewAction(String actionID,
                                  AbstractRunAction action) {
        if (!actions.containsKey(actionID)) {
            actions.put(actionID, action);
        }
    }

    public void doAction(ObjectSingleAction action, Player player, TrackerResult result) {
        for (AbstractRunAction runAction : actions.values()) {
            String type = action.getString("type");
            if (runAction.getType().equals(type)) {
                runAction.runAction(action, player, result);
            }
        }
    }
}
