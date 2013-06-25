/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.bam.controller;

import com.abada.bpm.console.client.model.ActiveNodeInfo;
import com.abada.bpm.console.client.model.DiagramInfo;
import com.abada.extjs.ExtjsStore;
import com.abada.extjs.SimpleGroupingComboBoxResponse;
import com.abada.extjs.Success;
import com.abada.jbpm.process.audit.NodeInstanceLogExt;
import com.abada.oggi.bam.controller.wrap.JumpInfo;
import com.abada.springframework.web.client.RestTemplate;
import com.abada.springframework.web.servlet.menu.MenuEntry;
import com.abada.springframework.web.servlet.view.OutputStreamView;
import com.google.gson.reflect.TypeToken;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import org.jboss.bpm.console.client.model.ProcessDefinitionRef;
import org.jboss.bpm.console.client.model.ProcessDefinitionRefWrapper;
import org.jbpm.process.audit.NodeInstanceLog;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author katsu
 */
@Controller
public class BamController{

    @Autowired
    private URL jbpmServerUrl;
    @Resource
    private ApplicationContext context;
    
    
    @RequestMapping(value = "/bam/bam.htm")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @MenuEntry(icon = "bam/image/monitoriza.png", menuGroup = "Monitorización Paciente", order = 0, text = "Monitorizar")
    public String getBam(Model model) {
        model.addAttribute("js", Arrays.asList(/*"bam/js/common/FormCustomFrame.js", "bam/js/common/NewProcessInstancePanel.js", 
                "bam/js/common/OncoguideGrid.js", "bam/js/common/PatientGrid.js", "bam/js/common/ProcessInstancePanel.js", 
                "bam/js/common/ProcessInstanceTabPanel.js", */"bam/js/bam.js"));
        return "dynamic/main";
    }

    @RequestMapping(value = "/bam/instance/tree.do")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public ExtjsStore getInstances(HttpServletRequest request, Long processInstanceId) throws Exception {
        ExtjsStore result = null;
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        restTemplate.setRequestFactory(request);

        Type extjsType = new TypeToken<ExtjsStore<ProcessInstanceLog>>() {
        }.getType();
        result = (ExtjsStore) restTemplate.getForObjectJson(new URL(jbpmServerUrl, "rs/process/instance/" + processInstanceId + "/tree").toString(), extjsType);
        return result;
    }

    @RequestMapping(value = "/bam/instance/image.do")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public OutputStreamView getProcessImage(HttpServletRequest request, Long processInstanceId) throws Exception {
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        restTemplate.setRequestFactory(request);
        byte[] result = restTemplate.getForBytes(new URL(jbpmServerUrl, "rs/process/instance/" + processInstanceId + "/image").toString());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(result);
        return new OutputStreamView(out, processInstanceId + "-image.png", "image/png");
    }
    
    @RequestMapping(value = "/bam/process/image.do")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public OutputStreamView getImage(HttpServletRequest request, String processId) throws Exception {
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        restTemplate.setRequestFactory(request);
        byte[] result = restTemplate.getForBytes(new URL(jbpmServerUrl, "rs/process/definition/" + processId + "/image").toString());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(result);
        return new OutputStreamView(out, processId + "-image.png", "image/png");
    }

    @RequestMapping(value = "/bam/oncoguides.do")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public ExtjsStore<SimpleGroupingComboBoxResponse> getOncoguides(HttpServletRequest request) throws Exception {
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        restTemplate.setRequestFactory(request);
        ProcessDefinitionRefWrapper aux = restTemplate.getForObject(new URL(jbpmServerUrl, "rs/process/definitions").toURI(), ProcessDefinitionRefWrapper.class);
        if (aux != null && aux.getDefinitions() != null && !aux.getDefinitions().isEmpty()) {
            List<SimpleGroupingComboBoxResponse> result = new ArrayList<SimpleGroupingComboBoxResponse>();
            for (ProcessDefinitionRef pdr : aux.getDefinitions()) {
                SimpleGroupingComboBoxResponse cbr = new SimpleGroupingComboBoxResponse();
                cbr.setId(pdr.getId());
                cbr.setValue(pdr.getName());
                cbr.setGroupingField(pdr.getPackageName());
                result.add(cbr);
            }
            ExtjsStore<SimpleGroupingComboBoxResponse> r = new ExtjsStore<SimpleGroupingComboBoxResponse>();
            r.setData(result);
            return r;
        }
        return null;
    }
    
    @RequestMapping(value = "/bam/instance/history.do")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public ExtjsStore getNodeHistoryInstance(HttpServletRequest request, Long processInstanceId) throws Exception {
        ExtjsStore<NodeInstanceLog> result;                
        ExtjsStore<NodeInstanceLogExt> aux;
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        restTemplate.setRequestFactory(request);

        Type extjsType = new TypeToken<ExtjsStore<NodeInstanceLogExt>>() {
        }.getType();
        aux = (ExtjsStore<NodeInstanceLogExt>) restTemplate.getForObjectJson(new URL(jbpmServerUrl, "rs/process/instance/"+processInstanceId+"/history").toString(), extjsType);
        result=new ExtjsStore<NodeInstanceLog>();
        result.setData(new ArrayList<NodeInstanceLog>());
        for (NodeInstanceLogExt nil:aux.getData()){
            if (!nil.getNodeName().equals("Gateway"))
                result.getData().add(nil);
        }
        return result;
    }
    
    @RequestMapping(value = "/bam/instance/data.do")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public OutputStreamView getDataInstance(HttpServletRequest request, Long patientId,Long processInstanceId) throws Exception {        
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        restTemplate.setRequestFactory(request);
        
        byte [] result = restTemplate.getForBytes(new URL(jbpmServerUrl, "rs/process/instance/"+processInstanceId+"/variables/extjs").toString());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(result);
        return new OutputStreamView(out,"", MediaType.APPLICATION_JSON.getType());
    }
    
    @RequestMapping(value = "/bam/process/diagram.do")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public DiagramInfo getDiagramInfo(HttpServletRequest request, String processId) throws Exception {        
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        restTemplate.setRequestFactory(request);
        
        DiagramInfo result = restTemplate.getForObject(new URL(jbpmServerUrl, "rs/process/definition/"+processId+"/diagram").toURI(),DiagramInfo.class);        
        return result;
    }
    
    @RequestMapping(value = "/bam/instance/jump.do")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public Success doJump(HttpServletRequest request, @RequestBody JumpInfo jumpInfo) throws Exception {                
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        restTemplate.setRequestFactory(request);

        Success result = restTemplate.postForObject(new URL(jbpmServerUrl,"rs/process/instance/"+jumpInfo.getProcessInstanceId()+"/jump").toURI(), jumpInfo, Success.class);
        return result;
    }
    
    @RequestMapping(value = "/bam/instance/version.do")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public Success doVersion(HttpServletRequest request, @RequestBody JumpInfo jumpInfo) throws Exception {                
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        restTemplate.setRequestFactory(request);

        Success result = restTemplate.postForObject(new URL(jbpmServerUrl,"rs/process/instance/"+jumpInfo.getProcessInstanceId()+"/"+jumpInfo.getProcessId()+"/version").toURI(), jumpInfo, Success.class);
        return result;
    }
    
    @RequestMapping(value = "/bam/process/tree.do")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public ExtjsStore getProcessTree(HttpServletRequest request, String processId) throws Exception {
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        restTemplate.setRequestFactory(request);
        
        String [] list = restTemplate.getForObject(new URL(jbpmServerUrl, "rs/process/definition/"+processId+"/tree").toString(),String[].class);        
        ExtjsStore result=new ExtjsStore();
        result.setData(Arrays.asList(list));
        return result;
    }    
    
    @RequestMapping(value = "/bam/instance/activeNodes.do")
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public ExtjsStore getProcessTree(HttpServletRequest request, Long processInstanceId) throws Exception {
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        restTemplate.setRequestFactory(request);
        
        ActiveNodeInfo [] ani = restTemplate.getForObject(new URL(jbpmServerUrl, "rs/process/instance/"+processInstanceId+"/allActiveNodeInfo").toString(),ActiveNodeInfo[].class);
        ExtjsStore result=new ExtjsStore();
        result.setData(Arrays.asList(ani));
        return result;
    }            
}
