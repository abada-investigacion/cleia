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
patient = null;

Ext.setup({
    viewport: {
        fullscreen: true
    },
    onReady: function() {

        function formSubmit() {

            var fvalues = Ext.getCmp('patientDataForm').getValues();

            fvalues.birthDay = Ext.Date.format(fvalues.birthDay, 'Y-m-d h:i:s');


            patient.name = fvalues.name;
            patient.surname = fvalues.surname;
            patient.surname1 = fvalues.surname1;
            patient.birthDay = fvalues.birthDay;
            patient.tlf = fvalues.tlf;
            patient.address.address = fvalues.address;
            patient.address.city = fvalues.city;
            patient.address.cp = fvalues.cp;
            patient.address.countryAddress = fvalues.address.countryAddress;
            patient.genre = fvalues.genre;
            
           /* if(fvalues.password != "" && fvalues.password == fvalues.repassword){
                patient.user.password = fvalues.password;t
            }*/

            alert(Ext.encode(patient));

            doAjaxrequestJson(getRelativeServerURI("/rs/patient/patientData"), patient, 'put', null, null, "bien", "mal");

        }

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

            var genreSelect = Ext.create("Ext.field.Select", {
                label: 'Genero',
                name: 'genre',
                store: genreStore,
                displayField: "id",
                valueField: "value"
            });


            var panel = Ext.create('Ext.form.Panel', {
                id: 'patientDataForm',
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
                                        name: 'name'
                                    }, {
                                        xtype: 'textfield',
                                        label: 'Primer Apellido',
                                        name: 'surname'
                                    }, {
                                        xtype: 'textfield',
                                        label: 'Segundo Apellido',
                                        name: 'surname1'
                                    }, {
                                        xtype: 'datepickerfield',
                                        label: 'Fecha de Nacimiento',
                                        name: 'birthDay'
                                    }, {
                                        xtype: 'textfield',
                                        label: 'Telefono',
                                        name: 'tlf'
                                    }, genreSelect
                                ]
                            }, {
                                xtype: 'fieldset',
                                title: 'Direccion',
                                items: [
                                    {
                                        xtype: 'textfield',
                                        label: 'Direccion',
                                        name: 'address'
                                    }, {
                                        xtype: 'textfield',
                                        label: 'Localidad',
                                        name: 'city'
                                    }, {
                                        xtype: 'textfield',
                                        label: 'C. Postal',
                                        name: 'cp'
                                    }, {
                                        xtype: 'textfield',
                                        label: 'Pais',
                                        name: 'countryAddress'
                                    }
                                ]
                            }, {
                                xtype: 'fieldset',
                                title: 'Cambio de Contrase&ntilde;a',
                                items: [
                                    {
                                        xtype: 'passwordfield',
                                        label: 'Contrase&ntilde;a',
                                        name: 'password'
                                    }, {
                                        xtype: 'passwordfield',
                                        label: 'Rep. Contrase&ntilde;a',
                                        name: 'repassword'
                                    }
                                ]
                            }, {
                                xtype: 'button',
                                ui: 'action',
                                margin: 15,
                                text: 'Guardar',
                                handler: formSubmit
                            }


                        ]
                    }
                ]
            });

            if (removePanel)
                Ext.Viewport.remove(removePanel, true);

            Ext.Viewport.add(panel);

            Ext.Ajax.request({
                url: getRelativeServerURI("/rs/patient/search/sessionPatient"),
                success: function(response) {

                    patient = Ext.decode(response.responseText);
                    
                    var form = Ext.getCmp('patientDataForm');
                    var bitdday = patient.birthDay.split('-');
                    form.setValues({
                        name: patient.name,
                        surname: patient.surname,
                        surname1: patient.surname1,
                        birthDay: new Date(bitdday[0], bitdday[1], bitdday[2]),
                        tlf: patient.tlf,
                        address: patient.address.address,
                        city: patient.address.city,
                        cp: patient.address.cp,
                        countryAddress: patient.address.countryAddress,
                        genre: patient.genre

                    });

                }
            });
        }
        principalAction();
    }
});

