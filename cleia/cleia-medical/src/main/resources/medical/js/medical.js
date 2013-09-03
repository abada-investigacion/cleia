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
    'Ext.layout.container.Table', 'Abada.toolbar.ToolbarInsertUpdateDelete', 'Abada.form.field.ComboBoxDeSelect',
    , 'Ext.form.field.Date', 'Abada.form.field.ComboBox', 'App.manager.js.common.gridids', 'App.patient.js.common.gridPatient',
    'App.patient.js.common.gridPatientExpander'

    ])

Ext.onReady(function() {

    var toolbar = Ext.create('Abada.toolbar.ToolbarInsertUpdateDelete', {
        listeners: {
            submitInsert: function() {
                handleFormulario('Inserta', medicalGrid, 'Medico', getRelativeServerURI('rs/medical'), medicalGrid.selModel);
            },
            submitUpdate: function() {
                if (medicalGrid.selModel.hasSelection()) {
                    if (medicalGrid.selModel.getCount() == 1) {
                        handleFormulario('Modifica', medicalGrid, 'Medico', getRelativeServerURI('rs/medical/{idmedical}', {
                            idmedical: medicalGrid.selModel.getLastSelected().get('id')
                        }), medicalGrid.selModel);
                    } else {
                        Ext.Msg.alert('', 'Seleccione un Medico');
                    }
                } else
                    Ext.Msg.alert('', 'Seleccione un Medico');
            },
            submitDelete: function() {
                if (medicalGrid.selModel.hasSelection()) {
                    var form = {
                        enabled: !medicalGrid.selModel.getLastSelected().get('enabled'),
                        id: medicalGrid.selModel.getLastSelected().get('id')
                    }
                   
                    var opt = 'modifica', status = 'habilita';

                    if (medicalGrid.selModel.getLastSelected().get('enabled')) {
                        status = 'deshabilita'
                    }
                    doAjaxrequestJson(getRelativeServerURI('rs/medical/{idmedical}/{enable}', {
                        idmedical: form.id,
                        enable: form.enabled
                    }), form, 'PUT', medicalGrid, null, 'Medico ' + status + 'do', 'Error. No se ha podido ' + opt + 'r');
                 
                    medicalGrid.selModel.deselectAll();
                 
                } else{
                    Ext.Msg.alert('', 'Seleccione un Medico');
                }
                
                
            }
        }

    });

    toolbar.add({
        xtype: 'button',
        id: 'assginPatient',
        text: 'Asignar Pacientes',
        handler: function() {
            if (medicalGrid.selModel.hasSelection()) {
                if (medicalGrid.selModel.getCount() == 1) {
                    assignPatient(medicalGrid.selModel);
                } else {
                    Ext.Msg.alert('', 'Seleccione un Medico');
                }
            } else
                Ext.Msg.alert('', 'Seleccione un Medico');
        }
    });

    var medicalGrid = Ext.create('App.medical.js.common.gridMedicalExpander', {
        url: getRelativeServerURI('rs/medical/search'),
        height: 400,
        page: 13
    });

    medicalGrid.getStore().load({
        params: {
            start: 0,
            limit: 14
        }
    });

    var grid = Ext.create('Ext.panel.Panel', {
        autoWidth: true,
        autoHeight: true,
        title: '',
        items: [toolbar, medicalGrid]//ponemos el grid

    });
    toolbar.getComponent("Borrar").setText("Habilitar/Deshabilitar");
    setCentralPanel(grid);

    //*Funcion para los frompanel
    function getO(selectionGroup, idGridStore) {
        var id = Ext.getCmp('id').getValue();
        if (id == '') {
            id = null;
        }

        var nid = idGridStore.getCount();
        var ids = new Array();
        for (var i = 0; i < nid; i++) {
            oid = idGridStore.getAt(i).getData();

            ids[i] = {
                value: oid.value,
                type: {
                    value: oid.idtype
                }
            };

        }

        var o = {
            patient: {
                user: {
                    id: id,
                    enabled: true,
                    username: Ext.getCmp('username').getValue(),
                    accountNonExpired: true,
                    credentialsNonExpired: true,
                    password: Ext.getCmp('password').getValue(),
                    accountNonLocked: true,
                    groups: getListForObject(selectionGroup, 'value'),
                    ids: ids
                },
                name: Ext.getCmp('name').getValue(),
                surname: Ext.getCmp('surname').getValue(),
                surname1: Ext.getCmp('surname1').getValue(),
                genre: Ext.getCmp('genre').getValue(),
                birthDay: Ext.Date.format(Ext.getCmp('birthDay').getValue(), 'Y-m-d H:i:s'),
                tlf: Ext.getCmp('tlf').getValue(),
                address: {
                    address: Ext.getCmp('address').getValue(),
                    city: Ext.getCmp('city').getValue(),
                    cp: Ext.getCmp('cp').getValue(),
                    countryAddress: Ext.getCmp('country').getValue()
                }


            }
        };
        return o;
    }


    function handleFormulario(opt, grid, title, url, selection) {
        var method = 'POST', tooltip = 'Insertar Medico';
        var id,
        username,
        password,
        name,
        surname,
        surname1,
        genre,
        birthDay = new Date(),
        tlf, address, city, cp, country;

        if (opt != 'Inserta' && selection.hasSelection()) {
            method = 'PUT';
            id = selection.getLastSelected().get('id');
            username = selection.getLastSelected().get('username');
            name = selection.getLastSelected().get('name');
            surname = selection.getLastSelected().get('surname');
            surname1 = selection.getLastSelected().get('surname1');
            genre = selection.getLastSelected().get('genre');
            birthDay = selection.getLastSelected().get('patient.birthDay');
            tlf = selection.getLastSelected().get('tlf');
            address = selection.getLastSelected().get('address');
            city = selection.getLastSelected().get('city');
            cp = selection.getLastSelected().get('cp');
            country = selection.getLastSelected().get('country');
            tooltip = 'Modificar Medico';


        }

        var combogenre = Ext.create('Abada.form.field.ComboBoxDeSelect', {
            id: 'genre',
            url: getRelativeServerURI('rs/patient/genre/combo'),
            fieldLabel: 'Genero',
            emptyText: 'Seleccione un Genero',
            width: 270,
            editable: false,
            allowBlank: false,
            noSelection: 'Seleccione un Genero',
            selectedValue: '',
            listeners: {
                load: function() {
                    combogenre.setValue(genre);
                }
            }
        });

        combogenre.loadStore();
        
        var loadbutton = Ext.create('Ext.Button', {
            text: 'Cargar Paciente',
            id: 'loadbutton',
            margin: '10 15 0 15'
        });
       


        var datebirthday = Ext.create('Ext.form.field.Date', {
            id: 'birthDay',
            name: 'birthDay',
            format: 'd/m/Y',
            altFormats: 'd/m/Y',
            fieldLabel: 'Fecha Nacimiento',
            value: birthDay,
            width: 270
        });

        var groupGrid = Ext.create('App.manager.js.common.gridgroup', {
            url: getRelativeServerURI('rs/group/search'),
            width: 350,
            checkboxse: true,
            page: 500,
            rowspan: 4,
            padding: '10 15 0 15'
        });
        
        groupGrid.getStore().load();

        var configIdGrid = {
            id: 'idGrid',
            title: '',
            width: 400,
            height: 250,
            url: null,
            page: 500,
            rowspan: 4
        };

        var idGrid = Ext.create('App.manager.js.common.gridids', configIdGrid);


        var comboidtype = Ext.create('Abada.form.field.ComboBox', {
            id: 'cbidtype',
            url: getRelativeServerURI('rs/idtype/search/combo'),
            fieldLabel: 'Tipo',
            emptyText: 'Seleccione un Tipo',
            padding: '0 15 10 0',
            width: 150,
            editable: false,
            labelWidth: 50,
            labelAlign: 'top',
            selectedValue: ''
        });

        comboidtype.loadStore();

        var formpanel = Ext.create('Ext.form.Panel', {
            url: url,
            monitorValid: true,
            frame: false,
            bodyPadding: '10 15 0 15',
            autoScroll: true,
            height: 500,
            layout: {
                type: 'vbox',
                columns: 2
            },
            items: [{
                xtype: 'fieldset',
                title: '<b>Datos de Usuario</b>',
                width: '100%',
                collapsible: false,
                defaultType: 'textfield',
                layout: {
                    type: 'table',
                    column: 2
                },
                padding: '10 15 10 15',
                items: [{
                    xtype: 'container',
                    defaultType: 'textfield',
                    layout: 'vbox',
                    items: [{
                        fieldLabel: 'Id',
                        name: 'id',
                        id: 'id',
                        value: id,
                        readOnly: true,
                        width: 270
                    }, {
                        fieldLabel: 'Usuario',
                        name: 'username',
                        id: 'username',
                        value: username,
                        allowBlank: false,
                        width: 270
                    }, {
                        fieldLabel: 'Contrase&ntilde;a',
                        name: 'password',
                        id: 'password',
                        allowBlank: false,
                        inputType: 'password',
                        value: password,
                        width: 270
                    },
                    {
                        fieldLabel: 'Repita Contrase&ntilde;a',
                        name: 'password2',
                        id: 'password2',
                        allowBlank: false,
                        inputType: 'password',
                        value: password,
                        width: 270

                    }]

                }, {
                    xtype: 'container',
                    layout: 'vbox',
                    items: [
                    {
                        xtype: 'container',
                        layout: 'hbox',
                        items: [loadbutton, {
                            xtype: 'button',
                            id: 'clearbutton',
                            text: 'Limpiar',
                            margin: '10 15 0 0',
                            handler: function() {

                                Ext.getCmp('id').setValue('');
                                Ext.getCmp('username').setReadOnly(false);
                                Ext.getCmp('username').setValue('');
                                groupGrid.selModel.deselectAll();
                                idGrid.getStore().removeAll();
                            }
                        }]
                    }
                    , groupGrid]
                }
                ]
            }, {
                xtype: 'fieldset',
                title: '<b>Identificadores</b>',
                width: '100%',
                collapsible: false,
                padding: '10 15 10 15',
                items: [
                {
                    xtype: 'container',
                    layout: 'hbox',
                    items: [{
                        xtype: 'textfield',
                        fieldLabel: 'N&uacute;mero',
                        name: 'idnumber',
                        id: 'idnumber',
                        padding: '0 15 10 0',
                        labelWidth: 50,
                        labelAlign: 'top',
                        width: 150

                    }, comboidtype, {
                        xtype: 'button',
                        id: 'addbutton',
                        icon: getRelativeURI('images/custom/add.png'),
                        handler: function() {

                            idGrid.getStore().insert(0, {
                                value: Ext.getCmp("idnumber").getValue(),
                                idtype: Ext.getCmp("cbidtype").getRawValue()
                            });

                            Ext.getCmp("idnumber").setValue('');
                            Ext.getCmp("cbidtype").setValue('');
                        }
                    }, {
                        xtype: 'button',
                        id: 'deletebutton',
                        icon: getRelativeURI('images/custom/delete.gif'),
                        handler: function() {

                            if (idGrid.getSelectionModel().getCount() > 0) {
                                idGrid.getStore().remove(idGrid.getSelectionModel().getSelection());
                            }

                        }
                    }]
                }, idGrid
                ]
            }, {
                xtype: 'fieldset',
                title: '<b>Datos personales</b>',
                width: '100%',
                collapsible: false,
                defaultType: 'textfield',
                padding: '10 15 10 15',
                items: [
                {
                    fieldLabel: 'Nombre',
                    name: 'name',
                    id: 'name',
                    value: name,
                    width: 270,
                    allowBlank: false
                }, {
                    fieldLabel: 'Primer Apellido',
                    name: 'surname',
                    id: 'surname',
                    value: surname,
                    width: 270,
                    allowBlank: false

                }, {
                    fieldLabel: 'Segundo Apellido',
                    name: 'surname1',
                    id: 'surname1',
                    value: surname1,
                    width: 270,
                    allowBlank: false
                }, datebirthday, {
                    fieldLabel: 'Telefono',
                    name: 'tlf',
                    id: 'tlf',
                    value: tlf,
                    width: 270,
                    allowBlank: false
                }, combogenre]
            }, {
                xtype: 'fieldset',
                title: '<b>Direcci&oacute;n</b>',
                width: '100%',
                collapsible: false,
                defaultType: 'textfield',
                padding: '10 15 10 15',
                items: [
                {
                    fieldLabel: 'Direcci&oacute;n',
                    name: 'address',
                    id: 'address',
                    value: address,
                    width: 270,
                    allowBlank: false
                }, {
                    fieldLabel: 'Ciudad',
                    name: 'city',
                    id: 'city',
                    value: city,
                    width: 270,
                    allowBlank: false

                }, {
                    fieldLabel: 'C. Postal',
                    name: 'cp',
                    id: 'cp',
                    value: cp,
                    width: 270,
                    allowBlank: false
                }, {
                    fieldLabel: 'Pais',
                    name: 'country',
                    id: 'country',
                    value: country,
                    width: 270,
                    allowBlank: false
                }]
            }],
            buttons: [{
                text: opt + 'r',
                id: 'formPatient',
                formBind: true,
                handler: function() {

                    if (Ext.getCmp('password2').getValue() == Ext.getCmp('password').getValue()) {
                        if (formpanel.getForm().isValid()) {

                            doAjaxrequestJson(url, getO(groupGrid.selModel, idGrid.getStore()), method, medicalGrid, wind, 'Medico ' + opt + 'do', 'Error. No se ha podido ' + opt + 'r');

                        }
                    } else {
                        Ext.Msg.alert('Error', 'Las contrase&ntilde;as no son iguales');
                    }

                },
                tooltip: tooltip
            }]
        });


        if (opt != 'Inserta' && selection.hasSelection()) {

            Ext.getCmp('clearbutton').disable();
            
            loadbutton.setDisabled(true);
            
            groupGrid.getStore().on('load', function() {
                selectGroupGrid(id, groupGrid);
            });
            
            loadIdGrid(id, idGrid);

        }

        loadbutton.on('click', function() {
            if (Ext.getCmp('loadbutton').disabled !== true) {
                gridpatient(groupGrid, idGrid, combogenre);
            }
        });

        var wind = Ext.create('Ext.window.Window', {
            title: opt + 'r',
            id: 'medicalWindow',
            closable: true,
            modal: true,
            width: 700,
            autoHeight: true,
            items: [formpanel]
        });

        wind.show();
        
       
        
        return formpanel;
    }


    function gridpatient(groupGrid, idGrid, combogenre) {
        
        var patientGrid = Ext.create('App.patient.js.common.gridPatient', {
            title:'',
            url: getRelativeServerURI('rs/patient/search/patientnotmedical'),
            width: 800,
            height: 400,
            padding: '5 5 5 5',
            page: 14
        });
        
        patientGrid.on('beforeitemdblclick', function(grid, record) {
            Ext.getCmp('id').setValue(record.data.id);
            Ext.getCmp('username').setReadOnly(true);
            Ext.getCmp('username').setValue(record.data.username);
            groupGrid.selModel.deselectAll();
            selectGroupGrid(record.data.id, groupGrid);
            loadIdGrid(record.data.id, idGrid);
            Ext.getCmp('tlf').setValue(record.data.tlf);
            Ext.getCmp('surname1').setValue(record.data.surname1);
            Ext.getCmp('surname').setValue(record.data.surname);
            Ext.getCmp('name').setValue(record.data.name);
            Ext.getCmp('address').setValue(record.data.address);
            Ext.getCmp('city').setValue(record.data.city);
            Ext.getCmp('cp').setValue(record.data.cp);
            Ext.getCmp('country').setValue(record.data.country);
            combogenre.setValue(record.data.genre);
            winds.close();

        });
        
        patientGrid.getStore().load({
            params: {
                start: 0,
                limit: 14
            }
        });
        var winds = Ext.create('Ext.window.Window', {
            title: 'Pacientes',
            id: 'patinetgridWindow',
            closable: true,
            modal: true,
            width: 800,
            autoHeight: true,
            items: [patientGrid]
        });

        winds.show();

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
                  
                    
                    patientsGrid.columns[4].hide();
                    patientsGrid.columns[6].hide();
                    patientsGrid.columns[7].hide();
                    patientsGrid.columns[8].hide();
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
                        
            var url=getRelativeServerURI('rs/medical/{idmedical}/{operation}/{idpatient}',{
                idmedical:selection.getSelection()[0].get('id'),
                operation:operation,
                idpatient:record.data.id                             
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
            
            var medicals;
            var exit;
            
            for(var i=0;i<records.length; i++){
                
                exit=false;
                medicals=records[i].data.medicals;
                
                for(var z=0;z<medicals.length && !exit;z++){
                  
                    if(medicals[z].id==selection.getSelection()[0].get('id')){
                       
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


    function selectGroupGrid(userid, groupGrid) {

        Abada.Ajax.requestJsonData({
            url: getRelativeServerURI('rs/user/{iduser}/groups', {
                iduser: userid
            }),
            scope: this,
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
            failure: function() {

            },
            success: function(object) {
                var groups = object.data;
                for (var i = 0; i < groupGrid.getStore().getCount(); i++) {
                    var record = groupGrid.getStore().getAt(i);
                    for (var j = 0; j < groups.length; j++) {
                        if (record.get("value") === groups[j].value) {
                            groupGrid.selModel.select(record, true, true);
                        }
                    }

                }
            }
        });

    }

    function loadIdGrid(id, idGrid) {
        if (id) {

            idGrid.getStore().getProxy().url = getRelativeServerURI('rs/user/{iduser}/ids', {
                iduser: id
            });

            idGrid.getStore().load();

        }
    }

});