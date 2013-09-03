/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.patient.controller;

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
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @MenuEntry(icon = "patient/image/paciente.png", menuGroup = "Pacientes", order = 0, text = "Gestión Pacientes")
    public String gridPatient(Model model) {
        model.addAttribute("js", Arrays.asList("patient/js/common/gridPatient.js","patient/js/common/gridPatientExpander.js",
                "patient/js/patient.js","manager/js/common/gridrole.js", "manager/js/common/gridgroup.js","manager/js/common/griduser.js",
                "manager/js/manager-utils.js","patient/js/common/detailsPanel.js"));
        return "dynamic/main";

    }
}
