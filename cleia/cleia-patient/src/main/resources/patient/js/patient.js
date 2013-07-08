/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.require([
    'Ext.form.Panel', 'Ext.form.field.Checkbox', 'Abada.Ajax', 'Ext.JSON', 'Ext.Ajax',
    'Ext.layout.container.Table', 'Abada.toolbar.ToolbarInsertUpdateDelete', 'Abada.form.field.ComboBoxDeSelect',
    'App.patient.js.common.gridPatientid', 'Ext.form.field.Date','Abada.form.field.ComboBox'

    ])

Ext.onReady(function() {

    var toolbar = Ext.create('Abada.toolbar.ToolbarInsertUpdateDelete', {
        listeners: {
            submitInsert: function() {
                handleFormulario('Inserta', patientsGrid, 'Paciente', getRelativeServerURI('rs/patient'), patientsGrid.selModel);
            },
            submitUpdate: function() {
                if (patientsGrid.selModel.hasSelection()) {
                    if (patientsGrid.selModel.getCount() == 1) {
                        handleFormulario('Modifica', patientsGrid, 'Paciente', getRelativeServerURI('rs/patient/{idpatient}', [patientsGrid.selModel.getLastSelected().get('idPatient')]), patientsGrid.selModel);
                    } else {
                        Ext.Msg.alert('', 'Seleccione un Paciente');
                    }
                } else
                    Ext.Msg.alert('', 'Seleccione un Paciente');
            },
            submitDelete: function() {
                if (patientsGrid.selModel.hasSelection()) {
                    var form = {
                        enabled: !patientsGrid.selModel.getLastSelected().get('enabled'),
                        id: patientsGrid.selModel.getLastSelected().get('id')
                    }
                    var opt = 'modifica', title = 'habilitando';
                    var habilitar = 'Deshabilitado: ';
                    if (!patientsGrid.selModel.getLastSelected().get('enabled')) {
                        habilitar = 'habilitado: '
                    }
                    habilitar = habilitar + patientsGrid.selModel.getLastSelected().get('name');
                    doAjaxrequestJson(getRelativeServerURI('rs/patient/{idpatient}/{enable}',[form.id,form.enabled]), form, 'PUT', patientsGrid, null, opt + 'ndo', opt + 'ndo ' + title + '...', habilitar, 'error no se ha podido ' + opt + 'r');
                } else
                    Ext.Msg.alert('', 'Seleccione un Paciente');
            }
        }

    });

    var patientsGrid = Ext.create('App.patient.js.common.gridPatient', {
        url: getRelativeServerURI('rs/patient/search'),
        height: 400,
        page: 13
    });

    patientsGrid.getStore().load({
        params: {
            start: 0,
            limit: 14
        }
    });

    var grid = Ext.create('Ext.panel.Panel', {
        frame: true,
        autoWidth: true,
        autoHeight: true,
        title: '',
        items: [toolbar, patientsGrid]//ponemos el grid

    });
    toolbar.getComponent("Borrar").setText("Habilitar/Deshabilitar");
    setCentralPanel(grid);

    //*Funcion para los frompanel
    function getO(form,selectionGroup) {
        var id = form.getComponent("id").getValue();
        if (id == "") {
            id = null;
        }
        var o = {
            id: id,
            enabled: true,
            username: form.getComponent("username").getValue(),
            accountNonExpired: form.getComponent("accountNonExpired").getValue(),
            credentialsNonExpired: form.getComponent("credentialsNonExpired").getValue(),
            password: form.getComponent("password").getValue(),
            accountNonLocked: form.getComponent("accountNonLocked").getValue(),
            groups: getListForObject(selectionGroup,'value'),
            roles:[],
            name: form.getComponent("name").getValue(),
            surname: form.getComponent("surname").getValue(),
            surname1: form.getComponent("surname1").getValue(),
            genre: form.getComponent("genre").getValue(),
            birthday: Ext.Date.format(form.getComponent("birthday").getValue(), 'Y-m-d H:i:s'),
            tlf:form.getComponent("tlf").getValue(),
            address:{
                address:form.getComponent("address").getValue(),
                city:form.getComponent("city").getValue(),
                cp:form.getComponent("cp").getValue(),
                country:form.getComponent("country").getValue()
            }        


        };
        return o;
    }


    function handleFormulario(opt, grid, title, url, selection) {
        var method = 'POST', tooltip = 'Insertar Paciente';
        var id, username,password, name, surname, surname1, genre, birthday = new Date(), tlf,address,city,cp,country;

        if (opt != 'Inserta' && selection.hasSelection()) {
            method = 'PUT';
            id = selection.getLastSelected().get('id');
            name = selection.getLastSelected().get('name');
            surname = selection.getLastSelected().get('surname1');
            surname1 = selection.getLastSelected().get('surname2');            
            genre = selection.getLastSelected().get('genre');
            birthday = selection.getLastSelected().get('birthday');
            tlf = selection.getLastSelected().get('tlf');
            tooltip = 'Modificar Paciente';
            
          
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
        
        var combouser = Ext.create('Abada.form.field.ComboBox', {
            id: 'cbuser',
            url: getRelativeServerURI('rs/user/withoutAssignedPatient/combo'),
            emptyText: 'Cargar datos del usuario...',
            width: 270,
            editable: false,
            allowBlank: true,
            noSelection: 'Cargar datos del usuario...',
            selectedValue: '',
            padding:'0 5 0 15',
            listeners: {
                select: function() {
                   
                    Ext.getCmp('id').setValue(combouser.getValue());
                    Ext.getCmp('username').setReadOnly(true);
                    Ext.getCmp('username').setValue(combouser.getRawValue());
                    groupGrid.selModel.deselectAll();
                    
                    Abada.Ajax.requestJsonData({
                        url:getRelativeServerURI('rs/user/{iduser}/groups',{iduser:combouser.getValue()}),
                        scope:this,
                        method:'GET',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        failure:function(){            
            
                        },
                        success:function(object){
                            var groups=object.data;
                            for (var i = 0; i < groupGrid.getStore().getCount(); i++) {
                                var record = groupGrid.getStore().getAt(i);
                                for (var j = 0; j < groups.length; j++) {
                                    if (record.get("value") == groups[j].value) {
                                        groupGrid.selModel.select(record, true, true);
                                    }
                                }

                            }
                        }
                    });
                    
                    
                }
            }
        });

        
        var datebirthday = Ext.create('Ext.form.field.Date', {
            id: 'birthday',
            name: 'birthday',
            format: 'd/m/Y',
            altFormats: 'd/m/Y',
            fieldLabel: 'Fecha Nacimiento',
            value: birthday,
            width: 270
        });

        var groupGrid = Ext.create('App.manager.js.common.gridgroup', {
            url: getRelativeServerURI('rs/group/search'),
            width: 350,
            checkboxse: true,
            page: 500,
            rowspan:4,
            padding:'10 15 0 15'
        });

        
        var checkboxaccountNonExpired = Ext.create('Ext.form.field.Checkbox', {
            checked: true,
            id: 'accountNonExpired',
            name: 'accountNonExpired',
            inputValue: true,
            hidden: true
        });

        var checkboxcredentialsNonExpired = Ext.create('Ext.form.field.Checkbox', {
            checked: true,
            id: 'credentialsNonExpired',
            name: 'credentialsNonExpired',
            inputValue: true,
            hidden: true
        });

        var checkboxaccountNonLocked = Ext.create('Ext.form.field.Checkbox', {
            checked: true,
            id: 'accountNonLocked',
            name: 'accountNonLocked',
            inputValue: true,
            hidden: true
        });


        //form panel de insertar
        var formpanel = Ext.create('Ext.form.Panel', {           
            url: url,
            monitorValid: true,
            frame: false,
            bodyPadding:'10 15 0 15',
            autoScroll: true,
            height:500,
            layout: {
                type: 'vbox',
                columns: 2
            },
            items: [{
                xtype:'fieldset',
                title: '<b>Datos de Usuario</b>',
                width:'100%',                
                collapsible: false,
                defaultType: 'textfield',
                layout:{
                    type:'table',
                    column:2
                },
                padding:'10 15 10 15',
                items :[{
                    xtype:'container',
                    defaultType: 'textfield',                    
                    layout:'vbox',
                    items:[{
                        fieldLabel: 'Id',
                        name: 'id',
                        id: 'id',
                        value: id,
                        readOnly:true,
                        width: 270
                    },{
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

                    },checkboxaccountNonExpired, checkboxcredentialsNonExpired, checkboxaccountNonLocked]

                },{
                    xtype:'container',                  
                    layout:'vbox',
                    items:[
                    {
                        xtype:'container',
                        layout:'hbox',
                        items:[combouser,{
                            xtype:'button',
                            text: 'limpiar',
                            handler: function(){
                          
                                Ext.getCmp('id').setValue('');
                                Ext.getCmp('username').setReadOnly(false);
                                Ext.getCmp('username').setValue('');
                                Ext.getCmp('cbuser').setValue('');
                                groupGrid.selModel.deselectAll();
                            }
                        }]
                    }
                    ,groupGrid]
                }
                ]
            },{
                xtype:'fieldset',
                title: '<b>Datos personales</b>',
                width:'100%',
                collapsible: false,
                defaultType: 'textfield',
                padding:'10 15 10 15',
                items :[
                {
                    fieldLabel: 'Nombre',
                    name: 'name',
                    id: 'name',
                    value: name,
                    width: 270,
                    allowBlank: false
                },{
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
                },combogenre]
            },{
                xtype:'fieldset',
                title: '<b>Direcci&oacute;n</b>',
                width:'100%',
                collapsible: false,
                defaultType: 'textfield',
                padding:'10 15 10 15',
                items :[
                {
                    fieldLabel: 'Direccion',
                    name: 'address',
                    id: 'address',
                    value: address,
                    width: 270,
                    allowBlank: false
                },{
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
                },{
                    fieldLabel: 'Pais',
                    name: 'country',
                    id: 'country',
                    value: country,
                    width: 270,
                    allowBlank: false
                }]
            } ],
            buttons: [{
                text: opt + 'r',
                id: 'formPatient',
                formBind: true,
                handler: function() {
                    alert(Ext.getCmp('username').getValue());
                    if (formpanel.getComponent("password2").getValue() == formpanel.getComponent("password").getValue()) {
                        if (formpanel.getForm().isValid()) {
               
                            doAjaxrequestJson(url, getO(formpanel,groupGrid.selModel), method, patientsGrid, wind, opt + 'ndo', opt + 'ndo ' + title + '...', opt + 'do', 'Error no se ha podido ' + opt + 'r');
                     
                        }
                    }else{
                        Ext.Msg.alert('Error', 'la contrase&ntilde;a no son iguales');
                    }

                },
                tooltip: tooltip
            }]
        });
        groupGrid.getStore().load();
        

        var wind = Ext.create('Ext.window.Window', {
            title: opt + 'r',
            id: 'Paciente',            
            closable: true,
            modal: true,            
            width:700,
            autoHeight:true,
            items: [formpanel]
        });

        wind.show();

        return formpanel;
    }

});