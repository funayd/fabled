/**
 * Fabled
 * studio.magemonkey.fabled.data.PlayerStats
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
package studio.magemonkey.fabled.data;

import studio.magemonkey.codex.mccore.config.FilterType;
import studio.magemonkey.codex.mccore.scoreboard.StatHolder;
import studio.magemonkey.fabled.Fabled;
import studio.magemonkey.fabled.api.player.PlayerClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Stat provider for the MCCore stat scoreboard
 */
public class PlayerStats implements StatHolder {
    private static final String                  BASE    = "Stats.";
    private static final String                  EXP     = "exp";
    private static final String                  HEALTH  = "health";
    private static final String                  LEVEL   = "level";
    private static final String                  POINTS  = "points";
    private static final String                  ATTRIB  = "attrib";
    private static final HashMap<String, String> statMap = new HashMap<>();
    private final        PlayerClass             player;

    /**
     * Constructor
     *
     * @param player player to show stats for
     */
    public PlayerStats(PlayerClass player) {
        this.player = player;
    }

    /**
     * Initializes the offline players used by the scoreboard. This is done
     * by the API on startup so do not call this method.
     */
    public static void init() {
        if (statMap.size() == 0) {
            load(EXP, BASE + EXP);
            load(HEALTH, BASE + HEALTH);
            load(LEVEL, BASE + LEVEL);
            load(POINTS, BASE + POINTS);
            load(ATTRIB, BASE + ATTRIB);
        }
    }

    /**
     * Clears the initialized offline players. This is done by the API
     * upon disable so do not call this method.
     */
    public static void clear() {
        statMap.clear();
    }

    private static void load(String key, String node) {
        statMap.put(key, Fabled.getLanguage().getMessage(node, true, FilterType.COLOR).get(0));
    }

    /**
     * @return map of stats for the scoreboard
     */
    @Override
    public List<String> getNames() {
        List<String> stats = new ArrayList<>();
        stats.add(statMap.get(HEALTH));
        if (Fabled.getSettings().isManaEnabled())
            stats.add(player.getData().getManaName());
        stats.add(statMap.get(POINTS));
        stats.add(statMap.get(LEVEL));
        stats.add(statMap.get(EXP));
        if (Fabled.getSettings().isAttributesEnabled()) {
            stats.add(statMap.get(ATTRIB));
        }

        return stats;
    }

    /**
     * @return populated list of values
     */
    @Override
    public ArrayList<Integer> getValues() {
        ArrayList<Integer> values = new ArrayList<>();
        values.add((int) Math.ceil(player.getPlayerData().getPlayer().getHealth()));
        if (Fabled.getSettings().isManaEnabled())
            values.add((int) player.getPlayerData().getMana());
        values.add(player.getPoints());
        values.add(player.getLevel());
        values.add((int) player.getExp());
        if (Fabled.getSettings().isAttributesEnabled())
            values.add(player.getPlayerData().getAttributePoints());

        return values;
    }
}
