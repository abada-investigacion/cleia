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
                        idPatient: patientsGrid.selModel.getLastSelected().get('idPatient')
                    }
                    var opt = 'modifica', title = 'habilitando';
                    var habilitar = 'Deshabilitado: ';
                    if (!patientsGrid.selModel.getLastSelected().get('enabled')) {
                        habilitar = 'habilitado: '
                    }
                    habilitar = habilitar + patientsGrid.selModel.getLastSelected().get('name');
                    doAjaxrequestJson(getRelativeServerURI('rs/patient/{idpatient}/{enable}',[form.idPatient,form.enabled]), form, 'PUT', patientsGrid, null, opt + 'ndo', opt + 'ndo ' + title + '...', habilitar, 'error no se ha podido ' + opt + 'r');
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
    function getO(form, store) {
        var id = form.getComponent("idPatient").getValue();
        if (id == "") {
            id = null;
        }
        var o = {
            idPatient: id,
            name: form.getComponent("name").getValue(),
            surname1: form.getComponent("surname1").getValue(),
            surname2: form.getComponent("surname2").getValue(),
            exitus: form.getComponent("exitus").getValue(),
            enabled: form.getComponent("enabled").getValue(),
            genre: form.getComponent("genre").getValue(),
            birthday: Ext.Date.format(form.getComponent("birthday").getValue(), 'Y-m-d H:i:s'),
            patientidList: getpatientid(store)


        };
        return o;
    }

    function getpatientid(store) {
        var rows = [];
        var columnsName = ['value', 'idPatientId'];
        var columnsObject = [];
        columnsObject.push({
            key: 'idTypeidIdType',
            value: ['idIdType', 'name']
        });
        for (var i = 0; i < store.getCount(); i++) {

            rows.push(Ext.JSON.decode(getjsondecode(store.getAt(i), columnsName, columnsObject)));

        }
        return rows;
    }

    function handleFormulario(opt, grid, title, url, selecion) {
        var method = 'POST', tooltip = 'Insertar Paciente';
        var idPatient, name, surname1, surname2, exitus = false, genre, enabled = true, birthday = new Date(), a;

        var patientidGrid = Ext.create('App.patient.js.common.gridPatientid', {
            url: getRelativeServerURI('rs/patient/{idpatient}/id', [selecion.getLastSelected().get('idPatient')]),
            width: 300,
            height: 200,
            checkboxse: true,
            page: 7,
            rowspan: 4
        });


        if (opt != 'Inserta' && selecion.hasSelection()) {
            method = 'PUT';
            idPatient = selecion.getLastSelected().get('idPatient');
            name = selecion.getLastSelected().get('name');
            surname1 = selecion.getLastSelected().get('surname1');
            surname2 = selecion.getLastSelected().get('surname2');
            exitus = selecion.getLastSelected().get('exitus');
            enabled = selecion.getLastSelected().get('enabled');
            genre = selecion.getLastSelected().get('genre');
            birthday = selecion.getLastSelected().get('birthday');
            birthday = new Date(birthday);
            tooltip = 'Modificar Paciente';
            //   patientidGrid.getStore().setBaseParam('idPatient',idPatient);
            patientidGrid.getStore().load({
                params: {
                    start: 0,
                    limit: 14,
                    idPatient: idPatient
                }
            });
        }
        var checkboxenabled = Ext.create('Ext.form.field.Checkbox', {
            checked: enabled,
            fieldLabel: 'Habilitar',
            id: 'enabled',
            name: 'enabled'

        });
        var checkboxexitus = Ext.create('Ext.form.field.Checkbox', {
            fieldLabel: 'Esta muerto?',
            checked: exitus,
            id: 'exitus',
            name: 'exitus'
        });
        var combogenre = Ext.create('Abada.form.field.ComboBoxDeSelect', {
            id: 'genre',
            url: getRelativeServerURI('rs/patient/genre/combo'),
            fieldLabel: 'Genero',
            emptyText: 'seleccione un Genero',
            maxWidth: 225,
            editable: false,
            allowBlank: false,
            noSelection: 'seleccione un Genero',
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
            width: 225

        });


        //form panel de insertar
        var formpanel = Ext.create('Ext.form.Panel', {
            title: opt + 'r',
            url: url,
            defaultType: 'textfield',
            monitorValid: true,
            layout: {
                type: 'table',
                columns: 3
            },
            defaults: {
                frame: true,
                width: 250
            },
            items: [
                {
                    fieldLabel: 'Nombre',
                    name: 'name',
                    id: 'name',
                    value: name,
                    allowBlank: false,
                    width: 225
                }, {
                    fieldLabel: 'Primer Apellido',
                    name: 'surname1',
                    id: 'surname1',
                    value: surname1,
                    allowBlank: false,
                    width: 225

                }, patientidGrid, {
                    fieldLabel: 'Segundo Apellido',
                    name: 'surname2',
                    id: 'surname2',
                    value: surname2,
                    allowBlank: false,
                    width: 225
                }, datebirthday, combogenre, checkboxexitus, checkboxenabled, {
                    name: 'idPatient',
                    id: 'idPatient',
                    value: idPatient,
                    hidden: true
                }

            ],
            buttons: [{
                    text: opt + 'r',
                    id: 'formPatient',
                    formBind: true,
                    handler: function() {
                        if (formpanel.getForm().isValid()) {
                            if (patientidGrid.getStore().getCount() > 0) {
                                doAjaxrequestJson(url, getO(formpanel, patientidGrid.getStore()), method, patientsGrid, wind, opt + 'ndo', opt + 'ndo ' + title + '...', opt + 'do', 'Error no se ha podido ' + opt + 'r');
                            } else
                                Ext.Msg.alert('', 'El paciente no tiene ningun Id');
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
            items: [formpanel]
        });

        wind.show();

        return formpanel;
    }

});