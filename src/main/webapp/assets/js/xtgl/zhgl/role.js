function selectRole(input_id,dlg_id,value_id,value_name){
	
	$("#"+input_id).kendoComplexeSelect({
        multiple: false,
        selectClick: function (e) {
            _select_jgxx("#"+input_id, dlg_id, false);
        }
       
    });

	//所属菜单初始化
	var id = value_id;
	var name = value_name;
	if(id > 0){
		var ser = get_el_in_dialog(dlg_id, '#'+input_id).data("kendoComplexeSelect");
        ser.add(id, name.replace("</span>", "").replace(/<span.*>/, ""));
	}
	
}

function selectRoleMultiLayer(input_id,dlg_id){
	$("#"+input_id).kendoComplexeSelect({
        multiple: true,
        selectClick: function (e) {
        	_select_jgxx("#"+input_id, dlg_id, true);
        }
    });
}

function _select_jgxx(el, dlgId, multiple) {
	quick_dialog("auth_role_select_dlg", "选择角色", ROOT + "/xtgl/qxgl/role/select?el=" + encodeURIComponent(el) + "&dlgId=" + dlgId + "&multiple=" + (multiple || false), 280, 480);
}