/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.bam.controller;

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

import com.abada.springframework.web.servlet.menu.Device;
import com.abada.springframework.web.servlet.menu.MenuEntry;
import java.util.Arrays;
import javax.annotation.security.RolesAllowed;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author katsu
 */
@Controller
public class BamController{        
    
    @RequestMapping(value = "/bam/bam.htm")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @MenuEntry(icon = "bam/image/monitoriza.png", menuGroup = "bam.group", order = 0, text = "bam.monitor")
    public String getBam(Model model) {
        model.addAttribute("js", Arrays.asList("bam/js/bam.js"));
        return "dynamic/main";
    }           
    
    @RequestMapping(value = "/bam/bam_m.htm")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @MenuEntry(icon = "bam/image/monitoriza.png", menuGroup = "bam.group", order = 0, text = "bam.monitor",devices = {Device.MOBILE, Device.TABLET})
    public String getBamkMobile(Model model) {
        model.addAttribute("js", Arrays.asList("bam/js_m/bam.js"));
        model.addAttribute("isDesktop", false);
        return "dynamic/main";
    }
}
