/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nbpayara.web.demo;

import java.text.MessageFormat;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.nbpayara.demo.beans.Message;
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
@PushEndpoint("/nb/{user}")
@Singleton
public class AppPushResource {

    @PathParam("user")
    private String username;
    @Inject
    private ServletContext ctx;

    @OnOpen
    public void onOpen(RemoteEndpoint r, EventBus eventBus) {
        String text = MessageFormat.format("{0} has entered.", username);
        eventBus.publish("/nb/*", new Message(null, text, System.currentTimeMillis()));
    }

    @OnClose
    public void onClose(RemoteEndpoint r, EventBus eventBus) {
        MessagesApplication app = (MessagesApplication) ctx.getAttribute("messagesApplication");
        app.getUsers().remove(username);
        String text = MessageFormat.format("{0} has left.", username);
        eventBus.publish("/nb/*", new Message(null, text, System.currentTimeMillis()));
    }

//    @OnMessage(decoders = {MessageDecoder.class}, encoders = {MessageEncoder.class})
    @OnMessage(encoders = {MessageEncoder.class})
    public Message onMessage(Message message) {
        return message;
    }

}
