package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan({"controllers", "services"})
@ImportResource("classpath:spring/common.xml")
public class AppConfig {

}