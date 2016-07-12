package com.bang.modules.admin.site;

import java.util.List;

import com.bang.component.util.JFlyFoxUtils;
import com.bang.jfinal.base.BaseService;
import com.bang.system.user.SysUser;
import com.bang.util.cache.Cache;
import com.bang.util.cache.CacheManager;

public class SiteService extends BaseService {

	private final static String cacheName = "SiteService";
	/**
	 * 缓存
	 */
	private static Cache cache = CacheManager.get(cacheName);

	public void clearCache() {
		cache.clear();
	}

	public TbSite getDefaultSite() {
		return TbSite.dao.findFirstCache(cacheName, "getDefaultSite" //
				, "select * from tb_site where id = ?", SiteConstant.DEFAULT_SITE_ID);
	}

	public List<TbSite> getSiteList(SysUser user) {
		return TbSite.dao.findCache(cacheName, "getSiteList", "select * from tb_site where status =  "
				+ JFlyFoxUtils.STATUS_SHOW);
	}

	public TbSite getSite(int siteId) {
		return TbSite.dao.findFirstCache(cacheName, "getSite_" + siteId //
		, "select * from tb_site where id = ? ", siteId);
	}

}
