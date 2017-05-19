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

/**
 *
 * @author boris.heithecker
 */
@Singleton
public class Messages {

    @EJB
    private MessagesNotificator notificator;
    private List<Message> messages;
    private List<String> users;

    @PostConstruct
    public void init() {
        messages = new ArrayList<>();
        users = new ArrayList<>();
    }

    public List<String> getUsers() {
        return new ArrayList<>(users);
    }

    public boolean addUser(String username) {
        if (users.contains(username)) {
            return false;
        }
        return users.add(username);
    }

    public boolean removeUser(String username) {
        return users.remove(username);
    }

    public List<Message> getMessages() {
        return new ArrayList<>(messages);
    }

    public void postMessage(Message msg) {
        messages.add(msg);
        notificator.notityConsumers(new MessageEvent(msg));
    }

}
