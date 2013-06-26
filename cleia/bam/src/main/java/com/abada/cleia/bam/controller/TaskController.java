/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.bam.controller;

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

//    @Autowired
//    private URL jbpmServerUrl;
//    @Resource
//    private ApplicationContext context;

    @RequestMapping(value = "/bam/task/task.htm")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @MenuEntry(icon = "bam/image/humanTask.png", menuGroup = "Monitorizaci&oacute;n Paciente", order = 0, text = "Tareas")
    public String getTask(Model model) {
        model.addAttribute("js", Arrays.asList("bam/js/task.js"));
        return "dynamic/main";
    }
//    
//    @RequestMapping(value = "/bam/task/user/task.do")
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public ExtjsStore<TaskRef> getAllTasks(HttpServletRequest request) throws Exception {
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//        TaskRefWrapper result2 = restTemplate.getForObject(new URL(jbpmServerUrl, "rs/tasks/"+request.getRemoteUser()+ "/participation").toString(),TaskRefWrapper.class);        
//        ExtjsStore<TaskRef> result=new ExtjsStore<TaskRef>();
//        result.setData(result2.getTasks());
//        return result;
//    }
//    
//    @RequestMapping(value = "bam/task/taskForPatient.do")
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public ExtjsStore<TaskRef> getAllTasksForPatient(HttpServletRequest request,Long patientId) throws Exception {
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//        TaskRefWrapper result2 = restTemplate.getForObject(new URL(jbpmServerUrl, "rs/tasks/patient/"+patientId+"/user/"+request.getRemoteUser()).toString(),TaskRefWrapper.class);
//        ExtjsStore<TaskRef> result=new ExtjsStore<TaskRef>();
//        result.setData(result2.getTasks());
//        return result;
//    }    
//
//    @RequestMapping(value = "/bam/task/task.do")
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public ExtjsStore<TaskRef> getTasks(HttpServletRequest request, Long processInstanceId) throws Exception {
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//        
//        if (processInstanceId!=null) {
//
//            TaskRefWrapper result1 = restTemplate.getForObject(new URL(jbpmServerUrl, "rs/tasks/process/"+processInstanceId+"/user/" + request.getRemoteUser()).toString(), TaskRefWrapper.class);
//            ExtjsStore<TaskRef> result = new ExtjsStore<TaskRef>();
//            result.setData(result1.getTasks());
//            return result;
//        }
//        return null;
//    }
}
