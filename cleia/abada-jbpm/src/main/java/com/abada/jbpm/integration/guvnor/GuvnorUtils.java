/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.integration.guvnor;

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

import com.abada.jbpm.integration.console.URLUtils;
import com.abada.jbpm.integration.guvnor.entity.Collection;
import com.abada.jbpm.integration.guvnor.entity.Package;
import com.abada.utils.Constants;
import com.thoughtworks.xstream.XStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author katsu
 */
public class GuvnorUtils {

    private static final Log logger = LogFactory.getLog(GuvnorUtils.class);
    //private static final String EXTENSION_TEMPLATE = ".ftl";
    private static final String EXTENSION_TEMPLATE = ".drl";
    private static final String EXTENSION_PNG = ".png";
    private static final String CHARACTER_TYPE = "UTF-8";

    private URLUtils urlUtils;

    public URLUtils getUrlUtils() {
        return urlUtils;
    }

    public void setUrlUtils(URLUtils urlUtils) {
        this.urlUtils = urlUtils;
    }
    
    private XStream x;

    public GuvnorUtils() {
        x = new XStream();        
        x.processAnnotations(Collection.class);
    }
    
    public InputStream getTemplate(String template) {
        return getFile(template, EXTENSION_TEMPLATE);
    }

    private List<String> getPackages() {
        List<String> result = new ArrayList<String>();
        DataInputStream bis = null;
        HttpClient httpClient = null;
        try {
            httpClient = getHttpClient();

            StringBuilder sb = urlUtils.getRESTGuvnorURL(Constants.EMPTY_STRING);

            HttpGet httpGet = new HttpGet(sb.toString());
            httpGet.addHeader("accept", "application/xml");
            HttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                Collection collection=(Collection) x.fromXML(new InputStreamReader(response.getEntity().getContent()));
                if (collection!=null && collection.getPackages()!=null && collection.getPackages().size()>0){
                    for (Package p:collection.getPackages()){
                        result.add(p.getTitle());
                    }
                }
            }else{
                logger.error(response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase()+" "+new java.util.Scanner(new InputStreamReader(response.getEntity().getContent())).useDelimiter("\\A").next());
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (Exception e) {
                }
            }
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
        return result;
    }

    public URL getURL(String name, String extension) {
        List<String> packages = getPackages();
        HttpClient httpClient = null;
        for (String selectedPackage : packages) {
            try {
                httpClient = getHttpClient();

                StringBuilder sb = urlUtils.getNormalGuvnorURL(selectedPackage);
                sb.append(URLEncoder.encode(name, CHARACTER_TYPE));
                sb.append(extension);

                HttpGet httpGet = new HttpGet(sb.toString());
                HttpResponse response = httpClient.execute(httpGet);

                if (response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
                    return new URL(sb.toString());
                }
            } catch (Exception e) {
                logger.warn(e);
            } finally {
                if (httpClient != null) {
                    httpClient.getConnectionManager().shutdown();
                }
            }
        }
        return null;
    }

    public byte[] getFileAsByte(String name, String extension) {
        InputStream is = getFile(name, extension);
        if (is != null) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                transfer(is, os);
            } catch (IOException e) {
                throw new RuntimeException("Could not read process file: " + e.getMessage());
            } finally {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
            byte[] result = os.toByteArray();
            return result;
        }
        return null;
    }

    public InputStream getFile(String name, String extension) {
        List<String> packages = getPackages();
        HttpClient httpClient = null;
        for (String selectedPackage : packages) {
            try {
                httpClient = getHttpClient();

                StringBuilder sb = urlUtils.getNormalGuvnorURL(selectedPackage);
                sb.append(URLEncoder.encode(name, CHARACTER_TYPE));
                sb.append(extension);

                HttpGet httpGet = new HttpGet(sb.toString());
                HttpResponse response = httpClient.execute(httpGet);

                if (response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
                    ByteArrayOutputStream bos=new ByteArrayOutputStream();
                    transfer(response.getEntity().getContent(), bos);
                    return new ByteArrayInputStream(bos.toByteArray());
                }
            } catch (Exception e) {
                logger.warn(e);
            } finally {
                if (httpClient != null) {
                    httpClient.getConnectionManager().shutdown();
                }
            }
        }
        return null;
    }
    private static final int BUFFER_SIZE = 512;

    public int transfer(InputStream in, OutputStream out) throws IOException {
        int total = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = in.read(buffer);
        while (bytesRead != -1) {
            out.write(buffer, 0, bytesRead);
            total += bytesRead;
            bytesRead = in.read(buffer);
        }
        return total;
    }

    private HttpClient getHttpClient() {
        DefaultHttpClient result = new DefaultHttpClient();
        result.getCredentialsProvider().setCredentials(
                new AuthScope(AuthScope.ANY),
                new UsernamePasswordCredentials(urlUtils.getGuvnorUser(), urlUtils.getGuvnorPassword()));
        return result;
    }
}
