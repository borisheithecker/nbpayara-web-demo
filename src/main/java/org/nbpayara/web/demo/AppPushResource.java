/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nbpayara.web.demo;

import org.nbpayara.demo.beans.Message;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.primefaces.push.EventBus;
import org.primefaces.push.RemoteEndpoint;
import org.primefaces.push.annotation.OnClose;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.OnOpen;
import org.primefaces.push.annotation.PathParam;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.annotation.Singleton;

/**
 *
 * @author boris.heithecker
 */
@PushEndpoint("/{room}/{user}")
@Singleton
public class AppPushResource {

    @PathParam("room")
    private String room;
    @PathParam("user")
    private String username;
    @Inject
    private ServletContext ctx;

    @OnOpen
    public void onOpen(RemoteEndpoint r, EventBus eventBus) {
        eventBus.publish(room + "/*", new Message(null, String.format("%s has entered the room '%s'", username, room), System.currentTimeMillis()));
    }

    @OnClose
    public void onClose(RemoteEndpoint r, EventBus eventBus) {
        AppUsers users = (AppUsers) ctx.getAttribute("chatUsers");
        users.getUsers().remove(username);
        eventBus.publish(room + "/*", new Message(null, String.format("%s has left the room", username), System.currentTimeMillis()));
    }

    @OnMessage(decoders = {MessageDecoder.class}, encoders = {MessageEncoder.class})
    public Message onMessage(Message message) {
        return message;
    }

}
