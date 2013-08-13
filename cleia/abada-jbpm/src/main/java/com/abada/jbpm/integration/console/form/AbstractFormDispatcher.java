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

import com.abada.jbpm.integration.console.URLUtils;
import com.abada.jbpm.integration.guvnor.GuvnorUtils;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import org.jboss.bpm.console.server.plugin.FormAuthorityRef;
import org.jboss.bpm.console.server.plugin.FormDispatcherPlugin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.bpm.console.server.integration.ProcessManagement;

/**
 * Sustituye {@link org.jbpm.integration.console.forms.AbstractFormDispatcher}
 * jbpm 5.4.0.Final compliant
 *
 * @author Kris Verlaenen
 * @author katsu
 */
public abstract class AbstractFormDispatcher implements FormDispatcherPlugin {

    private static final Log logger = LogFactory.getLog(AbstractFormDispatcher.class);
    private ProcessManagement processManagement;
    private GuvnorUtils guvnorUtils;

    public GuvnorUtils getGuvnorUtils() {
        return guvnorUtils;
    }

    public void setGuvnorUtils(GuvnorUtils guvnorUtils) {
        this.guvnorUtils = guvnorUtils;
    }

    public ProcessManagement getProcessManagement() {
        return processManagement;
    }

    public void setProcessManagement(ProcessManagement processManagement) {
        this.processManagement = processManagement;
    }

    public URL getDispatchUrl(FormAuthorityRef ref) {
        StringBuilder sb = guvnorUtils.getUrlUtils().getJBPMServerURL();
        sb.append("/rs/form/").append(getType(ref)).append("/");
        sb.append(ref.getReferenceId());
        sb.append("/render");

        try {
            return new URL(sb.toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to resolve form dispatch url", e);
        }
    }

    protected String getType(FormAuthorityRef ref) {
        FormAuthorityRef.Type type = ref.getType();
        if (type.equals(FormAuthorityRef.Type.TASK)) {
            return "task";
        }
        if (type.equals(FormAuthorityRef.Type.PROCESS)) {
            return "process";
        }
        throw new IllegalArgumentException(
                "Unknown form authority type: " + ref.getType());
    }

    public InputStream getTemplate(String name) {
        InputStream result = AbstractFormDispatcher.class.getResourceAsStream("/" + name + ".ftl");
        if (result == null) {
            result = guvnorUtils.getTemplate(name);
            if (result == null) {
                result = guvnorUtils.getTemplate(name + "-taskform");
            }
        }
        return result;
    }

    protected DataHandler processTemplate(final String name, InputStream src, Map<String, Object> renderContext) {
        DataHandler merged = null;
        try {
            freemarker.template.Configuration cfg = new freemarker.template.Configuration();
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            cfg.setTemplateUpdateDelay(0);
            Template temp = new Template(name, new InputStreamReader(src), cfg);
            final ByteArrayOutputStream bout = new ByteArrayOutputStream();
            Writer out = new OutputStreamWriter(bout);
            temp.process(renderContext, out);
            out.flush();
            merged = new DataHandler(new DataSource() {
                public InputStream getInputStream() throws IOException {
                    return new ByteArrayInputStream(bout.toByteArray());
                }

                public OutputStream getOutputStream() throws IOException {
                    return bout;
                }

                public String getContentType() {
                    return "*/*";
                }

                public String getName() {
                    return name + "_DataSource";
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to process form template", e);
        }
        return merged;
    }
}
