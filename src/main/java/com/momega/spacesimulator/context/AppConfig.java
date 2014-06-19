package com.momega.spacesimulator.context;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by martin on 6/18/14.
 */
@Configuration
@ComponentScan(basePackages = "com.momega.spacesimulator.service")
public class AppConfig {
}
