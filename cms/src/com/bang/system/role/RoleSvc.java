package com.bang.system.role;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.bang.jfinal.base.BaseService;
import com.bang.system.rolemenu.SysRoleMenu;
import com.bang.util.DateUtils;
import com.bang.util.NumberUtils;
import com.bang.util.StrUtils;

public class RoleSvc extends BaseService {

	/**
	 * 获取角色授权的菜单
	 *
	 * 2015年4月28日 下午5:01:54
	 *
	 * @param roleid
	 * @return
	 */
	public String getMemus(int roleid) {
		String sql = " select group_concat(menuid) as menus from sys_role_menu where roleid = ?";
		Record record = Db.findFirst(sql, roleid);
		String menus = record.getStr("menus");
		return menus;
	}

	/**
	 * 保存授权信息
	 *
	 * 2015年4月28日 下午5:00:30
	 *
	 * @param roleid
	 * @param menus
	 */
	public void saveAuth(int roleid, String menus, int update_id) {
		// 删除原有数据库
		Db.update("delete from sys_role_menu where roleid = ? ", roleid);

		if (StrUtils.isNotEmpty(menus)) {
			String[] arr = menus.split(",");
			for (String menuid : arr) {
				SysRoleMenu roleMenu = new SysRoleMenu();
				roleMenu.set("roleid", roleid);
				roleMenu.set("menuid", NumberUtils.parseInt(menuid));

				// 日志添加
				roleMenu.put("update_id", update_id);
				roleMenu.put("update_time", DateUtils.getNow(DateUtils.DEFAULT_REGEX_YYYY_MM_DD_HH_MIN_SS));
				roleMenu.save();
			}
		}
	}

}
