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

Ext.define('App.medical.js.common.gridMedical', {
    requires: ['Abada.data.JsonStore', 'Ext.toolbar.Paging', 'Abada.data.JsonStore'
                , 'Ext.ux.grid.FiltersFeature', 'Ext.ux.CheckColumn',
        'Abada.grid.column.CheckBox', 'Ext.grid.column.Date', 'Abada.grid.RowExpander'],
    extend: 'Abada.grid.Panel',
    config: {
        loadMask: true,
        page: 13,
        details: undefined,
        i18n: undefined
    },
    columns: [
        {
            header: 'medical.grid.column.idTitle',
            dataIndex: 'patient.id',
            hidden: true
        },
        {
            header: 'medical.grid.column.nameTitle',
            dataIndex: 'patient.name',
            width: 40

        },
        {
            header: 'medical.grid.column.surname1Title',
            dataIndex: 'patient.surname',
            width: 40

        },
        {
            header: 'medical.grid.column.surname2Title',
            dataIndex: 'patient.surname1',
            width: 40

        },
        {
            header: 'medical.grid.column.genreTitle',
            dataIndex: 'patient.genre',
            width: 30,
            hidden: true

        },
        {
            header: 'medical.grid.column.birthdayTitle',
            dataIndex: 'patient.birthDay',
            xtype: 'datecolumn',
            sortable: true,
            format: 'd-m-Y',
            width: 40
        },
        {
            header: 'medical.grid.column.phoneNumberTitle',
            dataIndex: 'patient.tlf',
            width: 30
        },
        {
            header: 'medical.grid.column.addressTitle',
            dataIndex: 'address',
            width: 50,
            hidden: true,
            renderer: function(value, meta, record, rowIndex, colIndex, store) {
                return new Ext.Template('{address}, {city}, {cp}, {country}').applyTemplate(record.data);
            }
        },
        {
            header: 'medical.grid.column.enabledTitle',
            dataIndex: 'patient.user.enabled',
            align: 'center',
            xtype: 'checkboxcolumn',
            width: 35

        }


    ],
    features: [{
            ftype: 'filters',
            autoReload: true,
            local: false,
            encode: true,
            filters: [{
                    type: 'string',
                    dataIndex: 'patient.name'
                },
                {
                    type: 'string',
                    dataIndex: 'patient.surname'
                },
                {
                    type: 'string',
                    dataIndex: 'patient.surname1'
                },
                {
                    type: 'list',
                    enumType: 'com.abada.cleia.entity.user.Genre',
                    dataIndex: 'genre',
                    options: [['Male', 'Male'],
                        ['Female', 'Female'],
                        ['Undefined', 'Undefined']]
                },
                {
                    type: 'string',
                    dataIndex: 'patient.tlf'
                },
                 {
                    type: 'boolean',
                    dataIndex: 'patient.user.enabled'
                },
                {
                    type: 'date',
                    dataIndex: 'patient.birthDay',
                    dateFormat: 'd/m/Y'
                }
            ]
        }],
    forceFit: true,
    constructor: function(config) {
        this.initConfig(config);
        if (config.url) {

            this.store = Ext.create('Abada.data.JsonStore', {
                storeId: 'gridPatientStore',
                sorters: {
                    property: 'patient.name',
                    direction: 'ASC'
                },
                fields: [
                    {
                        name: 'patient.birthDay',
                        mapping: 'patient.birthDay',
                        type: 'date',
                        dateFormat: 'c'
                    },
                    {
                        name: 'patient.id',
                        mapping: 'patient.id'
                    },
                    {
                        name: 'patient.name',
                        mapping: 'patient.name'
                    }, {
                        name: 'patient.surname',
                        mapping: 'patient.surname'
                    }, {
                        name: 'patient.surname1',
                        mapping: 'patient.surname1'
                    }, {
                        name: 'patient.genre',
                        mapping: 'patient.genre'
                    }, {
                        name: 'patient.address.city',
                        mapping: 'patient.address.city'
                    }, {
                        name: 'patient.address.address',
                        mapping: 'patient.address.address'
                    }, {
                        name: 'patient.address.cp',
                        mapping: 'patient.address.cp'
                    }, {
                        name: 'patient.address.countryAddress',
                        mapping: 'patient.address.countryAddress'
                    }, {
                        name: 'patient.tlf',
                        mapping: 'patient.tlf'
                    }, {
                        name: 'patient.user.username',
                        mapping: 'patient.user.username'
                    }, {
                        name: 'patient.user.enabled',
                        mapping: 'patient.user.enabled',
                        type: 'boolean'
                    }, {
                        name: 'roles',
                        mapping: 'patient.user.roles'
                    }, {
                        name: 'groups',
                        mapping: 'patient.user.groups' //grupo
                    }, {
                        name: 'ids',
                        mapping: 'patient.user.ids'
                    }],
                url: this.config.url,
                root: 'data',
                scope: this,
                pageSize: config.page
            });
        }

        if (config.details) {

            this.columns.push({
                xtype: 'actioncolumn',
                width: 30,
                header: 'medical.grid.column.detailsTitle',
                align: 'center',
                scope: this,
                items: [{
                        icon: getRelativeURI('patient/image/group-expand.png'),
                        handler: function(grid, rowIndex, colIndex) {
                            var record = grid.getStore().getAt(rowIndex);
                            this.getDetails(record, config.i18n);
                        }
                    }]
            });

        }

        this.selModel = Ext.create('Ext.selection.RowModel');
        this.bbar = Ext.create('Ext.toolbar.Paging', {
            store: this.store,
            pageSize: this.store.pageSize
        });
        this.callParent([config]);
    },
    getDetails: function(record, i18n) {

        var detailsPanel = Ext.create('App.medical.js.common.medicaldetailsPanel', {
            record: record,
            width: 410,
            height: 450,
            autoScroll: true,
            i18n: i18n
        });

        var detailsWind = Ext.create('Ext.window.Window', {
            title: i18n.getMsg('medical.wind.medicalDetails'),
            id: 'detailsWind',
            closable: true,
            modal: true,
            width: 450,
            autoHeight: true,
            items: [detailsPanel]
        });

        detailsWind.show();

    }

});