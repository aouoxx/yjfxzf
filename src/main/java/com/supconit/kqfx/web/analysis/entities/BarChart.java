package com.supconit.kqfx.web.analysis.entities;

public class BarChart {
	// y轴名称
	private String yaxisname;

	// 数字后缀名称
	private String numberSuffix;

	// 是否显示画布背景
	private String showcanvasbg;

	// y轴的步长
	private String yAxisValuesStep;

	// 标题
	private String caption;

	// 图表位置，居中 、左、右
	private String captionAlignment;

	private String rotateLabels;
	// y轴
	private String yaxisvalues;
	// 背景
	private String bgAlpha;

	private String setAdaptiveYMin;

	private String yAxisMinValue;

	private String yAxisMaxValue;

	private String showNames;

	private String showalternatehgridcolor;

	private String showvalues;

	private String labeldisplay;

	private String divlinecolor;

	private String divlinealpha;

	private String useroundedges;

	private String animation;

	private String palette;

	private String xAxisNamePadding;

	private String showYAxisValues = "1";

	private String showLimits = "1";

//	private String yAxisValuesPadding = "2500";

	private String canvasBottomMargin = "0";

	private String baseFont = "宋体";
	private String baseFontSize = "12";
	private String baseFontColor = "e9c341";
	private String outCnvBaseFont;
	private String outCnvBaseFontSize;
	private String outCnvBaseFontColor;
	private String toolTipBgColor = "313131";
	private String toolTipBorderColor = "000000";
	private String showToolTipShadow = "1";
	private String placeValuesInside = "0";
	private String valuePadding = "20";
	private String valueFontColor;
	private String showBorder = "1";
	private String canvasBgColor = "#000000";
	
	private String numDivLines;

	// y轴是否取整 0为取整 【1~10】位
	private String decimalPrecision;

	// 图例属性
	// 图例边框
	private String legendbordercolor;
	// 图例透明度
	private String legendBgAlpha;

	public String getLegendbordercolor() {
		return legendbordercolor;
	}

	public void setLegendbordercolor(String legendbordercolor) {
		this.legendbordercolor = legendbordercolor;
	}

	public String getLegendBgAlpha() {
		return legendBgAlpha;
	}

	public void setLegendBgAlpha(String legendBgAlpha) {
		this.legendBgAlpha = legendBgAlpha;
	}

	public BarChart() {
		super();
		this.yaxisname = "事件数量";
		this.numberSuffix = "件";
		this.yaxisvalues = "1";
		this.bgAlpha = "0";
		this.showNames = "1";
		this.showalternatehgridcolor = "0";
		this.showvalues = "1";
		this.labeldisplay = "WRAP";
		this.divlinecolor = "#393939";
		this.divlinealpha = "70";
		this.useroundedges = "1";
		this.showcanvasbg = "1";
		this.animation = "1";
		this.palette = "2";
//		this.xAxisNamePadding = "0";
		this.decimalPrecision = "0";
//		this.yAxisMinValue = "20";
		this.yAxisMinValue = "0";
		this.numDivLines = "5";
	}

	public String getYaxisname() {
		return yaxisname;
	}

	public void setYaxisname(String yaxisname) {
		this.yaxisname = yaxisname;
	}

	public String getNumberSuffix() {
		return numberSuffix;
	}

	public void setNumberSuffix(String numberSuffix) {
		this.numberSuffix = numberSuffix;
	}

	public String getYaxisvalues() {
		return yaxisvalues;
	}

	public void setYaxisvalues(String yaxisvalues) {
		this.yaxisvalues = yaxisvalues;
	}

	public String getBgAlpha() {
		return bgAlpha;
	}

	public void setBgAlpha(String bgAlpha) {
		this.bgAlpha = bgAlpha;
	}

	public String getShowNames() {
		return showNames;
	}

	public void setShowNames(String showNames) {
		this.showNames = showNames;
	}

	public String getShowalternatehgridcolor() {
		return showalternatehgridcolor;
	}

	public void setShowalternatehgridcolor(String showalternatehgridcolor) {
		this.showalternatehgridcolor = showalternatehgridcolor;
	}

	public String getShowvalues() {
		return showvalues;
	}

	public void setShowvalues(String showvalues) {
		this.showvalues = showvalues;
	}

	public String getLabeldisplay() {
		return labeldisplay;
	}

	public void setLabeldisplay(String labeldisplay) {
		this.labeldisplay = labeldisplay;
	}

	public String getDivlinecolor() {
		return divlinecolor;
	}

	public void setDivlinecolor(String divlinecolor) {
		this.divlinecolor = divlinecolor;
	}

	public String getDivlinealpha() {
		return divlinealpha;
	}

	public void setDivlinealpha(String divlinealpha) {
		this.divlinealpha = divlinealpha;
	}

	public String getUseroundedges() {
		return useroundedges;
	}

	public void setUseroundedges(String useroundedges) {
		this.useroundedges = useroundedges;
	}

	public String getShowcanvasbg() {
		return showcanvasbg;
	}

	public void setShowcanvasbg(String showcanvasbg) {
		this.showcanvasbg = showcanvasbg;
	}

	public String getAnimation() {
		return animation;
	}

	public void setAnimation(String animation) {
		this.animation = animation;
	}

	public String getPalette() {
		return palette;
	}

	public void setPalette(String palette) {
		this.palette = palette;
	}

	public String getxAxisNamePadding() {
		return xAxisNamePadding;
	}

	public void setxAxisNamePadding(String xAxisNamePadding) {
		this.xAxisNamePadding = xAxisNamePadding;
	}

	public String getShowYAxisValues() {
		return showYAxisValues;
	}

	public void setShowYAxisValues(String showYAxisValues) {
		this.showYAxisValues = showYAxisValues;
	}

	public String getShowLimits() {
		return showLimits;
	}

	public void setShowLimits(String showLimits) {
		this.showLimits = showLimits;
	}

	public String getyAxisValuesStep() {
		return yAxisValuesStep;
	}

	public void setyAxisValuesStep(String yAxisValuesStep) {
		this.yAxisValuesStep = yAxisValuesStep;
	}

	public String getBaseFontColor() {
		return baseFontColor;
	}

	public void setBaseFontColor(String baseFontColor) {
		this.baseFontColor = baseFontColor;
	}

	public String getBaseFont() {
		return baseFont;
	}

	public void setBaseFont(String baseFont) {
		this.baseFont = baseFont;
	}

	public String getBaseFontSize() {
		return baseFontSize;
	}

	public void setBaseFontSize(String baseFontSize) {
		this.baseFontSize = baseFontSize;
	}

	public String getOutCnvBaseFont() {
		return outCnvBaseFont;
	}

	public void setOutCnvBaseFont(String outCnvBaseFont) {
		this.outCnvBaseFont = outCnvBaseFont;
	}

	public String getOutCnvBaseFontSize() {
		return outCnvBaseFontSize;
	}

	public void setOutCnvBaseFontSize(String outCnvBaseFontSize) {
		this.outCnvBaseFontSize = outCnvBaseFontSize;
	}

	public String getOutCnvBaseFontColor() {
		return outCnvBaseFontColor;
	}

	public void setOutCnvBaseFontColor(String outCnvBaseFontColor) {
		this.outCnvBaseFontColor = outCnvBaseFontColor;
	}

	public String getToolTipBgColor() {
		return toolTipBgColor;
	}

	public void setToolTipBgColor(String toolTipBgColor) {
		this.toolTipBgColor = toolTipBgColor;
	}

	public String getToolTipBorderColor() {
		return toolTipBorderColor;
	}

	public void setToolTipBorderColor(String toolTipBorderColor) {
		this.toolTipBorderColor = toolTipBorderColor;
	}

	public String getShowToolTipShadow() {
		return showToolTipShadow;
	}

	public void setShowToolTipShadow(String showToolTipShadow) {
		this.showToolTipShadow = showToolTipShadow;
	}

//	public String getyAxisValuesPadding() {
//		return yAxisValuesPadding;
//	}
//
//	public void setyAxisValuesPadding(String yAxisValuesPadding) {
//		this.yAxisValuesPadding = yAxisValuesPadding;
//	}

	public String getValuePadding() {
		return valuePadding;
	}

	public void setValuePadding(String valuePadding) {
		this.valuePadding = valuePadding;
	}

	public String getCanvasBottomMargin() {
		return canvasBottomMargin;
	}

	public void setCanvasBottomMargin(String canvasBottomMargin) {
		this.canvasBottomMargin = canvasBottomMargin;
	}

	public String getRotateLabels() {
		return rotateLabels;
	}

	public void setRotateLabels(String rotateLabels) {
		this.rotateLabels = rotateLabels;
	}

	public String getPlaceValuesInside() {
		return placeValuesInside;
	}

	public void setPlaceValuesInside(String placeValuesInside) {
		this.placeValuesInside = placeValuesInside;
	}

	public String getValueFontColor() {
		return valueFontColor;
	}

	public void setValueFontColor(String valueFontColor) {
		this.valueFontColor = valueFontColor;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getCaptionAlignment() {
		return captionAlignment;
	}

	public void setCaptionAlignment(String captionAlignment) {
		this.captionAlignment = captionAlignment;
	}

	public String getSetAdaptiveYMin() {
		return setAdaptiveYMin;
	}

	public void setSetAdaptiveYMin(String setAdaptiveYMin) {
		this.setAdaptiveYMin = setAdaptiveYMin;
	}

	public String getyAxisMinValue() {
		return yAxisMinValue;
	}

	public void setyAxisMinValue(String yAxisMinValue) {
		this.yAxisMinValue = yAxisMinValue;
	}

	public String getyAxisMaxValue() {
		return yAxisMaxValue;
	}

	public void setyAxisMaxValue(String yAxisMaxValue) {
		this.yAxisMaxValue = yAxisMaxValue;
	}

	public String getDecimalPrecision() {
		return decimalPrecision;
	}

	public void setDecimalPrecision(String decimalPrecision) {
		this.decimalPrecision = decimalPrecision;
	}

	public String getShowBorder() {
		return showBorder;
	}

	public void setShowBorder(String showBorder) {
		this.showBorder = showBorder;
	}

	public String getCanvasBgColor() {
		return canvasBgColor;
	}

	public void setCanvasBgColor(String canvasBgColor) {
		this.canvasBgColor = canvasBgColor;
	}

	public String getNumDivLines() {
		return numDivLines;
	}

	public void setNumDivLines(String numDivLines) {
		this.numDivLines = numDivLines;
	}
	
}