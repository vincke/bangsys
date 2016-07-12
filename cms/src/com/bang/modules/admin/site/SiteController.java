package com.bang.modules.admin.site;

import com.jfinal.plugin.activerecord.Page;
import com.bang.component.base.BaseProjectController;
import com.bang.jfinal.component.annotation.ControllerBind;
import com.bang.jfinal.component.db.SQLUtils;
import com.bang.util.StrUtils;

/**
 * 站点
 *
 * @author flyfox 2014-4-24
 */
@ControllerBind(controllerKey = "/admin/site")
public class SiteController extends BaseProjectController {

	private static final String path = "/pages/admin/site/site_";

	public void index() {
		list();
	}

	public void list() {
		TbSite model = getModelByAttr(TbSite.class);

		SQLUtils sql = new SQLUtils(" from tb_site t where 1=1 ");
		if (model.getAttrValues().length != 0) {
			sql.setAlias("t");
			// 查询条件
			sql.whereLike("name", model.getStr("name"));
		}
		// 排序
		String orderBy = getBaseForm().getOrderBy();
		if (StrUtils.isEmpty(orderBy)) {
			sql.append(" order by t.sort,t.id ");
		} else {
			sql.append(" order by ").append(orderBy);
		}

		Page<TbSite> page = TbSite.dao.paginate(getPaginator(), "select t.* ", //
				sql.toString().toString());

		// 下拉框
		setAttr("page", page);
		setAttr("attr", model);
		render(path + "list.html");
	}

	public void add() {
		render(path + "add.html");
	}

	public void view() {
		TbSite model = TbSite.dao.findById(getParaToInt());
		setAttr("model", model);
		render(path + "view.html");
	}

	public void delete() {
		Integer pid = getParaToInt();
		TbSite model = new TbSite();
		Integer userid = getSessionUser().getUserID();
		String now = getNow();
		model.put("update_id", userid);
		model.put("update_time", now);
		model.deleteById(pid);

		new SiteService().clearCache();

		list();
	}

	public void edit() {
		TbSite model = TbSite.dao.findById(getParaToInt());
		setAttr("model", model);
		render(path + "edit.html");
	}

	public void save() {
		Integer pid = getParaToInt();
		TbSite model = getModel(TbSite.class);

		Integer userid = getSessionUser().getUserID();
		String now = getNow();
		model.put("update_id", userid);
		model.put("update_time", now);
		if (pid != null && pid > 0) { // 更新
			model.update();
		} else { // 新增
			model.remove("id");
			model.put("create_id", userid);
			model.put("create_time", now);
			model.save();
		}

		new SiteService().clearCache();

		renderMessage("保存成功");
	}

}
