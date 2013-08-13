/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.io;

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

