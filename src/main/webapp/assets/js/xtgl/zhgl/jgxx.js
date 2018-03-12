function selectJgxx(input_id,dlg_id,value_id,value_name,jgdj){
	
	$("#"+input_id).kendoComplexeSelect({
        multiple: false,
        selectClick: function (e) {
            _select_jgxx("#"+input_id, dlg_id, false,jgdj);
        }
       
    });

	//所属菜单初始化
	var id = value_id;
	var name = value_name;
	if(name == ""){
		name = "请选择";
	}
	if(id > 0){
		var ser = get_el_in_dialog(dlg_id, '#'+input_id).data("kendoComplexeSelect");
        ser.add(id, name.replace("</span>", "").replace(/<span.*>/, ""));
	}
	
}

function selectMultiLayer(input_id,dlg_id,jgdj){
	$("#"+input_id).kendoComplexeSelect({
        multiple: true,
        selectClick: function (e) {
        	_select_jgxx("#"+input_id,dlg_id,true,jgdj);
        }
    });
}

function _select_jgxx(el, dlgId, multiple,jgdj) {
	if( jgdj  == undefined || jgdj == "undefined") {
		jgdj ="";
	}
	quick_dialog("auth_jgxx_select_dlg", "选择机构",  ROOT +"/xtgl/zhgl/jgxx/select?el=" + encodeURIComponent(el) + "&dlgId=" + dlgId + "&multiple=" + (multiple || false)+"&jgdj=" + jgdj, 365, 500);
}