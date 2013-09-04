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
/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.require([
    'Ext.form.Panel', 'Ext.form.field.Checkbox', 'Abada.Ajax', 'Ext.JSON', 'Ext.Ajax',
    'Abada.toolbar.ToolbarInsertUpdateDelete', 'App.manager.js.common.gridgroupexpander','App.patient.js.common.gridPatient'])
Ext.onReady(function() {


    var toolbar=Ext.create('Abada.toolbar.ToolbarInsertUpdateDelete', {
        listeners: {
            submitInsert: function() {
                handleFormulario('Inserta', groupGrid, 'Servicio', getRelativeServerURI('rs/group'), groupGrid.selModel);
            },
            /* submitUpdate: function() {
                if (groupGrid.selModel.hasSelection()) {
                    if (groupGrid.selModel.getCount() == 1) {
                        handleFormulario('Modifica', groupGrid, 'Servicio', getRelativeServerURI('rs/group/{idgroup}', {
                            idgroup:groupGrid.selModel.getLastSelected().get('value')
                        }), groupGrid.selModel);
                    } else {
                        Ext.Msg.alert('', 'Seleccione un servicio');
                    }
                } else
                    Ext.Msg.alert('', 'Seleccione un servicio');
            },*/
            submitDelete: function() {
                if (groupGrid.selModel.hasSelection()) {
                    var form = {
                        value: groupGrid.selModel.getLastSelected().get('value'),
                        enabled: !groupGrid.selModel.getLastSelected().get('enabled')
                    }
                   
                    var status = 'habilita';
                 
                    if (groupGrid.selModel.getLastSelected().get('enabled')) {
                        status = 'deshabilita'
                    } 
                                 
                    doAjaxrequestJson(getRelativeServerURI('rs/group/{idgroup}/{enable}', {
                        idgroup:form.value,
                        enable:form.enabled
                    }), form, 'PUT', groupGrid, null, 'Servicio '+status+'do', 'Error. No se ha podido ' + status + 'r');
                    
                    groupGrid.selModel.deselectAll();
              
                } else
                    Ext.Msg.alert('', 'Seleccione un servicio');
            }
        }

    });
    
    toolbar.getComponent("Borrar").setText("Habilitar/Deshabilitar");    
    
    toolbar.remove('modificar');
    
    toolbar.add({
        xtype: 'button',
        id: 'assginPatients',
        text: 'Asignar Pacientes',
        handler: function() {
            if (groupGrid.selModel.hasSelection()) {
                if (groupGrid.selModel.getCount() == 1) {
                    assignPatient(groupGrid.selModel);
                } else {
                    Ext.Msg.alert('', 'Seleccione un Servicio');
                }
            } else
                Ext.Msg.alert('', 'Seleccione un Servicio');
        }
    });
    
    var groupGrid = Ext.create('App.manager.js.common.gridgroup', {
        url: getRelativeServerURI('rs/group/search'),
        width: 350,
        height: 400,
        padding: '5 5 5 5',        
        page: 14
    });

    groupGrid.getStore().load({
        params: {
            start: 0,
            limit: 14
        }
    });

    var panel = Ext.create('Ext.panel.Panel', {
        autoWidth: true,
        autoHeight: true,
        title: '',
        items: [toolbar, groupGrid]//ponemos el grid

    });
    setCentralPanel(panel);


    //Funcion para los frompanel
    function getO(form, selection) {
        
        var value = form.getComponent("value").getValue();
        
        if (value == "") {
            value = null;
        }
        
        var o = {
            value: value,
            enabled:true
        };
        
        return o;
    }

    function handleFormulario(opt, grid, title, url, selection) {
        
        var value, method = 'POST', tooltip = 'Insertar usuario'
        
        
        if (opt != 'Inserta' && selection.hasSelection()) {
         
            method = 'PUT';
            value = selection.getLastSelected().get('value');
            tooltip = 'Modificar Servicio';
            
        }
        
        //form panel de insertar
        var formpanel = Ext.create('Ext.form.Panel', {
            url: url,
            defaultType: 'textfield',
            monitorValid: true,
            items: [                
            {
                fieldLabel: 'Servicio',
                name: 'value',
                id: 'value',
                value: value,
                padding: '10 5 5 5',
                allowBlank: false
            }
            ],
            buttons: [{
                text: opt + 'r',
                id: 'formuser',
                formBind: true,
                handler: function() {
                    if (formpanel.getForm().isValid()) {
                       
                        doAjaxrequestJson(url, getO(formpanel), method, groupGrid, wind,'Servicio '+ opt + 'do', 'Error no se ha podido ' + opt + 'r');
                        
                    }

                },
                tooltip: tooltip
            }]
        });

        
        var wind = Ext.create('Ext.window.Window', {
            title: opt + 'r',
            id: 'groupwind',
            autoScroll: false,
            closable: true,
            modal: true,
            items: [formpanel]
        });

        wind.show();

        return formpanel;
    }

    function assignPatient(selection) {
        
        var patientsGrid = Ext.create('App.patient.js.common.gridPatient', {
            title:'',
            url: getRelativeServerURI('rs/patient/searchforassignment'),
            height:400,
            width:800,
            checkboxse: true,
            padding: '10 5 5 5',
            page:25,
            listeners: { 
                afterrender: function() {   
                    
                    for(var i=0;i<patientsGrid.columns.length;i++){
                        
                        if(patientsGrid.columns[i].dataIndex=='genre' || patientsGrid.columns[i].dataIndex=='tlf'
                            || patientsGrid.columns[i].dataIndex=='enabled' || patientsGrid.columns[i].text=='Direci&oacute;n'){
                            patientsGrid.columns[i].hide()                            
                        }
                        
                    }
                        
                    patientsGrid.headerCt.insert(patientsGrid.columns.length, Ext.create('Ext.grid.column.Column',{
                        header:'Identificador',
                        text:'identificador',
                        renderer:renderIdentifier(new Ext.Template('{ids}')) ,
                        width:40,
                        hidden:false
                    })
                    );
                    
                    patientsGrid.getView().refresh();
                    
                
                    
                },                            
                select:function(constructor,record){     
                    
                    doAjaxAssign('add',record);
                 
                },
                deselect:function(constructor, record){  
                                           
                    doAjaxAssign('remove',record);
                  
                }
            }
        });      
        
        function renderIdentifier(template) {
            return function(value, meta, record, rowIndex, colIndex, store) {
                
                var ids=record.data.ids;
                
                if(ids.length>0){
                    
                    for(var i=0;i<ids.length;i++){
                    
                        if(ids[i].type.value.toUpperCase()=='DNI'){
                         
                            return ids[i].type.value.toUpperCase() +': '+ids[i].value;
                         
                        }
                    
                    }               
                              
                    return ids[0].type.value.toUpperCase() +': '+ids[0].value;
                }else{
                    return '';
                }
            };
        }
        
        function doAjaxAssign(operation,record){
            
            var url=getRelativeServerURI('rs/group/{idgroup}/{operation}/{iduser}',{
                idgroup:selection.getSelection()[0].get('value'),
                operation:operation,
                iduser:record.data.id                                            
            });
            
            
            Abada.Ajax.requestJson({
                url: url,
                scope: this,
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                failure: function(error) {
                    if (error && error.reason) {
                        Ext.Msg.alert('Error', error.reason);
                    } else
                        Ext.Msg.alert('', 'Error');
                },
                success: function() {
                    
                }
            });
                                   
        }       
        
         
        patientsGrid.getStore().on('load',function(constructor, records){
            
            var groups;
            var exit;
            
            for(var i=0;i<records.length; i++){
                
                exit=false;
                groups=records[i].data.groups;
                
                for(var z=0;z<groups.length && !exit;z++){
                  
                    if(groups[z].value==selection.getSelection()[0].get('value')){
                       
                        patientsGrid.selModel.select(records[i], true, true);
                       
                        exit=true;
                    }
                    
                }
                
                
            }
            
            
            
        });

        patientsGrid.getStore().load({
            params: {
                start: 0,
                limit: 25
            }
        });

       
       
        var assignForm = Ext.create('Ext.form.Panel', {
            monitorValid: true,
            frame: false,
            autoScroll: true,
            items: [patientsGrid]            
        });
       
        
        var wind = Ext.create('Ext.window.Window', {
            title: 'Asignar Pacientes',
            id: 'assignPatientWindow',
            closable: true,
            modal: true,
            items: [assignForm]
        });

        wind.show();

        return assignForm;
        
    }
});
