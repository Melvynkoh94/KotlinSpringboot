package com.assessment.kotlinspringboot

import com.assessment.kotlinspringboot.controller.RetailerIndexController
import com.assessment.kotlinspringboot.model.Transaction
import com.assessment.kotlinspringboot.service.RetailerService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus

//@AutoConfigureMockMvc
//@WebMvcTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KotlinSpringBootApplicationTests(@Autowired val restTemplate: TestRestTemplate) {

	@Autowired
	lateinit var controller: RetailerIndexController

	@Test
	fun `UploadPage`() {
		val entity = restTemplate.getForEntity<String>("/upload")
		assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
	}

//	@Test
//	fun `IndexPage`() {
//		val entity = restTemplate.getForEntity<String>("/index")
//		assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
//	}

	@Test
	fun contextLoads() {
		assertThat(controller).isNotNull
	}

}
