/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.require([
    'Ext.form.Panel', 'Ext.form.field.Checkbox', 'Abada.Ajax', 'Ext.JSON', 'Ext.Ajax',
    'Abada.toolbar.ToolbarInsertUpdateDelete', 'App.manager.js.common.gridgroupexpander'])
Ext.onReady(function() {

    var groupGrid = Ext.create('App.manager.js.common.gridgroupexpander', {
        url: getRelativeServerURI('rs/group/search'),
        width: 300,
        height: 400,
        page: 14
    });

    groupGrid.getStore().load({
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
        items: [
        Ext.create('Abada.toolbar.ToolbarInsertUpdateDelete', {
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

        }), groupGrid]//ponemos el grid

    });
    setCentralPanel(grid);


    //*Funcion para los frompanel
    function getO(form, selection) {
        var value = form.getComponent("value").getValue();
        if (value == "") {
            value = null;
        }
        var o = {
            value: value,
            users: getListForObject(selection, 'id')
        };
        return o;
    }

    function handleFormulario(opt, grid, title, url, selecion) {
        var value, method = 'POST', tooltip = 'Insertar usuario'
        var usersGrid = Ext.create('App.manager.js.common.griduser', {
            url: getRelativeServerURI('rs/user/search'),
            width: 300,
            height: 250,
            checkboxse: true,
            page: 500
        });
        if (opt != 'Inserta' && selecion.hasSelection()) {
            method = 'PUT';
            value = selecion.getLastSelected().get('value');
            tooltip = 'Modificar Servicio';
        }


        //form panel de insertar
        var formpanel = Ext.create('Ext.form.Panel', {
            title: opt + 'r',
            url: url,
            defaultType: 'textfield',
            monitorValid: true,
            items: [
                
            {
                fieldLabel: 'Servicio',
                name: 'value',
                id: 'value',
                value: value,
                allowBlank: false
            },
            usersGrid
            ],
            buttons: [{
                text: opt + 'r',
                id: 'formuser',
                formBind: true,
                handler: function() {
                    if (formpanel.getForm().isValid()) {
                        var form = getO(formpanel, usersGrid.selModel);
                        doAjaxrequestJson(url, form, method, groupGrid, wind,'Servicio '+ opt + 'do', 'Error no se ha podido ' + opt + 'r');
                    }

                },
                tooltip: tooltip
            }]
        });
        usersGrid.getStore().load();

        /*
         *en caso de modificar carga grid de usuarios de un grupo
         */
        if (opt != 'Inserta') {
            usersGrid.getStore().on('load', function() {
                entityListPreSelected(usersGrid, value, 'value', "id", getRelativeServerURI('rs/group/{value}/users', {
                    value:value
                }));
            });
        }
        var wind = Ext.create('Ext.window.Window', {
            id: 'group',
            autoScroll: false,
            closable: true,
            modal: true,
            items: [formpanel]
        });

        wind.show();

        return formpanel;
    }


});




