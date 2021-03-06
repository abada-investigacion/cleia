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

Ext.define('App.bam.js.common.ProcessInstanceTabPanel', {
    requires: ['Abada.Ajax', 'Ext.window.MessageBox', 'Ext.JSON'],
    extend: 'Ext.tab.Panel',
    config: {
        urlImagePI: 'rs/process/instance/{0}/image',
        urlImageP: 'rs/process/definition/{0}/image',
        urlInfo: 'rs/process/instance/{0}/tree',
        urlDiagramInfo: 'rs/process/definition/{0}/diagram',
        urlDocumentsNode: getRelativeURI('/bam/dms/documentsnode.do'),
        urlDocumentsNodeProcessInstance: getRelativeURI('/bam/dms/process/instance/documentsnode.do'),
        urlJumpInTime: 'rs/process/instance/{0}/jump',
        i18n: undefined,
        mode: 'state'
    },
    initComponent: function() {
        this.callParent();

        this.addEvents('click', 'nodeselected');

        this.imgTooltip = Ext.create('Ext.tip.ToolTip', {
            target: this.getId(),
            dismissDelay: 0,
            trackMouse: true
        });
        this.imgTpl = new Ext.XTemplate('<b>Documentos Generales</b><br />' +
                '<tpl for="doc.data">' +
                '<a href="' + getRelativeURI('/bam/dms/file/read.do') + '?uuid={uuid}&fileName={fileName}" >{fileName}</a><br />' +
                '</tpl>' +
                '<b>' + this.i18n.getMsg('bam.tabinstance.text') + '</b><br />' +
                '<tpl for="patient.data">' +
                '<a href="' + getRelativeURI('/bam/dms/file/read.do') + '?uuid={uuid}&fileName={fileName}" >{fileName}</a><br />' +
                '</tpl>');

        if (this.mode === 'state') {
            var button = Ext.create('Ext.button.Button', {
                icon: getRelativeURI('bam/image/cambio.gif'),
                width: 130,
                text: this.i18n.getMsg('bam.tabinstance.button.text')
            });

            this.addDocked(Ext.create('Ext.toolbar.Toolbar', {
                dock: 'bottom',
                items: [
                    button
                ]
            }));

            button.addListener('click', function(btn, pressed, cfn) {
                function onNodeSelected(parentPanel, processId, nodeName, nodeId) {
                    panel.nextNode({
                        processId: processId,
                        nodeName: nodeName,
                        nodeId: nodeId
                    });
                }
                var panel = Ext.create('App.bam.js.common.JumpPanel', {
                    autoScroll: true,
                    height: 300,
                    width: 400,
                    i18n: this.i18n
                });
                panel.addListener('jump', function(p, store) {
                    function createInfo(store, processInstanceId, observation) {
                        var result = {};
                        result.observation = observation;
                        result.processInstanceId = processInstanceId;
                        result.nodes = [];
                        var aux;
                        for (var i = 0; i < store.count(); i++) {
                            aux = store.getAt(i);
                            result.nodes.push({
                                toNode: {
                                    id: aux.get('nodeIdTo'),
                                    processId: aux.get('processIdTo')
                                },
                                fromNode: {
                                    id: aux.get('nodeIdFrom'),
                                    processId: aux.get('processIdFrom')
                                }
                            });
                        }
                        return Ext.JSON.encode(result);
                    }

                    Ext.Msg.prompt(this.i18n.getMsg('bam.warning'), this.i18n.getMsg('bam.tabinstance.question1.text'), function(btn, text) {
                        var aux = text.substr(0, 2).toLowerCase();
                        if (aux == this.i18n.getMsg('bam.signal.response1') || aux == this.i18n.getMsg('bam.signal.response2')) {
                            Abada.Ajax.requestJson({
                                url: getRelativeServerURI(this.config.urlJumpInTime, [this.processInstanceId]),
                                method: 'POST',
                                scope: this,
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                params: createInfo(store, this.processInstanceId, text.substring(2)),
                                success: function(records) {
                                    this.loadProcessInstancePanels(this.processInstanceId);
                                    win.close();
                                },
                                failure: function(error) {
                                    Ext.Msg.show({
                                        title: this.i18n.getMsg('bam.error'),
                                        msg: this.i18n.getMsg('bam.tabinstance.error1.text', error.reason),
                                        icon: Ext.Msg.ERROR
                                    });
                                }
                            });
                        }
                    }, this, 100);
                }, this);

                panel.startAdding();

                this.addListener('nodeselected', onNodeSelected, this);
                var win = Ext.create('Ext.window.Window', {
                    title: this.i18n.getMsg('bam.tabinstance.jump'),
                    resizable: false,
                    modal: false,
                    items: [panel]
                });
                win.addListener('destroy', function() {
                    this.removeListener('nodeselected', onNodeSelected, this);
                }, this);
                win.show();
            }, this);
        }
    },
    loadProcessInstancePanels: function(processInstanceId) {
        this.processInstanceId = processInstanceId;
        this.removeAll();
        Abada.Ajax.requestJsonData({
            url: getRelativeServerURI(this.config.urlInfo, [processInstanceId]),
            scope: this,
            method: 'GET',
            success: function(json) {
                this.addProcessInstancePanel(json.data, 0);
                this.doLayout();
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
            }
        }

        var panel = Ext.create('App.bam.js.common.ProcessInstancePanel', {
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
        panel.addListener('nodeover', this.onNodeOver, this);
        panel.addListener('diagramloadsuccess', handle, this);
        panel.addListener('diagramloadfailure', handle, this);
        panel.loadDiagramInfo();
    },
    onImageClick: function(cmp, x, y) {
        this.fireEvent('click', this, cmp.processInstanceId, x, y);
    },
    onNodeOver: function(cmp, processId, nodeName, nodeId, x, y, rX, rY) {
        var data = {};
        Abada.Ajax.requestJsonData({
            url: this.config.urlDocumentsNode,
            method: 'GET',
            scope: this,
            params: {
                processId: processId,
                nodeId: nodeId
            },
            success: function(records) {
                data.doc = records;
                Abada.Ajax.requestJsonData({
                    url: this.config.urlDocumentsNodeProcessInstance,
                    method: 'GET',
                    scope: this,
                    params: {
                        processInstanceId: this.processInstanceId
                    },
                    success: function(records) {
                        data.patient = records;
                        if (data.doc || data.patient) {
                            this.imgTooltip.update(this.imgTpl.applyTemplate(data));
                            this.imgTooltip.showAt([rX, rY]);
                        }
                    },
                    failure: function() {
                        if (data.doc || data.patient) {
                            this.imgTooltip.update(this.imgTpl.applyTemplate(data));
                            this.imgTooltip.showAt([rX, rY]);
                        }
                    }
                });
            },
            failure: function() {
                Abada.Ajax.requestJsonData({
                    url: this.config.urlDocumentsNodeProcessInstance,
                    method: 'GET',
                    scope: this,
                    params: {
                        processInstanceId: this.processInstanceId
                    },
                    success: function(records) {
                        data.patient = records;
                        if (data.doc || data.patient) {
                            this.imgTooltip.update(this.imgTpl.applyTemplate(data));
                            this.imgTooltip.showAt([rX, rY]);
                        }
                    },
                    failure: function() {
                        if (data.doc || data.patient) {
                            this.imgTooltip.update(this.imgTpl.applyTemplate(data));
                            this.imgTooltip.showAt([rX, rY]);
                        }
                    }
                });
            }
        });
    },
    onNodeSelected: function(cmp, processId, nodeName, nodeId, x, y, rX, rY) {
        this.fireEvent('nodeselected', this, processId, nodeName, nodeId, x, y, rX, rY);
    }
});
