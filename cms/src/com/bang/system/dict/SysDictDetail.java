package com.bang.system.dict;

import com.bang.component.base.BaseProjectModel;
import com.bang.jfinal.component.annotation.ModelBind;

@ModelBind(table = "sys_dict_detail", key = "detail_id")
public class SysDictDetail extends BaseProjectModel<SysDictDetail> {

	private static final long serialVersionUID = 1L;
	public static final SysDictDetail dao = new SysDictDetail();

}
