/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nbpayara.web.demo;

import java.text.MessageFormat;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import org.nbpayara.demo.beans.Message;
import org.nbpayara.demo.beans.MessagesBean;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

/**
 *
 * @author boris.heithecker
 */
@Stateless
@Remote(MessagesBean.class)
public class MessagesBeanRemote implements MessagesBean {

    @EJB
    private Messages messages;
    private EventBus eventBus;

    @PostConstruct
    public void init() {
        eventBus = EventBusFactory.getDefault().eventBus();
    }

    @Override
    public List<Message> getMessages() {
        return messages.getMessages();
    }

    @Override
    public void postMessage(Message message) {
        messages.postMessage(message);
    }

    @Override
    public boolean login(String username) {
        boolean ret = messages.addUser(username);
        if (ret) {
            String text = MessageFormat.format("{0} has entered.", username);
            eventBus.publish("/nb/*", new Message(null, text, System.currentTimeMillis()));
        }
        return ret;
    }

    @Override
    public void disconnect(String username) {
        //remove user and update ui
        messages.removeUser(username);
        //push leave information
        String text = MessageFormat.format("{0} has left.", username);
        eventBus.publish("/nb/*", new Message(null, text, System.currentTimeMillis()));
    }

}
