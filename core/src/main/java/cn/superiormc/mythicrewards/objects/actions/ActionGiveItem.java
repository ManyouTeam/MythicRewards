package cn.superiormc.mythicrewards.objects.actions;

import cn.superiormc.mythicrewards.listeners.TrackerResult;
import cn.superiormc.mythicrewards.methods.BuildItem;
import cn.superiormc.mythicrewards.utils.CommonUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ActionGiveItem extends AbstractRunAction {

    public ActionGiveItem() {
        super("give_item");
        setRequiredArgs("item");
    }

    @Override
    protected void onDoAction(ObjectSingleAction singleAction, Player player, TrackerResult result) {
        ItemStack item = BuildItem.buildItemStack(player, singleAction.getSection().getConfigurationSection("item"));
        CommonUtil.giveOrDrop(player, item);
    }
}
