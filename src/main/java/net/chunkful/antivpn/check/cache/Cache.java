package net.chunkful.antivpn.check.cache;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.OptBoolean;
import java.net.InetAddress;
import net.chunkful.antivpn.check.CheckResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonTypeInfo(use = Id.CLASS, requireTypeIdForSubtypes = OptBoolean.FALSE)
public interface Cache {

  void put(@NotNull InetAddress address, @NotNull CheckResult result);

  @Nullable
  CheckResult get(@NotNull InetAddress address);

}
