/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.web.url;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import org.springframework.context.MessageSource;

/**
 *
 * @author katsu
 */
public class URLFactory {
    private MessageSource messageSource;
    
    private URL serverUrl;
    private String serverUrlRole;
    private String serverUrlDniRoles;
    
    public synchronized String getServerUrlDniRole() {
        if (serverUrlDniRoles==null)
            serverUrlDniRoles=messageSource.getMessage("urlServer", null, Locale.getDefault())+messageSource.getMessage("urlServerDniRoles", null, Locale.getDefault());
        return serverUrlDniRoles;
    }

    public void setServerUrlDniRole(String serverUrlDniRoles) {
        this.serverUrlDniRoles = serverUrlDniRoles;
    }

    public synchronized String getServerUrlRole() {
        if (serverUrlRole==null)
            serverUrlRole=messageSource.getMessage("urlServer", null, Locale.getDefault())+messageSource.getMessage("urlServerRoles", null, Locale.getDefault());
        return serverUrlRole;
    }

    public void setServerUrlRole(String serverUrlRole) {
        this.serverUrlRole = serverUrlRole;
    }

    public synchronized  URL getServerUrl() throws MalformedURLException {      
        if (serverUrl==null){
            serverUrl=new URL(messageSource.getMessage("urlServer", null, Locale.getDefault()));
        }
        return serverUrl;
    }

    public void setServerUrl(URL serverUrl) {
        this.serverUrl = serverUrl;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }        
}
