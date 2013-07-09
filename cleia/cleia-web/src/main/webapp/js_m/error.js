/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.require([
    'Ext.window.MessageBox'
])

Ext.onReady(function() {

 Ext.Msg.show({
     title:'Error Inesperado',
     msg: 'Ha ocurrido un error inesperado. Contacte con el servicio tecnico',
   //  buttons: Ext.Msg.YESNOCANCEL,
     icon: Ext.Msg.ERROR,
     closable:false
});

});