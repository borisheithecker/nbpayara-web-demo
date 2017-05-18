/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nbpayara.web.demo;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import org.nbpayara.demo.beans.Message;
import org.nbpayara.demo.beans.MessagesBean;

/**
 *
 * @author boris.heithecker
 */
@Stateless
@Remote(MessagesBean.class)
public class MessagesBeanRemote implements MessagesBean {

    @EJB
    private Messages messages;

    @Override
    public List<Message> getMessages() {
        return messages.getMessages();
    }

    @Override
    public void postMessage(String message) {
        Message msg = new Message("anonymous", message, System.currentTimeMillis());
        messages.postMessage(msg);
    }

}
