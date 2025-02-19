package studio.magemonkey.fabled.dynamic.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import studio.magemonkey.fabled.dynamic.ComponentType;
import studio.magemonkey.fabled.dynamic.EffectComponent;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Fabled © 2024
 * studio.magemonkey.fabled.dynamic.condition.ConditionComponent
 */
public abstract class ConditionComponent extends EffectComponent {

    /**
     * {@inheritDoc}
     */
    @Override
    public ComponentType getType() {
        return ComponentType.CONDITION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(
            final LivingEntity caster, final int level, final List<LivingEntity> targets, boolean force) {
        final List<LivingEntity> filtered = targets.stream()
                .filter(t -> test(caster, level, t))
                .collect(Collectors.toList());

        return filtered.size() > 0 && executeChildren(caster, level, filtered, force);
    }

    abstract boolean test(final LivingEntity caster, final int level, final LivingEntity target);

    /**
     * {@inheritDoc}
     */
    @Override
    public void playPreview(List<Runnable> onPreviewStop,
                            Player caster,
                            int level,
                            Supplier<List<LivingEntity>> targetSupplier) {
        super.playPreview(onPreviewStop, caster, level, () -> targetSupplier.get().stream()
                .filter(t -> test(caster, level, t))
                .collect(Collectors.toList()));
    }
}
