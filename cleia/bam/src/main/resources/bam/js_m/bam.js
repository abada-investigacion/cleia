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
        var i18n = Ext.create('Abada.i18n.Bundle', {
            path: getRelativeURI('/bam/locale'),
            bundle: 'messages',
            insertLocale: false
        });

        i18n.on('error', function() {
            i18n.language = i18n.defaultLanguage;
            i18n.load();
        });

        i18n.on('loaded', function() {
            function principalAction(removePanel) {

                var panel2 = Ext.create('App.bam.js_m.common.ProcessList', {
                    url: getRelativeServerURI('rs/patient/pinstance/list'), //FIXME
                    title: i18n.getMsg('bam.process'),
                    i18n:i18n,
                    listeners: {
                        processselected: function(list, processInstanceId) {
                            processSelectedAction(processInstanceId, panel);
                        }
                    }
                });

                var panel = Ext.create('Ext.Container', {
                    fullscreen: true,
                    items: [
                        {
                            xtype: 'toolbar',
                            docked: 'top',
                            title: i18n.getMsg('bam.process'),
                            height: 30,
                            items: [
                                {
                                    ui: 'back',
                                    text: i18n.getMsg('bam.toolbar1.back'),
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
                    Ext.Viewport.remove(removePanel, true);

                Ext.Viewport.add(panel);
            }

            function processSelectedAction(processInstanceId, removePanel) {
                var imageOncoguide = Ext.create('App.bam.js_m.common.ProcessInstanceTabPanel', {
                    listeners: {
                        backButtonTap: function() {
                            principalAction(imageOncoguide);
                        }
                    }
                });

                imageOncoguide.loadProcessInstancePanels(processInstanceId);

                if (removePanel)
                    Ext.Viewport.remove(removePanel, true);
                Ext.Viewport.add(imageOncoguide);
            }

            principalAction();
        });
        i18n.load();
    }});