package cn.superiormc.mythicrewards.managers;

import cn.superiormc.mythicrewards.MythicRewards;
import cn.superiormc.mythicrewards.objects.LicenseType;
import cn.superiormc.mythicrewards.objects.rule.ObjectSingleRule;
import cn.superiormc.mythicrewards.utils.TextUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;

import java.io.File;
import java.util.*;

public class ConfigManager {

    public static ConfigManager configManager;

    public FileConfiguration config;

    public Map<String, ObjectSingleRule> ruleMap = new TreeMap<>();

    public Map<UUID, ObjectSingleRule> entityMatchMap = new HashMap<>();

    public Collection<ObjectSingleRule> ruleCaches = new TreeSet<>();

    public ConfigManager() {
        configManager = this;
        config = MythicRewards.instance.getConfig();
        initRulesConfigs();
    }

    private void initRulesConfigs() {
        File dir = new File(MythicRewards.instance.getDataFolder(), "rules");
        if (!dir.exists()) {
            dir.mkdir();
        }
        loadRules(dir);
    }

    private void loadRules(File folder) {
        File[] files = folder.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                loadRules(file); // 递归调用以加载子文件夹内的文件
            } else {
                String fileName = file.getName();
                if (fileName.endsWith(".yml")) {
                    String substring = fileName.substring(0, fileName.length() - 4);
                    if (ruleMap.containsKey(substring)) {
                        ErrorManager.errorManager.sendErrorMessage("§cError: Already loaded a rule config called: " +
                                fileName + "!");
                        continue;
                    }
                    ObjectSingleRule rule = new ObjectSingleRule(substring, YamlConfiguration.loadConfiguration(file));
                    ruleCaches.add(rule);
                    ruleMap.put(substring, rule);
                    if (ruleMap.size() >= 8 && MythicRewards.freeVersion) {
                        TextUtil.sendMessage(null, TextUtil.pluginPrefix() + " §cError: Free version only allows you create up to 8 reward rules, ignored excess rules!");
                        break;
                    }
                    TextUtil.sendMessage(null, TextUtil.pluginPrefix() + " §fLoaded reward rule: " + fileName + "!");
                }
            }
        }
    }

    public boolean containsEntityMatchRuleCache(LivingEntity entity) {
        return entity != null && entityMatchMap.containsKey(entity.getUniqueId());
    }

    public ObjectSingleRule getEntityMatchRule(LivingEntity entity) {
        if (entity == null) {
            return null;
        }
        UUID entityId = entity.getUniqueId();
        if (entityMatchMap.containsKey(entityId)) {
            return entityMatchMap.get(entityId);
        }
        for (ObjectSingleRule rule: ruleCaches) {
            if (rule.getMatchEntity(entity)) {
                entityMatchMap.put(entityId, rule);
                if (ConfigManager.configManager.getBoolean("debug")) {
                    TextUtil.sendMessage(null, TextUtil.pluginPrefix() + " §fAdded rule cache for entity: " +
                            MythicRewards.methodUtil.getEntityName(entity) + " (" + entity.getUniqueId().toString() + "), match rule: " + rule.getId() + "!");
                    TextUtil.sendMessage(null, TextUtil.pluginPrefix() + " §fNow entity cache amount: " + entityMatchMap.size() + "!");
                }
                return rule;
            }
        }
        return null;
    }

    public ObjectSingleRule getEntityMatchRuleCache(LivingEntity entity) {
        if (entity == null) {
            return null;
        }
        UUID entityId = entity.getUniqueId();
        if (entityMatchMap.containsKey(entityId)) {
            return entityMatchMap.get(entityId);
        }
        return null;
    }

    public void removeEntityMatchMap(LivingEntity entity) {
        if (entity == null) {
            return;
        }
        if (ConfigManager.configManager.getBoolean("debug")) {
            TextUtil.sendMessage(null, TextUtil.pluginPrefix() + " §fRemoved rule cache for entity: " +
                    MythicRewards.methodUtil.getEntityName(entity) + " (" + entity.getUniqueId().toString() + ")!");
            TextUtil.sendMessage(null, TextUtil.pluginPrefix() + " §fNow entity cache amount: " + entityMatchMap.size() + "!");
        }
        removeEntityMatchMap(entity.getUniqueId());
    }

    public void removeEntityMatchMap(UUID entityId) {
        entityMatchMap.remove(entityId);
    }

    public Map<UUID, ObjectSingleRule> getEntityMatchMap() {
        return entityMatchMap;
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path, false);
    }

    public boolean getBoolean(String path, boolean defaultValue) {
        return config.getBoolean(path, defaultValue);
    }

    public String getString(String path, String... args) {
        String s = config.getString(path);
        if (s == null) {
            if (args.length == 0) {
                return null;
            }
            s = args[0];
        }
        for (int i = 1 ; i < args.length ; i += 2) {
            String var = "{" + args[i] + "}";
            if (args[i + 1] == null) {
                s = s.replace(var, "");
            }
            else {
                s = s.replace(var, args[i + 1]);
            }
        }
        return s.replace("{plugin_folder}", String.valueOf(MythicRewards.instance.getDataFolder()));
    }

    public int getInt(String path, int defaultValue) {
        return config.getInt(path, defaultValue);
    }

    public ConfigurationSection getSection(String path) {
        if (config.getConfigurationSection(path) == null) {
            return new MemoryConfiguration();
        }
        return config.getConfigurationSection(path);
    }
}
