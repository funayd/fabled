package studio.magemonkey.fabled.dynamic.condition;

import org.bukkit.entity.LivingEntity;
import studio.magemonkey.codex.mccore.config.parse.DataSection;
import studio.magemonkey.fabled.dynamic.DynamicSkill;

public class BurningCondition extends ConditionComponent {

    private static final String BURN = "burn";

    private String burn;

    @Override
    boolean test(LivingEntity caster, int level, LivingEntity target) {

        return (caster.getFireTicks() > 0) == burn.equalsIgnoreCase("Burn");
    }

    @Override
    public void load(DynamicSkill skill, DataSection config) {
        super.load(skill, config);

        burn = settings.getString(BURN);
    }

    @Override
    public String getKey() {
        return "Burning";
    }

}
