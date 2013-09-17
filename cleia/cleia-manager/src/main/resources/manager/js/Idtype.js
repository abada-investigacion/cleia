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
                    handleFormulario('insert', idtypeGrid, '', getRelativeServerURI('rs/idtype'), idtypeGrid.selModel);
                },
                submitUpdate: function() {
                    if (idtypeGrid.selModel.hasSelection()) {
                        if (idtypeGrid.selModel.getCount() == 1) {
                            handleFormulario('update', idtypeGrid, '', getRelativeServerURI('rs/idtype/{ididtype}', {
                                ididtype:idtypeGrid.selModel.getLastSelected().get('value')
                            }), idtypeGrid.selModel);
                        } else {
                            Ext.Msg.alert('', i18n.getMsg('manager.selectIdentifierType'));
                        }
                    } else
                        Ext.Msg.alert('', i18n.getMsg('manager.selectIdentifierType'));
                },
                submitDelete: function() {
                    if (idtypeGrid.selModel.hasSelection()) {
                                     
                        Ext.Msg.show({
                            title:i18n.getMsg('manager.attention'),
                            msg: i18n.getMsg('manager.deleteIdentifierTypeMsg')+ idtypeGrid.selModel.getLastSelected().get('value')+'?.',
                            buttons: Ext.Msg.YESNO,
                            fn:function (buttonid){
                            
                                if(buttonid=='yes'){                                
                                    doAjaxrequestJson(getRelativeServerURI('rs/idtype/{ididtype}', {
                                        ididtype:idtypeGrid.selModel.getLastSelected().get('value')
                                    }), null, 'DELETE', idtypeGrid, null, i18n.getMsg('manager.operationSucess'), i18n.getMsg('manager.operationError'));
                                                                   
                                }
                            
                            },
                            icon: Ext.Msg.QUESTION
                        });
                    
                    
                    } else
                        Ext.Msg.alert('', i18n.getMsg('manager.selectIdentifierType'));
                }
            }

        });

        var idtypeGrid = Ext.create('App.patient.js.common.gridIdtype', {
            title:i18n.getMsg('manager.grid.idsGridTitle'),
            url: getRelativeServerURI('rs/idtype/search'),
            width: 500,
            height: 400,
            page: 14,
            i18n:i18n
        });

        idtypeGrid.getStore().load({
            params: {
                start: 0,
                limit: 14
            }
        });

        var panel = Ext.create('Ext.panel.Panel', {
            autoWidth: true,
            autoHeight: true,
            title: '',
            items: [toolbar, idtypeGrid]

        });

        setCentralPanel(panel);


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
            var value, description, method = 'POST', repeatable = false, readOnly=false;

            if (opt != 'insert' && selection.hasSelection()) {
                method = 'PUT';
                value = selection.getLastSelected().get('value');
                description=selection.getLastSelected().get('description');
                repeatable = selection.getLastSelected().get('repeatable');
                readOnly=true;
            }


            var repeat = Ext.create('Ext.form.field.Checkbox', {
                checked: repeatable,
                fieldLabel:i18n.getMsg('manager.idtypeRepeatableMsg') +'?',
                labelWidth:150,
                id: 'repeatable',
                name: 'repeatable',
                padding:'5 5 5 5'
            });


            var formpanel = Ext.create('Ext.form.Panel', {
                url: url,
                defaultType: 'textfield',
                monitorValid: true,
                items: [
                {
                    fieldLabel:i18n.getMsg('manager.form.idtypeName'),
                    name: 'value',
                    id: 'value',
                    value: value,
                    readOnly:readOnly,
                    padding:'5 5 5 5'
                },
                {
                    fieldLabel: i18n.getMsg('manager.form.idtypeDescription'),
                    name: 'description',
                    id: 'description',
                    value: description,
                    allowBlank: false,
                    padding:'5 5 5 5'
                }, repeat
                ],
                buttons: [{
                    text: i18n.getMsg('manager.send'),
                    id: 'formIdtype',
                    formBind: true,
                    handler: function() {
                        if (formpanel.getForm().isValid()) {
                      
                            doAjaxrequestJson(url, getO(formpanel), method, idtypeGrid, wind, i18n.getMsg('manager.operationSucess'), i18n.getMsg('manager.operationError') );
                        }
                    }
                }]
            });

            var windTitle;
            
            if(opt=='insert'){
                windTitle=i18n.getMsg('manager.wind.insertIdTypeTitle');
            }else{
                windTitle=i18n.getMsg('manager.wind.updateIdTypeTitle');
            }
            
            var wind = Ext.create('Ext.window.Window', {
                title: windTitle,
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

    i18n.load();

});