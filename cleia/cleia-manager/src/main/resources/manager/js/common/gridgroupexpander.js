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
Ext.define('App.manager.js.common.gridgroupexpander', {
    requires: ['Abada.grid.RowExpander'],
    extend: 'App.manager.js.common.gridgroup',
    config: {
        checkboxse: undefined,
        scroll:true,
        page: 14
    },
    title: 'Servicios',
    plugins: [{
        ptype: 'abada.rowexpander',
        rowBodyTpl: [
        '<div>',
        '<p><b> Usuarios:</b></p>',
        '<tpl for="data">',
        '<li style="list-style: none;font-size: 12px;'+
        'font-weight: bold; font-family: helvetica,arial,verdana,sans-serif;'+
        'color: #666;"><b style="color:#157fcc">{#}  </b> {username}</li>',
        '</br>',
        '</tpl>',
        '</div>'
        ],
        url: 'rs/group/{value}/users',
        searchFields: ['value']

    }]
});
