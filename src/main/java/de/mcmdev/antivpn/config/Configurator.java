package de.mcmdev.antivpn.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.type.TypeFactory;
import tools.jackson.dataformat.yaml.YAMLFactory;
import tools.jackson.dataformat.yaml.YAMLMapper;
import tools.jackson.dataformat.yaml.YAMLMapper.Builder;
import java.nio.file.Path;

public final class Configurator {

  private static final Logger log = LoggerFactory.getLogger(Configurator.class);

  public RootConfig load(final Path path) {
    final ObjectMapper objectMapper = buildObjectMapper();
    return objectMapper.readValue(path, RootConfig.class);
  }

  private ObjectMapper buildObjectMapper() {
    return new YAMLMapper(
        new Builder(new YAMLFactory())
            .typeFactory(new TypeFactory()
                .withClassLoader(getClass().getClassLoader())
            )
    );
  }

}
