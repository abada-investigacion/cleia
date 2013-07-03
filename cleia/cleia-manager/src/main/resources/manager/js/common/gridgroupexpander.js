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
