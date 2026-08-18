package com.complaintservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableFeignClients
@EnableScheduling
public class ComplaintServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComplaintServiceApplication.class, args);
        System.out.println("RUN_SUCCESSFULLY____");
	}

}
