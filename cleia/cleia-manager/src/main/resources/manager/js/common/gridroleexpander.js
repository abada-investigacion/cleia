/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.define('App.manager.js.common.gridroleexpander', {
    requires: ['Abada.grid.RowExpander'],
    extend:'App.manager.js.common.gridrole',
    config:{
        checkboxse:undefined,
        scroll:true,
        page:14
    },
    title: 'Roles',
    plugins:[{
        ptype: 'abada.rowexpander',
        rowBodyTpl: [
        '<div>',
        '<p><b> Usuarios:</b></p>',
        '<tpl for=".">',
        '<li><b>{#}. </b> {username}</li>',
        '</tpl>',
      
        '</div>'
        ],
        url:getRelativeServerURI('rs/role/{idrole}/users', {idrole:['idRolePriv']}),
        searchFields:['idRolePriv']
    }]
});
