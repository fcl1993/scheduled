package com.dafy.scheduled.web.config;

import com.fuchenglei.spring.ContainerCoupling;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScheduledConfig
{

    @Bean
    public ContainerCoupling containerCoupling()
    {
        return new ContainerCoupling();
    }

}
