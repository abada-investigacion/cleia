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
    
    
    var toolbar=Ext.create('Abada.toolbar.ToolbarInsertUpdateDelete', {
        listeners: {
            submitInsert: function() {
                handleFormulario('Inserta', roleGrid, 'Rol', getRelativeServerURI('rs/role'), roleGrid.selModel);
            },
            /* submitUpdate: function() {
                if (roleGrid.selModel.hasSelection()) {
                    if (roleGrid.selModel.getCount() == 1) {
                        handleFormulario('Modifica', roleGrid, 'Usuario', getRelativeServerURI('rs/role/{idrole}', {
                            idrole:roleGrid.selModel.getLastSelected().get('authority')
                            }), roleGrid.selModel);
                    } else {
                        Ext.Msg.alert('', 'Seleccione un rol');
                    }
                } else
                    Ext.Msg.alert('', 'Seleccione un rol');
            },*/
            submitDelete: function() {
                if (roleGrid.selModel.hasSelection()) {
                    var form = {
                        authority: roleGrid.selModel.getLastSelected().get('authority')
                    }
                    var opt = 'borra';
                    
                    Ext.Msg.show({
                        title:'Atenci&oacute;n',
                        msg: '\u00BFEsta seguro que desear borrar el rol '+ form.authority+'?.',
                        buttons: Ext.Msg.YESNO,
                        fn:function (buttonid){
                            
                            if(buttonid=='yes'){
                                
                                doAjaxrequestJson(getRelativeServerURI('rs/role/{idrole}', {
                                    idrole:form.authority
                                }), form, 'DELETE', roleGrid, null, 'Rol '+opt+'do', 'Error. No se ha podido ' + opt + 'r');
                                                     
                            }
                            
                        },
                        icon: Ext.Msg.QUESTION
                    });
                  
                  
                } else
                    Ext.Msg.alert('', 'Seleccione un rol');
            }
        }

    });
    
    toolbar.remove('modificar');
    
    var roleGrid = Ext.create('App.manager.js.common.gridrole', {
        url: getRelativeServerURI('rs/role/search'),
        width: 300,
        height: 400,
        page: 14


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
        items: [toolbar, roleGrid]//ponemos el grid

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
        var authority = 'ROLE_', method = 'POST', tooltip = 'Insertar rol'

        if (opt != 'Inserta' && selection.hasSelection()) {
            method = 'PUT';
            authority = selection.getLastSelected().get('authority');           
            tooltip = 'Modificar rol';
        }
        

        //form panel de insertar
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
                fieldLabel: 'Role',
                name: 'authority',
                id: 'authority',
                value: authority,
                regex: /ROLE__*/,
                regextext: 'Debe contener el prefijo ROLE_',
                allowBlank: false,
                padding: '10 5 5 5',
                columnWidth: 1
            }

            ],
            buttons: [{
                text: opt + 'r',
                id: 'formrole',
                formBind: true,
                handler: function() {
                    if (formpanel.getForm().isValid()) {
                        doAjaxrequestJson(url, getO(formpanel), method, roleGrid, wind, 'Rol '+ opt + 'do', 'Error no se ha podido ' + opt + 'r');
                    }

                },
                tooltip: tooltip
            }]
        });
        
        
        var wind = Ext.create('Ext.window.Window', {
            title: opt + 'r',
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