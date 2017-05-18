/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nbpayara.web.demo;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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

    @ManagedProperty("#{appUsers}")
    private AppUsers users;
    private String message;
    private String username;
    private boolean loggedIn;
    private String user;
    private final static String CHANNEL = "/{room}/";

    public AppUsers getUsers() {
        return users;
    }

    public void setUsers(AppUsers users) {
        this.users = users;
    }

    public String getPrivateUser() {
        return user;
    }

    public void setPrivateUser(String privateUser) {
        this.user = privateUser;
    }

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
        //eventBus.publish(CHANNEL + "*", username + ": " + message);
//        eventBus.publish(CHANNEL + "*", msg);
        messages.postMessage(msg);
        message = null;
    }

    public void login() {
        RequestContext requestContext = RequestContext.getCurrentInstance();

        if (users.getUsers().contains(username)) {
            loggedIn = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username taken", "Try with another username."));
            requestContext.update("growl");
        } else {
            users.getUsers().add(username);
            requestContext.execute("PF('subscriber').connect('/" + username + "')");
            loggedIn = true;
        }
    }

    public void disconnect() {
        //remove user and update ui
        users.getUsers().remove(username);
        RequestContext.getCurrentInstance().update("form:users");

        //push leave information
        eventBus.publish(CHANNEL + "*", username + " left the channel.");

        //reset state 
        loggedIn = false;
        username = null;
    }
}
