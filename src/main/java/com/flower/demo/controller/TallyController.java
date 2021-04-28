package com.flower.demo.controller;

import com.flower.demo.Post;
import java.util.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;




@RestController
public class TallyController {
    private final RestTemplate rt;

    public TallyController(RestTemplateBuilder restTemplateBuilder){

        this.rt = restTemplateBuilder.build();
    }

    @RequestMapping("/tallies")
    public String GetPost(){
        String url = "http://jsonplaceholder.typicode.com/posts";
        Post[] temp = rt.getForObject(url, Post[].class);
        return "There are " + TallyUniqID(temp) + " unique user Ids";
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
