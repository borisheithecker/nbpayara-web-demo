/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nbpayara.web.demo;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.nbpayara.demo.beans.Message;
import org.primefaces.context.RequestContext;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

/**
 *
 * @author boris.heithecker
 */
@ManagedBean(name = "messagesApplication")
@ViewScoped
public class MessagesApplication implements Serializable {

    @EJB
    private Messages messages;
    private final EventBus eventBus = EventBusFactory.getDefault().eventBus();
    private String message;
    private String username;
    private boolean loggedIn;

    public String getGlobalMessage() {
        return message;
    }

    public void setGlobalMessage(String globalMessage) {
        this.message = globalMessage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void sendGlobal() {
        Message msg = new Message(username, message, System.currentTimeMillis());
        messages.postMessage(msg);
        message = null;
    }

    public List<String> getUsers() {
        return messages.getUsers();
    }

    public void login() {
        RequestContext requestContext = RequestContext.getCurrentInstance();
        if (messages.getUsers().contains(username)) {
            loggedIn = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username taken.", "Try with another username."));
            requestContext.update("growl");
        } else {
            messages.addUser(username);
            requestContext.execute("PF('subscriber').connect('/" + username + "')");
            loggedIn = true;
        }
    }

    public void disconnect() {
        RequestContext requestContext = RequestContext.getCurrentInstance();
        //remove user and update ui
        messages.removeUser(username);
        RequestContext.getCurrentInstance().update("form:users");
        //push leave information
        String text = MessageFormat.format("{0} has left.", username);
        eventBus.publish("/nb/*", new Message(null, text, System.currentTimeMillis()));
        requestContext.execute("PF('subscriber').disconnect()");
        //reset state 
        loggedIn = false;
        username = null;
    }
}
