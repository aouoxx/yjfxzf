/**
 * 选择单个人员
 * @param select_id 下拉框ID
 * @param dlg_id   当前页面ID
 */
function selectPerson(select_id,dlg_id){
	$("#"+select_id).kendoComplexeSelect({
        multiple: false,
        selectClick: function (e) {
        	_select_Person("#"+select_id, dlg_id, false);
        }
       
    });
}

/**
 * 
 * 选择多个人员
 * @param select_id 下拉框ID
 * @param dlg_id   当前页面ID
 */
function selectMultiPerson(select_id,dlg_id){
	$("#"+select_id).kendoComplexeSelect({
        multiple: true,
        selectClick: function (e) {
        	_select_Person("#"+select_id, dlg_id, true);
        }
    });
}

function _select_Person(el, dlgId, multiple) {
	var date = new Date();
	quick_dialog("auth_person_select_dlg", "选择人员", ROOT +"/xtgl/zhgl/person/select?el=" + encodeURIComponent(el) + "&dlgId=" + dlgId + "&multiple=" + (multiple || false) +"&_t="+date.getTime(), 950, 480);
}


function selectSendMessageMultPerson(options){
	var title, input_id, pdlg_id, dlg_id;
	if(options == null || options == undefined){
		
	}else{
		if(typeof options.title == "undefined" || options.title == null){
			title = "";
		}else{
			title = options.title;
		}
		if(typeof options.input_id == "undefined" || options.input_id == null){
			input_id = "";
		}else{
			input_id = options.input_id;
		}
		if(typeof options.pdlg_id == "undefined" || options.pdlg_id == null){
			pdlg_id = "";
		}else{
			pdlg_id = options.pdlg_id;
		}
		if(typeof options.dlg_id == "undefined" || options.dlg_id == null){
			dlg_id = "selectSendMessageMultPerson";
		}else{
			dlg_id = options.dlg_id;
		}
		
		if(title == null || title == ""){
			title = "选择人员";
		}
		quick_dialog(dlg_id, title, ROOT +"/xtgl/zhgl/person/selectSendMessageMultPerson?input_id="+input_id+"&dlg_id="+dlg_id +"&pdlg_id=" + pdlg_id, 900,550, null);	
	}
	
}
    
    
function multSelect_sendMessage_callback(idinput,selectedItems){
	
	
	var person="";
	
	$.each(selectedItems, function (idx, it) {
		
		person+=idx+":"+it+";";
      });

	$("#"+idinput).val(person);
	
	
	
}
 
var sendPerson="";

function selectedSendPerson(selectedItems,idinput){


	var person="";
	$.each(selectedItems, function (idx, it) {
		person+=idx+":"+it+";";
      });

	var result="";


    var contentArray=person.split(";");
  		contentArray.sort();
    
	var sendPersonArray=sendPerson.split(";");
    	sendPersonArray.sort();
	
	var resultArray=new Array();	
		
	resultArray=mergeArray(contentArray,sendPersonArray);
	
  for(var i=0;i<resultArray.length;i++){
      if(resultArray[i] !=""){
    	result+=resultArray[i]+";";
      }
  }
   
    sendPerson=result;
    $("#"+idinput).val($("#"+idinput).val() + sendPerson);

}


function mergeArray(arr1,arr2){

    var arr=[];
    for(var i=0;i<arr1.length;i++){
    	arr.push(arr1[i]);
    }

	var dup;
	
	for(var i=0;i<arr2.length;i++){
		dup=false;
		for(var j=0;j<arr1.length;j++){
		
    		if(arr2[i]==arr1[j]){
    			dup=true;
    			break;
    		}
			
			if(!dup){
				arr.push(arr2[i]);
			}
		}
	
	return arr;
	}
	
	
}

