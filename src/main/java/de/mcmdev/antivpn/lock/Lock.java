package de.mcmdev.antivpn.lock;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.OptBoolean;
import java.net.InetAddress;
import java.util.UUID;

@JsonTypeInfo(use = Id.CLASS, requireTypeIdForSubtypes = OptBoolean.FALSE)
public interface Lock {

  LockResult check(UUID uuid, InetAddress address);

  void addLock(UUID uuid, InetAddress address);

}
