package it.multicoredev.spigot;

import it.multicoredev.mclib.yaml.ConfigManager;
import it.multicoredev.mclib.yaml.Configuration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

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
public class Ding extends JavaPlugin {
    private final String CONFIG_NAME = "config.yml";
    private final File CONFIG_FILE = new File(getDataFolder(), CONFIG_NAME);
    private Configuration config;

    public void onEnable() {
        try {
            ConfigManager cm = new ConfigManager();
            if (!getDataFolder().exists() || !getDataFolder().isDirectory()) getDataFolder().mkdir();
            if (!CONFIG_FILE.exists() || !CONFIG_FILE.isFile()) cm.createConfig(CONFIG_FILE, getResource(CONFIG_NAME));
            config = cm.loadConfig(CONFIG_FILE);
            if (config == null) throw new IOException("Cannot load config file.");
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            onDisable();
            return;
        }

        getCommand("ding").setExecutor((sender, command, label, args) -> {
            if (!sender.hasPermission("ding.admin")) {
                Chat.send("&4You don't have the permissions to do this!", sender, true);
                return true;
            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    try {
                        ConfigManager cm = new ConfigManager();
                        if (!getDataFolder().exists() || !getDataFolder().isDirectory()) getDataFolder().mkdir();
                        if (!CONFIG_FILE.exists() || !CONFIG_FILE.isFile()) cm.createConfig(CONFIG_FILE, getResource(CONFIG_NAME));
                        config = cm.loadConfig(CONFIG_FILE);
                        if (config == null) throw new IOException("Cannot load config file.");

                        Chat.send("&6" + this.getName() + " reloaded!", sender, true);
                    } catch (IOException | NullPointerException e) {
                        e.printStackTrace();
                        onDisable();
                        Chat.send("&4" + this.getName() + " disabled due to errors!", sender, true);
                    }
                } else {
                    Chat.send("&2" + this.getName() + " v. " + this.getDescription().getVersion() + " by " + this.getDescription().getAuthors().get(0), sender, true);
                }
            } else {
                Chat.send("&2" + this.getName() + " v. " + this.getDescription().getVersion() + " by " + this.getDescription().getAuthors().get(0), sender, true);
            }
            return true;
        });

        getServer().getPluginManager().registerEvents(new OnChat(this, config), this);
    }
}
