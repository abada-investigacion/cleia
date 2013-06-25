/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.integration.console;

import com.abada.utils.Constants;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author katsu
 */
public class URLUtils {
    private static final String defaultPackage="defaultPackage";
    private static final String PRE_URL_NORMAL="/org.drools.guvnor.Guvnor/package/";
    private static final String POST_URL_NORMAL="/LATEST/";
    private static final String PRE_URL_WEBDAV="/org.drools.guvnor.Guvnor/webdav/packages/";
    private static final char SLASH='/';
    private static final String PROPERTIES_FILE="/jbpm.console.properties";
    private static final String PRE_URL="http://";
    private static final String GUVNOR_HOST="jbpm.console.guvnor.host";
    private static final String GUVNOR_PORT="jbpm.console.guvnor.port";    
    private static final String GUVNOR_USER="jbpm.console.guvnor.user";    
    private static final String GUVNOR_PASSWORD="jbpm.console.guvnor.password";    
    private static final String GUVNOR_DEPLOY_NAME="jbpm.console.guvnor.deploy.name";
    private static final String JBPM_HOST="jbpm.console.server.host";
    private static final String JBPM_PORT="jbpm.console.server.port";
    private static final String JBPM_DEPLOY_NAME="jbpm.console.server.deploy.name";
    
    private static Properties getProperties(){        
        Properties properties = new Properties();
        try {
            properties.load(URLUtils.class.getResourceAsStream(PROPERTIES_FILE));
        } catch (IOException e) {
            throw new RuntimeException("Could not load jbpm.console.properties", e);
        }
        return properties;
    }
    
    private static StringBuilder getGuvnorURL(Properties properties){        
        StringBuilder sb = new StringBuilder();
        sb.append(PRE_URL);
        sb.append(properties.get(GUVNOR_HOST));
        sb.append(Constants.COLON).append(new Integer(properties.getProperty(GUVNOR_PORT)));
        return sb;
    }
    
    /*public static StringBuilder getWebDAVGuvnorURL() {
        return getWebDAVGuvnorURL(defaultPackage);
    }
    
    public static StringBuilder getNormalGuvnorURL() {
        return getNormalGuvnorURL(defaultPackage);
    }*/
    
    public static StringBuilder getNormalGuvnorURL(String selectePackage) {
        Properties properties=getProperties();
        StringBuilder sb=getGuvnorURL(properties);
        sb.append(SLASH).append(properties.getProperty(GUVNOR_DEPLOY_NAME)).append(PRE_URL_NORMAL).append(selectePackage).append(POST_URL_NORMAL);
        return sb;
    }
    
    public static StringBuilder getWebDAVGuvnorURL(String selectePackage) {
        Properties properties=getProperties();
        StringBuilder sb=getGuvnorURL(properties);
        sb.append(SLASH).append(properties.getProperty(GUVNOR_DEPLOY_NAME)).append(PRE_URL_WEBDAV).append(selectePackage).append(SLASH);
        return sb;
    }
    
    public static StringBuilder getJBPMServerURL() {
        Properties properties=getProperties();
        StringBuilder sb = new StringBuilder();
        sb.append(PRE_URL);
        sb.append(properties.get(JBPM_HOST));
        sb.append(Constants.COLON).append(new Integer(properties.getProperty(JBPM_PORT)));
        sb.append(SLASH).append(properties.getProperty(JBPM_DEPLOY_NAME));
        return sb;
    }
    
    public static String getGuvnorUser(){
        return getProperties().getProperty(GUVNOR_USER);
    }
    
    public static String getGuvnorPassword(){
        return getProperties().getProperty(GUVNOR_PASSWORD);
    }
}
