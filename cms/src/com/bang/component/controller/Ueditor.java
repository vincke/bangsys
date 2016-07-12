package com.bang.component.controller;

import com.baidu.ueditor.ActionEnter;
import com.jfinal.kit.PathKit;
import com.bang.component.base.BaseProjectController;
import com.bang.jfinal.component.annotation.ControllerBind;

@ControllerBind(controllerKey = "ueditor")
public class Ueditor extends BaseProjectController {

	public void index() {
		String out = new ActionEnter(getRequest(), PathKit.getWebRootPath()).exec();
		renderText(out);
	}
}
