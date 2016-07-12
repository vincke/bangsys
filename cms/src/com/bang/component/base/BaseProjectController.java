/**
 * Copyright 2015-2025 FLY的狐狸(email:bang@sina.com qq:369191470).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.bang.component.base;

import com.bang.component.util.JFlyFoxUtils;
import com.bang.jfinal.base.BaseController;
import com.bang.jfinal.base.SessionUser;
import com.bang.jfinal.component.util.Attr;
import com.bang.modules.admin.article.ArticleConstant;
import com.bang.modules.admin.folder.FolderService;
import com.bang.modules.admin.site.SessionSite;
import com.bang.modules.admin.site.SiteConstant;
import com.bang.modules.admin.site.SiteService;
import com.bang.modules.admin.site.TbSite;
import com.bang.system.log.SysLog;
import com.bang.system.menu.SysMenu;
import com.bang.system.user.SysUser;
import com.bang.system.user.UserSvc;
import com.bang.util.NumberUtils;
import com.bang.util.StrUtils;
import com.bang.util.encrypt.DESUtils;
import com.jfinal.plugin.activerecord.Db;

import java.util.List;
import java.util.Map;

/**
 * 项目BaseControler
 *
 * @author flyfox
 * @date 2015-08-02
 *
 */
public abstract class BaseProjectController extends BaseController {

	private static final DESUtils COOKIE_DES = new DESUtils("ffcookie");

	public void renderAuto(String view) {
		String path = getAutoPath(view);

		super.render(path);
	}

	public void redirectAuto(String view) {
		String path = getAutoPath(view);

		super.redirect(path);
	}

	protected String getAutoPath(String view) {
		String path = view;

		if (!view.startsWith("/")) {
			path = "/" + path;
		}

		// path = (isMoblie() ? Attr.PATH_MOBILE : Attr.PATH_PC) + path;
		TbSite site = new SiteService().getSite(getSessionSite().getSiteId());
		path = SiteConstant.TEMPLATE_PATH + (isMoblie() ? site.getTemplateMobile() : site.getTemplate()) + path;

		if (view.startsWith("/")) {
			path = "/" + path;
		}

		path = path.replace("//", "/");
		return path;
	}

	/**
	 * 方法重写
	 *
	 * 2015年8月2日 下午3:17:29
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public SessionUser getSessionUser() {
		SysUser sysUser = getSessionAttr(Attr.SESSION_NAME);
		try {
			// 如果session没有，cookie有~那么就设置到Session
			if (sysUser == null) {
				String cookieContent = getCookie(Attr.SESSION_NAME);
				if (cookieContent != null) {
					String key = COOKIE_DES.decryptString(cookieContent);
					if (StrUtils.isNotEmpty(key) && key.split(",").length == 2) {
						int userid = NumberUtils.parseInt(key.split(",")[0]);
						String password = key.split(",")[1];
						sysUser = SysUser.dao.findFirstByWhere(" where userid = ? and password = ? ", userid, password);
						if (sysUser != null)
							setSessionUser(sysUser);
					}
				}
			}
		} catch (Exception e) {
			// 异常cookie重新登陆
			removeSessionAttr(Attr.SESSION_NAME);
			removeCookie(Attr.SESSION_NAME);

			log.error("cooke user异常:", e);
			return null;
		}
		return sysUser;
	}

	/**
	 * 方法重写
	 *
	 * 2015年8月2日 下午3:17:29
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public SessionUser setSessionUser(SessionUser user) {
		setSessionAttr(Attr.SESSION_NAME, user);
		// 设置cookie，用id+password作为
		SysUser sysUser = (SysUser) user;
		String key = sysUser.getUserid() + "," + user.getStr("password");
		String cookieContent = COOKIE_DES.encryptString(key);
		setCookie(Attr.SESSION_NAME, cookieContent, 7 * 24 * 60 * 60);
		// 如果是管理员 设置菜单权限
		if (user.getInt("usertype") == 1 || user.getInt("usertype") == 2) {
			Map<Integer, List<SysMenu>> map = new UserSvc().getAuthMap(sysUser);
			// 注入菜单
			setSessionAttr("menu", map);
		}
		return user;
	}

	/**
	 * 方法重写
	 *
	 * 2015年8月2日 下午3:17:29
	 *
	 * @return
	 */
	public void removeSessionUser() {
		removeSessionAttr(Attr.SESSION_NAME);
		// 删除cookie
		removeCookie(Attr.SESSION_NAME);
	}

	/**
	 * 用户登录，登出记录
	 *
	 * 2015年10月16日 下午2:36:39
	 *
	 * @param user
	 * @param operType
	 */
	protected void saveLog(SysUser user, String operType) {
		try {
			String tableName = user.getTable().getName();
			Integer updateId = user.getInt("update_id");
			String updateTime = user.getStr("update_time");
			String sql = "INSERT INTO `sys_log` ( `log_type`, `oper_object`, `oper_table`," //
					+ " `oper_id`, `oper_type`, `oper_remark`, `create_time`, `create_id`) " //
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			Db.update(sql, SysLog.TYPE_SYSTEM, SysLog.getTableRemark(tableName), tableName, //
					updateId, operType, "", updateTime, updateId);
		} catch (Exception e) {
			log.error("添加日志失败", e);
		}
	}

	// ///////////////////栏目处理////////////
	// 获取用户设置的SITE对象，设置默认
	public SessionSite getSessionSite() {
		SessionSite sessionSite = getSessionAttr(SiteConstant.SESSION_SITE);
		// 获取用户设置的SITE对象，设置默认
		if (sessionSite == null) {
			sessionSite = new SessionSite();
			sessionSite.setBackSiteId(SiteConstant.DEFAULT_SITE_ID);
			sessionSite.setSiteId(SiteConstant.DEFAULT_SITE_ID);
			setSessionAttr(SiteConstant.SESSION_SITE, sessionSite);
		}
		return sessionSite;
	}

	public SessionSite setSessionSite(SessionSite sessionSite) {
		setSessionAttr(SiteConstant.SESSION_SITE, sessionSite);
		return sessionSite;
	}

	public String selectFolder(Integer selected) {
		return new FolderService().selectFolder(selected, getSessionSite().getBackSiteId());
	}

	public String selectFolder(Integer selected, Integer selfId) {
		return new FolderService().selectFolder(selected, selfId, getSessionSite().getBackSiteId());
	}

	/**
	 * 公共文章查询sql
	 *
	 * 2016年3月19日 下午7:03:11
	 *
	 * @return
	 */
	public String getPublicWhere() {
		return " t.status =  " + JFlyFoxUtils.STATUS_SHOW //
				+ " and t.approve_status = " + ArticleConstant.APPROVE_STATUS_PASS // 审核通过
				+ " and t.type in (11,12) " // 查询状态为显示，类型是预览和正常的文章
		;
	}

}
