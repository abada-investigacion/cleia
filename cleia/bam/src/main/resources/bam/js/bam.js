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

Ext.require(['Abada.Ajax', 'Ext.XTemplate', 'Abada.tab.panel.TabPanel', 'App.bam.js.common.ProcessInstanceTabPanel', 'App.bam.js.common.ProcessInstancePanel', 'Abada.data.JsonStore', 'Ext.data.proxy.Ajax', 'Ext.window.MessageBox', 'App.bam.js.common.VersionFormPanel', 'App.bam.js.common.ProcessTabPanel', 'App.bam.js.common.JumpPanel']);

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
        function showVersionChangeForm(patientId, processInstanceId, processId, patientname) {
            var tbar = Ext.create('App.bam.js.common.PrincipalToolbar', {
                patientname: patientname,
                i18n: i18n
            });
            tbar.addListener('back', function(toolbar) {
                modeOncoguideList(patientId, processInstanceId, patientname);
            });

            var panelAux = Ext.create('App.bam.js.common.VersionFormPanel', {
                title: i18n.getMsg('bam.version', processId, processInstanceId),
                processInstanceId: processInstanceId,
                tbar: tbar,
                autoScroll: true,
                i18n: i18n
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
                patientname: patientname,
                i18n: i18n
            });
            tbar.addListener('back', function(toolbar) {
                modeOncoguideList(patientId, processInstanceId, patientname);
            });

            var panel = Ext.create('App.bam.js.common.SignalGrid', {
                url: getRelativeServerURI('rs/process/definition/{0}/eventnodes', [processId]),
                processInstanceId: processInstanceId,
                processId: processId,
                tbar: tbar,
                i18n: i18n
            });
            panel.addListener('signalselected', function(grid, eventType, processId, processInstanceId) {
                var win = Ext.Msg.prompt(i18n.getMsg('bam.warning'), i18n.getMsg('bam.signal.question', eventType, processId, processInstanceId), function(btn, text) {
                    if (text == i18n.getMsg('bam.signal.response1') || text == i18n.getMsg('bam.signal.response2')) {
                        Abada.Ajax.requestJson({
                            url: getRelativeServerURI('/rs/process/instance/event/{0}/transition/special', [processInstanceId]),
                            method: 'POST',
                            scope: this,
                            params: {
                                processId: processId,
                                type: eventType
                            },
                            success: function(records) {
                                Ext.Msg.alert(i18n.getMsg('bam.info'), i18n.getMsg('bam.alert1.text'),
                                        function() {
                                            win.close();
                                        });
                            },
                            failure: function() {
                                Ext.Msg.alert(i18n.getMsg('bam.error'), i18n.getMsg('bam.alert2.text'),
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
                    height: App.height
                });
                panelAux.addListener('success', function(panel, frame, response) {
                    Ext.Msg.alert(i18n.getMsg('bam.info'), i18n.getMsg('bam.alert1.text'),
                            function() {
                                win.close();
                            });
                }, this);
                panelAux.addListener('failure', function(panel, frame, text) {
                    Ext.Msg.alert(i18n.getMsg('bam.error'), i18n.getMsg('bam.alert3.text', text),
                            function() {
                                win.close();
                            });
                }, this);

                var win = Ext.create('Ext.window.Window', {
                    title: i18n.getMsg('bam.task', taskId),
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
                patientname: patientname,
                i18n: i18n
            });
            tbar.addListener('back', function(toolbar) {
                modeOncoguideList(patientId, processInstanceId, patientname);
            });

            var panel = Ext.create('App.bam.js.common.TaskGrid', {
                url: getRelativeServerURI('rs/tasks/process/{0}/loggeduser', [processInstanceId]),
                processInstanceId: processInstanceId,
                tbar: tbar,
                i18n: i18n
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
                patientname: patientname,
                i18n: i18n
            });
            tbar.addListener('back', function(toolbar) {
                modeOncoguideList(patientId, processInstanceId, patientname);
            });

            var grid = Ext.create('App.bam.js.common.DataGrid', {
                tbar: tbar,
                i18n: i18n,
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
                patientname: patientname,
                i18n: i18n
            });
            tbar.addListener('back', function(toolbar) {
                modeOncoguideList(patientId, processInstanceId, patientname);
            });

            var grid = Ext.create('App.bam.js.common.NodeHistoryGrid', {
                tbar: tbar,
                processInstanceId: processInstanceId,
                url: getRelativeServerURI('rs/process/instance/{0}/history', [processInstanceId]),
                i18n: i18n
            });

            grid.getStore().load();

            setCentralPanel(grid, true);
        }

        /**
         *Screen for patient's oncoguide
         */
        function modeOncoguide(patientId, processInstanceId, patientname) {
            var tbar = Ext.create('App.bam.js.common.PrincipalToolbar', {
                patientname: patientname,
                i18n: i18n
            });
            tbar.addListener('back', function(toolbar) {
                modeOncoguideList(patientId, processInstanceId, patientname);
            });

            var imageOncoguide = Ext.create('App.bam.js.common.ProcessInstanceTabPanel', {
                tbar: tbar,
                height: App.height,
                i18n: i18n
            });
            imageOncoguide.loadProcessInstancePanels(processInstanceId);

            setCentralPanel(imageOncoguide, true);
        }

        /**
         *Screen for patient's oncoguide
         */
        function modeOncoguideList(patientId, processInstanceId, patientname) {

            var tbar = Ext.create('App.bam.js.common.PrincipalToolbar', {
                patientname: patientname,
                i18n: i18n
            });
            tbar.addListener('back', function(toolbar) {
                modePatient();
            });

            var oncoguide = Ext.create('App.bam.js.common.OncoguideGrid', {
                url: getRelativeServerURI('rs/patient/{0}/pinstance/list', [patientId]),
                patientId: patientId,
                i18n: i18n
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
                heigth: App.height,
                i18n: i18n
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
                    Ext.Msg.alert(i18n.getMsg('bam.info'), i18n.getMsg('bam.alert1.text'),
                            function() {
                                win.close();
                            });
                }, this);
                panelAux.addListener('failure', function(panel, frame, text) {
                    Ext.Msg.alert(i18n.getMsg('bam.error'), i18n.getMsg('bam.alert3.text', text),
                            function() {
                                win.close();
                            });
                }, this);

                var win = Ext.create('Ext.window.Window', {
                    title: i18n.getMsg('bam.task', taskId),
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
                patientId: patientId,
                i18n: i18n
            });
            taskGrid.addListener('taskselected', onTaskSelected);
            /**
             *Tabs
             */
            var panel = Ext.create('Abada.tab.panel.TabPanel', {
                title: i18n.getMsg('bam.tab.title', patientId),
                layout: 'fit',
                items: [{
                        title: i18n.getMsg('bam.tab.process'),
                        items: [oncoguide]
                    }, {
                        title: i18n.getMsg('bam.tab.task'),
                        items: [taskGrid]
                    }, {
                        title: i18n.getMsg('bam.tab.new'),
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
            
            function assignPatient(panelPatient) {
                var id;
                Abada.Ajax.requestJsonObject({
                    scope: this,
                    method: 'GET',
                    url: getRelativeServerURI('rs/medical/idmedical'),
                    success: function(result) {
                        id = result;

                    },
                    failure: function() {

                    }
                });
                var patientsGrid = Ext.create('App.patient.js.common.gridPatient', {
                    title: '',
                    i18n: i18n,
                    url: getRelativeServerURI('rs/patient/searchforassignment'),
                    height: 400,
                    width: 800,
                    checkboxse: true,
                    padding: '10 5 5 5',
                    page: 25,
                    listeners: {
                        afterrender: function() {

                            for (var i = 0; i < patientsGrid.columns.length; i++) {

                                if (patientsGrid.columns[i].dataIndex === 'genre' || patientsGrid.columns[i].dataIndex === 'tlf'
                                        || patientsGrid.columns[i].dataIndex === 'enabled' || patientsGrid.columns[i].dataIndex === 'address') {
                                    patientsGrid.columns[i].hide()
                                }

                            }


                            patientsGrid.headerCt.insert(patientsGrid.columns.length, Ext.create('Ext.grid.column.Column', {
                                header: i18n.getMsg('medical.patientGrid.column.identifier'),
                                text: '',
                                renderer: renderIdentifier(new Ext.Template('{ids}')),
                                width: 40,
                                hidden: false
                            })
                                    );

                            patientsGrid.getView().refresh();



                        },
                        select: function(constructor, record) {

                            doAjaxAssign('add', record);

                        },
                        deselect: function(constructor, record) {

                            doAjaxAssign('remove', record);

                        }
                    }
                });

                function renderIdentifier(template) {
                    return function(value, meta, record, rowIndex, colIndex, store) {

                        var ids = record.data.ids;

                        if (ids.length > 0) {

                            for (var i = 0; i < ids.length; i++) {

                                if (ids[i].type.value.toUpperCase() === 'DNI') {

                                    return ids[i].type.value.toUpperCase() + ': ' + ids[i].value;

                                }

                            }

                            return ids[0].type.value.toUpperCase() + ': ' + ids[0].value;
                        } else {
                            return '';
                        }
                    };
                }


                function doAjaxAssign(operation, record) {
                    var url = getRelativeServerURI('rs/medical/{idmedical}/{operation}/{idpatient}', {
                        idmedical: id,
                        operation: operation,
                        idpatient: record.data.id
                    });
                    Abada.Ajax.requestJson({
                        url: url,
                        scope: this,
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json; charset=UTF-8'
                        },
                        failure: function(error) {
                            if (error && error.reason) {
                                Ext.Msg.alert(i18n.getMsg('medical.error'), error.reason);
                            } else
                                Ext.Msg.alert('', i18n.getMsg('medical.error'));
                        },
                        success: function() {

                        }
                    });

                }
                patientsGrid.getStore().on('load', function(constructor, records) {
                    var medicals;
                    var exit;

                    for (var i = 0; i < records.length; i++) {

                        exit = false;
                        medicals = records[i].data.medicals;

                        for (var z = 0; z < medicals.length && !exit; z++) {

                            if (medicals[z].id == id) {

                                patientsGrid.selModel.select(records[i], true, true);

                                exit = true;
                            }

                        }

                    }


                });

                patientsGrid.getStore().load({
                    params: {
                        start: 0,
                        limit: 25
                    }
                });



                var assignForm = Ext.create('Ext.form.Panel', {
                    monitorValid: true,
                    frame: false,
                    autoScroll: true,
                    items: [patientsGrid]
                });


                var wind = Ext.create('Ext.window.Window', {
                    title: i18n.getMsg('medical.assignPatients'),
                    id: 'assignPatientWindow',
                    closable: true,
                    modal: true,
                    items: [assignForm],
                    listeners: {
                        close: function() {
                            panelPatient.getStore().load();
                        }
                    }
                });

                wind.show();

                return assignForm;

            }

            var panelPatient = Ext.create('App.bam.js.common.PatientGrid', {
                url: getRelativeServerURI('rs/patient/assigned/search'),
                i18n: i18n
            });
            var assignar = Ext.create('Ext.Button', {
                text: i18n.getMsg('medical.assignPatients'),
                id: 'assginPatient',
                handler: function() {
                    assignPatient(panelPatient);
                }});
            panelPatient.addListener('patientselected', function(grid, patientId, patientname) {
                modeOncoguideList(patientId, null, patientname);
            });
            panelPatient.getStore().load();
            var panel = Ext.create('Ext.panel.Panel', {
                autoWidth: true,
                autoHeight: true,
                title: '',
                items: [assignar, panelPatient]});
            setCentralPanel(panel, true);
        }

        modePatient();
    });

    i18n.load();
});
