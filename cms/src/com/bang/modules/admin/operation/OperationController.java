package com.bang.modules.admin.operation;

import com.alibaba.fastjson.JSONObject;
import com.bang.component.base.BaseProjectController;
import com.bang.jfinal.component.annotation.ControllerBind;
import com.bang.modules.admin.comment.CommentService;
import com.bang.modules.admin.folder.FolderService;
import com.bang.modules.front.service.FrontCacheService;
import com.bang.modules.front.service.FrontImageService;
import com.bang.modules.front.service.FrontVideoService;
import com.bang.system.user.SysUser;

/**
 * 友情链接管理
 *
 * @author flyfox 2014-4-24
 */
@ControllerBind(controllerKey = "/admin/operation")
public class OperationController extends BaseProjectController {

	private static final String path = "/pages/admin/operation/";

	/**
	 * 跳转到操作页面
	 *
	 * 2015年3月16日 下午5:33:55
	 */
	public void index() {
		render(path + "operation.html");
	}

	/**
	 * 更新缓存
	 *
	 * 2015年3月16日 下午5:33:55
	 */
	public void updateCache() {
		JSONObject json = new JSONObject();
		json.put("status", 2);// 失败

		SysUser user = (SysUser) getSessionUser();
		if (user == null || user.getInt("usertype") != 1) {
			json.put("msg", "您不是管理员，无法操作！");
			renderJson(json.toJSONString());
			return;
		}

		// 更新目录缓存
		new FolderService().updateCache();
		// 清除回复数缓存
		new CommentService().clearCache();
		// 清除所有前台缓存
		new FrontCacheService().clearCache();
		// 清除前台图片缓存
		new FrontImageService().clearCache();
		// 清除前台视频缓存
		new FrontVideoService().clearCache();

		json.put("status", 1);// 成功
		renderJson(json.toJSONString());
	}
}
