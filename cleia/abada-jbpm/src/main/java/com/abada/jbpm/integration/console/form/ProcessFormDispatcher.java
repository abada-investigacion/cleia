/**
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.abada.jbpm.integration.console.form;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;

import org.jboss.bpm.console.server.plugin.FormAuthorityRef;

/**
 *
 * Sustituye {@link org.jbpm.integration.console.forms.ProcessFormDispatcher}
 * jbpm 5.4.0.Final compliant
 *
 * @author katsu
 */
public class ProcessFormDispatcher extends AbstractFormDispatcher {

    @Override
    public URL getDispatchUrl(FormAuthorityRef ref) {
        InputStream template = getTemplate(ref.getReferenceId());
        if (template == null) {
            return null;
        }
        return super.getDispatchUrl(ref);
    }

    public DataHandler provideForm(FormAuthorityRef ref) {
        InputStream template = getTemplate(ref.getReferenceId());
        Map<String, Object> map = new HashMap<String, Object>();
        if (template == null) {
            template = ProcessFormDispatcher.class.getResourceAsStream("/Default.ftl");
            map.put("template_name", ref.getReferenceId());
        }
        if (ref instanceof com.abada.bpm.console.server.plugin.FormAuthorityRef) {
            map.put("WEB_BROWSER_DEVICE", ((com.abada.bpm.console.server.plugin.FormAuthorityRef) ref).getDevice());
        }
        return processTemplate(ref.getReferenceId(), template, map);
    }
}
