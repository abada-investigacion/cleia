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
                        handleFormulario('Inserta', groupGrid, 'Usuario', getRelativeServerURI('rs/group'), groupGrid.selModel);
                    },
                    submitUpdate: function() {
                        if (groupGrid.selModel.hasSelection()) {
                            if (groupGrid.selModel.getCount() == 1) {
                                handleFormulario('Modifica', groupGrid, 'Usuario', getRelativeServerURI('rs/group/{idgroup}', [groupGrid.selModel.getLastSelected().get('idGroup')]), groupGrid.selModel);
                            } else {
                                Ext.Msg.alert('', 'Seleccione un servicio');
                            }
                        } else
                            Ext.Msg.alert('', 'Seleccione un servicio');
                    },
                    submitDelete: function() {
                        if (groupGrid.selModel.hasSelection()) {
                            handledelete(groupGrid, 'Borrado', 'idGroup', 'name', getRelativeServerURI('rs/group/{idgroup}', [groupGrid.selModel.getLastSelected().get('idGroup')]));
                        } else
                            Ext.Msg.alert('', 'Seleccione un Servicio');
                    }
                }

            }), groupGrid]//ponemos el grid

    });
    setCentralPanel(grid);


    //*Funcion para los frompanel
    function getO(form, seleuse) {
        var id = form.getComponent("idGroup").getValue();
        if (id == "") {
            id = null;
        }
        var o = {
            idGroup: id,
            name: form.getComponent("name").getValue(),
            userList: getSelectedIds(seleuse, ['idUser'])
        };
        return o;
    }

    function handleFormulario(opt, grid, title, url, selecion) {
        var name, idGroup, method = 'POST', tooltip = 'Insertar usuario'
        var usersGrid = Ext.create('App.manager.js.common.griduser', {
            url: getRelativeServerURI('rs/user/search'),
            width: 300,
            height: 250,
            checkboxse: true,
            page: 500
        });
        if (opt != 'Inserta' && selecion.hasSelection()) {
            method = 'PUT';
            name = selecion.getLastSelected().get('name');
            idGroup = selecion.getLastSelected().get('idGroup');
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
                    name: 'idGroup',
                    id: 'idGroup',
                    value: idGroup,
                    hidden: true
                },
                {
                    fieldLabel: 'Servicio',
                    name: 'name',
                    id: 'name',
                    value: name,
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
                            AjaxrequestJson(url, form, method, groupGrid, wind, opt + 'ndo', opt + 'ndo ' + title + '...', opt + 'do', 'Error no se ha podido ' + opt + 'r');
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
                selectautogrid(usersGrid, idGroup, 'idGroup', "idUser", getRelativeServerURI('rs/group/{idgroup}/users', [idGroup]));
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




