package com.flower.demo.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;



@RestController
@Service
public class UpdateController {

    @Autowired
    private RestTemplate rt;

    @RequestMapping(value = "/test")
    public String test(){
            return "Yes";
        }

    @RequestMapping(value = "/updates",produces= "application/json")
    public String GetPost(){
        try {
            String url = "http://jsonplaceholder.typicode.com/posts";
            String temp = rt.getForObject(url, String.class);
            JSONArray jsonArray = JSONArray.parseArray(temp);
            return UpdateId(jsonArray);
        }
        catch (Exception e) {
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
}
