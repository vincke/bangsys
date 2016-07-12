package com.bang.component.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.bang.component.util.JFlyFoxUtils;
import com.bang.jfinal.base.BaseController;
import com.bang.jfinal.component.util.Attr;
import com.bang.modules.admin.site.SessionSite;
import com.bang.modules.admin.site.SiteConstant;
import com.bang.modules.admin.site.SiteService;
import com.bang.modules.admin.site.TbSite;
import com.bang.system.user.SysUser;

/**
 * 站点认证拦截器
 *
 * @author flyfox 2014-2-11
 */
public class SiteInterceptor implements Interceptor {

	private static final Logger log = Logger.getLogger(SiteInterceptor.class);

	public void intercept(Invocation ai) {
		Controller controller = ai.getController();

		// 单站点设置
		if (!SiteConstant.isMultiSite()) {
			SessionSite sessionSite = controller.getSessionAttr(SiteConstant.SESSION_SITE);
			if (sessionSite == null) {
				sessionSite = new SessionSite();
				sessionSite.setBackSiteId(SiteConstant.DEFAULT_SITE_ID);
				sessionSite.setSiteId(SiteConstant.DEFAULT_SITE_ID);
				TbSite tmpSite = new SiteService().getSite(SiteConstant.DEFAULT_SITE_ID);
				sessionSite.setBackModel(tmpSite);
				sessionSite.setModel(tmpSite);
				controller.setSessionAttr(SiteConstant.SESSION_SITE, sessionSite);
			}
			ai.invoke();
			return;
		}

		// 每次访问获取session，没有可以从cookie取~
		SysUser user = null;
		if (controller instanceof BaseController) {
			user = (SysUser) ((BaseController) controller).getSessionUser();
		} else {
			user = controller.getSessionAttr(Attr.SESSION_NAME);
		}

		// 返回list
		List<TbSite> sites = new SiteService().getSiteList(user);
		controller.setAttr(SiteConstant.SESSION_SITES, sites);

		// 请求路径
		String tmpPath = ai.getActionKey();
		if (tmpPath.startsWith("/")) {
			tmpPath = tmpPath.substring(1, tmpPath.length());
		}
		if (tmpPath.endsWith("/")) {
			tmpPath = tmpPath.substring(0, tmpPath.length() - 1);
		}
		boolean isBack = JFlyFoxUtils.isBack(tmpPath);

		SessionSite sessionSite = controller.getSessionAttr(SiteConstant.SESSION_SITE);
		if (sessionSite == null) {
			sessionSite = new SessionSite();
		}

		if (isBack) { // 后台
			// 权限应该是用户拦截器处理
			if (user == null) {
				log.error("session user is null!");
				ai.invoke();
				return;
			}

			// 获取用户设置的SITE对象，设置默认
			if (sessionSite.getBackSiteId() <= 0) {
				int backSiteId = user.getInt("back_site_id");
				backSiteId = backSiteId > 0 ? backSiteId : SiteConstant.DEFAULT_SITE_ID;

				sessionSite.setBackSiteId(backSiteId);

				TbSite tmpSite = getSite(sites, backSiteId);
				if (tmpSite != null) {
					sessionSite.setBackSiteId(tmpSite.getId());
					sessionSite.setBackModel(tmpSite);
				}

				controller.setSessionAttr(SiteConstant.SESSION_SITE, sessionSite);
			}
		} else {
			HttpServletRequest request = controller.getRequest();
			// 通过site匹配站点
			String siteServer = request.getServerName();
			int siteId = 0;
			// if (StrUtils.isEmpty(sessionSite.getLastSite()) ||
			// !site.equals(sessionSite.getLastSite())) {
			sessionSite.setLastSite(siteServer);
			sessionSite.setSiteId(siteId);
			for (TbSite tmpSite : sites) {
				if (siteServer.equals(tmpSite.getDomainPc()) //
						|| siteServer.equals(tmpSite.getDomainMobile())) {
					siteId = tmpSite.getId();
					sessionSite.setSiteId(siteId);
					sessionSite.setModel(tmpSite);
					break;
				}
			}

			// 没有就用默认的
			if (siteId == 0) {
				siteId = SiteConstant.DEFAULT_SITE_ID;
				// 设置站点对象
				TbSite tmpSite = getSite(sites, siteId);
				if (tmpSite != null) {
					sessionSite.setSiteId(tmpSite.getId());
					sessionSite.setModel(tmpSite);
				}
			}

			controller.setSessionAttr(SiteConstant.SESSION_SITE, sessionSite);
			// }
		}

		ai.invoke();
	}

	protected TbSite getSite(List<TbSite> sites, int siteId) {
		// 设置站点对象
		for (TbSite tmpSite : sites) {
			if (siteId == tmpSite.getId()) {
				return tmpSite;
			}
		}
		return null;
	}

}
