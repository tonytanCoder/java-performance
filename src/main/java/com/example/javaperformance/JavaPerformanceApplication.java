package com.example.javaperformance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaPerformanceApplication {

	public static void main(String[] args) {

		CompileDemo compileDemo=new CompileDemo();
		Thread thread1=new Thread(compileDemo);
		thread1.start();
		SpringApplication.run(JavaPerformanceApplication.class, args);
	}

}
