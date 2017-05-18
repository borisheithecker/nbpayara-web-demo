/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nbpayara.web.demo;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import org.nbpayara.demo.beans.Message;
import org.nbpayara.demo.beans.MessageEvent;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

/**
 *
 * @author boris.heithecker
 */
@Singleton
public class Messages {

    @EJB
    private MessagesNotificator notificator;
    private EventBus eventBus;
    private List<Message> messages;
    private final static String CHANNEL = "/{room}/";

    @PostConstruct
    public void init() {
        eventBus = EventBusFactory.getDefault().eventBus();
        messages = new ArrayList<>();
    }

    public List<Message> getMessages() {
        return new ArrayList<>(messages);
    }

    public void postMessage(Message msg) {
        messages.add(msg);
        notificator.notityConsumers(new MessageEvent(msg));
    }
}
