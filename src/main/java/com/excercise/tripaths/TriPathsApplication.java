package com.excercise.tripaths;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TriPathsApplication implements CommandLineRunner {

    public static void main(final String[] args) {
        SpringApplication.run(TriPathsApplication.class, args);
    }

    @Override
    public void run(final String... args) {
        // TODO: read text format triangle from standard input
    }

}
