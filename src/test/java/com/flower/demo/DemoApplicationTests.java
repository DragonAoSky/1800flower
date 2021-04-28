package com.flower.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flower.demo.controller.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

    MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Mock
    private RestTemplate rt;

    @InjectMocks
    private TallyController tally = new TallyController();

    @InjectMocks
    private UpdateController update;

    @BeforeEach
    public void before() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//        tally = new TallyController();
//        update = new UpdateController();
//        tally = mock(TallyController.class);
//        update = mock(UpdateController.class);

    }

    @Test// debug
    public void test01() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/test"))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Yes", content, "Error! Return value is not correct!");
    }

    @Test// test TallyController.GetPost()
    public void test02() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/tallies")).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("[{\"message\": \"There are 10 unique user Ids\"}]", content, "Error! Return value is not correct!");
    }


    @Test // test TallyController.TallyUniqID() with empty array
    public void test03(){
        Post[] posts = new Post[0];
        int result = tally.TallyUniqID(posts);
        Assertions.assertEquals(0, result, "Error! Return value is not correct!");
    }

    @Test // test TallyController.TallyUniqID() with same Ids
    public void test04(){
        Post p1 = new Post(1,1,"a","aa");
        Post p2 = new Post(1,2,"b","bb");
        Post p3 = new Post(1,3,"c","cc");
        Post[] posts = new Post[3];
        posts[0] = p1;
        posts[1] = p2;
        posts[2] = p3;

        int result = tally.TallyUniqID(posts);
        Assertions.assertEquals(1, result, "Error! Return value is not correct!");
    }

    @Test// test TallyController.GetPost() with empty mock json response
    public void test05()  {
        Post[] posts = new Post[0];
        Mockito.when(rt.getForObject(anyString(),any(Class.class))).thenReturn(posts);
        String result = tally.GetPost();
        Assertions.assertEquals("[{\"message\": \"There are 0 unique user Ids\"}]", result, "Error! Return value is not correct!");
    }

    @Test // test TallyController.GetPost() with mock json response which has same Ids
    public void test06()  {
        Post p1 = new Post(1,1,"a","aa");
        Post p2 = new Post(1,2,"b","bb");
        Post p3 = new Post(1,3,"c","cc");
        Post[] posts = new Post[3];
        posts[0] = p1;
        posts[1] = p2;
        posts[2] = p3;
        Mockito.when(rt.getForObject(anyString(),any(Class.class))).thenReturn(posts);
        String result = tally.GetPost();
        Assertions.assertEquals("[{\"message\": \"There are 1 unique user Ids\"}]", result, "Error! Return value is not correct!");
    }

    @Test// test UpdateController.GetPost()
    public void test07() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/updates")).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        JSONArray jsonArray = JSONArray.parseArray(content);
        JSONObject jsonObject = jsonArray.getJSONObject(3);
        //check the 4th json object
        Assertions.assertEquals("1800Flowers", jsonObject.get("title"), "Error! Return value is not correct!");
        Assertions.assertEquals("1800Flowers", jsonObject.get("body"), "Error! Return value is not correct!");
    }

    @Test// test UpdateController.UpdateId() with empty json array
    public void test08(){
        JSONArray jsonArray = new JSONArray();
        String result = update.UpdateId(jsonArray);
        Assertions.assertEquals("[]", result, "Error! Return value is not correct!");
    }

    @Test// test UpdateController.UpdateId() with json array which has less than 4 elements
    public void test09(){
        String test = "[\n" +
                "\t{\n" +
                "\t\t\"id\":1,\n" +
                "\t\t\"title\":\"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\",\n" +
                "\t\t\"body\":\"quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto\",\n" +
                "\t\t\"userId\":1\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\":2,\n" +
                "\t\t\"title\":\"qui est esse\",\n" +
                "\t\t\"body\":\"est rerum tempore vitae\\nsequi sint nihil reprehenderit dolor beatae ea dolores neque\\nfugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\\nqui aperiam non debitis possimus qui neque nisi nulla\",\n" +
                "\t\t\"userId\":1\n" +
                "\t}]";

        JSONArray jsonArray = JSONArray.parseArray(test);
        String result = update.UpdateId(jsonArray);
        Assertions.assertEquals("[{\"id\":1,\"title\":\"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\",\"body\":\"quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto\",\"userId\":1},{\"id\":2,\"title\":\"qui est esse\",\"body\":\"est rerum tempore vitae\\nsequi sint nihil reprehenderit dolor beatae ea dolores neque\\nfugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\\nqui aperiam non debitis possimus qui neque nisi nulla\",\"userId\":1}]",
                result, "Error! Return value is not correct!");
    }

    @Test// test UpdateController.UpdateId() with json array which has 4 elements
    public void test10(){
        String test = "[\n" +
                "\t{\n" +
                "\t\t\"id\":1,\n" +
                "\t\t\"title\":\"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\",\n" +
                "\t\t\"body\":\"quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto\",\n" +
                "\t\t\"userId\":1\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\":2,\n" +
                "\t\t\"title\":\"qui est esse\",\n" +
                "\t\t\"body\":\"est rerum tempore vitae\\nsequi sint nihil reprehenderit dolor beatae ea dolores neque\\nfugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\\nqui aperiam non debitis possimus qui neque nisi nulla\",\n" +
                "\t\t\"userId\":1\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\":3,\n" +
                "\t\t\"title\":\"ea molestias quasi exercitationem repellat qui ipsa sit aut\",\n" +
                "\t\t\"body\":\"et iusto sed quo iure\\nvoluptatem occaecati omnis eligendi aut ad\\nvoluptatem doloribus vel accusantium quis pariatur\\nmolestiae porro eius odio et labore et velit aut\",\n" +
                "\t\t\"userId\":1\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\":4,\n" +
                "\t\t\"title\":\"1800Flowers\",\n" +
                "\t\t\"body\":\"1800Flowers\",\n" +
                "\t\t\"userId\":1\n" +
                "\t}]";
        JSONArray jsonArray = JSONArray.parseArray(test);
        String result = update.UpdateId(jsonArray);
        jsonArray = JSONArray.parseArray(result);
        JSONObject jsonObject = jsonArray.getJSONObject(3);
        //check the 4th json object
        Assertions.assertEquals("1800Flowers", jsonObject.get("title"), "Error! Return value is not correct!");
        Assertions.assertEquals("1800Flowers", jsonObject.get("body"), "Error! Return value is not correct!");
    }

    @Test// test UpdateController.GetPost() with empty mock json response
    public void test11(){
        Mockito.when(rt.getForObject(anyString(),eq(String.class))).thenReturn("[]");
        String result = update.GetPost();
        Assertions.assertEquals("[]", result, "Error! Return value is not correct!");
    }

    @Test// test UpdateController.GetPost() with mock json response which has less than 4 elements
    public void test12(){
        String test = "[\n" +
                "\t{\n" +
                "\t\t\"id\":1,\n" +
                "\t\t\"title\":\"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\",\n" +
                "\t\t\"body\":\"quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto\",\n" +
                "\t\t\"userId\":1\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\":2,\n" +
                "\t\t\"title\":\"qui est esse\",\n" +
                "\t\t\"body\":\"est rerum tempore vitae\\nsequi sint nihil reprehenderit dolor beatae ea dolores neque\\nfugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\\nqui aperiam non debitis possimus qui neque nisi nulla\",\n" +
                "\t\t\"userId\":1\n" +
                "\t}]";
        Mockito.when(rt.getForObject(anyString(),eq(String.class))).thenReturn(test);
        String result = update.GetPost();
        Assertions.assertEquals("[{\"id\":1,\"title\":\"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\",\"body\":\"quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto\",\"userId\":1},{\"id\":2,\"title\":\"qui est esse\",\"body\":\"est rerum tempore vitae\\nsequi sint nihil reprehenderit dolor beatae ea dolores neque\\nfugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\\nqui aperiam non debitis possimus qui neque nisi nulla\",\"userId\":1}]"
                , result, "Error! Return value is not correct!");
    }

    @Test// test UpdateController.GetPost() with mock json response which has 4 elements
    public void test13(){
        String test = "[\n" +
                "\t{\n" +
                "\t\t\"id\":1,\n" +
                "\t\t\"title\":\"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\",\n" +
                "\t\t\"body\":\"quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto\",\n" +
                "\t\t\"userId\":1\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\":2,\n" +
                "\t\t\"title\":\"qui est esse\",\n" +
                "\t\t\"body\":\"est rerum tempore vitae\\nsequi sint nihil reprehenderit dolor beatae ea dolores neque\\nfugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\\nqui aperiam non debitis possimus qui neque nisi nulla\",\n" +
                "\t\t\"userId\":1\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\":3,\n" +
                "\t\t\"title\":\"ea molestias quasi exercitationem repellat qui ipsa sit aut\",\n" +
                "\t\t\"body\":\"et iusto sed quo iure\\nvoluptatem occaecati omnis eligendi aut ad\\nvoluptatem doloribus vel accusantium quis pariatur\\nmolestiae porro eius odio et labore et velit aut\",\n" +
                "\t\t\"userId\":1\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\":4,\n" +
                "\t\t\"title\":\"1800Flowers\",\n" +
                "\t\t\"body\":\"1800Flowers\",\n" +
                "\t\t\"userId\":1\n" +
                "\t}]";
        Mockito.when(rt.getForObject(anyString(),eq(String.class))).thenReturn(test);
        String result = update.GetPost();
        JSONArray jsonArray = JSONArray.parseArray(result);
        JSONObject jsonObject = jsonArray.getJSONObject(3);
        //check the 4th json object
        Assertions.assertEquals("1800Flowers", jsonObject.get("title"), "Error! Return value is not correct!");
        Assertions.assertEquals("1800Flowers", jsonObject.get("body"), "Error! Return value is not correct!");

    }
}
