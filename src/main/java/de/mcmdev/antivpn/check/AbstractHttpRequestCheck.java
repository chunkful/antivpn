package de.mcmdev.antivpn.check;

import com.google.gson.JsonElement;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.StatusLine.StatusClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.util.Map;

public abstract class AbstractHttpRequestCheck implements Check {

  private static final Logger log = LoggerFactory.getLogger(AbstractHttpRequestCheck.class);
  protected final HttpClient httpClient;
  protected final ObjectMapper objectMapper;
  protected final JexlEngine jexlEngine;
  protected final JexlExpression jexlExpression;

  protected AbstractHttpRequestCheck(final String expression) {
    this.httpClient = HttpClients.createDefault();
    this.objectMapper = new JsonMapper();
    this.jexlEngine = new JexlBuilder().create();
    this.jexlExpression = compileExpression(expression);
  }

  @Override
  public CheckResult check(final InetAddress address) {
    try {
      return httpClient.execute(buildRequest(address), response -> {
        if(StatusClass.from(response.getCode()) != StatusClass.SUCCESSFUL) {
          log.warn("Received status code {} from check service", response.getCode());
          return CheckResult.ERRORED;
        }
        final String string = EntityUtils.toString(response.getEntity());
        final Map<String, Object> map = objectMapper.readValue(string, new TypeReference<>(){});
        return parseResponse(address, map);
      });
    } catch (final URISyntaxException | IOException e) {
      log.error("Error while checking IP", e);
      return CheckResult.ERRORED;
    }
  }

  protected abstract ClassicHttpRequest buildRequest(InetAddress address) throws URISyntaxException;

  protected CheckResult parseResponse(final InetAddress address, final Map<String, Object> map) {
    final MapContext context = new MapContext(map);
    return (CheckResult) jexlExpression.evaluate(context);
  }

  protected JexlExpression compileExpression(final String expression) {
    return jexlEngine.createExpression(expression);
  }

}
