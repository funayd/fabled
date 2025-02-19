package studio.magemonkey.fabled.dynamic.trigger;

import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import studio.magemonkey.fabled.api.Settings;

public class FishingFishTrigger extends FishingTrigger {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey() {
        return "FISHING";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldTrigger(PlayerFishEvent event, int level, Settings settings) {

        if (event.getState() == State.FISHING) {
            return true;
        }
        return false;
    }

}
