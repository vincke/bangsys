package com.bang.system.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.bang.jfinal.base.BaseService;
import com.bang.system.menu.SysMenu;
import com.bang.system.userrole.SysUserRole;
import com.bang.util.DateUtils;
import com.bang.util.NumberUtils;
import com.bang.util.StrUtils;

public class UserSvc extends BaseService {

	/**
	 * 返回菜单权限
	 *
	 * 2015年10月12日 下午3:22:00
	 *
	 * @param user
	 * @return
	 */
	public Map<Integer, List<SysMenu>> getAuthMap(SysUser user) {
		String menuids = "select menuid from sys_role_menu where roleid in"
				+ " ( select roleid from sys_user_role where userid = ? ) group by menuid";
		// 管理员
		if (user.getInt("usertype") == 1) {
			menuids = " select id from sys_menu where -1 != ? "; // 所有菜单
		}

		Integer userid = user.getUserid();
		Map<Integer, List<SysMenu>> map = new HashMap<Integer, List<SysMenu>>();

		String sql = " where status = 1 and parentid = ? " //
				+ "and id in (" + menuids + ") order by sort ";
		// 获取根目录
		List<SysMenu> rootList = SysMenu.dao.findByWhere(sql, 0, userid);
		if (rootList == null || rootList.size() == 0) {
			return null;
		}

		map.put(0, rootList);
		// 获取子目录
		for (SysMenu sysMenu : rootList) {
			List<SysMenu> list = SysMenu.dao.findByWhere(sql, sysMenu.getInt("id"), userid);
			map.put(sysMenu.getInt("id"), list);
		}

		return map;
	}

	/**
	 * 获取用户绑定的角色
	 *
	 * 2015年4月28日 下午5:04:55
	 *
	 * @param userid
	 * @return
	 */
	public String getRoleids(int userid) {
		String sql = " select group_concat(roleid) as roleids from sys_user_role where userid = ?";
		Record record = Db.findFirst(sql, userid);
		String roleids = record.getStr("roleids");
		return roleids;
	}

	/**
	 * 保存用户授权信息
	 *
	 * 2015年4月28日 下午5:05:05
	 *
	 * @param userid
	 * @param roleids
	 */
	public void saveAuth(int userid, String roleids, int update_id) {
		// 删除原有数据库
		Db.update("delete from sys_user_role where userid = ? ", userid);

		if (StrUtils.isNotEmpty(roleids)) {
			String[] arr = roleids.split(",");
			for (String roleid : arr) {
				SysUserRole userRole = new SysUserRole();
				userRole.set("userid", userid);
				userRole.set("roleid", NumberUtils.parseInt(roleid));

				// 日志添加
				userRole.put("update_id", update_id);
				userRole.put("update_time", DateUtils.getNow(DateUtils.DEFAULT_REGEX_YYYY_MM_DD_HH_MIN_SS));
				userRole.save();
			}
		}
	}

}
