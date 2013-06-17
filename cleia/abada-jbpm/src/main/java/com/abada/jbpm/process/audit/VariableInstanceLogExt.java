/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.process.audit;

import com.abada.gson.GsonImpl;
import com.abada.json.Json;
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
    private static final Json json = new GsonImpl();
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
