package com.assessment.kotlinspringboot

import com.assessment.kotlinspringboot.controller.RetailerIndexController
import com.assessment.kotlinspringboot.model.Transaction
import com.assessment.kotlinspringboot.service.RetailerService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
//@SpringBootTest
@ExtendWith(SpringExtension::class)
@WebMvcTest(RetailerIndexController::class)
class ControllerTest{

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var service: RetailerService

    @Test
    fun `List items`() {
        val item1 = Transaction("536373", "82482", "WOODEN PICTURE FRAME WHITE FINISH", 6, "12/1/2010 9:02", 2.1, 17850, "United Kingdom")
        val item2 = Transaction("536375", "85123A", "WHITE HANGING HEART T-LIGHT HOLDER", 6, "12/1/2010 9:32", 2.55, 17850, "United Kingdom")
        val transactionList = listOf(item1, item2)
        every { service.csvObjectMapper() } returns transactionList
        mockMvc.perform(get("/index"))
                .andExpect(status().isOk)
                .andExpect(model().attribute("files", transactionList))
    }

    @Test
    fun `Search items`() {
        val item1 = Transaction("536373", "82482", "WOODEN PICTURE FRAME WHITE FINISH", 6, "12/1/2010 9:02", 2.1, 17850, "United Kingdom")
        val item2 = Transaction("536375", "85123A", "WHITE HANGING HEART T-LIGHT HOLDER", 6, "12/1/2010 9:32", 2.55, 17850, "Singapore")
        val transactionList = listOf(item1, item2)
        val searchResult = listOf(item2)
        val column = "Country"
        val search = "Singapore"
        every { service.csvObjectMapper() } returns transactionList
        mockMvc.perform(get("/index/search/$column/$search"))
                .andExpect(status().isOk)
                .andExpect(model().attribute("files", searchResult))
    }
}

