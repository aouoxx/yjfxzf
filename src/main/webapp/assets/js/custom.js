$.fn.gisList = function(opts){
	var that = $(this);
	var operate = opts.operate;
	that.on("click",".g_search_item",function(e){
		$(this).addClass("current").siblings().removeClass("current");
	});
	$.each(operate,function(i,v){
		var el = v.selector;
		var fun = v.execute;
		that.on("click",el,function(e){
			fun($(this),$(this).parents(".g_search_item"));
		});
	});
};

/**
 * 地图冒泡初始化
 */
function gisPopInit(){
	var level = $('.g-d-btns').height()/38, index = 0;
	if (level > 1) {
		$('.g-d-btn-dir').show();
		$('.g-d-btn-left').addClass('disabled');
	}
	$('.g-d-btn-dir').on('click', function() {
		var turnRight = $(this).hasClass('g-d-btn-right');
		if (turnRight) {
			if (index === (level - 1)) {
				return;
			}
			$('.g-d-btn-left').removeClass('disabled');
			index ++;
		} else {
			if (index === 0) {
				return;
			}
			$('.g-d-btn-right').removeClass('disabled');
			index --;
		}
		if (index === 0) {
			$('.g-d-btn-left').addClass('disabled');
		}
		if (index === (level - 1)) {
			$('.g-d-btn-right').addClass('disabled');
		}
		$('.g-d-btns').css({'margin-top': (-1*index*38)+'px'});
	});
}

/**
 * 通过dialog_id获取Iframe
 * @param dialog_id
 * @returns
 */
function get_fun_in_dialog(dialog_id) {
    var dlg = get_dialog(dialog_id);
    if (!dlg)return null;
    var iframe = $("iframe", dlg.element[0]);
    if (!iframe)return null;
    return iframe[0].contentWindow;
}

function cus_dialog(id, title, url, width, height, btn, reload, useiframe, cus_option, dlg_type, target) {
    if (window != top.window) {
        top.cus_dialog(id, title, url, width, height, btn, reload, useiframe, cus_option, dlg_type, target);
        return;
    }
    if (btn) {
        btn_running(btn);
    }
    var $el;
    if (id) {
        $el = $("#" + id);
    } else {
        id = "dlg_" + new Date().getTime();
    }
    if (!$el || $el.length == 0) {
        $el = $('<div id="' + id + '"></div>').appendTo('body');
    }
    if (useiframe == undefined || useiframe == null) {
        useiframe = true;
    }
    var win = $("#" + id).data("kendoWindow");
    if (win) {
        win.open();
        if (reload) {
            win.refresh({
                iframe: useiframe
            });
        }
        return;
    }

    var options = {
        title: title,
        content: url,
        //appendTo : top.window.document.body,
        iframe: useiframe,
        modal: false,
        pinned: true,
        actions: ["Maximize", "Refresh", "Close"],
        activate: function () {
            if (useiframe) {
                $("#" + id).addClass("k-window-iframecontent-loading");
                iframe_loaded($("iframe", "#" + id)[0], function () {
                    $("#" + id).removeClass("k-window-iframecontent-loading");
                });
            }
        },
        open: function () {
        	if (dlg_type && target) {
        		target.addClass("clicked");
        	}
            this.center();
        },
        close: function () {
            if (btn) {
                btn_enabled(btn);
            }
            if (dlg_type && target) {
            	target.removeClass("clicked");
            	var imgUrl = target.attr("src");
            	imgUrl = imgUrl.substring(0, imgUrl.indexOf("_hover")) + ".png";
            	target.attr("src", imgUrl).siblings(".g-bigscreen-btn-text").css({"color": "#fff"});
        	}
            this.destroy();
        }
    };
    if (width) {
        options.width = width;
        options.orginalWidth = width;
    }
    if (height) {
        options.height = height;
    }

    $el.hcWindow($.extend({}, options, cus_option));
}


/**
*
* DataGrid 快捷方法
* 参数(el)：指定装载tree的容器
* 参数(formId)：表单的id，自动将指定表单中的数据传送到服务器，可选
* 参数(readUrl)：获取数据的 URL 地址
* 参数(columns)：TODO
* 参数(options)：tree 配置，可选
* 参数(dsOptions)：DataSource 配置，可选
* 参数(operates)：生成操作按钮，可选
* 参数(toolbarId)：工具条的id，可选
**/
function myquick_datagrid(el, formId, readUrl, columns, options, dsOptions, operates, toolbarId) {
   options = options || {};
   toolbarId = toolbarId || "#grid_toolbar_template";
   var _dbopt = {
       transport: {
           read: {
               url: readUrl,
               type: "POST",
               dataType: "json",
               cache: false,
               data: function (p) {
                   if (formId) {
                       var arr = $(formId).serializeArray();
                       if (arr && arr.length > 0) {
                           var d = {};
                           $.each(arr, function (idx, it) {
                               if (it.value) {
                                   // 多选情况下，同一key有多个值
                                   if (d[it.name]) {
                                       d[it.name] = d[it.name] + ","+it.value;
                                   }
                                   else{
                                       d[it.name] = it.value;
                                   }
                               }
                           });
                           return d;
                       }
                   }
                   return {};
               }
           },
           parameterMap: function (data, type) {
               if (type == "read") {
                   data.pageNo = data.page;
                   data.page = undefined;
                   data.take = undefined;
                   data.skip = undefined;
                   delete data.page;
                   delete data.take;
                   delete data.skip;
                   return data;
               }
           }
       },
       schema: {
           type: "json",
           data: "rows",
           total: "total"
       },
       serverPaging: true,
       pageSize: options.pageSize || 20,
       change: function () {
           _constants.DATA_GRID_NUMBER_START = 0;
       }
   };
   if (dsOptions) {
       _dbopt = $.extend(_dbopt, dsOptions);
   }
   var _opt = {
       dataSource: new kendo.data.DataSource(_dbopt),
       toolbar: hc.template($(toolbarId).html().replace(/&lt;/g, '<').replace(/&gt;/g, '>')),
       height: "100%",
       groupable: false,
       selectable: true,
       pageable: {
           refresh: true,
           pageSizes: [10, 20, 50, 100],
           input: true,
           buttonCount: 5,
           messages: {
               empty: "没有数据",
               display: "共{2}条数据，本页显示{0}-{1}条",
               page: "",
               of: "/ {0}",
               itemsPerPage: "",
               first: "首页",
               last: "末页",
               next: "下一页",
               previous: "上一页",
               refresh: "刷新",
               morePages: "更多页"
           }
       },
       showNumber: true,
       columns: columns
   };
   if (options) {
       _opt = $.extend(_opt, options);
   }

   if (_opt.showNumber) {
       _opt.columns.splice(0, 0, {
           title: "序号",
           width: 60,
           template: "#:(++(_constants.DATA_GRID_NUMBER_START)) #",
           attributes: {
               "class": "grid_number"
           }
       });
   }

   $(el).hcGrid(_opt);

   var $toolbar = $(".k-grid-toolbar .toolbar .grid_operates", el);
   if (operates && operates.length > 0) {
       $.each(operates, function (idx, it) {
           var $oper = $('<li><button ' + (it.id ? 'id="' + it.id + '"' : '') + '>' + (it.icon ? '<i class="' + it.icon + '"></i>' : '') + it.name + '</button></li>');
           if (it.onclick && $.isFunction(it.onclick)) {
               $("button", $oper).bind("click.toolbar", function (e) {
                   it.onclick(this, $(el).data("kendoGrid"));
               });
           }
           $("button", $oper).hcButton();
           $toolbar.append($oper);
       });
   }

//   if (_opt.excel) {
//       var $gridtool = $(".k-grid-toolbar .toolbar .grid_tool", el);
//       var $oper = $('<li><a href="javascript:void(0)" class=""><i class="fa fa-file-excel-o"></i></a></li>');
//       $oper.bind("click.gridtool", function (e) {
//           var grid = $(el).data("kendoGrid");
//           if (grid) {
////               grid.saveAsExcel();
//           }
//       });
////       $gridtool.append($oper);
//   }


   if (formId) {
       $(formId).submit(function (event) {
           var _this = this;
           var grid = $(el).data("kendoGrid");
           btn_running($("*[type='submit']", _this));

           grid.dataSource.xquery({
               page: 1,
               pageSize: grid.dataSource.pageSize()
           }, function () {
               btn_enabled($("button[type='submit']", _this));
           });
           event.preventDefault();
           return false;
       });
   }

}
