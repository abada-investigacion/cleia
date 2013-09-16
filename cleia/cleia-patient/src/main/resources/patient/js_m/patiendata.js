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
Ext.setup({
    viewport: {
        fullscreen: true
    },
    onReady: function() {
        function principalAction(removePanel) {
            Ext.define("Genre", {
                extend: "Ext.data.Model",
                config: {
                    fields: [
                        {name: "id", type: "string"},
                        {name: "value", type: "string"},
                        
                    ]
                }
            });
            var genreStore = Ext.create("Ext.data.Store", {
                model: "Genre",
                proxy: {
                    type: "ajax",
                    url: getRelativeServerURI("/rs/patient/genre/combo"),
                    reader: {
                        type: "json",
                        rootProperty: "data"
                    }
                },
                autoLoad: true
            });
            
            var genreSelect = Ext.create("Ext.field.Select",{
                
                label: 'Genero',
                name: 'cambiar',
                store: genreStore,
                displayField:"id",
                valueField : "value"
            });


            var panel = Ext.create('Ext.form.Panel', {
                id:'patientDataForm',
                items: [
                    {
                        xtype: 'toolbar',
                        docked: 'top',
                        title: 'Datos:',
                        height: 30,
                        items: [
                            {
                                ui: 'back',
                                text: 'Atras',
                                handler: function() {
                                    window.location = getRelativeURI('main.htm');
                                }
                            }
                        ]
                    }, {
                        xtype: 'container',
                        items: [
                            {
                                xtype: 'fieldset',
                                title: 'Datos Personales',
                                items: [
                                    {
                                        xtype: 'textfield',
                                        label: 'Nombre',
                                        name: 'firstName'
                                    }, {
                                        xtype: 'textfield',
                                        label: 'Primer Apellido',
                                        name: 'firstName'
                                    }, {
                                        xtype: 'textfield',
                                        label: 'Segundo Apellido',
                                        name: 'firstName'
                                    }, {
                                        xtype: 'datepickerfield',
                                        label: 'Fecha de Nacimiento',
                                        name: 'firstName'
                                    }, {
                                        xtype: 'textfield',
                                        label: 'Telefono',
                                        name: 'firstName'
                                    }, genreSelect
                                ]
                            }, {
                                xtype: 'fieldset',
                                title: 'Direccion',
                                items: [
                                    {
                                        xtype: 'textfield',
                                        label: 'Direccion',
                                        name: 'firstName'
                                    }, {
                                        xtype: 'textfield',
                                        label: 'Localidad',
                                        name: 'firstName'
                                    }, {
                                        xtype: 'textfield',
                                        label: 'C. Postal',
                                        name: 'firstName'
                                    }, {
                                        xtype: 'textfield',
                                        label: 'Pais',
                                        name: 'firstName'
                                    }
                                ]
                            }, {
                                xtype: 'button',
                                ui: 'action',
                                margin:15,
                                text: 'Guardar'}


                        ]
                    }
                ]
            });

            if (removePanel)
                Ext.Viewport.remove(removePanel, true);

            Ext.Viewport.add(panel);
        }
        principalAction();
    }
});

