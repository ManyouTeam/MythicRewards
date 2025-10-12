package cn.superiormc.mythicrewards.objects;

import cn.superiormc.mythicrewards.listeners.TrackerResult;
import cn.superiormc.mythicrewards.objects.actions.ObjectSingleAction;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ObjectAction {

    private ConfigurationSection section;

    private final List<ObjectSingleAction> everyActions = new ArrayList<>();

    private boolean isEmpty = false;

    public ObjectAction() {
        this.isEmpty = true;
    }

    public ObjectAction(ConfigurationSection section) {
        this.section = section;
        initAction();
    }

    private void initAction() {
        if (section == null) {
            this.isEmpty = true;
            this.section = new MemoryConfiguration();
            return;
        }
        for (String key : section.getKeys(false)) {
            ConfigurationSection singleActionSection = section.getConfigurationSection(key);
            if (singleActionSection == null || !section.isConfigurationSection(key)) {
                continue;
            }
            ObjectSingleAction singleAction = new ObjectSingleAction(this, singleActionSection);
            everyActions.add(singleAction);
        }
        this.isEmpty = everyActions.isEmpty();
    }

    public void runAllActions(Player player, TrackerResult result) {
        for (ObjectSingleAction singleAction : everyActions) {
            singleAction.doAction(player, result);
        }
    }

    public void runRandomEveryActions(Player player, TrackerResult result, int x) {
        Collections.shuffle(everyActions);  // 随机打乱动作顺序
        for (int i = 0; i < Math.min(x, everyActions.size()); i++) {
            everyActions.get(i).doAction(player, result);  // 执行 x 个随机动作
        }
    }

    public int getAmount() {
        return section.getInt("amount");
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
