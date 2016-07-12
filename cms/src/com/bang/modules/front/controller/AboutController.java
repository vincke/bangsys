package com.bang.modules.front.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.bang.component.base.BaseProjectController;
import com.bang.component.util.JFlyFoxUtils;
import com.bang.jfinal.base.Paginator;
import com.bang.jfinal.component.annotation.ControllerBind;
import com.bang.modules.admin.article.TbArticle;
import com.bang.modules.front.interceptor.FrontInterceptor;
import com.bang.modules.front.service.FrontCacheService;

/**
 * 关于我们
 *
 * 2015年5月26日 上午10:42:54
 */
@ControllerBind(controllerKey = "/front/about")
public class AboutController extends BaseProjectController {

	public static final String path = "/about/";

	/**
	 * 关于我们
	 */
	@Before(FrontInterceptor.class)
	public void index() {
		Integer articleId = getParaToInt();

		setAttr("folders_selected", JFlyFoxUtils.MENU_ABOUT);

		Page<TbArticle> pages = new FrontCacheService().getArticle(new Paginator(1, 100), JFlyFoxUtils.MENU_ABOUT);
		setAttr("pages", pages);

		TbArticle article = null;
		if (articleId == null || articleId <= 0) {
			article = pages.getList().get(0);
		} else {
			article = new FrontCacheService().getArticle(articleId);
		}
		setAttr("article", article);

		// seo：title优化
		setAttr(JFlyFoxUtils.TITLE_ATTR, article.getTitle() + " - " + "关于我们 - " + getAttr(JFlyFoxUtils.TITLE_ATTR));

		renderAuto(path + "show_about.html");
	}
}
