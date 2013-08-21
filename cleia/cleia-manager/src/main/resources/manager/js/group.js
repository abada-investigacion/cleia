/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.require([
    'Ext.form.Panel', 'Ext.form.field.Checkbox', 'Abada.Ajax', 'Ext.JSON', 'Ext.Ajax',
    'Abada.toolbar.ToolbarInsertUpdateDelete', 'App.manager.js.common.gridgroupexpander','App.patient.js.common.gridPatient'])
Ext.onReady(function() {


    var usersToAssignGrid;

    var toolbar=Ext.create('Abada.toolbar.ToolbarInsertUpdateDelete', {
        listeners: {
            submitInsert: function() {
                handleFormulario('Inserta', groupGrid, 'Servicio', getRelativeServerURI('rs/group'), groupGrid.selModel);
            },
            submitUpdate: function() {
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
            },
            submitDelete: function() {
                if (groupGrid.selModel.hasSelection()) {
                    var form = {
                        value: groupGrid.selModel.getLastSelected().get('value')
                    }
                    var opt = 'borra';
                                 
                    doAjaxrequestJson(getRelativeServerURI('rs/group/{idGroup}', {
                        idGroup:form.value
                    }), form, 'DELETE', groupGrid, null, 'Servicio '+opt+'do', 'Error. No se ha podido ' + opt + 'r');
              
                } else
                    Ext.Msg.alert('', 'Seleccione un servicio');
            }
        }

    });
    
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
            value: value
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
            url: getRelativeServerURI('rs/patient/search'),
            height:400,
            width:800,
            checkboxse: true,
            padding: '10 5 5 5',
            page:25,
            listeners: { 
                afterrender: function() {                  
                  
                    patientsGrid.columns[0].setVisible(true);
                    patientsGrid.columns[4].hide();
                     
                    
                },                            
                select:function(constructor,record){     
                    
                    doAjaxAssign('add',record);
                 
                },
                deselect:function(constructor, record){  
                                           
                    doAjaxAssign('remove',record);
                  
                }
            }
        });      
        
        function doAjaxAssign(operation,record){
            
          getRelativeServerURI('rs/group/assignpatient')
            Abada.Ajax.requestJson({
                url: url,
                scope: this,
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                params:
                Ext.JSON.encode(form)
                ,
                failure: function(error) {
                    
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