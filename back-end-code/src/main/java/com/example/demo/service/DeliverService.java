package com.example.demo.service;
import com.example.demo.dto.DeliverResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class DeliverService {

    private WebClient webClient;

    @Value("${huyaminiapp.deliverBaseUrl}")
    private String bastUrl;

    @Value("${huyaminiapp.extId}")
    private String extId;
    @Value("${huyaminiapp.appId}")
    private String appId;


    public DeliverService(){

        this.webClient = WebClient.builder()
                .baseUrl(bastUrl)
                .build();
    }

    /**
     * 发送观众端单播
     * @param userId	string	用户唯一标识（unionId）
     * @param roomId	string	主播房间号
     * @param event	string	自定义事件标识
     * @param message	string	自定义消息内容（大小不超过5K）
     * @return DeliverResult
     */
    public void  deliverByUserId(String userId, String roomId, String event, String message,String authorization){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.set("userId",userId);
        formData.set("roomId",roomId);
        formData.set("event",event);
        formData.set("message",message);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"userId\":");
        stringBuilder.append("\""+userId+"\",");
        stringBuilder.append("\"roomId\":");
        stringBuilder.append("\""+roomId+"\",");
        stringBuilder.append("\"event\":");
        stringBuilder.append("\""+event+"\",");
        stringBuilder.append("\"message\":");
        stringBuilder.append("\""+message+"\"");
        stringBuilder.append("}");
        System.out.println("event"+event+"message:"+message);
        webClient
                .post().uri(bastUrl+"/deliverByUserId?appId="+appId+"&extId="+extId)
                .syncBody(stringBuilder.toString())
                .header("authorization",authorization)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(DeliverResult.class)).subscribe(r->System.out.println(r.getMsg()));
    }


    /**
     * 发送直播间广播
     * @param  profileId	string	主播用户唯一标识（unionId）
     * @param  event	string	自定义事件标识
     * @param  message	string	自定义消息内容（大小不超过5K）
     * @return DeliverResult
     */
    public void deliverRoomByProfileId(String profileId,String event,String message,String authorization){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"profileId\":");
        stringBuilder.append("\""+profileId+"\",");
        stringBuilder.append("\"event\":");
        stringBuilder.append("\""+event+"\",");
        stringBuilder.append("\"message\":");
        stringBuilder.append("\""+message+"\"");
        stringBuilder.append("}");
        System.out.println("event"+event+"message:"+message);
        webClient
                .post().uri(bastUrl+"/deliverRoomByProfileId?appId="+appId+"&extId="+extId)
                .syncBody(stringBuilder.toString())
                .header("authorization",authorization)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(DeliverResult.class)).subscribe(r->System.out.println(r.getMsg()));;
    }

    /**
     * 发送主播单播
     * @param  profileId	string	主播用户唯一标识（unionId）
     * @param  event	string	自定义事件标识
     * @param  message	string	自定义消息内容（大小不超过5K）
     * @return DeliverResult
     */
    public void deliverByProfileId(String profileId,String event,String message,String authorization){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"profileId\":");
        stringBuilder.append("\""+profileId+"\",");
        stringBuilder.append("\"event\":");
        stringBuilder.append("\""+event+"\",");
        stringBuilder.append("\"message\":");
        stringBuilder.append("\""+message+"\"");
        stringBuilder.append("}");
        System.out.println("event"+event+"message:"+message);
        webClient
                .post().uri(bastUrl+"/deliverByProfileId?appId="+appId+"&extId="+extId)
                .syncBody(stringBuilder.toString())
                .header("authorization",authorization)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(DeliverResult.class)).subscribe(r->System.out.println(r.getMsg()));
    }



}
