/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.process.audit;

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

import com.abada.json.Json;
import com.abada.json.JsonFactory;
import com.abada.json.JsonType;
import java.io.Serializable;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.process.audit.VariableInstanceLog;

/**
 *
 * @author katsu
 */
//@Table(name="VariableInstanceLog")
@Entity
@AttributeOverride(column=@Column(name="valueJson"),name="value")
public class VariableInstanceLogExt extends VariableInstanceLog implements Serializable {
    private static final Log logger=LogFactory.getLog(VariableInstanceLogExt.class);
    private static final Json json = JsonFactory.getInstance().getInstance(JsonType.DEFAULT);
    @Column(nullable = false)
    @Lob    
    private String valueJson;
    @Column(nullable = false)
    private String valueType;

    public VariableInstanceLogExt() {
        super(Long.MIN_VALUE, null, null, null, null);
    }
    
    public VariableInstanceLogExt(long processInstanceId, String processId, String variableInstanceId, String variableId, Object value) {
        super(processInstanceId, processId, variableInstanceId, variableId, value.toString());

        this.setValueJson(toJson(value));
        this.setValueType(value.getClass().getName());
    }

    public String getValueJson() {
        return valueJson;
    }

    public void setValueJson(String valueJson) {
        this.valueJson = valueJson;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    private String toJson(Object value) {
        try {
            String result = json.serialize(value);
            return result;
        } catch (Throwable e) {
            logger.error(e);
        }
        return value.toString();
    }
}
