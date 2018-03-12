/*
 Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
 For licensing, see LICENSE.md or http://ckeditor.com/license
*/
 (function (){
	 
	 var a = {
			 exec:function(editor){
				alert(1);
				console(parent.document);
			 }
			 
	 },
	 
	 b='templateButton';
	 
	 CKEDITOR.plugins.add(b,{
		 init:function(editor){
			 editor.addCommand(b,a);
			 editor.ui.addButton('templateButton',{
				 label:'填充模板信息',
				 icon:this.path+'anchor.png',
				 command:b
			 });
		 }
	 });
	 
 })();