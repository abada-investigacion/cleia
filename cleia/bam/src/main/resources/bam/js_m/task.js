/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.require([
    'App.bam.js_m.common.TaskList'
]);
Ext.setup({
    viewport: {
        fullscreen: true
    },
    onReady: function() {

        function principalAction(removePanel) {

            var panel2 = Ext.create('App.bam.js_m.common.TaskList', {
                url: getRelativeServerURI('rs/tasks/participation/loggeduser'),
                title: 'Tareas Pendientes;',
                listeners: {
                    taskselected: function(list, taskId, url) {
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
                        title: 'Tareas Pendientes',
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
            panelAux.addListener('success', function(panel, frame, response) {
                Ext.Msg.alert('Info', 'Tarea completada correctamente',
                        function() {
                            principalAction(panel);
                        });
            }, this);
            panelAux.addListener('failure', function(panel, frame, text) {
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