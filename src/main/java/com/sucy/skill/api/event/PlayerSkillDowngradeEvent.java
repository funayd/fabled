/**
 * Fabled
 * studio.magemonkey.fabled.api.event.PlayerSkillDowngradeEvent
 * <p>
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2024 MageMonkeyStudio
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software") to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.sucy.skill.api.event;

import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.api.player.PlayerSkill;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event called when a player downgrades a skill
 * @deprecated use {@link studio.magemonkey.fabled.api.event.PlayerSkillDowngradeEvent} instead
 */
@Deprecated(forRemoval = true)
public class PlayerSkillDowngradeEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final PlayerData  playerData;
    private final PlayerSkill downgradedSkill;
    private final int         refund;
    private       boolean     cancelled = false;

    /**
     * Constructor
     *
     * @param playerData      data of the player downgrading the skill
     * @param downgradedSkill the skill that was downgraded
     * @param refund          the amount of refunded skill points
     */
    public PlayerSkillDowngradeEvent(studio.magemonkey.fabled.api.player.PlayerData playerData,
                                     studio.magemonkey.fabled.api.player.PlayerSkill downgradedSkill,
                                     int refund) {
        this.playerData = new PlayerData(playerData);
        this.downgradedSkill = new PlayerSkill(downgradedSkill);
        this.refund = refund;
    }

    /**
     * @return data of the player unlocking the skill
     */
    public PlayerData getPlayerData() {
        return playerData;
    }

    /**
     * @return skill that was unlocked
     */
    public PlayerSkill getDowngradedSkill() {
        return downgradedSkill;
    }

    /**
     * @return amount of refunded points from the downgrade
     */
    public int getRefund() {
        return refund;
    }

    /**
     * @return true if cancelled, false otherwise
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets whether the event is cancelled
     *
     * @param value true if cancelled, false otherwise
     */
    @Override
    public void setCancelled(boolean value) {
        cancelled = value;
    }


    /**
     * @return gets the handlers for the event
     */
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @return gets the handlers for the event
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
