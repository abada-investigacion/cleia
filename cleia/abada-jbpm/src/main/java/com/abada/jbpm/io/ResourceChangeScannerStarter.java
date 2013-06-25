/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.io;


import org.drools.io.ResourceChangeNotifier;
import org.drools.io.ResourceChangeScanner;
import org.drools.io.ResourceFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Temporary class until Drools Spring integration starts the Resource Change Scanner.
 * 
 * @author david
 */
public class ResourceChangeScannerStarter implements InitializingBean
{
    private ResourceChangeNotifier resourceChangeNotifierService;
    private ResourceChangeScanner resourceChangeScannerService;
    
    @Override
    public void afterPropertiesSet() throws Exception
    {
        resourceChangeNotifierService = ResourceFactory.getResourceChangeNotifierService();
        resourceChangeScannerService = ResourceFactory.getResourceChangeScannerService();

        resourceChangeNotifierService.start();
        resourceChangeScannerService.start();                
    }
    
    public void dispose(){
        resourceChangeNotifierService.stop();
        resourceChangeScannerService.stop();
    }

}

