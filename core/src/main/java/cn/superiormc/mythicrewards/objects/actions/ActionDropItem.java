package cn.superiormc.mythicrewards.objects.actions;

import cn.superiormc.mythicrewards.MythicRewards;
import cn.superiormc.mythicrewards.listeners.TrackerResult;
import cn.superiormc.mythicrewards.methods.BuildItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ActionDropItem extends AbstractRunAction {

    public ActionDropItem() {
        super("drop_item");
        setRequiredArgs("item");
    }

    @Override
    protected void onDoAction(ObjectSingleAction singleAction, Player player, TrackerResult result) {
        String worldName = singleAction.getString("world");
        Location location;
        if (worldName == null) {
            location = player.getLocation();
        } else {
            World world = Bukkit.getWorld(worldName);
            location = new Location(world,
                    singleAction.getDouble("x", player, result),
                    singleAction.getDouble("y", player, result),
                    singleAction.getDouble("z", player, result));

        }
        ItemStack item = BuildItem.buildItemStack(player, singleAction.getSection().getConfigurationSection("item"));
        MythicRewards.methodUtil.dropPrivateItem(player, item, location);
    }
}
