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
    'App.bam.js_m.common.ProcessList'
]);
Ext.setup({
    viewport: {
        fullscreen: true
    },
    onReady: function() {

        function principalAction(removePanel) {

            var panel2 = Ext.create('App.bam.js_m.common.ProcessList', {
                url: getRelativeServerURI('rs/patient/pinstance/list'),//FIXME
                title: 'Procesos:',
                listeners: {
                    processselected: function(list, taskId, url) {
                        taskSelectedAction(taskId,url,panel);
                    }
                }
            });

            var panel = Ext.create('Ext.Container', {
                fullscreen: true,
                items: [
                    {
                        xtype: 'toolbar',
                        docked: 'top',
                        title: 'Procesos:',
                        height: 30,
                        items: [
                            {
                                ui: 'back',
                                text: 'Atras',
                                handler: function() {
                                    window.location = getRelativeURI('main.htm');
                                }
                            }
                        ]
                    }, {
                        xtype: 'container',
                        items: [panel2]
                    }
                ]
            });

            if (removePanel)
                Ext.Viewport.remove(removePanel,true);
            
            Ext.Viewport.add(panel);
        }

        function taskSelectedAction(taskId, url,removePanel) {
            var panelAux = Ext.create('App.bam.js_m.common.FormCustomPanel', {
                taskId: taskId,
                url: url
            });
            panelAux.addListener('success', function(panel2, frame, response) {
                Ext.Msg.alert('Info', 'Tarea completada correctamente',
                        function() {
                            principalAction(panel);
                        });
            }, this);
            panelAux.addListener('failure', function(panel2, frame, text) {
                Ext.Msg.alert('Error', 'No se puede completar la tarea.' + text,
                        function() {
                            principalAction(panel);
                        });
            }, this);

            var panel = Ext.create('Ext.Container', {
                fullscreen: true,
                items: [
                    {
                        xtype: 'toolbar',
                        docked: 'top',
                        title: 'Tarea ' + taskId,
                        height: 30,
                        items: [
                            {
                                ui: 'back',
                                text: 'Atras',
                                handler: function() {
                                    principalAction(panel);
                                }
                            }
                        ]
                    }, {
                        xtype: 'container',
                        items: [panelAux]
                    }
                ]
            });

            if (removePanel)
                Ext.Viewport.remove(removePanel,true);
            Ext.Viewport.add(panel);
        }

        principalAction();
    }});