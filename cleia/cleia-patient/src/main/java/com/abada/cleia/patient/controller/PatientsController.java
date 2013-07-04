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
 * @author david
 */
@Controller
public class PatientsController {

    /**
     * js Patient load
     *
     * @param model
     * @return
     *
     */
    @RequestMapping(value = "/patient/patient.htm")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @MenuEntry(icon = "patient/image/paciente.png", menuGroup = "Pacientes", order = 0, text = "Gestión Pacientes")
    public String gridPatient(Model model) {
        model.addAttribute("js", Arrays.asList("patient/js/common/gridPatient.js", "patient/js/patient.js"));
        return "dynamic/main";

    }
}
