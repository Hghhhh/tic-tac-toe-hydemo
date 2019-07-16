package com.example.demo.dto;

import io.jsonwebtoken.Claims;
import lombok.Data;

import java.util.Date;

@Data
public class JwtPlayload {

    /**
     * jwt token的字段
     * iat	number	token生成时间戳（秒）
     * exp	number	过期时间戳（秒）
     * appId	string	小程序开发者appid
     * extId	string	小程序uuid
     * creator	string	创建者（token生成方：SYS平台，DEV开发者）
     * role	string	用户身份：U用户，P主播（可选）
     * profileId	string	主播unionId
     * roomId	string	主播房间号（可选）
     * userId	string	用户unionId（可选）
     */
    private Date iat;
    private Date exp;
    private String appId;
    private String extId;
    private String creator;
    private String role;
    private String profileId;
    private String roomId;
    private String userId;

    public JwtPlayload(){

    }

    public static JwtPlayload  buildeJwtPlayload(Claims claims){
        JwtPlayload jwtPlayload = new JwtPlayload();
        jwtPlayload.setIat(claims.get("iat", Date.class));
        jwtPlayload.setExp(claims.get("exp", Date.class));
        jwtPlayload.setAppId(claims.get("appId", String.class));
        jwtPlayload.setExtId(claims.get("extId", String.class));
        jwtPlayload.setCreator(claims.get("creator", String.class));
        jwtPlayload.setRole(claims.get("role", String.class));
        jwtPlayload.setProfileId(claims.get("profileId", String.class));
        jwtPlayload.setRoomId(claims.get("roomId", String.class));
        jwtPlayload.setUserId(claims.get("userId", String.class));
        System.out.println(jwtPlayload);
        return jwtPlayload;
    }

}
