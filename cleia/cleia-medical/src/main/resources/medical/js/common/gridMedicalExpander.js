/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('App.medical.js.common.gridMedicalExpander', {
    requires: ['Abada.grid.RowExpander'],
    extend:'App.medical.js.common.gridMedical',
    config:{
        checkboxse:undefined,
        loadMask: true,
        page:14
    },
    title: 'Medicos',
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
        '<tpl if="ids.length">',
        '<p><b>Identificadores:</b></p>',
        '</tpl>',
        '<tpl for="ids">',
        '<li style="list-style: none;font-size: 12px;'+
        'font-weight: bold; font-family: helvetica,arial,verdana,sans-serif;'+
        'color: #666;"><b style="color:#157fcc">{#}  </b> {type.value}: {value} </li>',
        '</tpl>',
        '</div>'
        ]
    }
    ]
    
});
