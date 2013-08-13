/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.process.statistic;

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

import com.abada.jbpm.integration.console.graph.AbadaGraphViewerPlugin;
import com.abada.springframework.web.servlet.view.OutputStreamView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.security.RolesAllowed;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author katsu
 */
@Controller
@RequestMapping("/rs/process/statistic")
public class StatisticController {

    private static final Log logger = LogFactory.getLog(StatisticController.class);
    @Autowired
    private AbadaGraphViewerPlugin graphViewerPlugin;
    /*@Autowired
    private KnowledgeAgent kagent1;*/
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * The diagram shows the numerical statistics of patients placed on a node.
     * @param processId Process id.
     * @param start Start date with format yyyy-MM-dd.
     * @param end End date not included in result with format yyyy-MM-dd.
     * @return  Return an image.
     * @throws IOException
     * @throws ParseException 
     */
    //TODO Move to other Controller and add a range of date
    @RequestMapping(value = "/{processId}/number/image", method = RequestMethod.GET)
    @RolesAllowed(value={"ROLE_ADMIN","ROLE_USER"})
    public OutputStreamView getProcessStatusImage(@PathVariable String processId, String start, String end) throws IOException, ParseException {
        Date startDate = simpleDateFormat.parse(start);
        Date endDate = simpleDateFormat.parse(end);
        byte[] result = graphViewerPlugin.getProcessStatusImage(processId, startDate, endDate);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(result);
        return new OutputStreamView(out, processId + "-image.png", "image/png");
    }


}
