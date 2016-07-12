package com.bang.modules.front.interceptor;

import java.util.List;
import java.util.Map;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.bang.component.base.BaseProjectController;
import com.bang.jfinal.base.Paginator;
import com.bang.modules.admin.article.TbArticle;
import com.bang.modules.admin.folder.FolderService;
import com.bang.modules.admin.folder.TbFolder;
import com.bang.modules.admin.friendlylink.FriendlylinkCache;
import com.bang.modules.admin.friendlylink.TbFriendlylink;
import com.bang.modules.admin.site.SessionSite;
import com.bang.modules.admin.site.SiteConstant;
import com.bang.modules.front.service.FrontCacheService;

/**
 * 用户认证拦截器
 *
 * @author flyfox 2014-2-11
 */
public class FrontInterceptor implements Interceptor {

	public void intercept(Invocation ai) {

		Controller controller = ai.getController();
		// 栏目，缓存
		SessionSite site = null;
		if (controller instanceof BaseProjectController) {
			site = ((BaseProjectController) controller).getSessionSite();
		} else {
			site = controller.getSessionAttr(SiteConstant.SESSION_SITE);
		}
		Map<String, List<TbFolder>> folders = new FolderService().getFolderMenus(site.getSiteId());
		controller.setAttr("folders", folders);

		// 推荐文章列表，缓存
		Page<TbArticle> recommendArticles = new FrontCacheService().getRecommendArticle(new Paginator(1, 8),
				site.getSiteId());
		controller.setAttr("recommendArticles", recommendArticles);

		// 友情链接，缓存
		List<TbFriendlylink> friendlylinkList = FriendlylinkCache.getFriendlylinkList(site.getSiteId());
		controller.setAttr("friendlylinkList", friendlylinkList);

		// 底部关于，缓存
		List<TbFriendlylink> aboutList = FriendlylinkCache.getAboutList(site.getSiteId());
		controller.setAttr("aboutList", aboutList);

		ai.invoke();
	}
}
