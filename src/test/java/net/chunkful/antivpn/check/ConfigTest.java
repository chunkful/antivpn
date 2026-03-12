package net.chunkful.antivpn.check;

import net.chunkful.antivpn.config.Configurator;
import net.chunkful.antivpn.config.RootConfig;
import java.nio.file.Path;

public class ConfigTest {

  public static void main(final String[] args) {
    final RootConfig rootConfig = new Configurator().load(Path.of("test", "config.yml"));
    System.out.println(rootConfig);
  }


}
