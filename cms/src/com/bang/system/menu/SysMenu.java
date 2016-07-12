package com.bang.system.menu;

import com.bang.component.base.BaseProjectModel;
import com.bang.jfinal.component.annotation.ModelBind;

@ModelBind(table = "sys_menu")
public class SysMenu extends BaseProjectModel<SysMenu> {

	private static final long serialVersionUID = 1L;
	public static final SysMenu dao = new SysMenu();

}