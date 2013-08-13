/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.bam.controller;

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
