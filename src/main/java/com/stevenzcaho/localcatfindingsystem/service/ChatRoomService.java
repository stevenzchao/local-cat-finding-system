package com.stevenzcaho.localcatfindingsystem.service;

import org.redisson.api.RListReactive;
import org.redisson.api.RTopicReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
public class ChatRoomService implements WebSocketHandler {

    @Autowired
    private RedissonReactiveClient client;


    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String room = getChatRoomName(session);
        RTopicReactive topic = this.client.getTopic("topic", StringCodec.INSTANCE);
        RListReactive<String> history = this.client.getList("history:"+room, StringCodec.INSTANCE);

        //subscriber
        session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .flatMap(e -> history.add(e).then(topic.publish(e)))
                .doOnError(System.out::println)
                .doFinally(s -> System.out.println("publisher finally " + s))
                .subscribe();


        //publisher
        Flux<WebSocketMessage> flux = topic.getMessages(String.class)
                .startWith(history.iterator())
                .map(session::textMessage)
                .doOnError(System.out::println)
                .doFinally(s -> System.out.println("publisher finally " + s));

        return session.send(flux);
    }


    private String getChatRoomName(WebSocketSession session){
        URI uri =session.getHandshakeInfo().getUri();
        return UriComponentsBuilder.fromUri(uri)
                .build()
                .getQueryParams()
                .toSingleValueMap()
                .getOrDefault("room", "default");

    }
}
