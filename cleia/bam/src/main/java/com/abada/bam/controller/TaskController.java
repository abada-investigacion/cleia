/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.bam.controller;

import com.abada.extjs.ExtjsStore;
import com.abada.springframework.web.client.RestTemplate;
import com.abada.springframework.web.servlet.menu.MenuEntry;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import org.jboss.bpm.console.client.model.TaskRef;
import org.jboss.bpm.console.client.model.TaskRefWrapper;
import org.jbpm.process.audit.ProcessInstanceLog;
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
public class TaskController {

    @Autowired
    private URL jbpmServerUrl;
    @Resource
    private ApplicationContext context;

    @RequestMapping(value = "/bam/task/task.htm")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @MenuEntry(icon = "bam/image/humanTask.png", menuGroup = "Monitorización Paciente", order = 0, text = "Tareas")
    public String getTask(Model model) {
        model.addAttribute("js", Arrays.asList(/*"bam/js/common/TaskGrid.js","bam/js/common/FormCustomFrame.js",
                "bam/js/common/FormCustomPanel.js",*/"bam/js/task.js"));
        return "dynamic/main";
    }
    
    @RequestMapping(value = "/bam/task/user/task.do")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public ExtjsStore<TaskRef> getAllTasks(HttpServletRequest request) throws Exception {
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        restTemplate.setRequestFactory(request);
        TaskRefWrapper result2 = restTemplate.getForObject(new URL(jbpmServerUrl, "rs/tasks/"+request.getRemoteUser()+ "/participation").toString(),TaskRefWrapper.class);        
        ExtjsStore<TaskRef> result=new ExtjsStore<TaskRef>();
        result.setData(result2.getTasks());
        return result;
    }
    
    @RequestMapping(value = "bam/task/taskForPatient.do")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public ExtjsStore<TaskRef> getAllTasksForPatient(HttpServletRequest request,Long patientId) throws Exception {
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        restTemplate.setRequestFactory(request);
        TaskRefWrapper result2 = restTemplate.getForObject(new URL(jbpmServerUrl, "rs/tasks/patient/"+patientId+"/user/"+request.getRemoteUser()).toString(),TaskRefWrapper.class);
        ExtjsStore<TaskRef> result=new ExtjsStore<TaskRef>();
        result.setData(result2.getTasks());
        return result;
    }    

    @RequestMapping(value = "/bam/task/task.do")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public ExtjsStore<TaskRef> getTasks(HttpServletRequest request, Long processInstanceId) throws Exception {
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        restTemplate.setRequestFactory(request);
        
        if (processInstanceId!=null) {

            TaskRefWrapper result1 = restTemplate.getForObject(new URL(jbpmServerUrl, "rs/tasks/process/"+processInstanceId+"/user/" + request.getRemoteUser()).toString(), TaskRefWrapper.class);
            ExtjsStore<TaskRef> result = new ExtjsStore<TaskRef>();
            result.setData(result1.getTasks());
            return result;
        }
        return null;
    }
}
