package net.chunkful.antivpn.exemption;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.net.InetAddress;
import java.util.UUID;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

public final class PermissionExemption implements Exemption {

  private LuckPerms luckPerms;

  @JsonCreator
  public PermissionExemption() {
  }

  @Override
  public boolean isExempt(final UUID uuid, final InetAddress address) {
    if (luckPerms == null) {
      luckPerms = LuckPermsProvider.get();
    }
    final User user = luckPerms.getUserManager().loadUser(uuid).join();
    return user.getCachedData().getPermissionData().checkPermission("antivpn.exempt").asBoolean();
  }
}
