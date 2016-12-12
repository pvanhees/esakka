package io.esakka.configuration;

import akka.actor.ActorSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by pieter on 12/8/16.
 */
@Configuration
public class AkkaConfig {

    @Bean
    ActorSystem actorSystem() {
        return ActorSystem.create("CustomerAggregateSystem");
    }
}
