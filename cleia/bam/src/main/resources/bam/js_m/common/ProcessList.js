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
Ext.define('App.bam.js_m.common.ProcessList', {
    extend: 'Ext.dataview.List',
    config: {
        url: undefined,
        itemTpl: '<div><b>Proceso: {processName}</b><br />Instancia: {processInstanceId}</div>',
        grouped: true
    },
    constructor: function(config) {
        this.callParent(arguments);
        Ext.define('App.bam.js_m.common.ProcessModel', {extend: 'Ext.data.Model',
            config: {
                idProperty: 'processInstanceId',
                fields: ['patientId', 'processId','processName', 'processInstanceId',{
                    name:'start',
                    type:'date',
                    dateFormat:'c'
                },{
                    name:'end',
                    type:'date',
                    dateFormat:'c'
                },'type']
            }});

        var url = this.config.url;

        var store = Ext.create('Ext.data.Store', {
            autoLoad: true,
            model: 'App.bam.js_m.common.ProcessModel',
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
        this.fireEvent('processselected',this,record.get('processInstanceId'));
    },
    onLoadStore: function(store) {
        this.setStore(store);
        this.updateAllListItems();
    }
});

