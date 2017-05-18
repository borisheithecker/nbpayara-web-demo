/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nbpayara.web.demo;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Topic;
import org.nbpayara.demo.beans.MessageEvent;

/**
 *
 * @author boris.heithecker
 */
@Stateless
public class MessagesNotificator {

    @Resource(mappedName = "jms/messages-topic")
    private Topic messages_topic;
    @Inject
    @JMSConnectionFactory("jms/message-topic-factory") 
    private JMSContext context;

    @TransactionAttribute(TransactionAttributeType.MANDATORY)//ZGN
    public void notityConsumers(MessageEvent event) {
        final JMSProducer messageProducer = context.createProducer();
        messageProducer.send(messages_topic, event);    
    }
}
