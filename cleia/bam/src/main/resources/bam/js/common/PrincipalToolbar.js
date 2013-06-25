/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.define('Oggi.bam.js.common.PrincipalToolbar',{
    extend:'Ext.toolbar.Toolbar',
    config:{
        patientname:''
    },
    
    constructor:function(config){
        this.initConfig(config);
        this.patientname=config.patientname;
        this.height= 25;
        this.callParent([config]);
        return this;
    },
    initComponent:function(){        
        this.items=[
        {
            icon:getRelativeURI('bam/image/atras.png'),
            width : 93,
            text:'Atras',
            scope:this,
            handler:this.onBackClick
        },
        {
            xtype: 'label',
            width : 900,
            forId: 'patient',
            text: 'Paciente: '+this.patientname
        }
       
        ];
        
        this.callParent();     
        
        this.addEvents('back');
    },
    onBackClick:function(){
        this.fireEvent('back',this);
    }
});

