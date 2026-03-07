package de.mcmdev.antivpn.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class Messages {

  private final Component lockFailMessage;
  private final Component checkFailMessage;

  @JsonCreator
  public Messages(
      @JsonProperty("lockFail") final String lockFailMessage,
      @JsonProperty("checkFail") final String checkFailMessage
  ) {
    this.lockFailMessage = MiniMessage.miniMessage().deserialize(lockFailMessage);
    this.checkFailMessage = MiniMessage.miniMessage().deserialize(checkFailMessage);
  }

  public Component getLockFailMessage() {
    return lockFailMessage;
  }

  public Component getCheckFailMessage() {
    return checkFailMessage;
  }
}