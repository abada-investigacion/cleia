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
public class SignalController{
//
//    @Autowired
//    private URL jbpmServerUrl;
//    @Resource
//    private ApplicationContext context;
//
//    @RequestMapping(value = "/bam/signal/nodes.do")
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public ExtjsStore getEventNodesInstances(HttpServletRequest request, String processId) throws Exception {
//        ExtjsStore result = new ExtjsStore();
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//
//        Type resultType = new TypeToken<List<EventNode>>() {
//        }.getType();
//        result.setData((List<EventNode>) restTemplate.getForObjectJson(new URL(jbpmServerUrl, "rs/process/definition/" + processId + "/eventnodes").toString(), resultType));
//        return result;
//    }
//
//    @RequestMapping(value = "/bam/signal/signal.do")
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public Success doSignal(HttpServletRequest request, Long processInstanceId,String type) throws Exception {                
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);        
//        
//        Map<String,Object> params=com.abada.web.util.URL.parseRequest(request);
//        params.remove("processInstanceId");        
//        byte [] result=restTemplate.post(new URL(jbpmServerUrl,"rs/process/instance/tokens/"+processInstanceId+"/transition/oggi").toString(), params);        
//        return new Success(Boolean.TRUE);
//    }
}
