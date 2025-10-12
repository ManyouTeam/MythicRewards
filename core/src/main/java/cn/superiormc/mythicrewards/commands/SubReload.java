package cn.superiormc.mythicrewards.commands;

import cn.superiormc.mythicrewards.MythicRewards;
import cn.superiormc.mythicrewards.managers.ConfigManager;
import cn.superiormc.mythicrewards.managers.ItemManager;
import cn.superiormc.mythicrewards.managers.LanguageManager;
import org.bukkit.entity.Player;

public class SubReload extends AbstractCommand {

    public SubReload() {
        this.id = "reload";
        this.requiredPermission =  "mythicrewards." + id;
        this.onlyInGame = false;
        this.requiredArgLength = new Integer[]{1};
    }

    @Override
    public void executeCommandInGame(String[] args, Player player) {
        MythicRewards.instance.reloadConfig();
        new ConfigManager();
        new ItemManager();
        new LanguageManager();
        LanguageManager.languageManager.sendStringText(player, "plugin.reloaded");
    }

    @Override
    public void executeCommandInConsole(String[] args) {
        MythicRewards.instance.reloadConfig();
        new ConfigManager();
        new ItemManager();
        new LanguageManager();
        LanguageManager.languageManager.sendStringText("plugin.reloaded");
    }
}
