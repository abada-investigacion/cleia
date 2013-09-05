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

Ext.require(['Ext.window.Window']);



Ext.onReady(function() {
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
        var startPanel = Ext.create('Abada.form.field.Date', {});
        var endPanel = Ext.create('Abada.form.field.Date', {});
        var cbOncoguide = Ext.create('Abada.form.field.SimpleGroupingComboBox', {
            url: getRelativeServerURI('/rs/process/definition/list/combo')
        });
        var button = Ext.create('Ext.button.Button', {
            text: i18n.getMsg('bam.statistic.button1.text'),
            scope: this,
            handler: function() {
                if (cbOncoguide.getValue()) {
                    var panelAux = Ext.create('App.bam.js.common.ProcessInstancePanel', {
                        urlImagePI: 'rs/process/statistic/{0}/number/image',
                        processInstanceId: cbOncoguide.getValue(),
                        start: startPanel.getValue(),
                        end: endPanel.getValue()
                    });
                } else {
                    Ext.Msg.alert(i18n.getMsg('bam.info'), i18n.getMsg('bam.statistic.alert1.text'));
                }


                var win = Ext.create('Ext.window.Window', {
                    title: i18n.getMsg('bam.statistic.window1.text',cbOncoguide.getValue()),
                    height: 600,
                    width: 900,
                    autoScroll: true,
                    layout: 'fit',
                    modal: true,
                    items: [panelAux]
                });
                win.show();
            }
        });

        var panel = Ext.create('Ext.panel.Panel', {
            title: i18n.getMsg('bam.statistic.title'),
            height: App.height,
            items: [
                cbOncoguide, startPanel, endPanel, button
            ]
        });

        setCentralPanel(panel);
    });

    i18n.load();
});
