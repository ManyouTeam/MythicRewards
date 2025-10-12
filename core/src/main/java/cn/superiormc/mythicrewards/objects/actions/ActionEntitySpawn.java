package cn.superiormc.mythicrewards.objects.actions;

import cn.superiormc.mythicrewards.MythicRewards;
import cn.superiormc.mythicrewards.listeners.TrackerResult;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class ActionEntitySpawn extends AbstractRunAction {

    public ActionEntitySpawn() {
        super("entity_spawn");
        setRequiredArgs("entity");
    }

    @Override
    protected void onDoAction(ObjectSingleAction singleAction, Player player, TrackerResult result) {
        EntityType entityType = EntityType.valueOf(singleAction.getString("entity").toUpperCase());
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
        MythicRewards.methodUtil.spawnEntity(location, entityType);
    }
}
