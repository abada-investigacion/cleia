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
public class BamController{        
    
    @RequestMapping(value = "/bam/bam.htm")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @MenuEntry(icon = "bam/image/monitoriza.png", menuGroup = "Monitorizaci&oacute;n Paciente", order = 0, text = "Monitorizar")
    public String getBam(Model model) {
        model.addAttribute("js", Arrays.asList("bam/js/bam.js"));
        return "dynamic/main";
    }

//    @RequestMapping(value = "/bam/instance/tree.do")
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public ExtjsStore getInstances(HttpServletRequest request, Long processInstanceId) throws Exception {
//        ExtjsStore result = null;
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//
//        Type extjsType = new TypeToken<ExtjsStore<ProcessInstanceLog>>() {
//        }.getType();
//        result = (ExtjsStore) restTemplate.getForObjectJson(new URL(jbpmServerUrl, "rs/process/instance/" + processInstanceId + "/tree").toString(), extjsType);
//        return result;
//    }
//
//    @RequestMapping(value = "/bam/instance/image.do")
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public OutputStreamView getProcessImage(HttpServletRequest request, Long processInstanceId) throws Exception {
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//        byte[] result = restTemplate.getForBytes(new URL(jbpmServerUrl, "rs/process/instance/" + processInstanceId + "/image").toString());
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        out.write(result);
//        return new OutputStreamView(out, processInstanceId + "-image.png", "image/png");
//    }
//    
//    @RequestMapping(value = "/bam/process/image.do")
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public OutputStreamView getImage(HttpServletRequest request, String processId) throws Exception {
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//        byte[] result = restTemplate.getForBytes(new URL(jbpmServerUrl, "rs/process/definition/" + processId + "/image").toString());
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        out.write(result);
//        return new OutputStreamView(out, processId + "-image.png", "image/png");
//    }
//
//    @RequestMapping(value = "/bam/oncoguides.do")
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public ExtjsStore<SimpleGroupingComboBoxResponse> getOncoguides(HttpServletRequest request) throws Exception {
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//        ProcessDefinitionRefWrapper aux = restTemplate.getForObject(new URL(jbpmServerUrl, "rs/process/definitions").toURI(), ProcessDefinitionRefWrapper.class);
//        if (aux != null && aux.getDefinitions() != null && !aux.getDefinitions().isEmpty()) {
//            List<SimpleGroupingComboBoxResponse> result = new ArrayList<SimpleGroupingComboBoxResponse>();
//            for (ProcessDefinitionRef pdr : aux.getDefinitions()) {
//                SimpleGroupingComboBoxResponse cbr = new SimpleGroupingComboBoxResponse();
//                cbr.setId(pdr.getId());
//                cbr.setValue(pdr.getName());
//                cbr.setGroupingField(pdr.getPackageName());
//                result.add(cbr);
//            }
//            ExtjsStore<SimpleGroupingComboBoxResponse> r = new ExtjsStore<SimpleGroupingComboBoxResponse>();
//            r.setData(result);
//            return r;
//        }
//        return null;
//    }
//    
//    @RequestMapping(value = "/bam/instance/history.do")
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public ExtjsStore getNodeHistoryInstance(HttpServletRequest request, Long processInstanceId) throws Exception {
//        ExtjsStore<NodeInstanceLog> result;                
//        ExtjsStore<NodeInstanceLogExt> aux;
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//
//        Type extjsType = new TypeToken<ExtjsStore<NodeInstanceLogExt>>() {
//        }.getType();
//        aux = (ExtjsStore<NodeInstanceLogExt>) restTemplate.getForObjectJson(new URL(jbpmServerUrl, "rs/process/instance/"+processInstanceId+"/history").toString(), extjsType);
//        result=new ExtjsStore<NodeInstanceLog>();
//        result.setData(new ArrayList<NodeInstanceLog>());
//        for (NodeInstanceLogExt nil:aux.getData()){
//            if (!nil.getNodeName().equals("Gateway"))
//                result.getData().add(nil);
//        }
//        return result;
//    }
//    
//    @RequestMapping(value = "/bam/instance/data.do")
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public OutputStreamView getDataInstance(HttpServletRequest request, Long patientId,Long processInstanceId) throws Exception {        
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//        
//        byte [] result = restTemplate.getForBytes(new URL(jbpmServerUrl, "rs/process/instance/"+processInstanceId+"/variables/extjs").toString());
//        
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        out.write(result);
//        return new OutputStreamView(out,"", MediaType.APPLICATION_JSON.getType());
//    }
//    
//    @RequestMapping(value = "/bam/process/diagram.do")
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public DiagramInfo getDiagramInfo(HttpServletRequest request, String processId) throws Exception {        
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//        
//        DiagramInfo result = restTemplate.getForObject(new URL(jbpmServerUrl, "rs/process/definition/"+processId+"/diagram").toURI(),DiagramInfo.class);        
//        return result;
//    }
//    
//    @RequestMapping(value = "/bam/instance/jump.do")
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public Success doJump(HttpServletRequest request, @RequestBody JumpInfo jumpInfo) throws Exception {                
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//
//        Success result = restTemplate.postForObject(new URL(jbpmServerUrl,"rs/process/instance/"+jumpInfo.getProcessInstanceId()+"/jump").toURI(), jumpInfo, Success.class);
//        return result;
//    }
//    
//    @RequestMapping(value = "/bam/instance/version.do")
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public Success doVersion(HttpServletRequest request, @RequestBody JumpInfo jumpInfo) throws Exception {                
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//
//        Success result = restTemplate.postForObject(new URL(jbpmServerUrl,"rs/process/instance/"+jumpInfo.getProcessInstanceId()+"/"+jumpInfo.getProcessId()+"/version").toURI(), jumpInfo, Success.class);
//        return result;
//    }
//    
//    @RequestMapping(value = "/bam/process/tree.do")
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public ExtjsStore getProcessTree(HttpServletRequest request, String processId) throws Exception {
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//        
//        String [] list = restTemplate.getForObject(new URL(jbpmServerUrl, "rs/process/definition/"+processId+"/tree").toString(),String[].class);        
//        ExtjsStore result=new ExtjsStore();
//        result.setData(Arrays.asList(list));
//        return result;
//    }    
//    
//    @RequestMapping(value = "/bam/instance/activeNodes.do")
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public ExtjsStore getProcessTree(HttpServletRequest request, Long processInstanceId) throws Exception {
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//        
//        ActiveNodeInfo [] ani = restTemplate.getForObject(new URL(jbpmServerUrl, "rs/process/instance/"+processInstanceId+"/allActiveNodeInfo").toString(),ActiveNodeInfo[].class);
//        ExtjsStore result=new ExtjsStore();
//        result.setData(Arrays.asList(ani));
//        return result;
//    }            
}
