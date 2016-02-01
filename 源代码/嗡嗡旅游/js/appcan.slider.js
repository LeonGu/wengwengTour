/*! appcan v0.1.12 |  from 3g2win.com */
appcan.define("slider", function(a, b, c) {
    var d = '<div class="slider-item ub-fh ub-fv ub-img1" style="background-image:url(<%=data.img%>)">\r\n    <span class="uabs"><%=data.label%></span>\r\n    </div>',
        e = appcan.view.template(d);
    function f() {
        return "ontouchstart" in window ?
        void 0 : !0
    }

    function g(b) {
        appcan.extend(this, appcan.eventEmitter);
        var c = this;
        c.option = a.extend({
            selector : "body",
            dir : "hor",
            hasIndicator : !0,
            hasLabel : !1,
            aspectRatio : 0,
            index : 0,
            auto : !1
        }, b, !0), this.isReset = !0, c.ele = a(c.option.selector), c.option.aspectRatio && c.ele.css("height", c.ele.offset().width * c.option.aspectRatio), c.option.data && c.set(data), c.option.auto && c.autoMove(c.option.auto)
    }
    g.prototype = {
        buildItem : function(b) {
            var c = this,
                d = a(e({
                data : b,
                option : c.option
            }));
            return d[0].lv_data = b, d
        },
        _moveTo : function(a, b) {
            var c = this;
            b !== !1 && (c.container.addClass("slider-anim"), c.container.on("webkitTransitionEnd", function() {
                c.container.off("webkitTransitionEnd"), c.container.removeClass("slider-anim"), c.option.index >= c.option.itemCount && (c.option.index = 0), c.option.index < 0 && (c.option.index = c.option.itemCount - 1), c.emit("change", c, c.option.index), c._moveTo(c.option.index, !1), c.isReset = !0
            }));
            var d = -(c.option.index + 1) * c.ele.offset().width;
            c.container.css("-webkit-transform", "translateX(" + d + "px)");
            var e = c.ele.offset().width / c.option.itemCount;
            c.focus.css("-webkit-transform", "translateX(" + c.option.index * e + "px)"), c.option.hasLabel && c.label.html(c.option.data[c.option.index + 1].label)
        },
        drag : function(a) {
            var b = this,
                c = -(b.option.index + 1) * b.ele.offset().width + a;
            b.container.css("-webkit-transform", "translateX(" + c + "px)"), b.isReset = !1
        },
        reset : function() {
            var a = this;
            this.isReset || (a._moveTo(a.option.index), a.autoMove(a.option.auto))
        },
        set : function(b) {
            var c = this;
            c.option.itemCount = b.length, c.container = c.container || a('<div class="slider-group ub-fh ub-fv"></div>'), c.container.empty(), b.unshift(b[b.length - 1]), b.push(b[1]), c.option.data =
            b;
            for (var d in b) {
                var e = c.buildItem(b[d]);
                c.container.append(e)
            }
            c.ele.append(c.container);
            var f = c.ele.offset().width / c.option.itemCount;
            return c.option.hasLabel && (c.label = c.label || a('<div class="uinn1 ulev-1 ut-s label sc-text-hint"></div>'), c.ele.append(c.label)), c.focus = c.focus || a('<div class="utra focus bc-head"></div>'), c.focus.css("width", f), c.focus.css("-webkit-transform", "translateX(" + c.option.index * f + "px)"), c.ele.append(c.focus), c._moveTo(c.option.index, !1), c.ele.on("swipeMoveLeft", function(a) {
                c.timer && clearInterval(c.timer), c.drag(-a._args.dx)
            }), c.ele.on("swipeMoveRight", function(a) {
                c.timer && clearInterval(c.timer), c.drag(a._args.dx)
            }), c.ele.on("touchend touchcancel", function(a) {
                c.reset()
            }), c.ele.on("tap", function(a) {
                c.emit("clickItem", c, c.option.index, b[c.option.index + 1])
            }), c.ele.on("swipeLeft", function(a) {
                c._moveTo(++c.option.index), c.autoMove(c.option.auto)
            }), c.ele.on("swipeRight", function(a) {
                c._moveTo(--c.option.index), c.autoMove(c.option.auto)
            }), c.ele.on("swipeUp", function(a) {
                c._moveTo(c.option.index), c.autoMove(c.option.auto)
            }), c.ele.on("swipeDown", function(a) {
                c._moveTo(c.option.index), c.autoMove(c.option.auto)
            }), a(document).on("touchstart", function(a) {
                var b = c.ele.offset().left,
                    d = c.ele.offset().top,
                    e = c.ele.width(),
                    f = c.ele.height(),
                    g = a.touches[0];
                return g.pageX > b && g.pageX < b + e && g.pageY > d && g.pageY < d + f ? (appcan.window.setMultilPopoverFlippingEnbaled(1), !1) :
                void  appcan.window.setMultilPopoverFlippingEnbaled(0)
            }), a(document).on("touchmove", function(a) {
                var b = c.ele.offset().left,
                    d = c.ele.offset().top,
                    e = c.ele.width(),
                    f = c.ele.height(),
                    g = a.touches[0];
                return g.pageX > b && g.pageX < b + e && g.pageY > d && g.pageY < d + f ? (appcan.window.setMultilPopoverFlippingEnbaled(1), c.timer && clearInterval(c.timer), !1) :
                void 0
            }), a(document).on("touchcancel", function(a) {
                var b = c.ele.offset().left,
                    d = c.ele.offset().top,
                    e = c.ele.width(),
                    f = c.ele.height(),
                    g = a.touches[0];
                return g.pageX > b && g.pageX < b + e && g.pageY > d && g.pageY < d + f ? (appcan.window.setMultilPopoverFlippingEnbaled(0), !1) :
                void 0
            }), a(document).on("touchend", function(a) {
                var b = c.ele.offset().left,
                    d = c.ele.offset().top,
                    e = c.ele.width(),
                    f = c.ele.height(),
                    g = a.touches[0];
                return g.pageX > b && g.pageX < b + e && g.pageY > d && g.pageY < d + f ? (appcan.window.setMultilPopoverFlippingEnbaled(0), !1) :
                void 0
            }), c
        },
        autoMove : function(a) {
            if (a) {
                var b = this;
                b.timer && clearInterval(b.timer), b.timer = setInterval(function() {
                    b._moveTo(++b.option.index, !0)
                }, a)
            }
        }
    }, c.exports = function(a) {
        return new g(a)
    }
}); 