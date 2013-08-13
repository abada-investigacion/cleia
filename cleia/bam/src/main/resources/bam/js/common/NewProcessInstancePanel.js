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

Ext.define('App.bam.js.common.NewProcessInstancePanel',{
    requires: ['Abada.Ajax','Abada.form.field.SimpleGroupingComboBox','Ext.button.Button','Ext.window.MessageBox'],
    extend:'Ext.form.Panel',        
    config:{
        urlOncoguides:undefined,
        patientId:undefined
    },
    initComponent:function(){                        
        
        this.cbOncoguide=Ext.create('Abada.form.field.SimpleGroupingComboBox',{
            emptyText : 'seleccione un Proceso',
            width:200,
            url:this.urlOncoguides,
            allowBlank:false
        });
        
        this.button=Ext.create('Ext.button.Button', {
            text: 'Crear',
            formBind: true
        });
        this.button.addListener('click',this.onButtonClick,this);
        
        this.items=[this.cbOncoguide];
        this.buttons=[this.button];
        
        this.callParent();                
        
        this.addEvents('success','failure');
    },
    onButtonClick:function(){        
        var panel=Ext.create('App.bam.js.common.FormCustomFrame',{                
            url:getRelativeServerURI('/rs/form/process/{0}/patient/{1}/render',[this.cbOncoguide.getValue(),this.patientId]),
            height:App.height
        });
        panel.addListener('success',function(frame,processInstanceId){  
            Ext.Msg.alert('Info','Oncoguia creada correctamente',                        
                function(){
                    win.close();    
                    this.fireEvent('success', this,processInstanceId);
                },this);       
        },this);
        panel.addListener('failure',function(frame,error){            
            Ext.Msg.alert('Error','No se puede cargar el formulario.'+error,                        
                function(){
                    win.close();    
                    this.fireEvent('failure', this,error);
                },this);        
        },this);
        
        var win=Ext.create('Ext.window.Window', {
            title: 'Tarea '+this.cbOncoguide.getValue(),
            height: App.height,
            width: 900,
            autoScroll:true,
            layout: 'fit',
            modal:true,
            items: [panel]
        });
        
        win.show();        
    },    
    loadContent:function(){
        this.cbOncoguide.loadStore();
    }
});
