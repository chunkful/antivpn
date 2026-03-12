package net.chunkful.antivpn.check;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.InetAddress;
import net.chunkful.antivpn.check.cache.Cache;

public final class CacheAdapter implements Check {

  private final Cache cache;
  private final Check delegate;

  @JsonCreator
  public CacheAdapter(
      @JsonProperty("cache") final Cache cache,
      @JsonProperty("check") final Check delegate
  ) {
    this.cache = cache;
    this.delegate = delegate;
  }

  @Override
  public CheckResult check(final InetAddress address) {
    final CheckResult cachedResult = cache.get(address);
    if (cachedResult != null) {
      return cachedResult;
    }
    final CheckResult freshResult = delegate.check(address);
    if (freshResult != CheckResult.ERRORED) {
      cache.put(address, freshResult);
    }
    return freshResult;
  }
}
