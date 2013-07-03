/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.require([
    'Ext.form.Panel', 'Ext.form.field.Checkbox', 'Abada.Ajax', 'Ext.JSON', 'Ext.Ajax',
    'Ext.layout.container.Table', 'Abada.toolbar.ToolbarInsertUpdateDelete'

    ])

Ext.onReady(function() {

    var toolbar = Ext.create('Abada.toolbar.ToolbarInsertUpdateDelete', {
        listeners: {
            submitInsert: function() {
                handleFormulario('Inserta', usersGrid, 'Usuario', getRelativeServerURI('rs/user'), usersGrid.selModel);
            },
            submitUpdate: function() {
                if (usersGrid.selModel.hasSelection()) {
                    if (usersGrid.selModel.getCount() == 1) {
                        handleFormulario('Modifica', usersGrid, 'Usuario', getRelativeServerURI('rs/user/{iduser}', {
                            iduser:usersGrid.selModel.getLastSelected().get('id')
                            }), usersGrid.selModel);
                    } else {
                        Ext.Msg.alert('', 'Seleccione un usuario');
                    }
                } else
                    Ext.Msg.alert('', 'Seleccione un usuario');
            },
            submitDelete: function() {
                if (usersGrid.selModel.hasSelection()) {
                    var form = {
                        enabled: !usersGrid.selModel.getLastSelected().get('enabled'),
                        id: usersGrid.selModel.getLastSelected().get('id')
                    }
                    var opt = 'modifica', status = 'habilita';
                 
                    if (usersGrid.selModel.getLastSelected().get('enabled')) {
                        status = 'deshabilita'
                    }                  
                    doAjaxrequestJson(getRelativeServerURI('rs/user/{iduser}/{enable}', {
                        iduser:form.id,
                        enable:form.enabled
                        }), form, 'PUT', usersGrid, null, 'Usuario '+status+'do', 'Error. No se ha podido ' + status + 'r');
              
                } else
                    Ext.Msg.alert('', 'Seleccione un usuario');
            }
        }

    });

    var usersGrid = Ext.create('App.manager.js.common.griduserexpander', {
        url: getRelativeServerURI('rs/user/search'),
        width: 300,
        height: 400,
        page: 14
    });

    usersGrid.getStore().load({
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
        items: [toolbar, usersGrid]//ponemos el grid

    });
    toolbar.getComponent("Borrar").setText("Habilitar/Deshabilitar");
    setCentralPanel(grid);

    //*Funcion para los frompanel
    function getO(form, selectionGroup, selectionRole) {
        var id = form.getComponent("id").getValue();
        if (id == "") {
            id = null;
        }
        var o = {
            id: id,
            enabled: form.getComponent("enabled").getValue(),
            username: form.getComponent("username").getValue(),
            accountNonExpired: form.getComponent("accountNonExpired").getValue(),
            credentialsNonExpired: form.getComponent("credentialsNonExpired").getValue(),
            password: form.getComponent("password").getValue(),
            accountNonLocked: form.getComponent("accountNonLocked").getValue(),
            groups: getListForObject(selectionGroup,'value'),
            roles: getListForObject(selectionRole,'authority')

        };
        return o;
    }
    
    

    function handleFormulario(opt, grid, title, url, selection) {
        
        var username, contrasena, id, method = 'POST', tooltip = 'Insertar usuario', enabled = true;

        if (opt != 'Inserta' && selection.hasSelection()) {
            method = 'PUT';
            username = selection.getLastSelected().get('username');
            contrasena = selection.getLastSelected().get('password');
            id = selection.getLastSelected().get('id');
            enabled = selection.getLastSelected().get('enabled');
            tooltip = 'Modificar usuario';
        }

        var groupGrid = Ext.create('App.manager.js.common.gridgroup', {
            url: getRelativeServerURI('rs/group/search'),
            width: 250,
            height: 250,
            checkboxse: true,
            page: 500,
            rowspan: 4
        });
        var roleGrid = Ext.create('App.manager.js.common.gridrole', {
            url: getRelativeServerURI('rs/role/search'),
            width: 250,
            height: 250,
            checkboxse: true,
            page: 500,
            rowspan: 4
        });

        var checkboxenabled = Ext.create('Ext.form.field.Checkbox', {
            checked: enabled,
            fieldLabel: 'Habilitar',
            id: 'enabled',
            name: 'enabled',
            width: 250

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
            title: opt + 'r',
            url: url,
            defaultType: 'textfield',
            monitorValid: true,
            layout: {
                type: 'table',
                columns: 3
            },
            items: [
            {
                fieldLabel: 'Usuario',
                name: 'username',
                id: 'username',
                value: username,
                allowBlank: false,
                width: 220
            }, groupGrid, roleGrid, {
                fieldLabel: 'Contrase&ntilde;a',
                name: 'password',
                id: 'password',
                allowBlank: false,
                inputType: 'password',
                value: contrasena,
                width: 220
            },
            {
                fieldLabel: 'Repita Contrase&ntilde;a',
                name: 'password2',
                id: 'password2',
                allowBlank: false,
                inputType: 'password',
                value: contrasena,
                width: 220

            }, checkboxenabled, checkboxaccountNonExpired, {
                name: 'id',
                id: 'id',
                value: id,
                hidden: true
            }, checkboxcredentialsNonExpired, checkboxaccountNonLocked


            ],
            buttons: [{
                text: opt + 'r',
                id: 'formuser',
                formBind: true,
                handler: function() {
                    if (formpanel.getComponent("password2").getValue() == formpanel.getComponent("password").getValue()) {
                        if (formpanel.getForm().isValid()) {
                            var form = getO(formpanel, groupGrid.selModel, roleGrid.selModel)
                            doAjaxrequestJson(url, form, method, usersGrid, wind, 'Usuario '+ opt + 'do', 'Error no se ha podido ' + opt + 'r');
                        }
                    } else {
                        Ext.Msg.alert('Error', 'la contrase&ntilde;a no son iguales');

                    }
                },
                tooltip: tooltip
            }]
        });
        roleGrid.getStore().load();
        groupGrid.getStore().load();
        /*
         *en caso de modificar seleciona los id de los grupos y roles del usuario elegido
         */

        roleGrid.getStore().on('load', function() {
          
            if (opt != 'Inserta') {
                var roles = selection.getLastSelected().get('roles');
                for (var i = 0; i < roleGrid.getStore().getCount(); i++) {
                    var record = roleGrid.getStore().getAt(i);
                    for (var j = 0; j < roles.length; j++) {
                        if (record.get("authority") == roles[j].authority) {
                            roleGrid.selModel.select(record, true, true);
                        }
                    }
                }
            }
        });
        if (opt != 'Inserta') {
            groupGrid.getStore().on('load', function() {
                var groups = selection.getLastSelected().get('groups');
                for (var i = 0; i < groupGrid.getStore().getCount(); i++) {
                    var record = groupGrid.getStore().getAt(i);
                    for (var j = 0; j < groups.length; j++) {
                        if (record.get("value") == groups[j].value) {
                            groupGrid.selModel.select(record, true, true);
                        }
                    }

                }
            });
        }


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