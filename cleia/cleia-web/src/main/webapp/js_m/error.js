/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.require([
    'Ext.MessageBox'
])

Ext.onReady(function() {

 Ext.MessageBox.show({
     title:'Error Inesperado',
     msg: 'Ha ocurrido un error inesperado. Contacte con el servicio tecnico',
   //  buttons: Ext.Msg.YESNOCANCEL,
     icon: Ext.MessageBox.ERROR,
     closable:false
});

});