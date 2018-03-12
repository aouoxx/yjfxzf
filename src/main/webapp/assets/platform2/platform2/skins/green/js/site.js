function bind_normal_search(advsearch_container){
    if(advsearch_container) $(advsearch_container).empty();
    $(".ui-form-normal-search .ui-form-normal-search-key").mSelect();

    $(".ui-form-normal-search .ui-form-normal-search-key").bind("change",function(){
      var container = $(this).closest(".ui-form-normal-search");
      var searchArea = $(".search-area",container);
      var option = $("option:selected",this);
      var type = option.attr("type");
      var control;
      switch(type){
      case "input_text" :
        control = $('<input type="text" class="ui-input" name="' + option.attr("value") + '" placeholder="请输入要搜索的内容">');
        break;
      case "input_datetime":
        control = $('<input type="text" class="ui-input Wdate date" name="' + option.attr("value") + '" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\'})" value="" />');
        break;
      case "input_date":
          control = $('<input type="text" class="ui-input Wdate date" name="' + option.attr("value") + '" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd\'})" value="" />');
          break;
      case "input_date":
          control = $('<input type="text" class="ui-input Wdate date" name="' + option.attr("value") + '" onclick="WdatePicker({dateFmt:\'HH:mm:ss\'})" value="" />');
          break;
      }
      searchArea.empty().append(control);
    });
}
