package de.mcmdev.antivpn.check;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.jexl3.MapContext;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.net.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public final class ProxycheckCheck extends AbstractHttpRequestCheck {

  private static final Logger log = LoggerFactory.getLogger(ProxycheckCheck.class);
  private final String apiKey;

  @JsonCreator
  public ProxycheckCheck(
      @JsonProperty("apiKey") final String apiKey,
      @JsonProperty("expression") final String expression
  ) {
    super(expression);
    this.apiKey = apiKey;
  }

  @Override
  protected ClassicHttpRequest buildRequest(final InetAddress address) throws URISyntaxException {
    final URI build = new URIBuilder()
        .setScheme("https")
        .setHost("proxycheck.io")
        .appendPathSegments("v2", address.getHostAddress())
        .setParameter("vpn", "1")
        .setParameter("key", apiKey)
        .build();
    return ClassicRequestBuilder.get(build).build();
  }

  @Override
  protected CheckResult parseResponse(final InetAddress address, final Map<String, Object> map) {
    final String addressString = address.getHostAddress();
    if (map.containsKey(addressString)) {
      final Object result = map.get(addressString);
      map.remove(addressString);
      map.put("result", result);
      final MapContext context = new MapContext(map);
      return (boolean) jexlExpression.evaluate(context) ? CheckResult.FAILED : CheckResult.PASSED;
    } else {
      return CheckResult.ERRORED;
    }
  }
}
