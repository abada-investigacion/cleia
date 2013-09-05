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
    'Ext.layout.container.Column', 'Abada.toolbar.ToolbarInsertUpdateDelete'

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
    
    
        var toolbar=Ext.create('Abada.toolbar.ToolbarInsertUpdateDelete', {
            listeners: {
                submitInsert: function() {
                    handleFormulario('insert', roleGrid, '', getRelativeServerURI('rs/role'), roleGrid.selModel);
                },
                submitDelete: function() {
                    if (roleGrid.selModel.hasSelection()) {
                        var form = {
                            authority: roleGrid.selModel.getLastSelected().get('authority')
                        }
                                      
                        Ext.Msg.show({
                            title:i18n.getMsg('manager.attention'),
                            msg: i18n.getMsg('manager.deleteRoleMsg')+ form.authority+'?.',
                            buttons: Ext.Msg.YESNO,
                            fn:function (buttonid){
                            
                                if(buttonid=='yes'){
                                
                                    doAjaxrequestJson(getRelativeServerURI('rs/role/{idrole}', {
                                        idrole:form.authority
                                    }), form, 'DELETE', roleGrid, null, i18n.getMsg('manager.operationSucess'), i18n.getMsg('manager.operationError'));
                                                     
                                }
                            
                            },
                            icon: Ext.Msg.QUESTION
                        });
                  
                  
                    } else
                        Ext.Msg.alert('', i18n.getMsg('manager.selectRole'));
                }
            }

        });
    
        toolbar.remove('modificar');
    
        var roleGrid = Ext.create('App.manager.js.common.gridrole', {
            title:i18n.getMsg('manager.grid.rolesGridTitle'),
            url: getRelativeServerURI('rs/role/search'),
            width: 300,
            height: 400,
            page: 14,
            i18n:i18n


        });

        roleGrid.getStore().load({
            params: {
                start: 0,
                limit: 14
            }
        });

        var panel = Ext.create('Ext.panel.Panel', {
            frame: false,
            autoWidth: true,
            autoHeight: true,
            title: '',
            items: [toolbar, roleGrid]

        });
        setCentralPanel(panel);


        function getO(form) {
            var authority = form.getComponent("authority").getValue();
            if (authority == "") {
                authority = null;
            }
            var o = {
                authority: authority

            };
            return o;
        }
    
    
        function handleFormulario(opt, grid, title, url, selection) {
            var authority = 'ROLE_', method = 'POST';

            if (opt != 'insert' && selection.hasSelection()) {
                method = 'PUT';
                authority = selection.getLastSelected().get('authority');           
            }
        

            var formpanel = Ext.create('Ext.form.Panel', {           
                url: url,
                defaultType: 'textfield',
                monitorValid: true,
                autoWidth: true,
                layout: {
                    type: 'hbox',
                    columns: 2
                },
                items: [
                {
                    fieldLabel: i18n.getMsg('manager.form.role'),
                    name: 'authority',
                    id: 'authority',
                    value: authority,
                    regex: /ROLE__*/,
                    regextext: i18n.getMsg('manager.form.prefixRegex'),
                    allowBlank: false,
                    padding: '10 5 5 5',
                    columnWidth: 1
                }

                ],
                buttons: [{
                    text: i18n.getMsg('manager.send'),
                    id: 'formrole',
                    formBind: true,
                    handler: function() {
                        if (formpanel.getForm().isValid()) {
                            doAjaxrequestJson(url, getO(formpanel), method, roleGrid, wind, i18n.getMsg('manager.operationSucess'), i18n.getMsg('manager.operationError'));
                        }

                    }
                }]
            });
        
            var windTitle;
            
            if(opt=='insert'){
                windTitle=i18n.getMsg('manager.wind.insertRoleTitle');
            }else{
                windTitle=i18n.getMsg('manager.wind.updateRoleTitle');
            }
            
            var wind = Ext.create('Ext.window.Window', {
                title: windTitle,
                id: 'rolewind',
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
    i18n.load();

});