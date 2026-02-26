package cn.superiormc.mythicrewards.objects.conditions;

import cn.superiormc.mythicrewards.listeners.TrackerResult;
import cn.superiormc.mythicrewards.managers.ErrorManager;
import cn.superiormc.mythicrewards.utils.MathUtil;
import org.bukkit.entity.Player;

public class ConditionPlaceholder extends AbstractCheckCondition {

    public ConditionPlaceholder() {
        super("placeholder");
        setRequiredArgs("placeholder", "rule", "value");
    }

    @Override
    protected boolean onCheckCondition(ObjectSingleCondition singleCondition, Player player, TrackerResult result) {
        String placeholder = singleCondition.getString("placeholder", player, result);
        if (placeholder.isEmpty()) {
            return true;
        }
        String value = singleCondition.getString("value", player, result);
        try {
            switch (singleCondition.getString("rule")) {
                case ">=":
                    return MathUtil.doCalculate(placeholder) >= MathUtil.doCalculate(value);
                case ">":
                    return MathUtil.doCalculate(placeholder) > MathUtil.doCalculate(value);
                case "=":
                    return MathUtil.doCalculate(placeholder) == MathUtil.doCalculate(value);
                case "<":
                    return MathUtil.doCalculate(placeholder) < MathUtil.doCalculate(value);
                case "<=":
                    return MathUtil.doCalculate(placeholder) <= MathUtil.doCalculate(value);
                case "==":
                    return placeholder.equals(value);
                case "!=":
                    return !placeholder.equals(value);
                case "*=":
                    return placeholder.contains(value);
                case "=*":
                    return value.contains(placeholder);
                case "!*=":
                    return !placeholder.contains(value);
                case "!=*":
                    return !value.contains(placeholder);
                default:
                    ErrorManager.errorManager.sendErrorMessage("§cError: Your placeholder condition can not being correctly load.");
                    return true;
            }
        } catch (Throwable throwable) {
            ErrorManager.errorManager.sendErrorMessage("§cError: Your placeholder condition can not being correctly load.");
            return true;
        }
    }
}
