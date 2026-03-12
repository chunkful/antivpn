package net.chunkful.antivpn.check;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.MapContext;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import java.util.Map;

public class JacksonMapTest {

  private static final String input = """
{
    "status": "ok",
    "45.83.220.210": {
        "network": {
            "asn": "AS39351",
            "range": "45.83.220.210/31",
            "hostname": null,
            "provider": "31173 Services AB",
            "organisation": "31173 Services AB",
            "type": "Hosting"
        },
        "location": {
            "continent_name": "Europe",
            "continent_code": "EU",
            "country_name": "Sweden",
            "country_code": "SE",
            "region_name": "Skåne",
            "region_code": "M",
            "city_name": "Malmo",
            "postal_code": "200 01",
            "latitude": 55.6058,
            "longitude": 12.9951,
            "timezone": "Europe/Stockholm",
            "currency": {
                "name": "Krona",
                "code": "SEK",
                "symbol": "kr"
            }
        },
        "device_estimate": {
            "address": 10,
            "subnet": 320
        },
        "detections": {
            "proxy": false,
            "vpn": true,
            "compromised": false,
            "scraper": false,
            "tor": false,
            "hosting": true,
            "anonymous": true,
            "risk": 50,
            "confidence": 100,
            "first_seen": "2025-11-18T04:58:24Z",
            "last_seen": "2026-01-02T16:36:09Z"
        },
        "operator": {
            "name": "Mullvad",
            "url": "https://mullvad.net/",
            "anonymity": "high",
            "popularity": "high",
            "protocols": [
                "WireGuard",
                "OpenVPN"
            ],
            "policies": {
                "ad_filtering": true,
                "free_access": false,
                "paid_access": true,
                "port_forwarding": false,
                "logging": false,
                "anonymous_payments": true,
                "crypto_payments": true,
                "traceable_ownership": true
            }
        },
        "last_updated": "2026-01-02T16:36:09Z"
    },
    "query_time": 5
}""";

  public static void main(final String[] args) {
    final ObjectMapper objectMapper = new JsonMapper();

    final Map<String, Object> stringObjectMap = objectMapper.readValue(input, new TypeReference<Map<String, Object>>() {});

    final JexlEngine engine = new JexlBuilder().create();
    final JexlContext jc = new MapContext(stringObjectMap);
    final Object evaluate = engine.createExpression("keySet()").evaluate(jc);
    System.out.println(evaluate);
  }

}
