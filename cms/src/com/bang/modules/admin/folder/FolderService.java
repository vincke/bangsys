package com.bang.modules.admin.folder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.log.Log;
import com.bang.component.util.JFlyFoxUtils;
import com.bang.jfinal.base.BaseService;
import com.bang.system.log.SysLog;
import com.bang.util.StrUtils;
import com.bang.util.cache.Cache;
import com.bang.util.cache.CacheManager;

/**
 * 目录管理
 *
 * @author flyfox 2014-2-11
 */
public class FolderService extends BaseService {

	private final static Log log = Log.getLog(SysLog.class);

	private final static String cacheName = "FolderService";
	/**
	 * 目录缓存
	 */
	private static Cache cache = CacheManager.get(cacheName);

	/**
	 * 更新缓存
	 *
	 * 2015年4月29日 下午4:37:40
	 */
	public void updateCache() {
		cache.clear();

		// 初始化urlKey
		initMenuKey();
	}

	private final static String urlkeyCacheName = "JFlyFoxUtils";
	private static Cache urlkeyCache = CacheManager.get(urlkeyCacheName);

    public static void initMenuKey() {
    	log.info("####目录Key初始化......");
		urlkeyCache.clear();
		List<TbFolder> folders = TbFolder.dao.findByWhere(" where status = 1 order by sort");
		for (TbFolder tbFolder : folders) {
			if (StrUtils.isNotEmpty(tbFolder.getKey())) {
				// 分站点存储
				urlkeyCache.add(tbFolder.getKey() + "_" + tbFolder.getSiteId(), tbFolder.getId() + "");
				urlkeyCache.add(tbFolder.getId() + "", tbFolder.getKey());
			}
		}
	}

	/**
	 * 通过ID获取URLKey
	 *
	 * 2016年6月23日 下午5:54:17
	 *
	 * @param key
	 * @return
	 */
	public static String getMenu(String key) {
		return (urlkeyCache.get(key) == null) ? key : urlkeyCache.get(key).toString();
	}

	/**
	 * 通过URLKey获取ID
	 *
	 * 2016年6月23日 下午5:51:26
	 *
	 * @param key
	 * @param siteId
	 * @return
	 */
	public static String getMenu(String key, int siteId) {
		String urlKey = key + "_" + siteId;
		return (urlkeyCache.get(urlKey) == null) ? key : urlkeyCache.get(urlKey).toString();
	}

	/**
	 * 获取栏目信息
	 *
	 * 2015年4月29日 下午4:37:55
	 *
	 * @return
	 */
	public TbFolder getFolder(int folderId) {
		TbFolder folder = TbFolder.dao.findFirstCache(cacheName, "getFolder_" + folderId //
		, "select * from tb_folder where id = ? ", folderId);
		return folder;
	}

	/**
	 * 不通过索引获取所有目录
	 *
	 * 2016年3月19日 下午6:04:32
	 *
	 * @return
	 */
	public List<TbFolder> getFolders(int siteId) {
		return TbFolder.dao.findCache(cacheName, "getFolders_" + siteId, //
				"select * from tb_folder where site_id = ? order by sort,id ", siteId);
	}

	/**
	 * 获取栏目菜单
	 *
	 * 2015年4月29日 下午4:37:55
	 *
	 * @return
	 */
	public Map<String, List<TbFolder>> getFolderMenus(int siteId) {
		Map<String, List<TbFolder>> map = new HashMap<String, List<TbFolder>>();
		List<TbFolder> folders = getFolders(siteId);
		for (TbFolder folder : folders) {
			if (folder.getStatus() == JFlyFoxUtils.STATUS_HIDE)
				continue;

			String parentId = String.valueOf(folder.getParentId());
			List<TbFolder> list = map.get(parentId);
			if (list == null) {
				list = new ArrayList<TbFolder>();
				map.put(parentId, list);
			}
			list.add(folder);
		}
		return map;
	}

	/**
	 * 目录复选框
	 *
	 * 2015年1月28日 下午5:28:40
	 *
	 * @return
	 */
	public String selectFolder(Integer selected, int siteId) {
		return selectFolder(selected, null, siteId);
	}

	/**
	 * 目录复选框
	 *
	 * 2015年1月28日 下午5:28:40
	 *
	 * @return
	 */
	public String selectFolder(Integer selected, Integer selfId, int siteId) {
		String where = " where 1 = 1 ";
		if (selfId != null && selfId > 0) {
			where += "and id !=" + selfId;
		}
		List<TbFolder> list = TbFolder.dao.find(" select id,name from tb_folder " //
				+ where + " and site_id = ? order by sort,create_time desc ", siteId);
		StringBuffer sb = new StringBuffer("");
		if (list != null && list.size() > 0) {
			for (TbFolder folder : list) {
				sb.append("<option value=\"");
				sb.append(folder.getInt("id"));
				sb.append("\" ");
				sb.append((selected != null && folder.getInt("id").intValue() == selected) ? "selected" : "");
				sb.append(">");
				sb.append(folder.getStr("name"));
				sb.append("</option>");
			}
		}
		return sb.toString();
	}
}