package com.flower.demo.controller;

//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;



@RestController
public class UpdateController {

    private final RestTemplate rt;

    public UpdateController(RestTemplateBuilder restTemplateBuilder){

        this.rt = restTemplateBuilder.build();
    }

    @RequestMapping(value = "/test")
        public String test(){
            return "YES";
        }

        //@RequestMapping(value = "/updates",produces = MediaType.APPLICATION_JSON_VALUE)
        //@RequestMapping(value = "/updates")
        @RequestMapping(value = "/updates",produces= "application/json")
        public String GetPost(){
        try {
            String url = "http://jsonplaceholder.typicode.com/posts";
            String temp = rt.getForObject(url, String.class);
            JSONArray jsonArray = JSONArray.parseArray(temp);
            return UpdateId(jsonArray);
        }
        catch (HttpStatusCodeException e) {
            return "Error!";
        }catch (Exception e) {
            return "Error!";
        }
    }

    public String UpdateId(JSONArray posts){
        if(posts.size() < 4)
            return posts.toString();
        else{
            JSONObject temp = posts.getJSONObject(3);
            temp.put("title","1800Flowers");
            temp.put("body","1800Flowers");
            return posts.toString(SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteDateUseDateFormat);
        }
    }


//    public String UpdateId(JSONArray posts) throws JSONException {
//        if(posts.length() < 4)
//            return posts.toString();
//        else{
//            JSONObject temp = posts.getJSONObject(3);
//            temp.put("title","1800Flowers");
//            temp.put("body","1800Flowers");
//            return posts.toString();
//        }
//    }
}
