/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.bam.controller;

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
public class TaskController {

    @RequestMapping(value = "/bam/task/task.htm")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @MenuEntry(icon = "bam/image/humanTask.png", menuGroup = "Monitorizaci&oacute;n Paciente", order = 0, text = "Tareas")
    public String getTask(Model model) {
        model.addAttribute("js", Arrays.asList("bam/js/task.js"));
        return "dynamic/main";
    }
    
    @RequestMapping(value = "/bam/task/task_m.htm")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @MenuEntry(icon = "bam/image/humanTask.png", menuGroup = "Monitorizaci&oacute;n Paciente", order = 0, text = "Tareas",devices = {Device.MOBILE, Device.TABLET})
    public String getTaskMobile(Model model) {
        model.addAttribute("js", Arrays.asList("bam/js_m/common/FormCustomPanel.js","bam/js_m/common/FormCustomFrame.js","bam/js_m/task.js"));
        model.addAttribute("isDesktop", false);
        return "dynamic/main";
    }
}
