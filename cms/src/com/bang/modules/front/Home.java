package com.bang.modules.front;

import com.jfinal.aop.Before;
import com.bang.component.base.BaseProjectController;
import com.bang.component.util.ImageCode;
import com.bang.component.util.JFlyFoxUtils;
import com.bang.jfinal.component.annotation.ControllerBind;
import com.bang.modules.CommonController;
import com.bang.modules.front.interceptor.FrontInterceptor;
import com.bang.system.log.SysLog;
import com.bang.system.user.SysUser;
import com.bang.util.StrUtils;

import javax.servlet.ServletException;
import java.io.IOException;

import static com.bang.modules.CommonController.firstPage;

/**
 *
 * 2015年5月11日 下午4:11:02
 */
@ControllerBind(controllerKey = "/front")
public class Home extends BaseProjectController {

	public static final String PATH = "/home/";

	/**
	 * 登录
	 */
	@Before(FrontInterceptor.class)
	public void login() {
		setAttr("pre_page", getPrePage());
		// 获取验证码
		String imageCode = getSessionAttr(ImageCode.class.getName());
		String checkCode = this.getPara("imageCode");
		if ( null == checkCode){
			renderAuto(CommonController.loginPage);
			return;
		}

		if (StrUtils.isEmpty(imageCode) || !imageCode.equalsIgnoreCase(checkCode)) {
			setAttr("msg", "验证码错误！");
			renderAuto(CommonController.loginPage);
			return;
		}

		// 初始化数据字典Map
		String username = getPara("username");
		String password = getPara("password");

		// 新加入，判断是否有上一个页面
		String prePage = getPara("pre_page");
		String toPage = StrUtils.isEmpty(prePage) || prePage.indexOf("login") >= 0 //
				|| prePage.indexOf("trans") >= 0 ? firstPage : prePage;
		setAttr("pre_page", prePage); // 如果密码错误还需要用到

		if (StrUtils.isEmpty(username)) {
			setAttr("msg", "用户名不能为空");
			renderAuto(CommonController.loginPage);
			return;
		} else if (StrUtils.isEmpty(password)) {
			setAttr("msg", "密码不能为空");
			renderAuto(CommonController.loginPage);
			return;
		}
		String encryptPassword = JFlyFoxUtils.passwordEncrypt(password); // 加密
		SysUser user = SysUser.dao.findFirstByWhere(" where username = ? and password = ? " //
															+ " and usertype != " + JFlyFoxUtils.USER_TYPE_THIRD // 第三方的只能通过oauth登录
				, username, encryptPassword);

		if (user == null || user.getInt("userid") <= 0) {
			setAttr("msg", "用户名或密码不正确，认证失败，请您重新输入。");
			renderAuto(CommonController.loginPage);
			return;
		} else {
			setSessionUser(user);
		}

		// 添加日志
		user.put("update_id", user.getUserid());
		user.put("update_time", getNow());
		saveLog(user, SysLog.SYSTEM_LOGIN);

		redirect(toPage);
	}

	/**
	 * 登出
	 */
	public void logout() {
		removeSessionUser();
		redirect(firstPage);
	}

	/**
	 * 生成注册码
	 *
	 * 2015年2月28日 下午1:50:25
	 */
	public void image_code() {
		try {
			new ImageCode().doGet(getRequest(), getResponse());
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		renderNull();
	}

}
