/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest;

/**
 *
 * @author katsu
 */
public class DisableCertificateHostnameValidator {

    public DisableCertificateHostnameValidator() {
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {
            public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
                if (hostname.contains("paas.abadasoft.com")) {
                    return true;
                }
                return false;
            }
        });
    }
}
