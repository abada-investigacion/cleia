/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.require([
    'Ext.form.Panel', 'Ext.form.field.Checkbox', 'Abada.Ajax', 'Ext.JSON', 'Ext.Ajax',
    'Ext.layout.container.Table', 'Abada.toolbar.ToolbarInsertUpdateDelete', 'Abada.form.field.ComboBoxDeSelect',
    'App.patient.js.common.gridPatientid', 'Ext.form.field.Date'

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
    function getO(form) {
        var id = form.getComponent("id").getValue();
        if (id == "") {
            id = null;
        }
        var o = {
            id: id,
            name: form.getComponent("name").getValue(),
            surname: form.getComponent("surname").getValue(),
            surname1: form.getComponent("surname1").getValue(),
            genre: form.getComponent("genre").getValue(),
            birthday: Ext.Date.format(form.getComponent("birthday").getValue(), 'Y-m-d H:i:s'),
            address:{},
            tlf:form.getComponent("tlf").getValue()          


        };
        return o;
    }


    function handleFormulario(opt, grid, title, url, selecion) {
        var method = 'POST', tooltip = 'Insertar Paciente';
        var id, name, surname, surname1, genre, birthday = new Date(), tlf,address,city,cp,country;

        if (opt != 'Inserta' && selecion.hasSelection()) {
            method = 'PUT';
            id = selecion.getLastSelected().get('id');
            name = selecion.getLastSelected().get('name');
            surname = selecion.getLastSelected().get('surname1');
            surname1 = selecion.getLastSelected().get('surname2');            
            genre = selecion.getLastSelected().get('genre');
            birthday = selecion.getLastSelected().get('birthday');
            tlf = selecion.getLastSelected().get('tlf');
            tooltip = 'Modificar Paciente';
            
          
        }
        
        var combogenre = Ext.create('Abada.form.field.ComboBoxDeSelect', {
            id: 'genre',
            url: getRelativeServerURI('rs/patient/genre/combo'),
            fieldLabel: 'Genero',
            emptyText: 'seleccione un Genero',
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
        
        var datebirthday = Ext.create('Ext.form.field.Date', {
            id: 'birthday',
            name: 'birthday',
            format: 'd/m/Y',
            altFormats: 'd/m/Y',
            fieldLabel: 'Fecha Nacimiento',
            value: birthday,
            width: 270
        });


        //form panel de insertar
        var formpanel = Ext.create('Ext.form.Panel', {
            title: opt + 'r',
            url: url,
            monitorValid: true,
            width: 350,
            frame: false,           
            items: [{
                xtype:'fieldset',
                title: 'Datos personales',
                width:'70%',
                collapsible: false,
                defaultType: 'textfield',
                padding:'10 0 0 20',
                items :[{
                    name: 'id',
                    id: 'id',
                    value: id,
                    hidden: true
                },
                {
                    fieldLabel: 'Nombre',
                    name: 'name',
                    id: 'name',
                    value: name,
                    width:270,
                    allowBlank: false
                },{
                    fieldLabel: 'Primer Apellido',
                    name: 'surname',
                    id: 'surname',
                    value: surname,
                    width:270,
                    allowBlank: false

                }, {
                    fieldLabel: 'Segundo Apellido',
                    name: 'surname1',
                    id: 'surname1',
                    value: surname1,
                    width:270,
                    allowBlank: false
                }, datebirthday, {
                    fieldLabel: 'Telefono',
                    name: 'tlf',
                    id: 'tlf',
                    value: tlf,
                    width:270,
                    allowBlank: false
                },combogenre]
            },{
                xtype:'fieldset',
                title: 'Direccion',
                width:'70%',
                collapsible: false,
                defaultType: 'textfield',
                padding:'10 0 0 20',
                items :[
                {
                    fieldLabel: 'Direccion',
                    name: 'address',
                    id: 'address',
                    value: address,
                    width:270,
                    allowBlank: false
                },{
                    fieldLabel: 'Ciudad',
                    name: 'city',
                    id: 'city',
                    value: city,
                    width:270,
                    allowBlank: false

                }, {
                    fieldLabel: 'C. Postal',
                    name: 'cp',
                    id: 'cp',
                    value: cp,
                    width:270,
                    allowBlank: false
                },{
                    fieldLabel: 'Pais',
                    name: 'country',
                    id: 'country',
                    value: country,
                    width:270,
                    allowBlank: false
                }]
            } ],
            buttons: [{
                text: opt + 'r',
                id: 'formPatient',
                formBind: true,
                handler: function() {
                    if (formpanel.getForm().isValid()) {
               
                        doAjaxrequestJson(url, getO(formpanel), method, patientsGrid, wind, opt + 'ndo', opt + 'ndo ' + title + '...', opt + 'do', 'Error no se ha podido ' + opt + 'r');
                     
                    }

                },
                tooltip: tooltip
            }]
        });

        var wind = Ext.create('Ext.window.Window', {
            id: 'Paciente',
            autoScroll: false,
            closable: true,
            modal: true,
            autoWidth:true,
            items: [formpanel]
        });

        wind.show();

        return formpanel;
    }

});




