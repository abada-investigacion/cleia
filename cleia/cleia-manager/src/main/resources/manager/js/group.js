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
    'Abada.toolbar.ToolbarInsertUpdateDelete','App.patient.js.common.gridPatient'])

Ext.onReady(function() {
    var i18n = Ext.create('Abada.i18n.Bundle', {
        path: getRelativeURI('/manager/locale'),
        bundle: 'messages',
        insertLocale: false
    });

    i18n.on('error', function() {
        i18n.language = i18n.defaultLanguage;
        i18n.load();
    });

    i18n.on('loaded', function() {


        var toolbar=Ext.create('Abada.toolbar.ToolbarInsertUpdateDelete', {
            listeners: {
                submitInsert: function() {
                    handleFormulario('insert', groupGrid, '', getRelativeServerURI('rs/group'), groupGrid.selModel);
                },
                submitDelete: function() {
                    if (groupGrid.selModel.hasSelection()) {
                        var form = {
                            value: groupGrid.selModel.getLastSelected().get('value'),
                            enabled: !groupGrid.selModel.getLastSelected().get('enabled')
                        }
                                                    
                        doAjaxrequestJson(getRelativeServerURI('rs/group/{idgroup}/{enable}', {
                            idgroup:form.value,
                            enable:form.enabled
                        }), form, 'PUT', groupGrid, null, i18n.getMsg('manager.operationSucess'), i18n.getMsg('manager.operationError'));
                    
                        groupGrid.selModel.deselectAll();
              
                    } else
                        Ext.Msg.alert('', i18n.getMsg('manager.selectGroup'));
                }
            }

        });
    
        toolbar.getComponent("Borrar").setText(i18n.getMsg('manager.toolbar.enable/disable'));    
    
        toolbar.remove('modificar');
    
        toolbar.add({
            xtype: 'button',
            id: 'assginPatients',
            text: i18n.getMsg('manager.toolbar.assignPatients'),
            handler: function() {
                if (groupGrid.selModel.hasSelection()) {
                    if (groupGrid.selModel.getCount() == 1) {
                        assignPatient(groupGrid.selModel);
                    } else {
                        Ext.Msg.alert('', i18n.getMsg('manager.selectGroup'));
                    }
                } else
                    Ext.Msg.alert('', i18n.getMsg('manager.selectGroup'));
            }
        });
    
        var groupGrid = Ext.create('App.manager.js.common.gridgroup', {
            title:i18n.getMsg('manager.grid.groupGridTitle'),
            url: getRelativeServerURI('rs/group/search'),
            width: 350,
            height: 400,
            padding: '5 5 5 5',        
            page: 14,
            i18n:i18n
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
        
            var value, method = 'POST'
        
        
            if (opt != 'insert' && selection.hasSelection()) {
         
                method = 'PUT';
                value = selection.getLastSelected().get('value');
                          
            }
        
            var formpanel = Ext.create('Ext.form.Panel', {
                url: url,
                defaultType: 'textfield',
                monitorValid: true,
                items: [                
                {
                    fieldLabel: i18n.getMsg('manager.form.group'),
                    name: 'value',
                    id: 'value',
                    value: value,
                    padding: '10 5 5 5',
                    allowBlank: false
                }
                ],
                buttons: [{
                    text: i18n.getMsg('manager.send'),
                    id: 'formuser',
                    formBind: true,
                    handler: function() {
                        if (formpanel.getForm().isValid()) {
                       
                            doAjaxrequestJson(url, getO(formpanel), method, groupGrid, wind,i18n.getMsg('manager.operationSucess'), i18n.getMsg('manager.operationError'));
                        
                        }

                    }
                }]
            });

            var windTitle;
            
            if(opt=='insert'){
                windTitle=i18n.getMsg('manager.wind.insertGroupTitle');
            }else{
                windTitle=i18n.getMsg('manager.wind.updateGroupTitle');
            }
            
            var wind = Ext.create('Ext.window.Window', {
                title: windTitle,
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
                i18n:i18n,
                listeners: { 
                    afterrender: function() {   
                    
                         for(var i=0;i<patientsGrid.columns.length;i++){
                        
                            if(patientsGrid.columns[i].dataIndex=='genre' || patientsGrid.columns[i].dataIndex=='tlf'
                                || patientsGrid.columns[i].dataIndex=='enabled' || patientsGrid.columns[i].dataIndex=='address'){
                                patientsGrid.columns[i].hide()                            
                            }
                        
                        }
                        
                        patientsGrid.headerCt.insert(patientsGrid.columns.length, Ext.create('Ext.grid.column.Column',{
                            header:i18n.getMsg('manager.patientGrid.column.identifier'),
                            text:'',
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
                        'Content-Type': 'application/json; charset=UTF-8'
                    },
                    failure: function(error) {
                        if (error && error.reason) {
                            Ext.Msg.alert(i18n.getMsg('manager.error'), error.reason);
                        } else
                            Ext.Msg.alert('', i18n.getMsg('manager.error'));
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
                title: i18n.getMsg('manager.assignPatients'),
                id: 'assignPatientWindow',
                closable: true,
                modal: true,
                items: [assignForm]
            });

            wind.show();

            return assignForm;
        
        }
    });

    i18n.load();

});