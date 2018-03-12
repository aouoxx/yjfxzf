/**
 * 主菜单。
 * author : songjiawei@supcon.com
 */
$.fn.navmenu = function (options) {
    this
        .each(function () {
            var _opts = options || {};
            var _c = 0;
            var _this = this;
            $(this)
                .addClass("ui-nav-menu")
                .wrap('<div class="ui-nav-menu-wrapper"></div>')
                .before(
                '<div class="ui-nav-carousel"><a class="l" href="javascript:void(0)"><i class="fa fa-chevron-left"></i></a><a class="r" href="javascript:void(0)"><i class="fa fa-chevron-right"></i></a></div>')
                .after('<div class="ui-nav-menu-sub"></div>');

            var wrapper = $(this).closest(".ui-nav-menu-wrapper");
            var subMenu = $(".ui-nav-menu-sub", wrapper);
            var carousel = $(".ui-nav-carousel", wrapper);
            var l = $("a.l", carousel);
            var r = $("a.r", carousel);

            var levelOneMenus = $(">li", _this).addClass("levelOne");
            if (levelOneMenus.length == 0)
                return;

            levelOneMenus.each(function () {
                $(">ul", this).hide();
            });

            var _carouselStatus = function () {
                if ($(">li.hidden", _this).length == 0) {
                    carousel.hide();
                } else {
                    carousel.show();
                }
                if ($(">li:first-child", _this).hasClass("hidden")) {
                    l.addClass("active");
                } else {
                    l.removeClass("active");
                }
                if ($(">li:last-child", _this).hasClass("hidden")) {
                    r.addClass("active");
                } else {
                    r.removeClass("active");
                }
            };

            var _topDistance = function () {
                var ulWidth = $(_this).width() - 60;
                var totalWidth = 0;
                levelOneMenus.each(function () {
                    var li = $(this);
                    if (totalWidth == 0 && li.hasClass("hidden"))
                        return;
                    totalWidth += li.outerWidth();
                    if (totalWidth > ulWidth) {
                        li.addClass("hidden").removeClass("show").hide();
                    } else {
                        li.removeClass("hidden").addClass("show").show();
                    }
                });
                _carouselStatus();
            };
            var _carouselEvent = function () {
                l.bind("click.navmenu", function () {
                    if (!$(this).hasClass("active"))
                        return;
                    var firstShow = $(">li.show:eq(0)", _this);
                    var prevHidden = firstShow.prev("li");
                    prevHidden.removeClass("hidden").addClass("show").show();
                    _topDistance();
                });
                r.bind("click.navmenu", function () {
                    if (!$(this).hasClass("active"))
                        return;
                    var firstShow = $(">li.show:eq(0)", _this);
                    if (firstShow.hasClass("current")) {
                        // var nextShow =
                        // firstShow.nextAll("li.show").get(0);
                        // firstShow.insertAfter(nextShow);
                        // firstShow = $(nextShow);
                    }
                    firstShow.addClass("hidden").removeClass("show").hide();

                    _topDistance();
                });
            };

            var _distance = function (o) {
                var ulWidth = o.width() - 100;
                var totalWidth = 0, n = 0;
                $(">li", o).each(function (idx, it) {

                    totalWidth += $(this).width();
                    if (totalWidth > ulWidth) {
                        return;
                    }
                    n = idx;
                });
                var menu = o.data("kendoMenu");
                $(">li", o).each(function (idx, it) {
                    if (idx <= n) {
                        return;
                    }
                    if (idx == (n + 1)) {
                        menu.insertBefore([{
                            text: "更多",
                            cssClass: "more",
                            items: []
                        }], this);
                    }
                    $(this).prependTo($(".more ul", o));//.removeClass("ui-nav-menu-sub-li");
                });
            };
            var _action = function (li) {
                var link = li.attr("link");
                if (typeof currentSubLink !== "undefined" && currentSubLink && currentSubLink !== link) {
                	link = currentSubLink;
                }
                if (link) {
                    $("#platform_loading").show();
                    if (_opts.type == "iframe" && _opts.targetFrame) {
                        var encodedUrl = $.base64.encode(link);
                        window.location.hash = encodedUrl;
                        $(_opts.targetFrame)[0].src = link;
                    }
                }
            };
            var _buildSub = function (o, currentSubLink, reload) {
                if (reload == undefined || reload == null) {
                    reload = true;
                }
                $(levelOneMenus).removeClass("hover");
                o.addClass("hover");
                // o.prependTo(_this);
                var levelTwoMenus = $(">ul", o);
                if (levelTwoMenus.length == 0)
                    return;
                subMenu.css("overflow", "hidden").empty();
                $(">li", levelTwoMenus).addClass("ui-nav-menu-sub-li");
                var newo = levelTwoMenus.clone().show().addClass("ui-nav-menu-sub-ul").appendTo(subMenu).hcMenu({
                    closeOnClick: true,
                    select: function (e) {
                        var item = e.item;
                        var li = $(item);
                        var cli = li;
                        if (!li.hasClass("ui-nav-menu-sub-li")) {
                            li = li.closest(".ui-nav-menu-sub-li");
                        }
                        $("li", li.closest(".ui-nav-menu-sub-ul")).removeClass("current");
                        li.addClass("current");
                        //选中一级菜单
                        var levelOneMenu = $(newo.context);
                        if (!levelOneMenu.hasClass("levelOne")) {
                        	levelOneMenu = levelOneMenu.parent().parent();
                        }
                        if (!levelOneMenu.hasClass("current")) {
                        	levelOneMenu.parent().children("li.current").removeClass("current");
                        	levelOneMenu.addClass("current");
                        } else {
                        	if (typeof currentSubLink !== "undefined" && currentSubLink && currentSubLink === cli.attr("link")) {
                        		return;
                        	}
                        }

                        var more = $(item).parent().parent().parent(".more");
                        if (more.length > 0) {
                            more.addClass("current");
                        }

                        _action(cli);
                    }
                });
                setTimeout(function () {
                    if (currentSubLink) {
                        var lis = $("li[link='" + currentSubLink + "']", newo);
                        if (lis.length > 0) {
                            var li = $(lis[0]);
                            li.click();
//                            li.addClass("current");
//                            if (reload) {
//                                _action(li);
//                            }
                        }
                    }
//                    _distance(newo);
//                    subMenu.css("overflow", "");
                }, 10);
            }

            var _doCurrent = function (o, manual) {
                var m = $(".k-menu", subMenu).data("kendoMenu");
                if (m) {
                    m.destroy();
                }
                subMenu.empty();
                var tolink = null;
                var _hash = window.location.hash;
                if (_hash) {
                    tolink = $.base64.decode(_hash);
                }

                var fli = $(">ul>li:first-child", o);
                if (manual && _opts.autofirst) {
                    tolink = fli.attr("link");
                    if (!tolink) {
                        fli = $(">ul>li:first-child", fli);
                        tolink = fli.attr("link");
                    }
                }
                _buildSub(o, tolink, false);


                if (manual && _opts.autofirst) {
                    _action(fli);
                }
            }

            var _resize = function () {
                _doCurrent($(">li.current", _this));
                _topDistance();
            };

            var _position = function (reload) {
                var currenLevelOneLi = $(levelOneMenus.get(0));
                var currentSubLink = null;
                var _hash = window.location.hash;
                if (_hash) {
                    var tolink = $.base64.decode(_hash);
                    if (tolink) {
                        var lis = $("li[link='" + tolink + "']", wrapper);
                        if (lis.length > 0) {
                            var li = $(lis[0]);
                            var levelOne = li.closest(".levelOne");
                            currenLevelOneLi = levelOne;
                            currentSubLink = tolink;
                        }
                    }
                }
                _buildSub(currenLevelOneLi, currentSubLink, reload);
            };

            /* init */


            _position();

            _topDistance();
            _carouselEvent();

            levelOneMenus.bind("click.nav_menu", function () {
//                if ($(this).hasClass("current"))
//                    return;
                _doCurrent($(this), true);

            });
            var handler = {
                onResize: function () {
                    _resize();
                },
                position: function () {
                    // 定位
                    _position(true);
                }
            };
            $(_this).data("navmenu", handler);
        });
};
