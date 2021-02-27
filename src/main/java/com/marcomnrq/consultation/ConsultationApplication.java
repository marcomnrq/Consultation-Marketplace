package com.marcomnrq.consultation;

import org.modelmapper.ModelMapper;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Locale;

@EnableAsync
@EnableJpaAuditing
@SpringBootApplication
public class ConsultationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsultationApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PrettyTime prettyTime() {
        return new PrettyTime(new Locale("ES"));
    }

}
