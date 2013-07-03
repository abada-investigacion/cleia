/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('App.manager.js.common.griduserexpander', {
    requires: ['Abada.grid.RowExpander'],
    extend:'App.manager.js.common.griduser',
    config:{
        checkboxse:undefined,
        loadMask: true,
        page:14
    },
    title: 'Usuarios',
    plugins:[{
        ptype: 'abada.rowexpander',
        rowBodyTpl: [
        '<div>',
        '<tpl if="roles.length">',
        '<p><b>Roles:</b></p>',
        '</tpl>',
        '<tpl for="roles">',
        '<li style="list-style: none;font-size: 12px;'+
        'font-weight: bold; font-family: helvetica,arial,verdana,sans-serif;'+
        'color: #666;"><b style="color:#157fcc">{#}  </b> {authority}</li>',
        '</tpl>',
        '<tpl if="groups.length">',
        '<p><b>Servicios:</b></p>',
        '</tpl>',
        '<tpl for="groups">',
        '<li style="list-style: none;font-size: 12px;'+
        'font-weight: bold; font-family: helvetica,arial,verdana,sans-serif;'+
        'color: #666;"><b style="color:#157fcc">{#}  </b> {value} </li>',
        '</tpl>',
        '</div>'
        ]
    }
    ]
    
});
