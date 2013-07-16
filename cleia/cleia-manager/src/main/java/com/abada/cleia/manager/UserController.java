/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.manager;

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
public class UserController {

  
    /**
     * js user load
     * @param model
     * @return 
     */
    @RequestMapping(value = "/manager/user.htm")
    @RolesAllowed(value={"ROLE_ADMIN","ROLE_ADMINISTRATIVE"})
    @MenuEntry(icon = "manager/image/user.png", menuGroup = "Manager", order = 0, text = "Usuarios")
    public String gridUser(Model model) {
        model.addAttribute("js", Arrays.asList("manager/js/common/griduser.js","manager/js/common/griduserexpander.js", 
                "manager/js/common/gridrole.js", "manager/js/common/gridgroup.js","manager/js/common/gridids.js", "manager/js/user.js","manager/js/manager-utils.js"));
        return "dynamic/main";

    }

}
