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
        '<li><b>{#}. </b> {authority}</li>',
        '</tpl>',
        '<tpl if="groups.length">',
        '<p><b>Servicios:</b></p>',
        '</tpl>',
        '<tpl for="groups">',
        '<li><b>{#}. </b> {value} </li>',
        '</tpl>',
        '</div>'
        ] 
    }
    ]
    
});
