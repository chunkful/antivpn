package net.chunkful.antivpn;

import net.chunkful.antivpn.config.Configurator;
import net.chunkful.antivpn.config.RootConfig;
import net.chunkful.antivpn.listener.ConnectionListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AntiVpnPlugin extends JavaPlugin {

  private static final Logger log = LoggerFactory.getLogger(AntiVpnPlugin.class);

  @Override
  public void onEnable() {
    saveDefaultConfig();
    try {
      final RootConfig rootConfig = new Configurator().load(getDataPath().resolve("config.yml"));
      Bukkit.getPluginManager().registerEvents(new ConnectionListener(
          rootConfig
      ), this);
    } catch (final Exception exception) {
      log.error("Error while enabling plugin", exception);
    }
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
