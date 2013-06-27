/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.require([
    'Ext.form.Panel', 'Ext.form.field.Checkbox', 'Abada.Ajax', 'Ext.JSON', 'Ext.Ajax',
    'Ext.layout.container.Column', 'Abada.toolbar.ToolbarInsertUpdateDelete'

])

Ext.onReady(function() {
    var roleGrid = Ext.create('App.manager.js.common.gridroleexpander', {
        url: getRelativeServerURI('rs/role/search'),
        width: 300,
        height: 350,
        checkboxse: true,
        page: 14


    });

    roleGrid.getStore().load({
        params: {
            start: 0,
            limit: 14
        }
    });

    var panel = Ext.create('Ext.panel.Panel', {
        frame: true,
        autoWidth: true,
        autoHeight: true,
        title: '',
        items: [
            Ext.create('Abada.toolbar.ToolbarInsertUpdateDelete', {
                listeners: {
                    submitInsert: function() {
                        handleFormulario('Inserta', roleGrid, 'Role', getRelativeServerURI('rs/role'), roleGrid.selModel);
                    },
                    submitUpdate: function() {
                        if (roleGrid.selModel.hasSelection()) {
                            if (roleGrid.selModel.getCount() == 1) {
                                handleFormulario('Modifica', roleGrid, 'Usuario', getRelativeServerURI('rs/role/{idrole}', [roleGrid.selModel.getLastSelected().get('idRolePriv')]), roleGrid.selModel);
                            } else {
                                Ext.Msg.alert('', 'Seleccione un role');
                            }
                        } else
                            Ext.Msg.alert('', 'Seleccione un role');
                    },
                    submitDelete: function() {
                        if (roleGrid.selModel.hasSelection()) {
                            handledelete(roleGrid, 'Borrado', 'idRolePriv', 'authority', getRelativeServerURI('rs/role/{idrole}', roleGrid.selModel.getLastSelected().get('idRolePriv')));
                        } else
                            Ext.Msg.alert('', 'Seleccione un role');
                    }
                }

            }), roleGrid]//ponemos el grid

    });
    setCentralPanel(panel);


    function getO(form, seleuse) {
        var id = form.getComponent("idRolePriv").getValue();
        if (id == "") {
            id = null;
        }
        var o = {
            authority: form.getComponent("authority").getValue(),
            idRolePriv: id,
            userList: getSelectedIds(seleuse, ['idUser'])

        };
        return o;
    }
    function handleFormulario(opt, grid, title, url, selecion) {
        var authority = 'ROLE_', idRolePriv = null, method = 'POST', tooltip = 'Insertar usuario'

        if (opt != 'Inserta' && selecion.hasSelection()) {
            method = 'PUT';
            authority = selecion.getLastSelected().get('authority');
            idRolePriv = selecion.getLastSelected().get('idRolePriv');
            tooltip = 'Modificar usuario';
        }
        var usersGrid = Ext.create('App.manager.js.common.griduser', {
            url: getRelativeServerURI('rs/user/search'),
            width: 300,
            height: 200,
            checkboxse: true,
            page: 500
        });


        //form panel de insertar
        var formpanel = Ext.create('Ext.form.Panel', {
            title: opt + 'r',
            url: url,
            defaultType: 'textfield',
            monitorValid: true,
            autoWidth: true,
            items: [
                {
                    name: 'idRolePriv',
                    id: 'idRolePriv',
                    value: idRolePriv,
                    hidden: true
                },
                {
                    fieldLabel: 'Role',
                    name: 'authority',
                    id: 'authority',
                    value: authority,
                    regex: /ROLE__*/,
                    regextext: 'Debe contener el prefijo ROLE_',
                    allowBlank: false,
                    columnWidth: 1
                },
                usersGrid


            ],
            buttons: [{
                    text: opt + 'r',
                    id: 'formrole',
                    formBind: true,
                    handler: function() {
                        if (formpanel.getForm().isValid()) {
                            AjaxrequestJson(url, getO(formpanel, usersGrid.selModel), method, roleGrid, wind, opt + 'ndo', opt + 'ndo ' + title + '...', opt + 'do', 'Error no se ha podido ' + opt + 'r');
                        }

                    },
                    tooltip: tooltip
                }]
        });
        usersGrid.getStore().load();
        if (opt != 'Inserta') {
            usersGrid.getStore().on('load', function() {
                selectautogrid(usersGrid, idRolePriv, 'idRolePriv', "idUser", getRelativeServerURI('rs/role/{idrole}/users', {idrole: idRolePriv}));
            });
        }

        var wind = Ext.create('Ext.window.Window', {
            id: 'usuario',
            autoScroll: false,
            autoWidth: true,
            closable: true,
            modal: true,
            items: [formpanel]
        });

        wind.show();

        return formpanel;
    }

});