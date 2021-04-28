package com.flower.demo;

import com.flower.demo.controller.UpdateController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest
//@WebMvcTest
@AutoConfigureMockMvc
class DemoApplicationTests {

    MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    RestTemplate rt;

    @BeforeEach
    public void before() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void test01() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/test"))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
    }


//    @Autowired
//    TestRestTemplate restTemplate;
//    @Test
//     void test(){
//
//        String res = this.restTemplate.getForObject("http://127.0.0.1:8080/test",String.class);
//        System.out.println(res);
//
//    }
//



}
