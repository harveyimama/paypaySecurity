package com.techland.paypay.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.techland.paypay")
public class PayPaySecurityApplication {
	public static void main(String[] args) {
		SpringApplication.run(PayPaySecurityApplication.class, args);
	}

}
