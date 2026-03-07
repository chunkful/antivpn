package de.mcmdev.antivpn.listener;

import de.mcmdev.antivpn.check.Check;
import de.mcmdev.antivpn.check.CheckResult;
import de.mcmdev.antivpn.config.Messages;
import de.mcmdev.antivpn.config.RootConfig;
import de.mcmdev.antivpn.exemption.Exemption;
import de.mcmdev.antivpn.lock.Lock;
import de.mcmdev.antivpn.lock.LockResult;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import java.net.InetAddress;
import java.util.UUID;

public final class ConnectionListener implements Listener {

  private final Messages messages;
  private final Exemption exemption;
  private final Lock lock;
  private final Check check;

  public ConnectionListener(final RootConfig rootConfig) {
    this.messages = rootConfig.getMessages();
    this.exemption = rootConfig.getExemption();
    this.lock = rootConfig.getLock();
    this.check = rootConfig.getCheck();
  }

  @EventHandler
  private void onPreLogin(final AsyncPlayerPreLoginEvent event) {
    final InetAddress address = event.getAddress();
    final UUID uniqueId = event.getUniqueId();
    if (exemption.isExempt(uniqueId, address)) return;
    final LockResult lockResult = lock.check(uniqueId, address);
    if(LockResult.MATCH == lockResult) return;
    if(LockResult.NO_MATCH == lockResult) {
      event.disallow(Result.KICK_OTHER, messages.getLockFailMessage());
      return;
    }

    final CheckResult result = check.check(address);
    if(CheckResult.FAILED == result)
      event.disallow(Result.KICK_OTHER, messages.getCheckFailMessage());
  }

}
