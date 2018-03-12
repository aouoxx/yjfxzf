

/**
 * contextPath : 工程的$!{rc.contextPath}
 * 
 * spanId :页面存放地区select的span id
 * 
 * xzqhdmName :  表单中存放地区代码的input的name 如:jgxx.xzqhdm
 * 
 * width : 每个select的宽度数字类型   如：32.74%(共6列占合并5列)，33.26%(共4列占合并3列)
 * 
 * selectedValue : 修改时传入上次选中值
 * 
 * required : true/false
 * 
 * xzqhmcName : 表单中存放行政区划名称的input的name 如:jgxx.xzqhmc
 */
$.xzqhSelect = function(contextPath, spanId, xzqhdmName, width, selectedValue, required, xzqhmcName){
	if(contextPath == undefined || contextPath == null){
		contextPath = "";
	}
	if(width == undefined || width == null || width == ''){
		width = "33.26%";
	}else{
		width = width;
	}
	var datarule = 'data-rule="行政区划:required;"';
	if(!required){
		datarule ="";
	}
	if(xzqhmcName == undefined || xzqhmcName == null){
		xzqhmcName = "xzqhmc";
	}
	var city = spanId+"_city";
	var county = spanId+"_county";
	var village = spanId+"_village";
	var xzqhdm = spanId+"_xzqhdm";
	var xzqhmc = spanId+"_xzqhmc";		
	var xzqhStr = '<input type="hidden" id="'+xzqhdm+'" name="'+xzqhdmName+'" value="'+selectedValue+'" /><input type="hidden" id="'+xzqhmc+'" name="'+xzqhmcName+'"/><input id="'+city+'" style="width:' + width +'"/><input id="'+county+'" style="width:' + width +'"/><input id="'+village+'" style="width:' + width +'"/>';
	$("#"+spanId).html(xzqhStr);
	
	//初始化市选择框
	$('#'+city).hcComboBox({
        dataTextField: "dqmc",
        dataValueField: "xzqhdm",
        dataSource: null,
        placeholder:"---请选择---"
      }).data("kendoComboBox");
    //初始化县选择框
    $('#'+county).hcComboBox({
        dataTextField: "dqmc",
        dataValueField: "xzqhdm",
        dataSource: null,
        placeholder:"---请选择---"
      }).data("kendoComboBox");	
    //初始化区选择框
    $('#'+village).hcComboBox({
        dataTextField: "dqmc",
        dataValueField: "xzqhdm",
        dataSource: null,
        placeholder:"---请选择---"
      }).data("kendoComboBox");	
    

	$.post(contextPath + "/xtgl/xzqh/findCity",{},function(result){
		if(result.status == "success"){
			var ds  = new Array();
				ds.push({xzqhdm: "", dqmc : "---请选择---"});
			$.each(result.data,function(){
				ds.push({"xzqhdm" : this.xzqhdm, "dqmc" : this.dqmc});
			});
			
			$('#'+city).data("kendoComboBox").setDataSource(new kendo.data.DataSource({
				  data: ds
				}));
			
		    //设置选中
			if(selectedValue != ""){
				if(selectedValue.length == 6){
					selectedValue = selectedValue +"000"; //补全9位代码
				}
				//获取地区代码前4位
				var temp = selectedValue.substring(0,4);
				temp = temp + "00000";
				//设置选中
				$('#'+city).data("kendoComboBox").value(temp);
				$('#'+city).trigger("change");
			}
			
		}
	});
	//市
	$('#'+city).change(function(){
		$('#'+xzqhdm).val($('#'+city).val());
		if($('#'+city).val() == ""){
			$('#'+county).data("kendoComboBox").setDataSource(new kendo.data.DataSource({
				  data: [{xzqhdm : "", dqmc: "---请选择---"}]
			}));
			$('#'+county).data("kendoComboBox").value("");
			$('#'+village).data("kendoComboBox").setDataSource(new kendo.data.DataSource({
				  data: [{xzqhdm : "", "dqmc":"---请选择---"}]
			}));
			$('#'+village).data("kendoComboBox").value("");
			return;
		}
		
		$.post(contextPath + "/xtgl/xzqh/findCounty",{xzqhdm : $('#'+city).val()},function(result){
			
			var ds  = new Array();
				ds.push({xzqhdm : "", dqmc : "---请选择---"});
			$.each(result.data,function(){
				ds.push({"xzqhdm" : this.xzqhdm, "dqmc" : this.dqmc});
			});
			$('#'+county).data("kendoComboBox").setDataSource(new kendo.data.DataSource({
				  data: ds
			}));		
			//设置选中
			if(selectedValue != ""){
				if(selectedValue.substring(4) != "00000"){  //如果是市，那后5位全是0；不是时需选中区、县。
					//获取地区代码前6位
					var temp = selectedValue.substring(0,6);
					temp = temp + "000";
					//设置选中
					$('#'+county).data("kendoComboBox").value(temp);
					$('#'+county).trigger("change");
				}else{
					selectedValue = "";//设置选中后把传入的选中值设成空
				}
			}
		});
	});
	//区、县
	$('#'+county).change(function(){
		$('#'+village).data("kendoComboBox").setDataSource(new kendo.data.DataSource({
			  data: [{xzqhdm : "", dqmc : "---请选择---"}]
		}));
		$('#'+village).data("kendoComboBox").value("");
		
		if($('#'+county).val() == ""){
			$('#'+xzqhdm).val($('#'+city).val());
			return;
		}
		$('#'+xzqhdm).val($('#'+county).val());
		
		$.post(contextPath + "/xtgl/xzqh/findVillage",{xzqhdm : $('#'+county).val()},function(result){
			var ds  = new Array();
			ds.push({xzqhdm: "", dqmc : "---请选择---"});
			$.each(result.data,function(){
				ds.push({"xzqhdm" : this.xzqhdm, "dqmc" : this.dqmc});
			});
			$('#'+village).data("kendoComboBox").setDataSource(new kendo.data.DataSource({
				  data: ds
			}));
			//设置选中
			if(selectedValue != ""){
				if(selectedValue.substring(6) != "000"){  //如果是县，那后3位全是0；不是时需选中、镇。
					//设置选中
					$('#'+village).data("kendoComboBox").value(selectedValue);
					$('#'+village).trigger("change");
					selectedValue = "";//乡、镇设置选中后把传入的选中值设成空
				}
			}
		});
	});
	//乡、镇
	$('#'+village).change(function(){
		$('#'+xzqhdm).val($('#'+village).val());
		if($('#'+village).val() == ""){
			$('#'+xzqhdm).val($('#'+county).val());
			if($('#'+county).val() == ""){
				$('#'+xzqhdm).val($('#'+city).val());
			}
		}
	});
	
};

/**
 *
 * 查看页面根据行政区划代码显示行政名称
 * 
 * { contextPath, spanId , xzqhdm, isQC }
 * 
 * 
 */
$.xzqh_mc = function(options){
	if(options == undefined || options == null || options == ""){
		return;
	}
	//
	var contextPath = options.contextPath;
	//要显示名称的span
	var spanId = options.spanId;
	//行政区划代码
	var xzqhdm = options.xzqhdm;
	//是否显示全称，默认显示名称
	var isQC = options.isQC;
	if(contextPath == undefined || options.contextPath == null){
		contextPath = "";
	}
	//行政区划代码为空时不做查询
	if(xzqhdm == undefined || xzqhdm == null || xzqhdm == ""){
		return;
	}
	if(isQC == undefined || isQC == null || isQC == ""){
		isQC = false;
	}
	$.post(contextPath + "/xtgl/xzqh/findRegion", { "xzqhdm" : xzqhdm },function(result){
		if(result.status == "success"){
			if(result.data){
				if(isQC){
					$("#"+spanId).html(result.data.dqqc);
				}else{
					$("#"+spanId).html(result.data.dqmc);
				}
			}
		}
	});
};
