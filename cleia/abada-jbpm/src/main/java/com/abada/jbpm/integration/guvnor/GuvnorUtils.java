/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.integration.guvnor;

import com.abada.jbpm.integration.console.URLUtils;
import com.abada.utils.Constants;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
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

    public static InputStream getTemplate(String template) {
        return getFile(template, EXTENSION_TEMPLATE);
    }

    private static List<String> getPackages() {
        List<String> result = new ArrayList<String>();
        DataInputStream bis = null;
        HttpClient httpClient = null;
        try {
            httpClient = getHttpClient();

            StringBuilder sb = URLUtils.getWebDAVGuvnorURL(Constants.EMPTY_STRING);

            HttpGet httpGet = new HttpGet(sb.toString());
            HttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                bis = new DataInputStream(response.getEntity().getContent());
                String s;
                boolean first = true;
                while ((s = bis.readLine()) != null) {
                    if (first) {
                        first = false;
                    } else {
                        result.add(s);
                    }
                }
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

    public static URL getURL(String name, String extension) {
        List<String> packages = getPackages();
        HttpClient httpClient = null;
        for (String selectedPackage : packages) {
            try {
                httpClient = getHttpClient();

                StringBuilder sb = URLUtils.getNormalGuvnorURL(selectedPackage);
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

    public static byte[] getFileAsByte(String name, String extension) {
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

    public static InputStream getFile(String name, String extension) {
        List<String> packages = getPackages();
        HttpClient httpClient = null;
        for (String selectedPackage : packages) {
            try {
                httpClient = getHttpClient();

                StringBuilder sb = URLUtils.getNormalGuvnorURL(selectedPackage);
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

    public static int transfer(InputStream in, OutputStream out) throws IOException {
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

    private static HttpClient getHttpClient() {
        DefaultHttpClient result = new DefaultHttpClient();
        result.getCredentialsProvider().setCredentials(
                new AuthScope(AuthScope.ANY),
                new UsernamePasswordCredentials(URLUtils.getGuvnorUser(), URLUtils.getGuvnorPassword()));
        return result;
    }
}
