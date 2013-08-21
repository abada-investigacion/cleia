/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.require([
    'Ext.form.Panel', 'Ext.form.field.Checkbox', 'Abada.Ajax', 'Ext.JSON', 'Ext.Ajax',
    'Ext.layout.container.Column', 'Abada.toolbar.ToolbarInsertUpdateDelete'

    ])

Ext.onReady(function() {
    
    var usersToAssignGrid;
    
    var toolbar=Ext.create('Abada.toolbar.ToolbarInsertUpdateDelete', {
        listeners: {
            submitInsert: function() {
                handleFormulario('Inserta', roleGrid, 'Rol', getRelativeServerURI('rs/role'), roleGrid.selModel);
            },
            submitUpdate: function() {
                if (roleGrid.selModel.hasSelection()) {
                    if (roleGrid.selModel.getCount() == 1) {
                        handleFormulario('Modifica', roleGrid, 'Usuario', getRelativeServerURI('rs/role/{idrole}', {
                            idrole:roleGrid.selModel.getLastSelected().get('authority')
                            }), roleGrid.selModel);
                    } else {
                        Ext.Msg.alert('', 'Seleccione un rol');
                    }
                } else
                    Ext.Msg.alert('', 'Seleccione un rol');
            },
            submitDelete: function() {
                if (roleGrid.selModel.hasSelection()) {
                    var form = {
                        authority: roleGrid.selModel.getLastSelected().get('authority')
                    }
                    var opt = 'borra';
                                 
                    doAjaxrequestJson(getRelativeServerURI('rs/role/{idrole}', {
                        idrole:form.authority
                    }), form, 'DELETE', roleGrid, null, 'Rol '+opt+'do', 'Error. No se ha podido ' + opt + 'r');
              
                } else
                    Ext.Msg.alert('', 'Seleccione un rol');
            }
        }

    })
    
    var roleGrid = Ext.create('App.manager.js.common.gridroleexpander', {
        url: getRelativeServerURI('rs/role/search'),
        width: 300,
        height: 400,
        page: 14


    });

    roleGrid.getStore().load({
        params: {
            start: 0,
            limit: 14
        }
    });

    var panel = Ext.create('Ext.panel.Panel', {
        frame: true,
        autoWidth: true,
        autoHeight: true,
        title: '',
        items: [toolbar, roleGrid]//ponemos el grid

    });
    setCentralPanel(panel);


    function getO(form, selection) {
        var authority = form.getComponent("authority").getValue();
        if (authority == "") {
            authority = null;
        }
        var o = {
            authority: authority,
            users: getListForObject(selection, 'id')

        };
        return o;
    }
    
    
    function handleFormulario(opt, grid, title, url, selection) {
        var authority = 'ROLE_', method = 'POST', tooltip = 'Insertar rol'

        if (opt != 'Inserta' && selection.hasSelection()) {
            method = 'PUT';
            authority = selection.getLastSelected().get('authority');           
            tooltip = 'Modificar rol';
        }
        var usersGrid = Ext.create('App.manager.js.common.griduser', {
            title:'Listado de usuarios',
            url: getRelativeServerURI('rs/user/search'),
            width: 400,
            height:400,
            checkboxse: true,
            padding: '10 5 5 5',
            listeners: {
               
                select:function(constructor,record){     
                    
                    usersToAssignGrid.getStore().insert(0,record);
                 
                },
                deselect:function(constructor, record){  
                                           
                    usersToAssignGrid.getStore().remove(record);
                  
                }
            }
        });
        
        if(opt != 'Inserta' && selection.hasSelection()){
            var assignGridUrl=getRelativeServerURI('rs/role/{idrole}/users',{
                idrole:selection.selected.items[0].data.authority
            });
        }
        
        usersToAssignGrid=Ext.create('App.manager.js.common.griduser', {
            title:'Usuarios asignados',
            url: assignGridUrl,
            checkboxse:false,
            width: 400,
            height:400,
            padding: '10 5 5 5'
        });
        
        usersGrid.getStore().on('load',function(){
            
            var records= usersToAssignGrid.getStore().data.items;  
            
            usersGrid.selModel.select(records, true, true);
            
        });

        
        usersToAssignGrid.getStore().on('load',function(constructor,records){
            
            usersGrid.getStore().load({
                params: {
                    start: 0
                }
            });
            
            usersGrid.selModel.select(records, true, true);
            
        });

        usersToAssignGrid.getStore().load({
            params: {
                start: 0,
                limit: 25
            }
        });


        //form panel de insertar
        var formpanel = Ext.create('Ext.form.Panel', {           
            url: url,
            defaultType: 'textfield',
            monitorValid: true,
            autoWidth: true,
            layout: {
                type: 'hbox',
                columns: 2
            },
            items: [
            {
                fieldLabel: 'Role',
                name: 'authority',
                id: 'authority',
                value: authority,
                regex: /ROLE__*/,
                regextext: 'Debe contener el prefijo ROLE_',
                allowBlank: false,
                padding: '10 5 5 5',
                columnWidth: 1
            },
            usersGrid,usersToAssignGrid


            ],
            buttons: [{
                text: opt + 'r',
                id: 'formrole',
                formBind: true,
                handler: function() {
                    if (formpanel.getForm().isValid()) {
                        doAjaxrequestJson(url, {
                            authority: formpanel.getComponent("authority").getValue() , 
                            users:getListForObjectByGridStore(usersToAssignGrid.getStore(), 'id')
                        }, method, roleGrid, wind, 'Rol '+ opt + 'do', 'Error no se ha podido ' + opt + 'r');
                    }

                },
                tooltip: tooltip
            }]
        });
        
      
        usersGrid.getStore().load();
            
        if (opt != 'Inserta') {
            usersGrid.getStore().on('load', function() {
                entityListPreSelected(usersGrid, authority, 'authority', "id", getRelativeServerURI('rs/role/{idrole}/users', {
                    idrole: authority
                }));
            });
        }
        
        var wind = Ext.create('Ext.window.Window', {
            title: opt + 'r',
            id: 'rolewind',
            autoScroll: false,
            autoWidth: true,
            closable: true,
            modal: true,
            items: [formpanel]
        });

        wind.show();

        return formpanel;
    }

});