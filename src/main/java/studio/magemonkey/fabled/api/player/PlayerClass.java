/**
 * Fabled
 * studio.magemonkey.fabled.api.player.PlayerClass
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
package studio.magemonkey.fabled.api.player;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import studio.magemonkey.codex.mccore.config.Filter;
import studio.magemonkey.fabled.Fabled;
import studio.magemonkey.fabled.api.classes.FabledClass;
import studio.magemonkey.fabled.api.enums.ExpSource;
import studio.magemonkey.fabled.api.enums.PointSource;
import studio.magemonkey.fabled.api.event.*;
import studio.magemonkey.fabled.api.skills.Skill;
import studio.magemonkey.fabled.data.Settings;
import studio.magemonkey.fabled.data.TitleType;
import studio.magemonkey.fabled.dynamic.DynamicSkill;
import studio.magemonkey.fabled.language.NotificationNodes;
import studio.magemonkey.fabled.language.RPGFilter;
import studio.magemonkey.fabled.manager.TitleManager;

/**
 * <p>Represents a player's class progress.</p>
 *
 * <p>This class if for handling individual players.</p>
 *
 * <p>This does not include information about the class specifically,
 * rather what the player has within the class. For more general information
 * about the class, you should use the RPGClass class.</p>
 */
public class PlayerClass {
    private final PlayerData  player;
    private       FabledClass classData;
    private       int         level;

    // If shared-skill-points is enabled, this tracks the points gained (not lost) by each class,
    // so that if it is later disabled, points can be distributed properly
    private int points;

    private double exp;

    ///////////////////////////////////////////////////////
    //                                                   //
    //                   Constructors                    //
    //                                                   //
    ///////////////////////////////////////////////////////

    /**
     * Initializes a new PlayerClass. This should not be used
     * by other plugins as the API provides the data. Get
     * instances from the PlayerData object.
     *
     * @param player    owning player data
     * @param classData class template
     */
    public PlayerClass(PlayerData player, FabledClass classData) {
        this.player = player;
        this.classData = classData;
        this.level = 1;
        this.points = 0;
        this.exp = 0;

        for (Skill skill : classData.getSkills()) {
            player.giveSkill(skill, this);
        }
    }

    ///////////////////////////////////////////////////////
    //                                                   //
    //                 Accessor Methods                  //
    //                                                   //
    ///////////////////////////////////////////////////////

    /**
     * <p>Retrieves the data of the player owning this class.</p>
     *
     * @return data of owning player
     */
    public PlayerData getPlayerData() {
        return player;
    }

    /**
     * <p>Retrieves the generic data for the class.</p>
     *
     * @return generic data for the class
     */
    public FabledClass getData() {
        return classData;
    }

    /**
     * <p>Retrieves the experience of the class towards the next level.</p>
     * <p>This should not ever be higher than the required experience.</p>
     *
     * @return the current experience of the class towards the next level
     */
    public double getExp() {
        return exp;
    }

    /**
     * Sets the current experience for the player
     *
     * @param exp experience to set to
     */
    public void setExp(double exp) {
        this.exp = Math.max(Math.min(exp, getRequiredExp() - 1), 0);
    }

    /**
     * <p>Retrieves the required experience to level up to the next level.</p>
     *
     * @return the current required experience
     */
    public int getRequiredExp() {
        return classData.getRequiredExp(level);
    }

    /**
     * <p>Retrieves the total amount of experience the player has accumulated
     * for this class since professing as it.</p>
     *
     * @return total accumulated experience for the class
     */
    public double getTotalExp() {
        double exp = this.exp;
        for (int i = 1; i < level; i++)
            exp += classData.getRequiredExp(i);
        return exp;
    }

    /**
     * <p>Retrieves the current level of the class.</p>
     * <p>This should never be less than 1 or greater than the maximum level.</p>
     *
     * @return current level of the class
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets the level for the class
     *
     * @param level level to set to
     */
    public void setLevel(int level) {
        if (level < 1)
            throw new IllegalArgumentException("Cannot be a level less than 1");

        this.level = level;
    }

    /**
     * <p>Retrieves the number of skill points the class has currently available.</p>
     * <p>This should never be a negative number.</p>
     *
     * @return number of available skill points
     */
    public int getPoints() {
        return Fabled.getSettings().isSharedSkillPoints() ? this.getPlayerData().getPoints() : this.points;
    }

    /**
     * <p>Sets the amount of points the player's class has without
     * launching an event.</p>
     * <p>This cannot be less than 0.</p>
     * <p>This is used primarily for initialization. You should generally
     * use givePoints(int, PointSource) instead.</p>
     *
     * @param amount number of points to set it to
     */
    public void setPoints(int amount) {
        // Cannot have a negative amount of points
        /*if (amount < 0) {
            throw new IllegalArgumentException("Invalid point amount - cannot be less than 1");
        }*/

        // Set the points
        if (Fabled.getSettings().isSharedSkillPoints()) {
            this.getPlayerData().setPoints(amount);
        } else {
            points = amount;
        }
    }

    /**
     * When {@link Settings#isSharedSkillPoints() is enabled} sets the amount of skill points
     * this class has earned. Used if the shared skill points option is later disabled,
     * to correctly redistribute the points across all classes
     */
    public void setEarnedPoints(int amount) {
        if (!Fabled.getSettings().isSharedSkillPoints())
            throw new IllegalStateException("'shared-skill-points' is not enabled");
        this.points = amount;
    }


    /**
     * When {@link Settings#isSharedSkillPoints() is enabled} gets the amount of skill points
     * this class has earned. Used if the shared skill points option is later disabled,
     * to correctly redistribute the points across all classes
     */
    public int getEarnedPoints() {
        if (!Fabled.getSettings().isSharedSkillPoints())
            throw new IllegalStateException("'shared-skill-points' is not enabled");
        return this.points;
    }

    ///////////////////////////////////////////////////////
    //                                                   //
    //                Functional Methods                 //
    //                                                   //
    ///////////////////////////////////////////////////////

    /**
     * <p>Checks whether the class has reached the max level.</p>
     *
     * @return true if max level, false otherwise
     */
    public boolean isLevelMaxed() {
        return level == classData.getMaxLevel();
    }

    /**
     * Retrieves the amount of health this class provides the player
     *
     * @return health provided for the player by this class
     */
    public double getHealth() {
        return classData.getHealth(level);
    }

    /**
     * Retrieves the amount of mana this class provides the player
     *
     * @return mana provided for the player by this class
     */
    public double getMana() {
        return classData.getMana(level);
    }

    /**
     * <p>Gives skill points to be used for the class.</p>
     * <p>The number of points cannot be negative.</p>
     * <p>This calls an event that can be cancelled or have the number
     * of points modified.</p>
     * <p>This treats the points as coming from the source "SPECIAL".</p>
     *
     * @param amount amount of points to give
     * @throws java.lang.IllegalArgumentException if the points are less than 1
     */
    public void givePoints(int amount) {
        givePoints(amount, PointSource.SPECIAL);
    }

    /**
     * <p>Gives skill points to be used for the class.</p>
     * <p>The number of points cannot be negative.</p>
     * <p>This calls an event that can be cancelled or have the number
     * of points modified.</p>
     *
     * @param amount amount of points to give
     * @param source source of the points
     * @throws java.lang.IllegalArgumentException if the points are less than 1
     */
    public void givePoints(int amount, PointSource source) {
        // Cannot give a non-positive amount of points
        /*if (amount < 1) {
            throw new IllegalArgumentException("Invalid point amount - cannot be less than 1");
        }*/

        // Call the event
        PlayerGainSkillPointsEvent event = new PlayerGainSkillPointsEvent(this, amount, source);
        Bukkit.getPluginManager().callEvent(event);

        // Add the points if not cancelled
        if (!event.isCancelled()) {
            if (Fabled.getSettings().isSharedSkillPoints()) {
                this.getPlayerData().setPoints((int) (this.getPlayerData().getPoints() + event.getAmount()));
            }
            if (!Fabled.getSettings().isSharedSkillPoints() || source != PointSource.REFUND)
                points += event.getAmount();
        }
    }

    /**
     * Uses points from the player for skill upgrades.
     *
     * @param amount amount of points to use
     */
    public void usePoints(int amount) {
        // Cannot use too few points
        if (amount < 0) {
            throw new IllegalArgumentException("Invalid points amount - cannot be less than 1");
        }

        // Cannot use more points than obtained
        if (amount > this.getPoints()) {
            throw new IllegalArgumentException("Invalid points amount - more than current total");
        }

        // Use the points
        if (Fabled.getSettings().isSharedSkillPoints()) {
            this.getPlayerData().setPoints(this.getPlayerData().getPoints() - amount);
        } else {
            this.points -= amount;
        }
    }

    /**
     * <p>Gives experience to the class under the context of the experience source.</p>
     * <p>This will also check for leveling up after the experience is added.</p>
     * <p>If the class does not normally receive experience from the source,
     * it will still launch an experience event, just it will start off as
     * cancelled in case it should still be given in select circumstances.</p>
     *
     * @param amount amount of experience to give
     * @param source type of the source of the experience
     */
    public void giveExp(double amount, ExpSource source) {
        giveExp(amount, source, true);
    }

    /**
     * <p>Gives experience to the class under the context of the experience source.</p>
     * <p>This will also check for leveling up after the experience is added.</p>
     * <p>If the class does not normally receive experience from the source,
     * or the player is already max level, it will still launch an experience event,
     * just it will start off as cancelled in case it should still be given in select circumstances.</p>
     *
     * @param amount      amount of experience to give
     * @param source      type of the source of the experience
     * @param showMessage whether to show the configured message if enabled
     */
    public void giveExp(double amount, ExpSource source, boolean showMessage) {
        // Cannot give a non-positive amount of exp
        if (amount <= 0) {
            return;
        }

        // Call an event for the experience gained
        PlayerExperienceGainEvent event = new PlayerExperienceGainEvent(this, amount, source);
        event.setCancelled(!classData.receivesExp(source) || level >= classData.getMaxLevel());
        Bukkit.getPluginManager().callEvent(event);
        Bukkit.getPluginManager()
                .callEvent(new com.sucy.skill.api.event.PlayerExperienceGainEvent(new com.sucy.skill.api.player.PlayerClass(
                        this), amount, source));

        int rounded = (int) Math.ceil(event.getExp());

        // Add experience if not cancelled
        if (!event.isCancelled() && rounded > 0) {
            if (showMessage && Fabled.getSettings().isShowExpMessages() && player.getPlayer() != null) {
                TitleManager.show(
                        player.getPlayer(),
                        TitleType.EXP_GAINED,
                        NotificationNodes.EXP,
                        RPGFilter.EXP.setReplacement(rounded + ""),
                        RPGFilter.CLASS.setReplacement(classData.getName()),
                        Filter.AMOUNT.setReplacement(rounded + "")
                );
            }

            exp += rounded;
            checkLevelUp();
        }
    }

    /**
     * Causes the player to lose experience
     * This will launch a {@link PlayerExperienceLostEvent} event before taking the experience.
     *
     * @param amount      percent of experience to lose
     * @param percent     whether to take the amount as a percentage
     * @param changeLevel whether to lower the level if the exp lost exceeds the current exp,
     *                    or to cap at 0 exp and keep the current level
     */
    public void loseExp(double amount, boolean percent, boolean changeLevel) {
        Preconditions.checkArgument(amount > 0, "Amount must be positive");
        if (percent) {
            amount *= getRequiredExp();
        }

        // Launch the event
        PlayerExperienceLostEvent event = new PlayerExperienceLostEvent(this, amount, changeLevel);
        Bukkit.getPluginManager().callEvent(event);

        // Subtract the experience
        if (!event.isCancelled()) {
            changeLevel = event.isLevelChangeAllowed();
            if (!changeLevel) {
                amount = Math.min(event.getExp(), exp);
            }
            exp = exp - amount;


            // Exp loss message
            if (Fabled.getSettings().isShowLossExpMessages() && (int) amount > 0) {
                TitleManager.show(
                        player.getPlayer(),
                        TitleType.EXP_LOST,
                        NotificationNodes.EXP_LOSE,
                        RPGFilter.EXP.setReplacement((int) amount + ""),
                        RPGFilter.CLASS.setReplacement(classData.getName()),
                        Filter.AMOUNT.setReplacement((int) amount + ""));
            }
            checkLevelDown();
        }
    }

    /**
     * Causes the player to lose experience
     * This will launch a {@link PlayerExperienceLostEvent} event before taking the experience.
     *
     * @param percent percent of experience to lose
     */
    public void loseExp(double percent) {loseExp(percent, true, false);}

    /**
     * <p>Checks whether the player has leveled up based on
     * their current experience.</p>
     */
    private void checkLevelUp() {
        // Count the number of levels gained, if any
        int levels = 0;
        int required;
        while (exp >= (required = classData.getRequiredExp(level + levels))
                && level + levels < classData.getMaxLevel()) {
            exp -= required;
            levels++;
        }

        // Give the levels if applicable
        if (levels > 0) {
            giveLevels(levels);

            // Level up message
            if (Fabled.getSettings().isShowLevelMessages()) {
                TitleManager.show(
                        player.getPlayer(),
                        TitleType.LEVEL_UP,
                        NotificationNodes.LVL,
                        RPGFilter.LEVEL.setReplacement(level + ""),
                        RPGFilter.CLASS.setReplacement(classData.getName()),
                        RPGFilter.POINTS.setReplacement(getPoints() + ""),
                        Filter.AMOUNT.setReplacement(levels + "")
                );
            }
        }
    }

    private void checkLevelDown() {
        int levels = 0;
        while (exp < 0) {
            if (level - levels == 1) {
                exp = 0;
                break;
            }
            levels++;
            exp += classData.getRequiredExp(level - levels);
        }

        if (levels == 0) {
            return;
        }
        loseLevels(levels);

        // Level down message
        if (Fabled.getSettings().isShowLossLevelMessages()) {
            TitleManager.show(
                    player.getPlayer(),
                    TitleType.LEVEL_DOWN,
                    NotificationNodes.LVL_LOSE,
                    RPGFilter.LEVEL.setReplacement(level + ""),
                    RPGFilter.CLASS.setReplacement(classData.getName()),
                    RPGFilter.POINTS.setReplacement(getPoints() + ""),
                    Filter.AMOUNT.setReplacement(levels + ""));
        }
    }

    /**
     * <p>Gives levels to the player's class, leveling it up.</p>
     * <p>The amount of levels must be a positive number.</p>
     * <p>This will launch a level event for the gained levels.</p>
     *
     * @param amount amount of levels to give
     * @throws java.lang.IllegalArgumentException when the level amount is less than 1
     */
    public void giveLevels(int amount) {
        // Cannot give non-positive amount of levels
        if (amount < 1) {
            throw new IllegalArgumentException("Invalid level amount - cannot be less than 1");
        }

        // Level up
        amount = Math.min(amount, classData.getMaxLevel() - level);
        if (amount <= 0) return;
        level += amount;
        this.givePoints(classData.getGroupSettings().getPointsForLevels(level, level - amount), PointSource.LEVEL);
        getPlayerData().giveAttribPoints(classData.getGroupSettings().getAttribsForLevels(level, level - amount));

        // Update health/mana
        final Player player = getPlayerData().getPlayer();
        if (player != null) {
            getPlayerData().updatePlayerStat(getPlayerData().getPlayer());
            getPlayerData().getEquips().update(getPlayerData().getPlayer());
        }
        getPlayerData().autoLevel();

        // Call the event
        PlayerLevelUpEvent event = new PlayerLevelUpEvent(this, amount);
        Bukkit.getPluginManager().callEvent(event);
        Bukkit.getPluginManager()
                .callEvent(new com.sucy.skill.api.event.PlayerLevelUpEvent(this, amount));

        // Apply the effect
        if (Fabled.getSettings().hasLevelUpEffect()) {
            DynamicSkill skill = Fabled.getSettings().getLevelUpSkill();
            skill.cast(player, level);
        }
    }

    public void loseLevels(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Invalid level amount - cannot be less than 1");
        }

        // Level up
        amount = Math.min(amount, level - 1);
        if (amount <= 0) {
            return;
        }
        level -= amount;
        this.givePoints(classData.getGroupSettings().getPointsForLevels(level, level + amount), PointSource.LEVEL);
        getPlayerData().giveAttribPoints(classData.getGroupSettings().getAttribsForLevels(level, level + amount));

        // Update health/mana
        final Player player = getPlayerData().getPlayer();
        if (player != null) {
            getPlayerData().updatePlayerStat(getPlayerData().getPlayer());
            getPlayerData().getEquips().update(getPlayerData().getPlayer());
        }
        getPlayerData().autoLevel();

        // Call the event
        PlayerLevelDownEvent event = new PlayerLevelDownEvent(this, amount);
        Bukkit.getPluginManager().callEvent(event);

        // Apply the effect
        if (Fabled.getSettings().hasLevelUpEffect()) {
            DynamicSkill skill = Fabled.getSettings().getLevelUpSkill();
            skill.cast(player, level);
        }
    }

    /**
     * Sets the class data this player class is based off of, optionally
     * resetting the class progress.
     *
     * @param classData class data to switch to
     */
    public void setClassData(FabledClass classData) {
        FabledClass previous = this.classData;
        this.classData = classData;
        getPlayerData().setClass(previous, classData, false);
    }
}
