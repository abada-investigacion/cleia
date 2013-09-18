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
        var i18n = Ext.create('Abada.i18n.Bundle', {
            path: getRelativeURI('/patient/locale'),
            bundle: 'messages',
            insertLocale: false
        });

        i18n.on('error', function() {
            i18n.language = i18n.defaultLanguage;
            i18n.load();
        });

        i18n.on('loaded', function() {
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
                patient.address.countryAddress = fvalues.countryAddress;
                patient.genre = fvalues.genre;
                
                if(fvalues.password != "" ){
                    if (fvalues.password == fvalues.repassword) {
                        patient.user.password = fvalues.password;
                    }else{
                        Ext.Msg.show({
                            title: i18n.getMsg('patient.mobile.data.errorpass'),
                            message: i18n.getMsg('patient.mobile.data.errorpass'),
                            height: 100,
                            width: 350,
                            buttons: Ext.MessageBox.OK,
                            multiLine: true,
                            prompt: {maxlength: 180, autocapitalize: true},
                        });
                        return false;
                    }
                }
               

                //doAjaxrequestJson(getRelativeServerURI("/rs/patient/patientData"), patient, 'put', null, null, i18n.getMsg('patient.mobile.data.msg.ok'), i18n.getMsg('patient.mobile.data.msg.error'));

                Abada.Ajax.requestJson({
                    url: getRelativeServerURI("/rs/patient/patientData"),
                    scope: this,
                    method: 'put',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    params:
                            Ext.JSON.encode(patient)
                            ,
                    failure: function(error) {
                        
                        if (error && error.reason) {
                            Ext.Msg.alert(i18n.getMsg('patient.mobile.data.msg.error'), error.reason);
                        } else
                            Ext.Msg.alert('', i18n.getMsg('patient.mobile.data.msg.error'));
                    },
                    success: function() {
                        
                        Ext.Msg.show({
                            title: i18n.getMsg('patient.mobile.data.msg.ok'),
                            message: i18n.getMsg('patient.mobile.data.msg.ok'),
                            height: 100,
                            width: 350,
                            buttons: Ext.MessageBox.OK,
                            multiLine: true,
                            prompt: {maxlength: 180, autocapitalize: true},
                        });
                    }
                });

                setFormValues();

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
                            title: i18n.getMsg('patient.mobile.data.titlebar'),
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
                                    title: i18n.getMsg('patient.mobile.data.titlebar'),
                                    items: [
                                        {
                                            xtype: 'textfield',
                                            label: i18n.getMsg('patient.mobile.data.name'),
                                            name: 'name'
                                        }, {
                                            xtype: 'textfield',
                                            label: i18n.getMsg('patient.mobile.data.surname'),
                                            name: 'surname'
                                        }, {
                                            xtype: 'textfield',
                                            label: i18n.getMsg('patient.mobile.data.surname1'),
                                            name: 'surname1'
                                        }, {
                                            xtype: 'datepickerfield',
                                            label: i18n.getMsg('patient.mobile.data.birthday'),
                                            name: 'birthDay'
                                        }, {
                                            xtype: 'textfield',
                                            label: i18n.getMsg('patient.mobile.data.tlf'),
                                            name: 'tlf'
                                        }, genreSelect
                                    ]
                                }, {
                                    xtype: 'fieldset',
                                    title: i18n.getMsg('patient.mobile.data.address'),
                                    items: [
                                        {
                                            xtype: 'textfield',
                                            label: i18n.getMsg('patient.mobile.data.address'),
                                            name: 'address'
                                        }, {
                                            xtype: 'textfield',
                                            label: i18n.getMsg('patient.mobile.data.city'),
                                            name: 'city'
                                        }, {
                                            xtype: 'textfield',
                                            label: i18n.getMsg('patient.mobile.data.cp'),
                                            name: 'cp'
                                        }, {
                                            xtype: 'textfield',
                                            label: i18n.getMsg('patient.mobile.data.countryAddress'),
                                            name: 'countryAddress'
                                        }
                                    ]
                                }, {
                                    xtype: 'fieldset',
                                    title: i18n.getMsg('patient.mobile.data.changePass'),
                                    items: [
                                        {
                                            xtype: 'passwordfield',
                                            label: i18n.getMsg('patient.mobile.data.password'),
                                            name: 'password'
                                        }, {
                                            xtype: 'passwordfield',
                                            label: i18n.getMsg('patient.mobile.data.repassword'),
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

                setFormValues();

                if (removePanel)
                    Ext.Viewport.remove(removePanel, true);

                Ext.Viewport.add(panel);


            }

            function setFormValues() {
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
                            genre: patient.genre,
                            password: '',
                            repassword: ''

                        });

                    }
                });
                
            }

            principalAction();
        });
        i18n.load();
    }

});

