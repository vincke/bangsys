package com.bang.component.config;

import org.beetl.core.GroupTemplate;
import org.beetl.ext.jfinal.BeetlRenderFactory;

import com.beetl.functions.BeetlStrUtils;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.bang.component.beelt.BeeltFunctions;
import com.bang.component.interceptor.CommonInterceptor;
import com.bang.component.interceptor.PageViewInterceptor;
import com.bang.component.interceptor.SiteInterceptor;
import com.bang.component.interceptor.UpdateCacheInterceptor;
import com.bang.component.interceptor.UserKeyInterceptor;
import com.bang.component.util.JFlyFoxCache;
import com.bang.jfinal.component.handler.HtmlHandler;
import com.bang.jfinal.config.JflyfoxConfig;
import com.bang.modules.front.template.TemplateImageService;
import com.bang.modules.front.template.TemplateService;
import com.bang.modules.front.template.TemplateVideoService;
import com.bang.system.user.UserInterceptor;

/**
 * API引导式配置
 */
public class BaseConfig extends JflyfoxConfig {

	public void configConstant(com.jfinal.config.Constants me) {
		super.configConstant(me);
		me.setMainRenderFactory(new BeetlRenderFactory());
		// 获取GroupTemplate ,可以设置共享变量等操作
		GroupTemplate groupTemplate = BeetlRenderFactory.groupTemplate;
		groupTemplate.registerFunctionPackage("strutil", BeetlStrUtils.class);
		groupTemplate.registerFunctionPackage("flyfox", BeeltFunctions.class);
		groupTemplate.registerFunctionPackage("temp", TemplateService.class);
		groupTemplate.registerFunctionPackage("tempImage", TemplateImageService.class);
		groupTemplate.registerFunctionPackage("tempVideo", TemplateVideoService.class);

	};

	@Override
	public void configHandler(Handlers me) {
		// Beelt
		// me.add(new BeeltHandler());

		me.add(new HtmlHandler());
		super.configHandler(me);
	}

	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		// session model转换
		super.configInterceptor(me);
		// 用户Key设置
		me.add(new UserKeyInterceptor());
		// page view 统计
		me.add(new PageViewInterceptor());
		// 缓存更新
		me.add(new UpdateCacheInterceptor());
		// 用户认证
		me.add(new UserInterceptor());
		// 站点拦截
		me.add(new SiteInterceptor());
		// 公共属性
		me.add(new CommonInterceptor());
	}

	/**
	 * 初始化
	 */
	@Override
	public void afterJFinalStart() {
		JFlyFoxCache.init();
		System.out.println("##################################");
		System.out.println("############系统启动完成##########");
		System.out.println("##################################");
	}

}
