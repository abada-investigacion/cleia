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
    'Ext.layout.container.Table', 'Abada.toolbar.ToolbarInsertUpdateDelete','Abada.form.field.ComboBox'

    ])

Ext.onReady(function() {
    var i18n = Ext.create('Abada.i18n.Bundle', {
        path: getRelativeURI('/manager/locale'),
        bundle: 'messages',
        insertLocale: false
    });

    i18n.on('error', function() {
        i18n.language = i18n.defaultLanguage;
        i18n.load();
    });

    i18n.on('loaded', function() {
        var toolbar = Ext.create('Abada.toolbar.ToolbarInsertUpdateDelete', {
            listeners: {
                submitInsert: function() {
                    handleFormulario('insert', usersGrid, '', getRelativeServerURI('rs/user'), usersGrid.selModel);
                },
                submitUpdate: function() {
                    if (usersGrid.selModel.hasSelection()) {
                        if (usersGrid.selModel.getCount() == 1) {
                            handleFormulario('update', usersGrid, '', getRelativeServerURI('rs/user/{iduser}', {
                                iduser: usersGrid.selModel.getLastSelected().get('id')
                            }), usersGrid.selModel);
                        } else {
                            Ext.Msg.alert('', i18n.getMsg('manager.selectUser'));
                        }
                    } else
                        Ext.Msg.alert('', i18n.getMsg('manager.selectUser'));
                },
                submitDelete: function() {
                    if (usersGrid.selModel.hasSelection()) {
                        var form = {
                            enabled: !usersGrid.selModel.getLastSelected().get('enabled'),
                            id: usersGrid.selModel.getLastSelected().get('id')
                        }
                        
                        doAjaxrequestJson(getRelativeServerURI('rs/user/{iduser}/{enable}', {
                            iduser: form.id,
                            enable: form.enabled
                        }), form, 'PUT', usersGrid, null, i18n.getMsg('manager.operationSucess'), i18n.getMsg('manager.operationError'));

                        usersGrid.selModel.deselectAll();

                    } else
                        Ext.Msg.alert('', i18n.getMsg('manager.selectUser'));
                }
            }

        });

        var usersGrid = Ext.create('App.manager.js.common.griduserexpander', {
            title:i18n.getMsg('manager.grid.userGridTitle'),
            url: getRelativeServerURI('rs/user/search'),
            width: 500,
            height: 400,
            padding: '5 5 5 5',
            page: 14,
            i18n:i18n
        });

        usersGrid.getStore().load({
            params: {
                start: 0,
                limit: 14
            }
        });

        var panel = Ext.create('Ext.panel.Panel', {
            autoWidth: true,
            autoHeight: true,
            title: '',
            items: [toolbar, usersGrid]

        });
        toolbar.getComponent("Borrar").setText(i18n.getMsg('manager.toolbar.enable/disable'));
        setCentralPanel(panel);

        //*Funcion para los frompanel
        function getO(form, selectionGroup, selectionRole, idGridStore) {
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
            var id = Ext.getCmp("id").getValue();
            if (id == "") {
                id = null;
            }
            var o = {
                id: id,
                enabled: Ext.getCmp("enabled").getValue(),
                username: Ext.getCmp("username").getValue(),
                accountNonExpired: true,
                credentialsNonExpired: true,
                password: Ext.getCmp("password").getValue(),
                accountNonLocked: true,
                groups: getListForObject(selectionGroup, 'value'),
                roles: getListForObject(selectionRole, 'authority'),
                ids: ids
            };

            return o;
        }



        function handleFormulario(opt, grid, title, url, selection) {

            var username, contrasena, id, idList, method = 'POST', enabled = true;

            if (opt != 'insert' && selection.hasSelection()) {
                method = 'PUT';
                username = selection.getLastSelected().get('username');
                contrasena = selection.getLastSelected().get('password');
                id = selection.getLastSelected().get('id');
                enabled = selection.getLastSelected().get('enabled');
                idList= selection.getLastSelected().get('ids');
            }

            var groupGrid = Ext.create('App.manager.js.common.gridgroup', {
                //  title: i18n.getMsg('manager.grid.groupGridTitle'),
                url: getRelativeServerURI('rs/group/search'),
                width: 400,
                height: 250,
                checkboxse: true,
                page: 500,
                rowspan: 4,
                i18n:i18n
            });
            
            var roleGrid = Ext.create('App.manager.js.common.gridrole', {
                // title: i18n.getMsg('manager.grid.rolesGridTitle'),
                url: getRelativeServerURI('rs/role/search'),
                width: 400,
                height: 250,
                checkboxse: true,
                page: 500,
                rowspan: 4,
                i18n:i18n
            });

            var configIdGrid = {
                title: '',
                width: 400,
                height: 250,
                page: 500,
                rowspan: 4,
                i18n:i18n
            };
            
         
            var idGrid = Ext.create('App.manager.js.common.gridids', configIdGrid);
            
            for(var i=0;i<idList.length;i++){
                
                idGrid.getStore().insert(0, {
                    value: idList[i].value,
                    idtype: idList[i].type.value
                });
                
            }            

            var comboidtype = Ext.create('Abada.form.field.ComboBox', {
                id: 'cbidtype',
                url: getRelativeServerURI('rs/idtype/search/combo'),
                fieldLabel: i18n.getMsg('manager.combo.type'),
                emptyText: i18n.getMsg('manager.combo.selectType'),
                padding: '0 15 10 0',
                width: 150,
                editable: false,
                labelWidth: 50,
                labelAlign: 'top',
                selectedValue: ''
            });

            comboidtype.loadStore();

            var checkboxenabled = Ext.create('Ext.form.field.Checkbox', {
                checked: enabled,
                fieldLabel: i18n.getMsg('manager.checkbox.enable'),
                id: 'enabled',
                name: 'enabled',
                labelWidth: 125

            });

     
            var formpanel = Ext.create('Ext.form.Panel', {
                url: url,
                defaultType: 'textfield',
                monitorValid: true,
                bodyPadding: '10 15 0 15',
                autoScroll: true,
                height: 500,
                layout: {
                    type: 'vbox',
                    columns: 2
                },
                defaults: {
                },
                items: [{
                    xtype: 'fieldset',
                    title: '<b>'+i18n.getMsg('manager.form.fieldset.userData')+'</b>',
                    width: '100%',
                    collapsible: false,
                    defaultType: 'textfield',
                    padding: '10 15 10 15',
                    items: [
                    {
                        fieldLabel:i18n.getMsg('manager.form.user'),
                        name: 'username',
                        id: 'username',
                        value: username,
                        allowBlank: false,
                        labelWidth: 125,
                        width: 400
                    }, {
                        fieldLabel: i18n.getMsg('manager.form.password'),
                        name: 'password',
                        id: 'password',
                        allowBlank: false,
                        inputType: 'password',
                        value: contrasena,
                        labelWidth: 125,
                        width: 400
                    },
                    {
                        fieldLabel: i18n.getMsg('manager.form.repeatPassword'),
                        name: 'password2',
                        id: 'password2',
                        allowBlank: false,
                        inputType: 'password',
                        value: contrasena,
                        labelWidth: 125,
                        width: 400

                    }, checkboxenabled, {
                        name: 'id',
                        id: 'id',
                        value: id,
                        hidden: true
                    }]
                }, {
                    xtype: 'fieldset',
                    title: '<b>'+i18n.getMsg('manager.form.fieldset.identifiers')+'</b>',
                    width: '100%',
                    collapsible: false,
                    padding: '10 15 10 15',
                    items: [
                    {
                        xtype: 'container',
                        layout: 'hbox',
                        items: [{
                            xtype: 'textfield',
                            fieldLabel: i18n.getMsg('manager.form.identifierNumber'),
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
                    title: '<b>'+i18n.getMsg('manager.form.fieldset.services')+'</b>',
                    width: '100%',
                    collapsible: false,
                    padding: '10 15 10 15',
                    items: [groupGrid]
                }, {
                    xtype: 'fieldset',
                    title: '<b>'+i18n.getMsg('manager.form.fieldset.roles')+'</b>',
                    width: '100%',
                    collapsible: false,
                    padding: '10 15 10 15',
                    items: [roleGrid]
                }


                ],
                buttons: [{
                    text: i18n.getMsg('manager.send'),
                    id: 'formuser',
                    formBind: true,
                    handler: function() {
                        if (Ext.getCmp("password2").getValue() == Ext.getCmp("password").getValue()) {
                            if (formpanel.getForm().isValid()) {
                                var form = getO(formpanel, groupGrid.selModel, roleGrid.selModel, idGrid.getStore())
                                doAjaxrequestJson(url, form, method, usersGrid, wind,i18n.getMsg('manager.operationSucess'), i18n.getMsg('manager.operationError') );
                            }
                        } else {
                            Ext.Msg.alert('Error',i18n.getMsg('manager.form.passwordNotEquals'));

                        }
                    }
                }]
            });
   

            var windTitle;
            
            if(opt=='insert'){
                windTitle=i18n.getMsg('manager.wind.insertUserTitle');
            }else{
                windTitle=i18n.getMsg('manager.wind.updateUserTitle');
            }

            var wind = Ext.create('Ext.window.Window', {
                title: windTitle,
                id: 'usuario',
                autoScroll: false,
                closable: true,
                modal: true,
                width: 500,
                autoHeight: true,
                items: [formpanel]
            });

            wind.show();

            if (opt != 'insert') {
                roleGrid.getStore().on('load', function() {

                    if (opt != 'insert') {
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

            roleGrid.getStore().load();
            groupGrid.getStore().load();


            return formpanel;
        }
    });

    i18n.load();

});