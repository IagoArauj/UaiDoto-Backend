package br.edu.ufsj.tp.Helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

// @Configuration
@Slf4j
public class EnvPropertiesHelper implements EnvironmentAware {
    private Environment env;

    @Override
    public void setEnvironment(final Environment environment) {
        this.env = environment;
    }

    @PostConstruct
    public String getSecret() {
        log.info(env.toString());
        return env.getProperty("custom.secret");
    }
}
