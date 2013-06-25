/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.bam.controller;

import com.abada.springframework.web.client.RestTemplate;
import com.abada.springframework.web.servlet.menu.MenuEntry;
import com.abada.springframework.web.servlet.view.OutputStreamView;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author katsu
 */
@Controller
public class BamStatisticController {
    @Autowired
    private URL jbpmServerUrl;
    @Resource
    private ApplicationContext context;
    
    @RequestMapping(value = "/bam/statistic.htm")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @MenuEntry(icon = "bam/image/estadistica.gif", menuGroup = "Monitorización Paciente", order = 2, text = "Estadisticas")
    public String getBam(Model model) {
        model.addAttribute("js", Arrays.asList(/*"bam/js/common/ProcessInstancePanel.js", */"bam/js/statistics.js"));
        return "dynamic/main";
    }
    
    @RequestMapping(value = "/bam/statistic/image.do")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public OutputStreamView getProcessStatisticsImage(HttpServletRequest request, String processInstanceId,String start,String end) throws Exception {
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        restTemplate.setRequestFactory(request);
        Map<String,Object> params=new HashMap<String, Object>();
        if (start!=null)
            params.put("start", start);
        if (end!=null)
            params.put("end", end);
        byte[] result = restTemplate.getForBytes(new URL(jbpmServerUrl, "rs/process/statistics/" + processInstanceId + "/number/image"+com.abada.web.util.URL.paramUrlGrid(params)).toString());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(result);
        return new OutputStreamView(out, processInstanceId + "-image.png", "image/png");
    }
}
