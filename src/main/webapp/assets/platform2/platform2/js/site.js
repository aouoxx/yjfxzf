var _content_layouts = [];
var _hash;

function clearContentLayouts() {
    if (_inner_layout) {
        if (!_inner_layout.destroyed)
            _inner_layout.destroy();
        _inner_layout = null;
    }
    if (_content_layouts && _content_layouts.length > 0) {
        for (var i = 0; i < _content_layouts.length; i++) {
            if (_content_layouts[i]) {
                if (!_content_layouts[i].destroyed)
                    _content_layouts[i].destroy();
                _content_layouts[i] = null;
            }
        }

        _content_layouts = [];
    }
}

function loadUrl(url, defaultUrl) {
    $("#_loading").show();
    if (!url) return;
    var encodedUrl = $.base64.encode(url);
    window.location.hash = encodedUrl;
    _hash = window.location.hash;
    if (url.indexOf("?") > 0)
        url += "&";
    else
        url += "?";
    url += "_t=" + new Date().getTime();
    clearContentLayouts();
    try{if(_destructor && jQuery.isFunction(_destructor)){_destructor();}}catch(e){}
    $("#centerMain").load(url, function(responseText, textStatus, xhr) {
        // setTimeout(function(){$("#_loading").hide(0);},100);
        $("#_loading").hide(100, "linear");
        if (xhr.status == 200) {
            // $.cookie(cookie || 'loaded_url', encodedUrl, {
            //     expires: 7,
            //     path: '/'
            // });
        } else {
            if (defaultUrl) {
                //clearContentLayouts();
                loadUrl(defaultUrl,  null);
            }
        }
    });
}

$(function(){
    try{
        $(window).bind("hashchange",function(e){
            if (window.location.hash && window.location.hash != "#") {
		if(_hash != window.location.hash)
                	loadUrl($.base64.decode(window.location.hash.substring(1)));
                return;
            }
        });
    }catch(e){}
});

function init_main_page(defaultUrl) {
    if (window.location.hash) {
        if (window.location.hash != "#")
            try {
                loadUrl($.base64.decode(window.location.hash.substring(1)), defaultUrl);
                return;
            } catch (e) {}
    }
    loadUrl(defaultUrl,null);
    // var loadedUrl = $.cookie(cookie || 'loaded_url');
    // if (loadedUrl) {
    //     loadUrl($.base64.decode(loadedUrl), defaultUrl);
    //     return;
    // }
}

function toCamel(s1) {
    return s1.replace(/_(\w)/, function(x) {
        return x.slice(1).toUpperCase();
    });
}

function toClassName(s1) {
    s1 = toCamel(s1);
    s1 = s1.substring(0, 1).toUpperCase() + s1.substring(1);
    return s1;
}

function openFullScreen(f,b,g){var c=b||window.screen.availHeight;var a=g||window.screen.availWidth;if(window.navigator.userAgent.indexOf("MSIE 8.0")>-1){c=c-45;a=a-20;}else{if(window.navigator.userAgent.indexOf("MSIE 7.0")>-1){c=c-50;a=a-15;}else{if(window.navigator.userAgent.indexOf("AppleWebKit")>-1){c=c-70;a=a-20;}else{if(window.navigator.userAgent.indexOf("Firefox")>-1){c=c-70;a=a-20;}}}}var d="width = "+a+",height="+c+",scrollbars=yes,resizable =yes,top=0,left=0,toolbar=no,menubar=no,location=no,status=no";window.open(f,"",d);}

(function($) {
    $.cachedScript = function( url, options ) {
          options = $.extend( options || {}, {
            dataType: "script",
            cache: true,
            url: url
          });
          return $.ajax( options );
        };
    $.fn.box = function(options) {
        var _this = this;
        if (options == "resize") {
            var c = this.data("_cal");
            if (c)
                c();
            return;
        }

        var _cal = function() {
            if ($.isFunction(options.width)) {
                _this.outerWidth(options.width());
            } else {
                _this.outerWidth(options.width || 250);
            }

            if ($.isFunction(options.height)) {
                $(".ui-box-container", _this).height(options.height() - $(".ui-box-head", _this).outerHeight() - 7);
            } else if (options.height) {
                $(".ui-box-container", _this).height(options.height - $(".ui-box-head", _this).outerHeight() - 7);
            }
        };
        this.data("_cal", _cal);
        _cal();
    };



    $
        .ajaxSetup({
            error: function(xhr, textStatus, errorThrown) {
                if (xhr.status == 401) {
                    $.message("会话已失效，请重新登录！", $.message.TYPE_ERROR, 4, true, true);
                    var _login_dlg = dialog({
                        content: '<form id="_login_dlg_form" class="ui-form" method="post" action="'+$.CONTEXT_PATH+'/platform/login"><input type="hidden" name="rtype" value="json" /><fieldset>' + '<div class="ui-form-item block"><label style="width:80px;" class="ui-label" for="">用户名:</label><input class="ui-input" type="text" name="username" value="" data-rule="required;" />' + '</div><div class="ui-form-item block"><label style="width:80px;" class="ui-label" for="">密码:</label><input class="ui-input" type="password" name="password" value="" data-rule="required;" />' + '</div><div class="ui-form-item block fn-mt-20"><label style="width:80px;" class="ui-label"></label><button class="btn btn-primary" type="submit"><i class="iconfont icon-key" style="color:#FFF;"></i>登录</button>' + '<a class="btn" href="javascript:void(0)" onclick="dialog.list[\'_login_dlg\'].close();"><i class="iconfont icon-minus-sign" style="color:#333;"></i>取消</a>' + '</div></fieldset></form>',
                        id: '_login_dlg',
                        lock: true,
                        width: 380,
                        height: 150,
                        title : '请重新登录'
                    });
                    $("#_login_dlg_form input[name='username']").focus();
                    $("#_login_dlg_form").submit(function(){
                        $.forms.submit("#_login_dlg_form",function(){
                            _login_dlg.close();
                        });
                        return false;
                    });
                    return false;
                } else if (xhr.status == 403) {
                    $.message("无权访问！", $.message.TYPE_ERROR, 4, false, true);
                    return false;
                } else if (xhr.status == 404) {
                    $.message("不存在的访问路径！", $.message.TYPE_ERROR, 4, false, true);
                    return false;
                } else if (xhr.status == 500) {
                    $.message("服务器发生错误！", $.message.TYPE_ERROR, 4, false, true);
                    return false;
                } else if (textStatus == "error") {
                    $.message("未知错误！", $.message.TYPE_ERROR, 4, false, true);
                    return false;
                }
            }
        });
    $.dialogs = {
        show: function(id, content, callback, width, height, title, okVal, showOkBtn, cancelVal, padding) {
            var config = {
                content: content,
                id: id,
                padding: padding || '0',
                lock: true,
                width: width || 650,
                height: height || 380,
                cancel: true,
                cancelVal: '<i class="iconfont" data-value="cancel" style="color:#333;">&#xf056;</i>' + (cancelVal || "取消")
            };
            if (!(showOkBtn === false)) {
                config.okVal = '<i class="iconfont" data-value="ok" style="color:#FFF;">&#xf058;</i>' + (okVal || "确定");
                config.ok = function(btn) {
                    if (callback) {
                        return callback(this, btn);
                    }
                    return true;
                };
            }
            if (title) {
                config.title = title;
            }
            return dialog(config);
        },
        normalDialog: function(id, url, callback, width, height, title, okVal, showOkBtn, cancelVal, padding) {
            return $.dialogs.show(id, 'load:' + url, callback, width, height, title, okVal, showOkBtn, cancelVal, padding);
        }
    };
    $.forms = {
        linkRunning: function(a, msg) {
            a = $(a);
            if (!a.data("org"))
                a.data("org", a.html());
            a.html(msg || '<i class="iconfont icon-refresh iconfont-spin"></i>运行中...').attr("disabled",true).addClass("disabled");
            if (!a.data("onclick"))
                a.data("onclick", a[0].onclick);
        },
        linkEnabled: function(a) {
            a = $(a);
            a.attr("disabled",false).removeClass("disabled");
            if (a.data("org"))
                a.html(a.data("org"));
            if (a.data("onclick"))
                a[0].onclick = a.data("onclick");
        },
        handleMessage: function(responseText, callback, form, errorCallback) {
            if (responseText && (responseText.status == 'success')) {
                $.message(responseText.message, $.message.TYPE_OK, 4, true, true);
                if (callback) {
                    callback(responseText, form);
                }
            } else if (responseText && (responseText.status == 'fail' || responseText.status == 'error')) {
                $.message(responseText.message, $.message.TYPE_ERROR, 4, false, true);
                if (errorCallback) {
                    errorCallback(responseText, form);
                }
            } else {
                $.message("发生异常，请重试！", $.message.TYPE_ERROR, 4, false, true);
                if (errorCallback) {
                    errorCallback(responseText, form);
                }
            }
        },
        submits: function(form, callback, btn, errorCallback) {
            var $el = $(form);
            $el.isValid(function(v) {
                if (v) {
                    if (btn) {
                        $.btn.running($(btn), "正在提交...");
                    }
                    $el.ajaxSubmit({
                        success: function(responseText, statusText, xhr, $form) {
                            $.forms.handleMessage(responseText, callback, $el, errorCallback);
                        },
                        error: function(o) {
                            $.message("发生异常，请重试！", $.message.TYPE_ERROR, 4, false, true);
                        },
                        complete: function() {
                            if (btn) {
                                $.btn.enabled($(btn));
                            }
                        }
                    });
                }
            }, false);
        },
        submit: function(form, callback, btn, errorCallback) {
            var $el = $(form);
            if (btn) {
                $.btn.running($(btn), "正在提交...");
            }
            $el.ajaxSubmit({
                success: function(responseText, statusText, xhr, $form) {
                    $.forms.handleMessage(responseText, callback, $el, errorCallback);
                },
                error: function(o) {
                    $.message("发生异常，请重试！", $.message.TYPE_ERROR, 4, false, true);
                },
                complete: function() {
                    if (btn) {
                        $.btn.enabled($(btn));
                    }
                }
            });
        },
        validSubmit: function(el, callback, errorCallback) {
            var $el = $(el);
            $el.on('valid.form', function(e, form) {
                var $btn = $("*[type=submit]", form);
                $.btn.running($btn, "正在提交...");
                $(this).ajaxSubmit({
                    success: function(responseText, statusText, xhr, $form) {
                        $.forms.handleMessage(responseText, callback, $el, errorCallback);
                    },
                    error: function(o) {
                        $.message("发生异常，请重试！", $.message.TYPE_ERROR, 4, false, true);
                    },
                    complete: function() {
                        $.btn.enabled($btn);
                    }
                });
            });
        }
    };
})(jQuery);

(function($) {

    $.fn.magicTab = function(options) {

        if (typeof options == "string") {

            return;
        }

        var _currentPane, _currentItem;
        var _this = this;
        var _config = {
            defaults: {
                resizable: false,
                closable: false,
                spacing_open: 5
            }
        };

        var _judgeShow = function(currentItem) {

            var dataTarget = currentItem.attr("data-target");
            var href = currentItem.attr("href");
            var pane;
            var loaded;
            if (dataTarget) {
                pane = $(dataTarget);
                loaded = pane.data("layouted");
                pane.show();
                if (currentItem.attr("data-reload") == "true" || !pane.data("loaded")) {
                    href = currentItem.attr("href");
                    pane.load(href, function() {
                        pane.data("loaded", true);
                        _processLayout(pane, currentItem.attr("data-reload") == "true");
                    });
                }
            } else {
                pane = $(href);
                loaded = pane.data("layouted");
                pane.show();
                _processLayout(pane, currentItem.attr("data-reload") == "true");
            }

            if (loaded) {
                var fun = currentItem.attr("data-reload-function");
                if (fun) {
                    try {
                        eval(fun + "()");
                    } catch (e) {}
                }
            }
            _currentPane = pane;
        };

        var _processLayout = function(pane, reload) {
            if (reload || !pane.data("layouted")) {
                pane.width("100%").height("100%");
                var _old_layout = pane.data("_layout");
                if (_old_layout && !_old_layout.destroyed) {
                    _old_layout.destroy();
                }
                if (options.resize && $.isFunction(options.resize)) {
                    _config.onresize_end = function(name, element, state) {
                        if (name == "center") {
                            options.resize(pane, name, state);
                        }
                    };
                }

                var _pane_layout = pane.layout(_config);
                _content_layouts.push(_pane_layout);
                if (options.load && $.isFunction(options.load)) {
                    options.load(pane, _pane_layout);
                }
                pane.data("layouted", true);
                pane.data("_layout", _pane_layout);
            }

        };

        _judgeShow($(".ui-tab-items .current a", _this));

        $(".ui-tab-items a", _this).click(function(e) {
            var href = $(this).attr("href");
            if (href == null || href == "" || href == "#") {
                $.message($(this).attr("data-msg") || "操作出错啦！", $.message.TYPE_ERROR, 4, false, true);
                return false;
            }
            $(".ui-tab-items .current", _this).removeClass("current");
            $(this).parent().addClass("current");
            $(".ui-tab-panels>div", _this).hide();
            _currentItem = $(this);
            _judgeShow($(this));
            return false;
        });

        return {
            currentPane: function() {
                return _currentPane;
            },
            reload: function() {
                _judgeShow(_currentItem);
            }
        };

    };
})(jQuery);

(function($) {
    $.select_refer = function(_this,url,dlg_id,form_id,title,width,height,keys_callback,func_callback){
      if(url.indexOf("?")>0){url+="&";}else{url+="?";}
      url += "formId=" + form_id;
      url += "&dlgId=" + dlg_id;
      if(keys_callback){url+="&callbackKeys=" + keys_callback;}

      var _dlg = $.dialogs.normalDialog(dlg_id, url, function(dlg,btn){
        var row = $("#"+dlg_id+" .bDiv>table").getSelectedRows()[0];
        _callback_func(row);
      },width,height,title);
      var _callback_func = function(row){
        if(!row)return;
        var keys = keys_callback.split(",");
        for(var i = 0 ; i < keys.length;i++){
          var listKey = keys[i].split(".").slice(2).join(".");
          if(row[listKey]==undefined || row[listKey] == null) continue;
          $("#"+form_id+" *[assname='"+keys[i]+"']").val(row[listKey]);
          $("#"+form_id+" *[name='"+keys[i]+"']").val(row[listKey]);
        }
        _dlg.close();
      };
      $("#" + dlg_id).data("_callback_func", _callback_func);
    };
    var _common_base_select = function(_this, url, dlg_id, title, width, height, func_selected, options, func_query) {
        var _opt = options || {};
        var query = "?";
        if (_opt.muilt == true) {} else {
            _opt.muilt = false;
        }
        query += "muilt=" + _opt.muilt;
        if (_opt.muilt) {
            var muilt_select_container = $(_this).closest(".ui-muilt-select");
            var valInput = $("input[type='hidden']", muilt_select_container);
            if (valInput.val()) {
                query += "&selected=" + valInput.val();
            }
        }
        if (_opt.queryCallback && $.isFunction(_opt.queryCallback)) {
            query += _opt.queryCallback(_opt);
        }
        //clearContentLayouts();
        var _dlg = $.dialogs.show(dlg_id, url + query, function(dlg, btn) {
            $.btn.running(btn);
            var selected;
            if (func_selected && $.isFunction(func_selected))
                selected = func_selected(_opt);
            //            if (!selected || selected.length == 0) {
            //                $.btn.enabled(btn);
            //                $.message("没做任何选择", $.message.TYPE_WARN, 2, true, true);
            //                return false;
            //            }
            if (!selected) {
                selected = [];
            }

            if (_opt.muilt) {
                if (selected.length > 0) {
                    var muilt_select_container = $(_this).closest(".ui-muilt-select");
                    var valInput = $("input[type='hidden']", muilt_select_container);
                    var v = "";
                    $("ul", muilt_select_container).empty();
                    for (var i = 0; i < selected.length; i++) {
                        //var v = valInput.val();
                        //							if (("," + v + ",").indexOf("," + selected[i][_opt.muiltFill.val] + ",") >= 0)
                        //								continue;
                        $("ul", muilt_select_container).append('<li>' + selected[i][_opt.muiltFill.key] + '</li>');
                        if (v)
                            v += ",";
                        v += selected[i][_opt.muiltFill.val];
                    }
                    valInput.val(v);
                    muilt_select_container.muiltSelect();
                }
            } else {
                if (_opt.fill && selected.length > 0) {
                    $.each(_opt.fill, function(idx, it) {
                        $(it).val(selected[0][idx]);
                    });
                }
            }
            if (_opt.callback && $.isFunction(_opt.callback)) {
                _opt.callback(selected, dlg, btn);
            }
            $.btn.enabled(btn);
        }, width, height, title, "确定选择", true, "取消");

        var _dbl_click_func = function(row) {
            if (_opt.fill && row) {
                $.each(_opt.fill, function(idx, it) {
                    $(it).val(row[idx]);
                });
            }
            _dlg.close();
        };
        $("#" + dlg_id).data("_dbl_click_func", _dbl_click_func);
    };
    var _common_select = function(_this, url, dlg_id, title, width, height, func_selected, options, func_query) {
        _common_base_select(_this, "load:" + $.CONTEXT_PATH + url, dlg_id, title, width, height, func_selected, options, func_query);
    };
    $.commonSelect = _common_select;
    $.selectPoi = function(_this, options) {
        options.muilt = false;
        var url = options.url || ($.CONTEXT_PATH + "/platform/gis/poi/select");
        _common_base_select(_this, "url:" + url, "_select_gis_poi", "标记地理信息", 600, 400, function(_opt) {
            return _select_gis_poi._select_point();
        }, options);
    };

    $.selectUser = function(_this, options) {
        _common_select(_this, "/platform/authorization/user/select", "_select_user", "选择用户", 880, 600, function(_opt) {
            return plat_auth_user_select_datagrid.getSelectedRows();
        }, options);
    };
    /**
     * 通用选人员
     * @param _this
     * @param options
     */
    $.selectPerson = function(_this, options) {
        if ($._plat_org_person_select_main_layout && !$._plat_org_person_select_main_layout.destroyed)
            $._plat_org_person_select_main_layout.destroy();
        if ($._plat_org_person_select_inner_layout && !$._plat_org_person_select_inner_layout.destroyed)
            $._plat_org_person_select_inner_layout.destroy();
        _common_select(_this, "/platform/organization/person/select"+(options.permission ? "?permission=" + options.permission : ""), "_select_person", "选择人员", 880, 600, function(_opt) {
            return plat_org_person_select_grid.getSelectedRows();
        }, options);
    };
    /**
     * 通用选岗位
     * @param options
     */
    $.selectRole = function(_this, options) {
        _common_select(_this, "/platform/authorization/role/select", "_select_role", "选择角色", 350, 500, function(_opt) {
            var selected;
            if (_opt.muilt) {
                selected = plat_auth_role_select_tree.getCheckedNodes(true);
            } else {
                selected = plat_auth_role_select_tree.getSelectedNodes();
            }
            return selected;
        }, options);
    };
    /**
     * 通用选岗位组件
     */
    $.selectPosition = function(_this, options) {
        _common_select(_this, "/platform/organization/position/select", "_select_position", "选择岗位", 350, 500, function(_opt) {
            var selected;
            if (_opt.muilt) {
                selected = plat_org_position_select_tree.getCheckedNodes(true);
            } else {
                selected = plat_org_position_select_tree.getSelectedNodes();
            }
            return selected;
        }, options);
    };
    /**
     * 通用选部门组件
     */
    $.selectDepartment = function(_this, options) {
        _common_select(_this, "/platform/organization/department/select", "_select_department", "选择部门", 350, 500, function(_opt) {
            var selected;
            if (_opt.muilt) {
                selected = plat_org_department_select_tree.getCheckedNodes(true);
            } else {
                selected = plat_org_department_select_tree.getSelectedNodes();
            }
            return selected;
        }, options);
    };
    $.fn.muiltSelect = function(options) {
        var combineVal = function(vals) {
            var v = "";
            for (var i = 0; i < vals.length; i++) {
                if (vals[i])
                    v += vals[i] + ",";
            }
            if (v != "") {
                v = v.substring(0, v.length - 1);
            }
            return v;
        };
        this.each(function() {
            var _this = this;
            var _opt = options || {};

            var valInput = $("input[type='hidden']", _this);

            $("ul li", this).each(function(idx, it) {
                var me = $(this);
                if (me.hasClass("muilt-select-inited"))
                    return;
                me.addClass("muilt-select-inited");
                me.append('<a href="javascript:void(0)" class="ui-input-remove">x</a>');
                $(".ui-input-remove", this).click(function() {
                    if (_opt.removeCallback && $.isFunction(_opt.removeCallback)) {
                        _opt.removeCallback($(this), _this);
                    } else {
                        var li = $(this).parent("li");
                        //					var ul = $(this).closest("ul");
                        //					var idx = $("li", ul).index(li);
                        //					if (idx < 0) {
                        //						alert("数据错误?");
                        //						return false;
                        //					}
                        var arr = valInput.val().split(",");
                        if (arr.length == 0) {
                            alert("数据错误?");
                            return false;
                        }
                        arr.splice(idx, 1);
                        valInput.val(combineVal(arr));
                        li.remove();
                    }
                });
            });
        });


    };

    $.fn.suggests = function(options) {
        var _opt = options || {};
        this.each(function() {
            var _this = this;
            var params = _opt.params || {};
            if (_opt.key) {
                params[_opt.key] = function() {
                    return $(_this).val();
                };
            }
            _opt.params = params;
            _opt.type = "POST";
            $(this).autocomplete(_opt);
        });
    };
})(jQuery);
