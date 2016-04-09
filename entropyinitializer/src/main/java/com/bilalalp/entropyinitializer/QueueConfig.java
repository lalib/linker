package com.bilalalp.entropyinitializer;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan(value = {"com.bilalalp.entropyinitializer"})
@EnableTransactionManagement(proxyTargetClass = true)
@Configuration
public class QueueConfig {


}