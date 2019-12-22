package it.multicoredev.spigot;

import it.multicoredev.mclib.yaml.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;

/**
 * Copyright © 2019 by Lorenzo Magni
 * This file is part of Ping.
 * Ping is under "The 3-Clause BSD License", you can find a copy <a href="https://opensource.org/licenses/BSD-3-Clause">here</a>.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
public class OnChat implements Listener {
    private final Plugin plugin;
    private final Configuration config;

    OnChat(Plugin plugin, Configuration config) {
        this.plugin = plugin;
        this.config = config;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String[] msg = event.getMessage().split(" ");
            HashMap<String, Player> players = new HashMap<>();

            for (Player player : Bukkit.getOnlinePlayers()) {
                players.put(player.getName().toLowerCase(), player);
                if (!player.getDisplayName().equalsIgnoreCase(player.getName())) {
                    players.put(player.getDisplayName().toLowerCase(), player);
                }
            }

            for (String s : msg) {
                if (players.containsKey(s.toLowerCase())) {
                    Player target = players.get(s.toLowerCase());
                    if (target.isOnline()) {
                        target.playSound(target.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    }
                }
            }

            for (String s : msg) {
                for (String p : players.keySet()) {
                    if (p.contains(s.toLowerCase())) {
                        if (s.equalsIgnoreCase(p)) continue;
                        if (((float) p.length() / (float) s.length()) >= config.getFloat("name-percentage-match")) {
                            Player target = players.get(p);
                            if (target.isOnline()) {
                                target.playSound(target.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                            }
                        }
                    }
                }
            }

            List<String> staffWords = config.getStringList("staff-words");
            for (String s : msg) {
                if (staffWords.contains(s)) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.hasPermission("ding.staff")) {
                            if (player.isOnline()) {
                                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException ignored) {
                                }
                                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException ignored) {
                                }
                                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                            }
                        }
                    }
                }
            }
        });
    }
}
