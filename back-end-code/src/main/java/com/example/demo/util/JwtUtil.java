package com.example.demo.util;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${huyaminiapp.appId}")
    private String APPID;
    @Value("${huyaminiapp.secret}")
    private String SECRET;
    private static  final String DEV = "DEV";
    @Value("${huyaminiapp.extId}")
    private String EXTID;

    /**
     * JWT token 生成
     * @return
     */
    public  String issueJwt(int curTime,String role,String profileId,String roomId,String userId) {
        Map<String, Object> stringObjectMap = new HashMap<>(2);
        stringObjectMap.put("alg", "HS256");
        stringObjectMap.put("typ", "JWT");
        int expTime = curTime+1200;
        String payload = "{\"iat\":"+ curTime + ",\"exp\":"+ expTime +",\"appId\":\""+APPID+"\""
                + ",\"extId\":\""+ EXTID +"\",\"creator\":\""+DEV + "\",\"role\":\""+ role
                + "\",\"profileId\":\""+ profileId +"\",\"roomId\":\""+roomId + "\",\"userId\":\""+ userId+
                "\"}";
        String token = Jwts.builder().setHeader(stringObjectMap)
                .setPayload(payload).signWith(SignatureAlgorithm.HS256, SECRET.getBytes()).compact();
        return token;
    }

}
