/**
 * Fabled
 * studio.magemonkey.fabled.data.io.ConfigIO
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
package studio.magemonkey.fabled.data.io;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import studio.magemonkey.codex.mccore.config.CommentedConfig;
import studio.magemonkey.codex.mccore.config.parse.DataSection;
import studio.magemonkey.fabled.Fabled;
import studio.magemonkey.fabled.api.player.PlayerAccounts;
import studio.magemonkey.fabled.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * IO manager that saves/loads to a .yml configuration file
 */
public class ConfigIO extends IOManager {
    /**
     * Initializes a new .yml config manager
     *
     * @param plugin Fabled reference
     */
    public ConfigIO(Fabled plugin) {
        super(plugin);
    }

    public Map<String, PlayerAccounts> loadAll() {
        Map<String, PlayerAccounts> result = new HashMap<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            result.put(player.getUniqueId().toString().toLowerCase(), loadData(player));
        }
        return result;
    }

    /**
     * Loads data for the given player
     *
     * @param player player to load data for
     * @return loaded player data
     */
    @Override
    public PlayerAccounts loadData(OfflinePlayer player) {
        String          playerKey  = player.getUniqueId().toString().toLowerCase();
        CommentedConfig config     = new CommentedConfig(api, "players/" + playerKey);
        CommentedConfig nameConfig = new CommentedConfig(api, "players/" + player.getName());
        if (!playerKey.equals(player.getName()) && nameConfig.getConfigFile().exists()) {
            DataSection old = nameConfig.getConfig();
            for (String key : old.keys())
                config.getConfig().set(key, old.get(key));
            nameConfig.getConfigFile().delete();
        }
        DataSection file = config.getConfig();

        return load(player, file);
    }

    /**
     * Saves player data to the config
     *
     * @param data data to save to the config
     */
    @Override
    public void saveData(PlayerAccounts data) {
        if (!data.isLoaded()) return;

        try {
            CommentedConfig config = new CommentedConfig(api,
                    "players/" + data.getOfflinePlayer().getUniqueId().toString().toLowerCase());
            config.clear();

            DataSection file = save(data);
            config.getConfig().applyDefaults(file);

            config.save();
        } catch (Exception ex) {
            Logger.bug("Failed to save data for invalid player");
        }
    }

    /**
     * Saves all player data to the config
     */
    @Override
    public void saveAll() {
        Map<String, PlayerAccounts> data = Fabled.getPlayerAccounts();
        ArrayList<String>           keys = new ArrayList<String>(data.keySet());
        for (String key : keys)
            saveData(data.get(key));
    }
}