/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('App.bam.js_m.common.TaskList', {
    extend: 'Ext.dataview.List',
    config: {
        url: undefined,
        itemTpl: '<div><b>Tarea: {name}</b><br />Proceso: {processId}<br />Instancia: {processInstanceId}</div>',
        grouped: true
    },
    constructor: function(config) {
        this.callParent(arguments);
        Ext.define('App.bam.js_m.common.TaskModel', {extend: 'Ext.data.Model',
            config: {
                idProperty: 'id',
                fields: ['id', 'processInstanceId', 'processId', 'name', 'assignee', 'isBlocking', 'isSignalling',
                    'currentState', 'url', 'dueDate', 'createDate', 'priority', 'description', 'participantGroups', 'participantUsers']
            }});

        var url = this.config.url;

        var store = Ext.create('Ext.data.Store', {
            autoLoad: true,
            model: 'App.bam.js_m.common.TaskModel',
            grouper: {
                groupFn: function(record) {
                    return record.get('name');
                }
            },
            proxy: {
                type: 'ajax',
                url: url,
                reader: {
                    type: 'json',
                    rootProperty: 'data'
                }
            }
        });
        store.addListener('load', this.onLoadStore, this);

        this.addListener('itemsingletap', this.fireTaskSelected, this);
    },
    fireTaskSelected: function(list, index, target, record) {
        this.fireEvent('taskselected',this,record.get('id'),record.get('url'));
    },
    onLoadStore: function(store) {
        this.setStore(store);
        this.updateAllListItems();
    }
});

