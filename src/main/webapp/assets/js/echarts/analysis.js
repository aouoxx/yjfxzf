
/**
 * 饼图统计。针对单个柱状且可调用回调函数
 * @param option
 * 		  option.title:统计图表名称
 * 		  option.selected:默认不选中对象
 * 		  option.legendData:统计图表类型名称(数组)
 * 		  option.seriesName:统计数据名称
 * 		  option.seriesData:统计图表数据值(数组)
 * 		  option.elementId:统计图表所在div的id
 * 		  option.callBack:点击图表触发的函数
 */
function pieChart(option){
			
		if(!option.selected)
		{
			option.selected = {};
		}
		var pieOption = {
		    title : {
		        text: option.title,
		        subtext: ""!=option.subtext?option.subtext:"",
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter:'{b} : {c} ({d}%)'
		    },
		    legend: {
		    	orient:'vertical',
		    	x:'left',
		    	selected:option.selected,
		        data:option.legendData
		       
		    },
		    toolbox: {
		        show : true,
		        feature : {
		        	dataView : {
		                show : true,
		                title : '数据视图',
		                readOnly: false,
		                lang : ['数据视图', '关闭', '刷新']
		            },
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    series : [
		    	{
		            name:option.seriesName,
		            type:'pie',
		            radius:'70%',
		            center:['50%','50%'],
		            itemStyle:{
		            	normal:{
		                	labelLine:{
		                		show:true,
		                		length:5
		                	}
		            	}
		            },
		            data:option.seriesData
		        }
		    ]
		};
		
		var domMain = document.getElementById(option.elementId); 
    	pieOne = echarts.init(domMain);
    	pieOne.showLoading();
    	pieOne.setOption(pieOption,false);	
    	pieOne.hideLoading();
		
		pieOne.on(echarts.config.EVENT.CLICK, option.callBack);
}





/**
 * 柱状图统计。针对单个柱状且可调用回调函数
 * @param option
 * 		  option.title:统计图表名称
 * 		  option.selected:默认不选中对象
 * 		  option.legendData:统计图表类型名称(数组)
 * 		  option.xData:统计图表x轴数据(数组)
 * 		  option.seriesData:统计图表y轴数据值(数组)
 * 		  option.elementId:统计图表所在div的id
 * 		  option.callBack:点击图表触发的函数
 */
function barChart(option){
		if(!option.selected)
		{
			option.selected = {};
		}
		var	barOption = {
			    title : {
			    	text: option.title,
			    	subtext: ""!=option.subtext?option.subtext:"",
					x:'center'
			    },
			    tooltip : {
			        trigger: 'axis'
			    },
			    legend: {
			    	selected:option.selected,
			    	y:'bottom',
			        data: option.legendData
			    },
			    toolbox: {
			        show : true,
			        feature : {
			        	dataView : {
			                show : true,
			                title : '数据视图',
			                readOnly: false,
			                lang : ['数据视图', '关闭', '刷新']
			            },
			            magicType : {show: true, type: ['line', 'bar']},
			            restore : {show: true},
			            saveAsImage : {show: true}
			        }
			    },
			    calculable : false,
			    xAxis : [
			        {
			            type : 'category',
			            data : option.xData
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value'
			        }
			    ],
			    series : option.seriesData
			};
		
		var domMain = document.getElementById(option.elementId); 
		pieOne = echarts.init(domMain);
    	pieOne.showLoading();
    	pieOne.setOption(barOption,false);	
    	pieOne.hideLoading();
		
		pieOne.on(echarts.config.EVENT.CLICK, option.callBack);
}  