/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.manager;

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
    @MenuEntry(icon = "manager/image/user.png", menuGroup = "manager.group", order = 0, text = "manager.usuarios")
    public String gridUser(Model model) {
        model.addAttribute("js", Arrays.asList("manager/js/common/griduser.js","manager/js/common/griduserexpander.js", 
                "manager/js/common/gridrole.js", "manager/js/common/gridgroup.js","manager/js/common/gridids.js", "manager/js/user.js","manager/js/manager-utils.js"));
        return "dynamic/main";

    }

}
