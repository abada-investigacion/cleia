/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.bam.controller;

import org.springframework.stereotype.Controller;

/**
 *
 * @author katsu
 */
@Controller
public class FormGateWayController {
//
//    @Autowired
//    private URL jbpmServerUrl;
//    @Resource
//    private ApplicationContext context;
//
//    /**
//     * form of a human task.
//     * @param taskid
//     * @param request
//     * @return
//     * @throws IOException 
//     */
//    @RequestMapping(value = "/{taskid}/render", method = RequestMethod.GET)
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public View renderTask(@PathVariable String taskid, HttpServletRequest request) throws IOException, Exception {        
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//        byte[] result = restTemplate.getForBytes(new URL(jbpmServerUrl, "rs/form/task/"+taskid+"/render").toString());
//        ByteArrayInputStream out = new ByteArrayInputStream(result);        
//        return new InputStreamView(out,"text/html",null);        
//    }
//
//    /**
//     * Complete a human task
//     * @param taskid
//     * @param request
//     * @param model
//     * @return 
//     */
//    @RequestMapping(value = "/{taskid}/complete", method = RequestMethod.POST)
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public View completeTask(HttpServletRequest request,@PathVariable String taskid, Model model) throws Exception {                
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//        byte [] result=restTemplate.post(new URL(jbpmServerUrl, "rs/form/task/"+taskid+"/complete").toString(),com.abada.web.util.URL.parseRequest(request));
//        ByteArrayInputStream out = new ByteArrayInputStream(result);        
//        return new InputStreamView(out,"text/html",null); 
//    }        
//    
//     /**
//     * form of a human task.
//     * @param taskid
//     * @param request
//     * @return
//     * @throws IOException 
//     */
//    @RequestMapping(value = "/{definitionid}/patient/{patientId}/render", method = RequestMethod.GET)
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})    
//    public View renderProcess(@PathVariable String definitionid, @PathVariable Long patientId, HttpServletRequest request) throws IOException, Exception {        
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//        byte[] result = restTemplate.getForBytes(new URL(jbpmServerUrl, "rs/form/process/"+definitionid+"/patient/"+patientId+"/render").toString());
//        ByteArrayInputStream out = new ByteArrayInputStream(result);        
//        return new InputStreamView(out,"text/html",null);        
//    }
//
//    /**
//     * Complete a human task
//     * @param taskid
//     * @param request
//     * @param model
//     * @return 
//     */
//    @RequestMapping(value = "/{definitionid}/patient/{patientId}/complete", method = RequestMethod.POST)
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public View startProcess(@PathVariable String definitionid, @PathVariable Long patientId, HttpServletRequest request, Model model) throws Exception {                
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//        byte [] result=restTemplate.post(new URL(jbpmServerUrl, "rs/form/process/"+definitionid+"/patient/"+patientId+"/complete").toString(),com.abada.web.util.URL.parseRequest(request));
//        ByteArrayInputStream out = new ByteArrayInputStream(result);        
//        return new InputStreamView(out,"text/html",null); 
//    }
//    
//    /**
//     * form of a human task.
//     * @param taskid
//     * @param request
//     * @return
//     * @throws IOException 
//     */
//    @RequestMapping(value = "/{definitionid}/{type}/patient/{patientId}/render", method = RequestMethod.GET)
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})    
//    public View renderProcessType(@PathVariable String definitionid, @PathVariable Long patientId,@PathVariable String type, HttpServletRequest request) throws IOException, Exception {        
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//        byte[] result = restTemplate.getForBytes(new URL(jbpmServerUrl, "rs/form/process/"+definitionid+"/"+type+"/patient/"+patientId+"/render").toString());
//        ByteArrayInputStream out = new ByteArrayInputStream(result);        
//        return new InputStreamView(out,"text/html",null);        
//    }
//
//    /**
//     * Complete a human task
//     * @param taskid
//     * @param request
//     * @param model
//     * @return 
//     */
//    @RequestMapping(value = "/{definitionid}/{type}/patient/{patientId}/complete", method = RequestMethod.POST)
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public View startProcessType(@PathVariable String definitionid, @PathVariable Long patientId, @PathVariable String type,HttpServletRequest request, Model model) throws Exception {                
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//        byte [] result=restTemplate.post(new URL(jbpmServerUrl, "rs/form/process/"+definitionid+"/"+type+"/patient/"+patientId+"/complete").toString(),com.abada.web.util.URL.parseRequest(request));
//        ByteArrayInputStream out = new ByteArrayInputStream(result);        
//        return new InputStreamView(out,"text/html",null); 
//    }
}
