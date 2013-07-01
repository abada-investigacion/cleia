/**
 * Panel that show a image with information about status of process instance bpm
 */
Ext.define('App.bam.js.common.ProcessInstancePanel', {
    requires: ['Ext.Img', 'Ext.Date', 'Abada.Ajax'],
    extend: 'Ext.panel.Panel',
    autoScroll: true,
    config: {
        urlImagePI: undefined,
        urlImageP: undefined,
        urlDiagramInfo: undefined,
        processInstanceId: undefined,
        processId: undefined,
        start: undefined,
        end: undefined
    },
    initComponent: function() {
        this.callParent();
        this.addImageCmp();
        this.setTitle(this.processId);

        this.addEvents('click', 'nodeselected', 'nodeover', 'diagramloadsuccess', 'diagramloadfailure');

        if (this.autoLoad)
            this.loadDiagramInfo();
    },
    addImageCmp: function() {

        if (this.processInstanceId) {
            var url = getRelativeServerURI(this.urlImagePI, [this.processInstanceId]);
        } else {
            if (this.processId) {
                url += getRelativeServerURI(this.urlImageP, [this.processId]);
            }
        }
        var initParams;
        if (url.indexOf('?') >= 0)
            initParams = true;
        else {
            initParams = false;
        }

        if (this.start) {
            if (initParams)
                url += '&';
            else {
                url += '?';
                initParams = true;
            }
            url += 'start=' + Ext.htmlEncode(Ext.Date.format(this.start, 'Y-m-d'));
        }
        if (this.end) {
            if (initParams)
                url += '&';
            else {
                url += '?';
                initParams = true;
            }
            url += 'end=' + Ext.htmlEncode(Ext.Date.format(this.end, 'Y-m-d'));
        }

        var urlAppend;
        if (initParams)
            urlAppend = '&';
        else
            urlAppend = '?';

        urlAppend += 'unique=' + new Date().valueOf();
        this.imageCmp = Ext.create('Ext.Img', {
            src: url + urlAppend
        });
        this.add(this.imageCmp);
        this.imageCmp.addListener('render', this.onRenderEvt, this);
    },
    onClick: function(e, t) {
        this.fireEvent('click', this, this.getX(e), this.getY(e), e.browserEvent.clientX, e.browserEvent.clientY);

        this.fireNodeSelected(this.getX(e), this.getY(e), e.browserEvent.clientX, e.browserEvent.clientY);
    },
    onMouseMove: function(e, t) {
        this.fireNodeOver(this.getX(e), this.getY(e), e.browserEvent.clientX, e.browserEvent.clientY);
    },
    getX: function(e) {
        return e.getX() + this.getScrollLeft(e) - this.getOffsetX(e);
    },
    getY: function(e) {
        return e.getY() + this.getScrollTop(e) - this.getOffsetY(e);
    },
    getOffsetX: function(e) {
        var obj = e.target;
        var result = 0;
        while (obj.offsetParent) {
            result += obj.offsetParent.offsetLeft;
            obj = obj.offsetParent;
        }
        return result;
    },
    getOffsetY: function(e) {
        var obj = e.target;
        var result = 0;
        while (obj.offsetParent) {
            result += obj.offsetParent.offsetTop;
            obj = obj.offsetParent;
        }
        return result;
    },
    getScrollLeft: function(e) {
        var obj = e.target;
        var result = 0;
        while (obj.offsetParent) {
            result += obj.offsetParent.scrollLeft;
            obj = obj.offsetParent;
        }
        return result;
        //return e.browserEvent.layerX+e.browserEvent.currentTarget.offsetParent.scrollLeft;
    },
    getScrollTop: function(e) {
        var obj = e.target;
        var result = 0;
        while (obj.offsetParent) {
            result += obj.offsetParent.scrollTop;
            obj = obj.offsetParent;
        }
        return result;
        //return e.browserEvent.layerY+e.browserEvent.currentTarget.offsetParent.scrollTop;
    },
    fireNodeOver: function(x, y, rX, rY) {
        var node = this.findNode(x, y);
        if (node) {
            if (!this.actualNode) {
                this.actualNode = node;
                this.fireEvent('nodeover', this, this.processId, node.name, node.id, x, y, rX, rY);
            } else {
                if (this.actualNode.id != node.id) {
                    this.actualNode = node;
                    this.fireEvent('nodeover', this, this.processId, node.name, node.id, x, y, rX, rY);
                }
            }
        }
    },
    fireNodeSelected: function(x, y, rX, rY) {
        var node = this.findNode(x, y);
        if (node) {
            this.fireEvent('nodeselected', this, this.processId, node.name, node.id, x, y, rX, rY);
        }
    },
    findNode: function(x, y) {
        if (this.diagramInfo && this.diagramInfo.nodeList && this.diagramInfo.nodeList.length > 0) {
            var i, node;
            for (i = 0; i < this.diagramInfo.nodeList.length; i++) {
                node = this.diagramInfo.nodeList[i];
                if (x > node.x && x < node.x + node.width)
                    if (y > node.y && y < node.y + node.height)
                        return node;//.substr(3);
            }
        }
        return undefined;
    },
    onRenderEvt: function(cmp, opts) {
        this.imageCmp.mon(cmp.getEl(), {
            click: this.onClick,
            mousemove: this.onMouseMove,
            scope: this
        });
    },
    loadDiagramInfo: function() {
        if (this.urlDiagramInfo) {
            Abada.Ajax.requestJsonObject({
                scope: this,
                method: 'GET',
                url: getRelativeServerURI(this.urlDiagramInfo, [this.processId]),
                success: function(result) {
                    this.diagramInfo = result;
                    this.fireEvent('diagramloadsuccess', this);
                },
                failure: function() {
                    this.diagramInfo = undefined;
                    this.fireEvent('diagramloadfailure', this);
                }
            });
        }
    }
});