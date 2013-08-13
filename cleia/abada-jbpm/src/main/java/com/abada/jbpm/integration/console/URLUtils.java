/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.integration.console;

/*
 * #%L
 * Cleia
 * %%
 * Copyright (C) 2013 Abada Servicios Desarrollo (investigacion@abadasoft.com)
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
