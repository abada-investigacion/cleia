/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.manager;

import com.abada.springframework.web.servlet.menu.MenuEntry;
import java.util.Arrays;
import javax.annotation.security.RolesAllowed;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

/**
 *
 * @author katsu
 */
@Controller
public class GroupController {

    /**
     * js group load
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/manager/group.htm")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @MenuEntry(icon = "manager/image/servicio_me.png", menuGroup = "Manager", order = 1, text = "Servicios")
    public String gridgroup(Model model) {
        model.addAttribute("js", Arrays.asList("manager/js/common/gridgroup.js", "manager/js/common/gridgroupexpander.js", "manager/js/group.js", "manager/js/common/griduser.js"));
        return "dynamic/main";

    }
}
