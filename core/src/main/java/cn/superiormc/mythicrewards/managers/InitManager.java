package cn.superiormc.mythicrewards.managers;

import cn.superiormc.mythicrewards.MythicRewards;
import cn.superiormc.mythicrewards.objects.LicenseType;
import cn.superiormc.mythicrewards.utils.CommonUtil;
import cn.superiormc.mythicrewards.utils.TextUtil;

import java.io.File;

public class InitManager {

    public static InitManager initManager;

    private boolean firstLoad = false;

    private LicenseType licenseType;

    public InitManager() {
        initManager = this;
        File file = new File(MythicRewards.instance.getDataFolder(), "config.yml");
        if (!file.exists()) {
            MythicRewards.instance.saveDefaultConfig();
            firstLoad = true;
        }
        init();
    }

    public void init() {
        resourceOutput("rules/All.yml", false);
        resourceOutput("rules/MultiPack.yml", false);
        resourceOutput("rules/Rank.yml", false);
        resourceOutput("rules/Pack.yml", false);
        resourceOutput("rules/Separate.yml", false);
        resourceOutput("rules/SeparatePack.yml", false);
        resourceOutput("languages/en_US.yml", true);
        resourceOutput("languages/zh_CN.yml", true);
    }

    public void initLicense(String... classNames) {
        if (ConfigManager.configManager.getString("license-type").equalsIgnoreCase("NORL")) {
            licenseType = LicenseType.NUMBER_OF_RULES_LIMITED;
        } else if (ConfigManager.configManager.getString("license-type").equalsIgnoreCase("ATL")) {
            licenseType = LicenseType.ACTION_TYPE_LIMITED;
        } else {
            licenseType = LicenseType.NUMBER_OF_RULES_LIMITED;
        }

        for (String name : classNames) {
            try {
                Class<?> clazz = Class.forName(name);
                boolean value = clazz.getField("freeVersion").getBoolean(null);
                if (!value) {
                    licenseType = LicenseType.FULL;
                    TextUtil.sendMessage(null, TextUtil.pluginPrefix() + " §cFULL license active by " + clazz.getSimpleName() + " plugin, thanks for your support and we hope you have good experience with this plugin!");
                    break;
                }
            } catch (Throwable e) {
                // ignored
            }
        }
    }

    private void resourceOutput(String fileName, boolean regenerate) {
        File tempVal1 = new File(MythicRewards.instance.getDataFolder(), fileName);
        if (!tempVal1.exists()) {
            if (!firstLoad && !regenerate) {
                return;
            }
            File tempVal2 = new File(fileName);
            if (tempVal2.getParentFile() != null) {
                CommonUtil.mkDir(tempVal2.getParentFile());
            }
            MythicRewards.instance.saveResource(tempVal2.getPath(), false);
        }
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public boolean isFirstLoad() {
        return firstLoad;
    }
}
