package com.example.demo.controller;

import com.example.demo.dto.JwtPlayload;
import com.example.demo.dto.Point;
import com.example.demo.service.DeliverService;
import com.example.demo.util.JsonUtil;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MessageController {
    @Autowired
    DeliverService deliverService;


    @Autowired
    private JwtUtil jwtUtil;

    /*
    @PostMapping(value = "/roomOpen")
    public void roomOpen(HttpServletRequest request){
        JwtPlayload userInfo = (JwtPlayload) request.getAttribute("userInfo");
        deliverService.deliverRoomByProfileId(userInfo.getProfileId(),"ROOMOPEN","ROOMOPEN",jwtUtil.issueJwt((int)(System.currentTimeMillis()/1000),"P",userInfo.getProfileId(),
                userInfo.getRoomId(),userInfo.getUserId()));
    }
    */

    @PostMapping(value = "/point")
    public void point(HttpServletRequest request,@RequestBody String point){
        Point xy = JsonUtil.parseJsonWithGson(point, Point.class);
        JwtPlayload userInfo = (JwtPlayload) request.getAttribute("userInfo");
        System.out.println(userInfo);
        if(userInfo.getRole().equals("U")){
            deliverService.deliverByProfileId(userInfo.getProfileId(),"POINT",xy.getX()+"",jwtUtil.issueJwt((int)(System.currentTimeMillis()/1000),userInfo.getRole(),userInfo.getProfileId(),
                    userInfo.getRoomId(),userInfo.getUserId()));
        }else
            deliverService.deliverRoomByProfileId(userInfo.getProfileId(),"POINT",xy.getX()+"",jwtUtil.issueJwt((int)(System.currentTimeMillis()/1000),userInfo.getRole(),userInfo.getProfileId(),
                userInfo.getRoomId(),userInfo.getUserId()));
    }

}
