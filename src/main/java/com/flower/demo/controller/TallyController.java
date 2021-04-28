package com.flower.demo.controller;

import com.flower.demo.Post;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;




@RestController
@Service
public class TallyController {

    @Autowired
    private RestTemplate rt;

    @RequestMapping(value = "/tallies",produces= "application/json")
    public String GetPost(){
        String url = "http://jsonplaceholder.typicode.com/posts";
        try {
            Post[] temp = rt.getForObject(url, Post[].class); // get the JSON response in Post format
            String text = "There are " + TallyUniqID(temp) + " unique user Ids";
            return "[{\"message\": \"" + text + "\"}]"; // return result as json format
        }catch (Exception e) {
            return "Error!";
        }
    }


    public int TallyUniqID(Post[] posts){ // count uniq IDs
        Set<Integer> set = new HashSet<>();
        for(Post p: posts){
            if(!set.contains(p.getUserId())){
                set.add(p.getUserId());
            }
        }
        return set.size();
    }




}
