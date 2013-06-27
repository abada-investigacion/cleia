/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.require([
    'Ext.form.Panel', 'Ext.form.field.Checkbox', 'Abada.Ajax', 'Ext.JSON', 'Ext.Ajax'])

Ext.onReady(function() {

    var toolbar = Ext.create('Abada.toolbar.ToolbarInsertUpdateDelete', {
        listeners: {
            submitInsert: function() {
                handleFormulario('Inserta', idtypeGrid, 'Tipo de Identificador', getRelativeServerURI('rs/idtype'), idtypeGrid.selModel);
            },
            submitUpdate: function() {
                if (idtypeGrid.selModel.hasSelection()) {
                    if (idtypeGrid.selModel.getCount() == 1) {
                        handleFormulario('Modifica', idtypeGrid, 'Tipo de Identificador', getRelativeServerURI('rs/idtype/{ididtype}', [idtypeGrid.selModel.getLastSelected().get('idIdType')]), idtypeGrid.selModel);
                    } else {
                        Ext.Msg.alert('', 'Seleccione un tipo de identificador');
                    }
                } else
                    Ext.Msg.alert('', 'Seleccione un tipo de identificador');
            },
            submitDelete: function() {
                if (idtypeGrid.selModel.hasSelection()) {
                    handledelete(idtypeGrid, 'Borrado', 'idIdType', 'name', getRelativeServerURI('rs/idtype/{ididtype}', [idtypeGrid.selModel.getLastSelected().get('idIdType')]));
                } else
                    Ext.Msg.alert('', 'Seleccione un Tipo de Identificador');
            }
        }

    });

    var idtypeGrid = Ext.create('App.patient.js.common.gridIdtype', {
        url: getRelativeServerURI('rs/idtype/search'),
        width: 300,
        height: 400,
        checkboxse: true,
        page: 14
    });

    idtypeGrid.getStore().load({
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
        items: [toolbar, idtypeGrid]//ponemos el grid

    });

    setCentralPanel(grid);

    //*Funcion para los formpanel
    function getO(form) {
        var id = form.getComponent("idIdType").getValue();
        if (id == "") {
            id = null;
        }
        var o = {
            idIdType: id,
            name: form.getComponent("name").getValue(),
            isrepeatable: form.getComponent("repeat").getValue()
        };
        return o;
    }

    function handleFormulario(opt, grid, title, url, selection) {
        var name, idIdType, method = 'POST', tooltip = 'Insertar Tipo de Identificador', isrepeatable = false

        if (opt != 'Inserta' && selection.hasSelection()) {
            method = 'PUT';
            name = selection.getLastSelected().get('name');
            idIdType = selection.getLastSelected().get('idIdType');
            isrepeatable = selection.getLastSelected().get('isrepeatable');
            tooltip = 'Modificar Tipo de Identificador';
        }


        var repeat = Ext.create('Ext.form.field.Checkbox', {
            checked: isrepeatable,
            fieldLabel: 'Se puede repetir?',
            id: 'repeat',
            name: 'repeat'
        });


        //form panel de insertar
        var formpanel = Ext.create('Ext.form.Panel', {
            title: opt + 'r',
            url: url,
            defaultType: 'textfield',
            monitorValid: true,
            items: [
                {
                    name: 'idIdType',
                    id: 'idIdType',
                    value: idIdType,
                    hidden: true
                },
                {
                    fieldLabel: 'Nombre Identificador',
                    name: 'name',
                    id: 'name',
                    value: name,
                    allowBlank: false
                }, repeat
            ],
            buttons: [{
                    text: opt + 'r',
                    id: 'formIdtype',
                    formBind: true,
                    handler: function() {
                        if (formpanel.getForm().isValid()) {
                            var form = getO(formpanel)
                            AjaxrequestJson(url, form, method, idtypeGrid, wind, opt + 'ndo', opt + 'ndo ' + title + '...', opt + 'do', 'Error. No se ha podido ' + opt + 'r');
                        }
                    },
                    tooltip: tooltip
                }]
        });

        var wind = Ext.create('Ext.window.Window', {
            id: 'usuario',
            autoScroll: false,
            closable: true,
            modal: true,
            items: [formpanel]
        });

        wind.show();

        return formpanel;
    }


});