/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.integration.console;

import com.abada.utils.Constants;
import java.util.Properties;

/**
 *
 * @author katsu
 */
public class URLUtils {

    private static final String PRE_URL_NORMAL = "/org.drools.guvnor.Guvnor/package/";
    private static final String POST_URL_NORMAL = "/LATEST/";
    //private static final String PRE_URL_WEBDAV = "/org.drools.guvnor.Guvnor/webdav/packages";
    private static final String PRE_URL_REST = "/rest/packages";
    private static final char SLASH = '/';
    private static final String GUVNOR_URL = "jbpm.console.guvnor.url";
    private static final String GUVNOR_USER = "jbpm.console.guvnor.user";
    private static final String GUVNOR_PASSWORD = "jbpm.console.guvnor.password";
    private static final String JBPM_URL = "jbpm.console.server.url";
    private Properties properties;

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    private Properties getProperties() {
        return properties;
    }

    private StringBuilder getGuvnorURL(Properties properties) {
        StringBuilder sb = new StringBuilder();
        sb.append(properties.get(GUVNOR_URL));
        return sb;
    }

    public StringBuilder getNormalGuvnorURL(String selectePackage) {
        StringBuilder sb = getGuvnorURL(properties);
        sb.append(PRE_URL_NORMAL).append(selectePackage).append(POST_URL_NORMAL);
        return sb;
    }

//    public StringBuilder getWebDAVGuvnorURL(String selectePackage) {
//        StringBuilder sb = getGuvnorURL(properties);
//        sb.append(PRE_URL_WEBDAV);
//        if (selectePackage != null && !Constants.EMPTY_STRING.equals(selectePackage)) {
//            sb.append(SLASH).append(selectePackage);
//        }
//        return sb;
//    }
    
    public StringBuilder getRESTGuvnorURL(String selectePackage) {
        StringBuilder sb = getGuvnorURL(properties);
        sb.append(PRE_URL_REST);
        if (selectePackage != null && !Constants.EMPTY_STRING.equals(selectePackage)) {
            sb.append(SLASH).append(selectePackage);
        }
        return sb;
    }

    public StringBuilder getJBPMServerURL() {
        StringBuilder sb = new StringBuilder();
        sb.append(properties.getProperty(JBPM_URL));
        return sb;
    }

    public String getGuvnorUser() {
        return getProperties().getProperty(GUVNOR_USER);
    }

    public String getGuvnorPassword() {
        return getProperties().getProperty(GUVNOR_PASSWORD);
    }
}
