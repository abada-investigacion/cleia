/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.patient.controller;

import com.abada.springframework.web.servlet.menu.MenuEntry;
import java.util.Arrays;
import javax.annotation.security.RolesAllowed;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author mmartin
 */
@Controller
public class IdtypeController {

    /**
     * js idtype load
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/patient/idtype.htm")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @MenuEntry(icon = "patient/image/identificacion.png", menuGroup = "Pacientes", order = 0, text = "Identificación Pacientes")
    public String gridIdtype(Model model) {
        model.addAttribute("js", Arrays.asList("patient/js/common/gridIdtype.js", "patient/js/Idtype.js"));
        return "dynamic/main";
    }
}
