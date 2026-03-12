package net.chunkful.antivpn.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.chunkful.antivpn.check.Check;
import net.chunkful.antivpn.exemption.Exemption;
import net.chunkful.antivpn.lock.Lock;

public final class RootConfig {

  private final Messages messages;
  private final Check check;
  private final Exemption exemption;
  private final Lock lock;

  @JsonCreator
  public RootConfig(
      @JsonProperty("messages") final Messages messages,
      @JsonProperty("check") final Check check,
      @JsonProperty("exemption") final Exemption exemption,
      @JsonProperty("lock") final Lock lock
  ) {
    this.messages = messages;
    this.check = check;
    this.exemption = exemption;
    this.lock = lock;
  }

  public Messages getMessages() {
    return messages;
  }

  public Check getCheck() {
    return check;
  }

  public Exemption getExemption() {
    return exemption;
  }

  public Lock getLock() {
    return lock;
  }

  @Override
  public String toString() {
    return "RootConfig{" +
        "check=" + check +
        ", exemption=" + exemption +
        ", lock=" + lock +
        '}';
  }
}
