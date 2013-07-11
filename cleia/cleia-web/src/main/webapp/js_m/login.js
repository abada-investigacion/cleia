/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.require([
    'Abada.Ajax',
    'Abada.Base64'
]);

Ext.setup({
    onReady: function() {

        function formSubmit() {
            authHeader = getBasicAuthentication();
            Abada.Ajax.request({
                url: App.urlServer + App.urlServerRoles,
                headers: {
                    Authorization: authHeader
                },
                method: 'GET',
                success: function(result, request) {
                    formSubmitPriv();
                },
                failure: function() {
                }
            });
            //formSubmitPriv();
        }

        function getBasicAuthentication() {
            return 'Basic ' + Abada.Base64.encode(login.getAt(0).getValue() + ':' + login.getAt(1).getValue());
        }

        function formSubmitPriv() {
            login.submit({
                method: 'POST',
                waitTitle: 'Conectando',
                waitMsg: 'Comprobando usuario y contrase&ntilde;a...',
                failure: function(form, action) {
                },
                success: function() {
                    //window.location='main.htm';
                }
            });
        }

        var login = Ext.create('Ext.form.Panel', {
            url: 'j_spring_security_check',
            fullscreen: true,
            standardSubmit: true,
            items: [
                {
                    xtype: 'fieldset',
                    title: 'Acceder',
                    defaults: {
                        required: true
                    },
                    items: [{
                            xtype: 'textfield',
                            label: 'Usuario',
                            name: 'j_username',
                            id: 'j_username'
                        }, {
                            xtype: 'passwordfield',
                            label: 'Contrase&ntilde;a',
                            name: 'j_password',
                            id: 'j_password'
                        }]
                },
                {
                    xtype: 'toolbar',
                    docked: 'top',
                    height:30,
                    items: [{
                            text: 'Acceso',
                            ui: 'round',
                            id: 'blogin',
                            scope: this,
                            handler: formSubmit
                        }]
                }]
        });

        Ext.Viewport.add(login);
    }});