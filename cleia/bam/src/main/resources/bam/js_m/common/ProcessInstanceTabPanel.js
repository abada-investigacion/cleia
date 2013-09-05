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

Ext.define('App.bam.js_m.common.ProcessInstanceTabPanel', {
    requires: ['Abada.Ajax', 'Ext.MessageBox', 'Ext.JSON'],
    extend: 'Ext.tab.Panel',
    config: {
        urlImagePI: 'rs/process/instance/{0}/image',
        urlImageP: 'rs/process/definition/{0}/image',
        urlInfo: 'rs/process/instance/{0}/tree',
        urlDiagramInfo: 'rs/process/definition/{0}/diagram',
        i18n:undefined
    },
    constructor: function(config) {
        this.callParent(arguments);
    },
    loadProcessInstancePanels: function(processInstanceId) {
        this.processInstanceId = processInstanceId;
        //this.removeAll();
        Abada.Ajax.requestJsonData({
            url: getRelativeServerURI(this.config.urlInfo, [processInstanceId]),
            scope: this,
            method: 'GET',
            success: function(json) {
                this.addProcessInstancePanel(json.data, 0);
                //this.doLayout();
            },
            failure: function() {
            }
        });
    },
    addProcessInstancePanel: function(records, i) {
        function handle(cmp) {
            this.add(cmp);
            if (i < records.length - 1) {
                this.addProcessInstancePanel(records, i + 1);
            } else {
                this.getTabBar().add(Ext.create('Ext.Spacer',{}));
                var button=Ext.create('Ext.Button', {
                    text: this.config.i18n.getMsg('bam.toolbar1.back'),
                    ui: 'back'
                });
                button.addListener('tap',function(){
                    this.fireEvent('backButtonTap',this);
                },this);
                this.getTabBar().add(button);
            }
        }

        var panel = Ext.create('App.bam.js_m.common.ProcessInstancePanel', {
            urlImagePI: this.config.urlImagePI,
            urlImageP: this.config.urlImageP,
            urlDiagramInfo: this.config.urlDiagramInfo,
            autoScroll: true,
            processInstanceId: records[i].processInstanceId,
            processId: records[i].processId,
            autoLoad: false
        });
        panel.addListener('click', this.onImageClick, this);
        panel.addListener('nodeselected', this.onNodeSelected, this);
        panel.addListener('diagramloadsuccess', handle, this);
        panel.addListener('diagramloadfailure', handle, this);
        panel.loadDiagramInfo();
    },
    onImageClick: function(cmp, x, y) {
        this.fireEvent('click', this, cmp.processInstanceId, x, y);
    },
    onNodeSelected: function(cmp, processId, nodeName, nodeId, x, y, rX, rY) {
        this.fireEvent('nodeselected', this, processId, nodeName, nodeId, x, y, rX, rY);
    }
});
