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

Ext.require(['Abada.Ajax', 'Ext.XTemplate', 'Abada.tab.panel.TabPanel', 'App.bam.js.common.ProcessInstanceTabPanel', 'App.bam.js.common.ProcessInstancePanel', 'Abada.data.JsonStore', 'Ext.data.proxy.Ajax', 'Ext.window.MessageBox', 'App.bam.js.common.VersionFormPanel', 'App.bam.js.common.ProcessTabPanel', 'App.bam.js.common.JumpPanel', , 'App.bam.js.common.JumpNodeGrid']);

Ext.onReady(function() {
    function showVersionChangeForm(patientId, processInstanceId, processId, patientname) {
        var tbar = Ext.create('App.bam.js.common.PrincipalToolbar', {
            patientname: patientname
        });
        tbar.addListener('back', function(toolbar) {
            modeOncoguideList(patientId, processInstanceId, patientname);
        });

        var panelAux = Ext.create('App.bam.js.common.VersionFormPanel', {
            title: 'Cambio Versi&oacute;n ' + processId + ' ' + processInstanceId,
            processInstanceId: processInstanceId,
            tbar: tbar,
            autoScroll: true
        });
        panelAux.addListener('success', function(panel) {
            modeOncoguideList(patientId, processInstanceId, patientname);
        });
        setCentralPanel(panelAux, true);
    }

    /**
     *Screen for Signal associated with
     */
    function modeSignalOnconguide(patientId, processInstanceId, processId, patientname) {
        var tbar = Ext.create('App.bam.js.common.PrincipalToolbar', {
            patientname: patientname
        });
        tbar.addListener('back', function(toolbar) {
            modeOncoguideList(patientId, processInstanceId, patientname);
        });

        var panel = Ext.create('App.bam.js.common.SignalGrid', {
            url: getRelativeServerURI('rs/process/definition/{0}/eventnodes', [processId]),
            processInstanceId: processInstanceId,
            processId: processId,
            tbar: tbar
        });
        panel.addListener('signalselected', function(grid, eventType, processId, processInstanceId) {
            var win = Ext.Msg.prompt('Cuidado', '¿Esta seguro de querer provocar este evento (' + eventType + ') en el proceso (' + processId + ') numero ' + processInstanceId + '\n' +
                    'S&iacute; o No', function(btn, text) {
                if (text == 'Sí' || text == 'Si') {
                    Abada.Ajax.requestJson({
                        url: getRelativeServerURI('/rs/process/instance/event/{0}/transition/special', [processInstanceId]),
                        method: 'POST',
                        scope: this,
                        params: {
                            processId: processId,
                            type: eventType
                        },
                        success: function(records) {
                            Ext.Msg.alert('Info', 'Tarea completada correctamente',
                                    function() {
                                        win.close();
                                    });
                        },
                        failure: function() {
                            Ext.Msg.alert('Error', 'No se pudo completar el evento correctamente',
                                    function() {
                                        win.close();
                                    });
                        }
                    });
                }
            }, this);
        }, this);

        setCentralPanel(panel, true);

        panel.getStore().load();
    }

    /**
     *Screen for task associated with
     */
    function modeTaskOnconguide(patientId, processInstanceId, patientname) {
        function onTaskSelected(grid, taskId, taskUrl) {
            var panelAux = Ext.create('App.bam.js.common.FormCustomPanel', {
                taskId: taskId,
                url: taskUrl,
                height:App.height
            });
            panelAux.addListener('success', function(panel, frame, response) {
                Ext.Msg.alert('Info', 'Tarea completada correctamente',
                        function() {
                            win.close();
                        });
            }, this);
            panelAux.addListener('failure', function(panel, frame, text) {
                Ext.Msg.alert('Error', 'No se puede completar la tarea.' + text,
                        function() {
                            win.close();
                        });
            }, this);

            var win = Ext.create('Ext.window.Window', {
                title: 'Tarea ' + taskId,
                autoScroll: true,
                layout: 'fit',
                modal: true,
                width: 900,
                items: [panelAux]
            });
            win.addListener('destroy', function() {
                panel.getStore().load();
            }, this)
            win.show();
        }

        var tbar = Ext.create('App.bam.js.common.PrincipalToolbar', {
            patientname: patientname
        });
        tbar.addListener('back', function(toolbar) {
            modeOncoguideList(patientId, processInstanceId, patientname);
        });

        var panel = Ext.create('App.bam.js.common.TaskGrid', {
            url: getRelativeServerURI('rs/tasks/process/{0}/loggeduser', [processInstanceId]),
            processInstanceId: processInstanceId,
            tbar: tbar
        });
        panel.addListener('taskselected', onTaskSelected);

        setCentralPanel(panel, true);

        panel.getStore().load();
    }

    /**
     *Screen for patient's oncoguide data
     */
    function modeDataOncoguide(patientId, processInstanceId, patientname) {
        var tbar = Ext.create('App.bam.js.common.PrincipalToolbar', {
            patientname: patientname
        });
        tbar.addListener('back', function(toolbar) {
            modeOncoguideList(patientId, processInstanceId, patientname);
        });

        var grid = Ext.create('App.bam.js.common.DataGrid', {
            tbar: tbar,
            processInstanceId: processInstanceId,
            url: getRelativeServerURI('rs/process/instance/{0}/variables/extjs', [processInstanceId])
        });

        grid.getStore().load();

        setCentralPanel(grid, true);
    }

    /**
     *Screen for patient's oncoguide history
     */
    function modeHistoryOncoguide(patientId, processInstanceId, patientname) {
        var tbar = Ext.create('App.bam.js.common.PrincipalToolbar', {
            patientname: patientname
        });
        tbar.addListener('back', function(toolbar) {
            modeOncoguideList(patientId, processInstanceId, patientname);
        });

        var grid = Ext.create('App.bam.js.common.NodeHistoryGrid', {
            tbar: tbar,
            processInstanceId: processInstanceId,
            url: getRelativeServerURI('rs/process/instance/{0}/history', [processInstanceId])
        });

        grid.getStore().load();

        setCentralPanel(grid, true);
    }

    /**
     *Screen for patient's oncoguide
     */
    function modeOncoguide(patientId, processInstanceId, patientname) {
        var tbar = Ext.create('App.bam.js.common.PrincipalToolbar', {
            patientname: patientname
        });
        tbar.addListener('back', function(toolbar) {
            modeOncoguideList(patientId, processInstanceId, patientname);
        });

        var imageOncoguide = Ext.create('App.bam.js.common.ProcessInstanceTabPanel', {
            tbar: tbar,
            height:App.height
        });
        imageOncoguide.loadProcessInstancePanels(processInstanceId);

        setCentralPanel(imageOncoguide, true);
    }

    /**
     *Screen for patient's oncoguide
     */
    function modeOncoguideList(patientId, processInstanceId, patientname) {

        var tbar = Ext.create('App.bam.js.common.PrincipalToolbar', {
            patientname: patientname
        });
        tbar.addListener('back', function(toolbar) {
            modePatient();
        });

        var oncoguide = Ext.create('App.bam.js.common.OncoguideGrid', {
            url: getRelativeServerURI('rs/patient/{0}/pinstance/list', [patientId]),
            patientId: patientId
        });
        oncoguide.addListener('graphclick', function(grid, processInstanceId) {
            modeOncoguide(patientId, processInstanceId, patientname);
        });
        oncoguide.addListener('processinstanceselected', function(grid, processInstanceId) {
            modeOncoguide(patientId, processInstanceId, patientname);
        });
        oncoguide.addListener('historyclick', function(grid, processInstanceId) {
            modeHistoryOncoguide(patientId, processInstanceId, patientname);
        });
        oncoguide.addListener('dataclick', function(grid, processInstanceId) {
            modeDataOncoguide(patientId, processInstanceId, patientname);
        });
        oncoguide.addListener('taskclick', function(grid, processInstanceId) {
            modeTaskOnconguide(patientId, processInstanceId, patientname);
        });
        oncoguide.addListener('signalclick', function(grid, processInstanceId, processId) {
            modeSignalOnconguide(patientId, processInstanceId, processId, patientname);
        });
        oncoguide.addListener('versionclick', function(grid, processInstanceId, processId) {
            showVersionChangeForm(patientId, processInstanceId, processId, patientname);
        });

        /**
         *Seleccionamos la oncoguia que estuviese antes seleccionada.
         */
        oncoguide.getStore().addListener('load', function() {
            if (processInstanceId) {
                var store = oncoguide.getStore();
                var record;
                for (var i = 0; i < store.getCount(); i++) {
                    record = store.getAt(i);
                    if (record.get('processInstanceId') == processInstanceId)
                        oncoguide.selModel.select(i, undefined, false);
                }
            }
        }, this);

        oncoguide.getStore().load();

        /**
         *Tab for new process
         */
        var newOncoguide = Ext.create('App.bam.js.common.NewProcessInstancePanel', {
            urlOncoguides: getRelativeServerURI('/rs/process/definition/list/combo'),
            patientId: patientId,
            heigth:App.height
        });

        newOncoguide.addListener('success', function(nOncoguide, processInstanceId) {
            modeTaskOnconguide(patientId, processInstanceId, patientname);
        }, this);

        /**
         *Tab for task
         */
        function onTaskSelected(grid, taskId, taskUrl) {
            var panelAux = Ext.create('App.bam.js.common.FormCustomPanel', {
                taskId: taskId,
                url: taskUrl
            });
            panelAux.addListener('success', function(panel, frame, response) {
                Ext.Msg.alert('Info', 'Tarea completada correctamente',
                        function() {
                            win.close();
                        });
            }, this);
            panelAux.addListener('failure', function(panel, frame, text) {
                Ext.Msg.alert('Error', 'No se puede completar la tarea.' + text,
                        function() {
                            win.close();
                        });
            }, this);

            var win = Ext.create('Ext.window.Window', {
                title: 'Tarea ' + taskId,
                autoScroll: true,
                layout: 'fit',
                modal: true,
                width: 900,
                items: [panelAux]
            });
            win.addListener('destroy', function() {
                taskGrid.getStore().load();
            }, this)
            win.show();
        }

        var taskGrid = Ext.create('App.bam.js.common.TaskGrid', {
            url: getRelativeServerURI('rs/tasks/patient/{patientId}/loggeduser', {patientId: patientId}),
            patientId: patientId
        });
        taskGrid.addListener('taskselected', onTaskSelected);
        /**
         *Tabs
         */
        var panel = Ext.create('Abada.tab.panel.TabPanel', {
            title: 'Id Paciente: ' + patientId,
            layout:'fit',
            items: [{
                    title: 'Actuales',
                    items: [oncoguide]
                }, {
                    title: 'Tareas Pendientes',
                    items: [taskGrid]
                }, {
                    title: 'Nueva',
                    items: [newOncoguide]
                }],
            tbar: tbar,
            listeners: {
                tabchange: function(tab) {
                    i = tab.getIndexActiveTab();
                    if (i == 0) {
                        oncoguide.getStore().load();
                    } else if (i == 1) {
                        taskGrid.getStore().load();
                    }
                }
            }
        });
        setCentralPanel(panel, true);
    }

    /**
     * Screen for patient's search
     */
    function modePatient() {
        var panelPatient = Ext.create('App.bam.js.common.PatientGrid', {
            url: getRelativeServerURI('rs/patient/search')
        });
        panelPatient.addListener('patientselected', function(grid, patientId, patientname) {
            modeOncoguideList(patientId, null, patientname);
        });
        panelPatient.getStore().load();

        setCentralPanel(panelPatient, true);
    }

    modePatient();
});
