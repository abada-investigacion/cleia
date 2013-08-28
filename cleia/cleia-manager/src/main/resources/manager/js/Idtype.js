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
    'Ext.form.Panel', 'Ext.form.field.Checkbox', 'Abada.Ajax', 'Ext.JSON', 'Ext.Ajax','Abada.toolbar.ToolbarInsertUpdateDelete'])

Ext.onReady(function() {

    var toolbar = Ext.create('Abada.toolbar.ToolbarInsertUpdateDelete', {
        listeners: {
            submitInsert: function() {
                handleFormulario('Inserta', idtypeGrid, 'Tipo de Identificador', getRelativeServerURI('rs/idtype'), idtypeGrid.selModel);
            },
            submitUpdate: function() {
                if (idtypeGrid.selModel.hasSelection()) {
                    if (idtypeGrid.selModel.getCount() == 1) {
                        handleFormulario('Modifica', idtypeGrid, 'Tipo de Identificador', getRelativeServerURI('rs/idtype/{ididtype}', {
                            ididtype:idtypeGrid.selModel.getLastSelected().get('value')
                        }), idtypeGrid.selModel);
                    } else {
                        Ext.Msg.alert('', 'Seleccione un tipo de identificador');
                    }
                } else
                    Ext.Msg.alert('', 'Seleccione un tipo de identificador');
            },
            submitDelete: function() {
                if (idtypeGrid.selModel.hasSelection()) {
                 
                    var status='borra';
                    
                    Ext.Msg.show({
                        title:'Atenci&oacute;n',
                        msg: '\u00BFEsta seguro que desear borrar el identificador '+ idtypeGrid.selModel.getLastSelected().get('value')+'?.',
                        buttons: Ext.Msg.YESNO,
                        fn:function (buttonid){
                            
                            if(buttonid=='yes'){                                
                                doAjaxrequestJson(getRelativeServerURI('rs/idtype/{ididtype}', {
                                    ididtype:idtypeGrid.selModel.getLastSelected().get('value')
                                }), null, 'DELETE', idtypeGrid, null, 'Identificador de usuario '+status+'do', 'Error. No se ha podido ' + status + 'r');
                                                                   
                            }
                            
                        },
                        icon: Ext.Msg.QUESTION
                    });
                    
                    
                } else
                    Ext.Msg.alert('', 'Seleccione un Tipo de Identificador');
            }
        }

    });

    var idtypeGrid = Ext.create('App.patient.js.common.gridIdtype', {
        url: getRelativeServerURI('rs/idtype/search'),
        width: 500,
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
        var id = form.getComponent("value").getValue();
        if (id == "") {
            id = null;
        }
        var o = {
            value: id,
            description: form.getComponent("description").getValue(),
            repeatable: form.getComponent("repeatable").getValue()
        };
        return o;
    }

    function handleFormulario(opt, grid, title, url, selection) {
        var value, description, method = 'POST', tooltip = 'Insertar Tipo de Identificador', repeatable = false, readOnly=false;

        if (opt != 'Inserta' && selection.hasSelection()) {
            method = 'PUT';
            value = selection.getLastSelected().get('value');
            description=selection.getLastSelected().get('description');
            repeatable = selection.getLastSelected().get('repeatable');
            tooltip = 'Modificar Tipo de Identificador';
            readOnly=true;
        }


        var repeat = Ext.create('Ext.form.field.Checkbox', {
            checked: repeatable,
            fieldLabel: '\u00BFSe puede repetir?',
            labelWidth:150,
            id: 'repeatable',
            name: 'repeatable',
            padding:'5 5 5 5'
        });


        //form panel de insertar
        var formpanel = Ext.create('Ext.form.Panel', {
            url: url,
            defaultType: 'textfield',
            monitorValid: true,
            items: [
            {
                fieldLabel:'Nombre',
                name: 'value',
                id: 'value',
                value: value,
                readOnly:readOnly,
                padding:'5 5 5 5'
            },
            {
                fieldLabel: 'Descripci&oacute;n',
                name: 'description',
                id: 'description',
                value: description,
                allowBlank: false,
                padding:'5 5 5 5'
            }, repeat
            ],
            buttons: [{
                text: opt + 'r',
                id: 'formIdtype',
                formBind: true,
                handler: function() {
                    if (formpanel.getForm().isValid()) {
                      
                        doAjaxrequestJson(url, getO(formpanel), method, idtypeGrid, wind, 'Tipo de identificador '+ opt + 'do', 'Error. No se ha podido ' + opt + 'r');
                    }
                },
                tooltip: tooltip
            }]
        });

        var wind = Ext.create('Ext.window.Window', {
            title: opt + 'r',
            id: 'winidtype',
            autoScroll: false,
            autoWidth:true,
            autoHeight:true,
            closable: true,
            modal: true,
            items: [formpanel]
        });

        wind.show();

        return formpanel;
    }


});