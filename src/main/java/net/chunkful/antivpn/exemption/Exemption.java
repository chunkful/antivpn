package net.chunkful.antivpn.exemption;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.OptBoolean;
import java.net.InetAddress;
import java.util.UUID;

@JsonTypeInfo(use = Id.CLASS, requireTypeIdForSubtypes = OptBoolean.FALSE)
public interface Exemption {

  boolean isExempt(UUID uuid, InetAddress address);

}
