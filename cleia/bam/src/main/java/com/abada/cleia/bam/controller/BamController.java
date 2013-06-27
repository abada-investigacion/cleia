/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.bam.controller;

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
    @MenuEntry(icon = "bam/image/monitoriza.png", menuGroup = "Monitorizaci&oacute;n Paciente", order = 0, text = "Monitorizar")
    public String getBam(Model model) {
        model.addAttribute("js", Arrays.asList("bam/js/bam.js"));
        return "dynamic/main";
    }           
}
