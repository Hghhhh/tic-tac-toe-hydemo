package com.example.demo.filter;

import com.example.demo.dto.JwtPlayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {


    @Value("${huyaminiapp.secret}")
    String secret;


    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain filterChain) throws ServletException, IOException {
        // jwt token
        String token = httpRequest.getHeader("authorization");
        log.info(token+"\n "+getIp2(httpRequest)+" "+httpRequest.getRequestURI()+" "+httpRequest.getMethod()+" "+httpRequest.getRequestedSessionId());
        if(httpRequest.getMethod().equals("OPTIONS")){
            System.out.println(111);
            httpResponse.setHeader("Access-Control-Allow-Headers","*");
            httpResponse.setHeader("Access-Control-Allow-Origin","*");
            httpResponse.setHeader("Access-Control-Allow-Method","*");
            httpResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return ;
        }
        if (token == null) {
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ;
        } else {
            try{
                Claims claims = Jwts.parser()
                        .setSigningKey(secret.getBytes())
                        .parseClaimsJws(token)
                        .getBody();
                JwtPlayload jwtPlayload = JwtPlayload.buildeJwtPlayload(claims);
                httpRequest.setAttribute("userInfo",jwtPlayload);
                httpResponse.setStatus(HttpServletResponse.SC_OK);
            }catch (Exception e){
                log.error(e.getMessage());
                e.printStackTrace();
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return ;
            }
        }
        filterChain.doFilter(httpRequest,httpResponse);
    }

    public static String getIp2(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }

    @Override
    public void destroy() {
    }
}