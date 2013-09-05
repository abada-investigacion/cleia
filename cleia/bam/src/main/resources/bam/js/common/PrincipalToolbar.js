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

Ext.define('App.bam.js.common.PrincipalToolbar',{
    extend:'Ext.toolbar.Toolbar',
    config:{
        patientname:'',
        i18n:undefined
    },    
    constructor:function(config){
        this.initConfig(config);
        this.patientname=config.patientname;
        //this.height= 25;
        this.callParent([config]);
        return this;
    },
    initComponent:function(){        
        this.items=[
        {
            icon:getRelativeURI('bam/image/atras.png'),
            width : 93,
            text:this.i18n.getMsg('bam.toolbar1.back'),
            scope:this,
            handler:this.onBackClick
        },
        {
            xtype: 'label',
            width : 900,
            forId: 'patient',
            text: this.i18n.getMsg('bam.toolbar1.patient',this.patientname)
        }
       
        ];
        
        this.callParent();     
        
        this.addEvents('back');
    },
    onBackClick:function(){
        this.fireEvent('back',this);
    }
});

