/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.require([
    'Ext.form.Panel',
    'Ext.form.field.Text',
    'Ext.form.action.StandardSubmit',
    'Ext.button.Button',
    'Ext.window.Window',
    'Ext.util.KeyNav',
    'Abada.Ajax',
    'Abada.Base64'
]);

Ext.onReady(function() {

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
        return 'Basic ' + Abada.Base64.encode(login.getForm().findField('j_username').getValue() + ':' + login.getForm().findField('j_password').getValue());
    }

    function formSubmitPriv() {
        if (login.getForm().isValid()) {
            login.getForm().submit({
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
    }

    var login = Ext.create('Ext.form.Panel', {
        region: 'center',
        url: 'j_spring_security_check',
        defaultType: 'textfield',
        monitorValid: true,
        frame: false,
        standardSubmit: true,
        bodyPadding: '15 10 15 10',
        defaults: {
            labelStyle: 'margin-left: 15px;',
            labelPad: 5,
            labelWidth: 130
        },
        items: [
            {
                fieldLabel: 'Nombre de Usuario',
                name: 'j_username',
                id: 'j_username',
                allowBlank: false
            }, {
                fieldLabel: 'Contrase&ntilde;a',
                name: 'j_password',
                id: 'j_password',
                allowBlank: false,
                inputType: 'password'
            }],
        buttons: [
            {
                xtype: 'component',
                style: {
                    top: 0
                },
                html: '<a href="main.htm">' +
                        '<img alt=\" \" src=\"' + getRelativeURI('/images/logos/dnie.png') + '\" style=\"height:30px;\" />' +
                        '</a>'
            }, '->'
                    , {
                text: 'Acceso',
                id: 'blogin',
                formBind: true,
                handler: formSubmit,
                tooltip: 'Acceso a la aplicaci&oacute;n'
            }]
    });

    var win = Ext.create('Ext.window.Window', {
        id: 'wlogin',
        closable: false,
        title: 'Acceder',
        items: [login]
    });

    var view = Ext.create('Ext.container.Viewport', {
        //layout: 'ux.center',               
        autoScroll: true,
        cls: ['body-abada'],
        items: [
        ]
    });

    win.show();

    new Ext.util.KeyNav('wlogin', {
        'enter': formSubmit,
        scope: login
    });

});