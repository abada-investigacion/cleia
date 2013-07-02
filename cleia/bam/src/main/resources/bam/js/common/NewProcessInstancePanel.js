/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.define('App.bam.js.common.NewProcessInstancePanel',{
    requires: ['Abada.Ajax','Abada.form.field.SimpleGroupingComboBox','Ext.button.Button','Ext.window.MessageBox'],
    extend:'Ext.form.Panel',        
    config:{
        urlNewOncoguide:undefined,
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
        
//        var types = Ext.create('Ext.data.Store', {
//            fields: ['id', 'value'],
//            data : [
//            {
//                "id":"ONCOGUIDE", 
//                "value":"Proceso Oncoguia"
//            },
//            {
//                "id":"EMERGENCY", 
//                "value":"Proceso Urgencias"
//            },
//            {
//                "id":"UNDEFINED", 
//                "value":"Proceso Tipo Desconocido"
//            }
//            ]
//        });
//
//        // Create the combo box, attached to the states data store
//        this.cbType=Ext.create('Ext.form.ComboBox', { 
//            emptyText : 'seleccione tipo Oncoguia',
//            width:200,
//            store: types,
//            queryMode: 'local',
//            displayField: 'value',
//            valueField: 'id',
//            editable:false,
//            allowBlank:false
//        });
        
        
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
            url:getRelativeServerURI('/rs/form/process/{0}/patient/{1}/render',[this.cbOncoguide.getValue(),this.patientId])
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
            height: 600,
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
