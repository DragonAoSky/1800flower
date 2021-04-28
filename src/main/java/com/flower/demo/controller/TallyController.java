package com.flower.demo.controller;

import com.flower.demo.Post;
import java.util.*;

import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;




@RestController
public class TallyController {
    private final RestTemplate rt;

    public TallyController(RestTemplateBuilder restTemplateBuilder){

        this.rt = restTemplateBuilder.build();
    }

    @RequestMapping(value = "/tallies",produces= "application/json")
    public String GetPost(){
        String url = "http://jsonplaceholder.typicode.com/posts";
        try {
            Post[] temp = rt.getForObject(url, Post[].class);
            String text = "There are " + TallyUniqID(temp) + " unique user Ids";
            return "[{\"message\": \"" + text + "\"}]";
        }
        catch (HttpStatusCodeException e) {
            return "Error!";
        }catch (Exception e) {
            return "Error!";
        }
    }


    public int TallyUniqID(Post[] posts){
        Set<Integer> set = new HashSet<>();
        for(Post p: posts){
            if(!set.contains(p.getUserId())){
                set.add(p.getUserId());
            }
        }
        return set.size();
    }


}
