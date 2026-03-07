package de.mcmdev.antivpn.check;

import de.mcmdev.antivpn.config.Configurator;
import de.mcmdev.antivpn.config.RootConfig;
import java.nio.file.Path;

public class ConfigTest {

  public static void main(final String[] args) {
    final RootConfig rootConfig = new Configurator().load(Path.of("test", "config.yml"));
    System.out.println(rootConfig);
  }


}
