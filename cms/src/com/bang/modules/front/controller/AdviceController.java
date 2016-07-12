package com.bang.modules.front.controller;

import com.alibaba.fastjson.JSONObject;
import com.bang.component.base.BaseProjectController;
import com.bang.component.util.ImageCode;
import com.bang.jfinal.component.annotation.ControllerBind;
import com.bang.modules.admin.advicefeedback.TbAdviceFeedback;
import com.bang.system.user.SysUser;
import com.bang.system.user.UserCache;
import com.bang.util.StrUtils;

/**
 * 意见反馈
 *
 * 2016年1月28日 下午6:12:22
 */
@ControllerBind(controllerKey = "/front/advice")
public class AdviceController extends BaseProjectController {

	/**
	 * 个人信息保存
	 */
	public void save() {
		JSONObject json = new JSONObject();
		json.put("status", 2);// 失败

		// 获取验证码
		String imageCode = getSessionAttr(ImageCode.class.getName());
		String checkCode = this.getPara("imageCode");

		if (StrUtils.isEmpty(imageCode) || !imageCode.equalsIgnoreCase(checkCode)) {
			json.put("msg", "验证码错误！");
			renderJson(json.toJSONString());
			return;
		}

		SysUser user = (SysUser) getSessionUser();
		if (user==null) {
			json.put("msg", "请先登录再填写意见反馈！");
			renderJson(json.toJSONString());
			return;
		}

		TbAdviceFeedback model = getModel(TbAdviceFeedback.class);

		int userid = user.getInt("userid");
		String now = getNow();
		model.setUsername(user.getUserName());
		model.setUserid(userid);
		model.set("create_id", userid);
		model.set("create_time", now);
		model.save();
		UserCache.init(); // 设置缓存
		SysUser newUser = SysUser.dao.findById(userid);
		setSessionUser(newUser); // 设置session
		json.put("status", 1);// 成功

		renderJson(json.toJSONString());
	}

}
