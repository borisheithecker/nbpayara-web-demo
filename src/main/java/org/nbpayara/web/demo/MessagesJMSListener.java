/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nbpayara.web.demo;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.nbpayara.demo.beans.MessageEvent;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

/**
 *
 * @author boris.heithecker
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "jms/messages-topic"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/messages-topic"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "jms/messages-topic"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")
})
public class MessagesJMSListener implements MessageListener {

    private EventBus eventBus;
    private final static String CHANNEL = "/{room}/";

    public MessagesJMSListener() {
    }

    @PostConstruct
    public void init() {
        eventBus = EventBusFactory.getDefault().eventBus();
    }

    @Override
    public void onMessage(Message msg) {
        try {
            if (msg.isBodyAssignableTo(MessageEvent.class)) {
                MessageEvent mb = msg.getBody(MessageEvent.class);
                eventBus.publish(CHANNEL + "*", mb.getMessage());
            }
        } catch (JMSException ex) {
        }
    }

}
