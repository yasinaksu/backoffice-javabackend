package com.omniteam.backofisbackend.config;

import com.github.javafaker.Educator;
import com.github.javafaker.Faker;
import com.github.javafaker.LordOfTheRings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Eğlenceli değerler üretmesi için Faker Bean tanımlamaları
 */
@Configuration
public class FakerBeanConfig {

    @Bean
    public LordOfTheRings lordOfTheRings() {
        return new Faker().lordOfTheRings();
    }

    @Bean
    public Educator educator() {return new Faker().educator();}

}
