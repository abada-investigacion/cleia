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
public class PatientController{
//
//    @Autowired
//    private URL jbpmServerUrl;
//    @Resource
//    private ApplicationContext context;
//
//    @RequestMapping(value = "/bam/patient/patient.do")
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public ExtjsStore<Patient> getPatient(HttpServletRequest request) throws Exception {
//        Map<String, Object> params = com.abada.web.util.URL.parseRequest(request);
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//        Type extjsType = new TypeToken<ExtjsStore<Patient>>() {
//        }.getType();
//        ExtjsStore<Patient> result = (ExtjsStore<Patient>) restTemplate.getForObjectJson(new URL(jbpmServerUrl, "rs/identity/search/patient").toString() + com.abada.web.util.URL.paramUrlGrid(params), extjsType);
//        return result;
//    }
//
//    @RequestMapping(value = "/bam/patient/pinstances.do")
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public ExtjsStore<PatientPInstance> getOncoguide(HttpServletRequest request, Long patientId) throws Exception {
//        Map<String, Object> params = com.abada.web.util.URL.parseRequest(request);
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//        Type extjsType = new TypeToken<ExtjsStore<PatientPInstance>>() {
//        }.getType();
//        ExtjsStore<PatientPInstance> result = (ExtjsStore<PatientPInstance>) restTemplate.getForObjectJson(new URL(jbpmServerUrl, "rs/identity/patient/" + patientId + "/pinstances").toString() + com.abada.web.util.URL.paramUrlGrid(params), extjsType);
//        return result;
//    }
//
//    @RequestMapping(value = "/bam/patient/newoncoguide.do")
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    public Success newOncoguide(HttpServletRequest request, Long patientId, Long processInstanceId) throws Exception {
//        Success result = new Success();
//        RestTemplate restTemplate = context.getBean(RestTemplate.class);
//        restTemplate.setRequestFactory(request);
//        try {
//            restTemplate.post(new URL(jbpmServerUrl, "rs/identity/patient/" + patientId + "/oncoguide/" + processInstanceId).toString(), null);
//            result.setSuccess(Boolean.TRUE);
//        } catch (Exception e) {
//            result.setSuccess(Boolean.FALSE);
//            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
//        }
//        return result;
//    }
}
