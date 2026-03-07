package de.mcmdev.antivpn.check.cache;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.cache.CacheBuilder;
import de.mcmdev.antivpn.check.CheckResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.net.InetAddress;

public final class LocalCache implements Cache {

  private final com.google.common.cache.Cache<InetAddress, CheckResult> cache;

  @JsonCreator
  public LocalCache(@JsonProperty("spec") final String cacheSpec) {
    this.cache = CacheBuilder.from(cacheSpec).build();
  }

  @Override
  public void put(@NotNull final InetAddress address, @NotNull final CheckResult result) {
    cache.put(address, result);
  }

  @Override
  public @Nullable CheckResult get(@NotNull final InetAddress address) {
    return cache.getIfPresent(address);
  }
}
