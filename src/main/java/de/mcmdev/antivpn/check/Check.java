package de.mcmdev.antivpn.check;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.OptBoolean;
import java.net.InetAddress;

@JsonTypeInfo(use = Id.CLASS, requireTypeIdForSubtypes = OptBoolean.FALSE)
public interface Check {

  CheckResult check(InetAddress address);

}
