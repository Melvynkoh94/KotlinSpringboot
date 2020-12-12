package com.assessment.kotlinspringboot

import com.assessment.kotlinspringboot.service.FileStorage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class KotlinSpringBootApplication{
	@Autowired
	lateinit var fileStorage: FileStorage;

	@Bean
	fun run() = CommandLineRunner {
		fileStorage.deleteAll()
		fileStorage.init()
	}
}

fun main(args: Array<String>) {
	runApplication<KotlinSpringBootApplication>(*args)
	println("Kotlin Spring Boot started...")
}
