package de.mcmdev.antivpn.check;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

public class AddressUtil {

  public static InetAddress getOwnAddress() {
    try {
      final String urlString = "http://checkip.amazonaws.com/";
      final URL url = new URL(urlString);
      try (final BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
        return InetAddress.getByName(br.readLine());
      }
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

}
