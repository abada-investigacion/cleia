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
Ext.define('App.bam.js_m.common.TaskList', {
    extend: 'Ext.dataview.List',
    config: {
        url: undefined,
        i18n:undefined,
        grouped: true
    },
    constructor: function(config) {
        this.callParent(arguments);
        
        this.setItemTpl('<div><b>'+this.config.i18n.getMsg('bam.task.list.text1')+' {name}</b><br />'+this.config.i18n.getMsg('bam.process.list.text2')+' {processInstanceId}</div>');
        
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

