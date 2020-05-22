package hu.illesjosh.bulbwatch;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.util.Properties;

import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class BasicModule extends AbstractModule {

    @Override
    protected void configure() {
        try {
            Properties properties = new Properties();
            var propsRes = Resources.getResource(App.PROPERTIES_RESOURCE);
            var propsStream = Resources.asCharSource(propsRes, UTF_8).openBufferedStream();
            properties.load(propsStream);
            Names.bindProperties(binder(), properties);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
