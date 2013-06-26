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
public class DmsBamController {
//    @Autowired
//    private URL jbpmServerUrl;
//    @Resource
//    private ApplicationContext context;
//    
//    @RequestMapping(value = "/bam/dms/documentsnode.do")
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public ExtjsStore<DocumentNode> getDocumentNodesByProcess(HttpServletRequest request, String processId,Long nodeId) throws Exception {  
//        ExtjsStore<DocumentNode> result,result1;
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//
//        Type extjsType = new TypeToken<ExtjsStore<DocumentNode>>() {
//        }.getType();
//        result1 = (ExtjsStore<DocumentNode>)restTemplate.getForObjectJson(new URL(jbpmServerUrl, "rs/dms/documentnode/"+processId+"/"+nodeId+"/searchByNode").toString(),extjsType);
//        result = (ExtjsStore<DocumentNode>)restTemplate.getForObjectJson(new URL(jbpmServerUrl, "rs/dms/documentnode/"+processId+"/searchByProcess").toString(),extjsType);
//        result.getData().addAll(result1.getData());
//        result.setTotal(result.getData().size());
//        return result;
//    }
//    
//    @RequestMapping(value = "/bam/dms/process/instance/documentsnode.do")
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public ExtjsStore<DocumentNode> getDocumentNodesByProcessInstance(HttpServletRequest request, Long processInstanceId) throws Exception {  
//        ExtjsStore<DocumentNode> result=new ExtjsStore<DocumentNode>();
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//
//        Type extjsType = new TypeToken<ExtjsStore<DocumentNode>>() {
//        }.getType();
//        result = (ExtjsStore<DocumentNode>)restTemplate.getForObjectJson(new URL(jbpmServerUrl, "rs/dms/documentnode/"+processInstanceId+"/searchByOncoguide").toString(),extjsType);
//        return result;
//    }
//    
//    @RequestMapping(value = "/bam/dms/file/read.do", method = RequestMethod.GET)
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})        
//    public FileView read(HttpServletRequest request,String uuid,String fileName) throws Exception {
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);                
//        restTemplate.setRequestFactory(request);
//
//        File result=restTemplate.getForFile(new URL(jbpmServerUrl, "rs/dms/file/"+uuid+"/read").toString());        
//        FileView view =new FileView(result,fileName);        
//        /*InputStreamView result = new InputStreamView(fis.getIs(), fis.getType(), null);
//        result.addHeaderPropertyName(fis.getName());*/
//        return view;
//    }
    
    /*@RequestMapping(value = "/bam/dms/file/save.do", method = RequestMethod.POST)
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public OutputStreamView save(HttpServletRequest request,@RequestParam(required = false) String processId, @RequestParam(required = false) String nodeId, 
    @RequestParam(required = false) Long oncoguideId, @RequestParam("file") MultipartFile file, Model model) throws FileNotFoundException, IOException, Exception{         
        File temp=new File(System.getProperty("java.io.tmpdir")+"/"+UUID.randomUUID().toString());
        if (temp.exists()) temp.delete();
        
        FileOutputStream fo=new FileOutputStream(temp);
        fo.write(file.getBytes());
        fo.close();
        
        Map<String,Object> parts=new HashMap<String, Object>();
        if (processId!=null) parts.put("processId", processId);
        if (nodeId!=null) parts.put("nodeId", nodeId);
        if (oncoguideId!=null) parts.put("oncoguideId", oncoguideId);
        parts.put("fileName", file.getOriginalFilename());        
        parts.put("file", temp);        
        
        RestTemplate restTemplate = context.getBean(RestTemplate.class);                
        restTemplate.setRequestFactory(request);
        
        String result=restTemplate.postMultipart(new URL(jbpmServerUrl, "rs/dms/file/save").toString(), parts);
        
        if (temp.exists()) temp.delete();
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(result.getBytes());
        return new OutputStreamView(out, "text/html",new HashMap<String, String>());
    }*/
}
