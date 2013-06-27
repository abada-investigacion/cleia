/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('App.manager.js.common.gridgroupexpander', {
    requires: ['Abada.grid.RowExpander'],
    extend: 'App.manager.js.common.gridgroup',
    config: {
        checkboxse: undefined,
        page: 14
    },
    title: 'Servicios',
    plugins: [{
            ptype: 'abada.rowexpander',
            rowBodyTpl: [
                '<div>',
                '<p><b> Usuarios:</b></p>',
                '<tpl for=".">',
                '<li><b>{#}. </b> {username}</li>',
                '</tpl>',
                '</div>'
            ],
            url: getRelativeServerURI('rs/group/{idgroup}/users', {idgroup: ['idGroup']}),
            searchFields: ['idGroup']

        }]
});
