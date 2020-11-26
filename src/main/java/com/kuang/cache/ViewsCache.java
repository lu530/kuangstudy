package com.kuang.cache;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ViewsCache {
    ExpiringMap<String,String> blogView = ExpiringMap.builder()
            .maxSize(1000)//最大容量，防止恶意注入
            .expiration(1, TimeUnit.HOURS)//过期时间5分钟
            .expirationPolicy(ExpirationPolicy.CREATED)
            .variableExpiration()
            .build();

    ExpiringMap<String,String> questionView = ExpiringMap.builder()
            .maxSize(1000)//最大容量，防止恶意注入
            .expiration(1, TimeUnit.HOURS)//过期时间5分钟
            .expirationPolicy(ExpirationPolicy.CREATED)
            .variableExpiration()
            .build();


    public void putIpBid(String ipBid,String bid){
        blogView.put(ipBid,bid);
    }

    public String getIpBid(String ipBid){
        return blogView.get(ipBid);
    }

    public void putIpPid(String ipPid,String pid){
        questionView.put(ipPid,pid);
    }

    public String getIpPid(String ipPid){
        return questionView.get(ipPid);
    }

}
