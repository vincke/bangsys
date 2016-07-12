package com.bang.system.department;

import com.jfinal.plugin.activerecord.Page;
import com.bang.component.base.BaseProjectController;
import com.bang.jfinal.component.annotation.ControllerBind;
import com.bang.jfinal.component.db.SQLUtils;
import com.bang.util.StrUtils;

/**
 * 部门
 *
 * @author flyfox 2014-4-24
 */
@ControllerBind(controllerKey = "/system/department")
public class DepartmentController extends BaseProjectController {

	private static final String path = "/pages/system/department/department_";

	public void list() {
		SysDepartment model = getModelByAttr(SysDepartment.class);

		SQLUtils sql = new SQLUtils(" from sys_department t where 1=1 ");
		if (model.getAttrValues().length != 0) {
			sql.setAlias("t");
			// 查询条件
			sql.whereLike("name", model.getStr("name"));
		}

		// 排序
		String orderBy = getBaseForm().getOrderBy();
		if (StrUtils.isEmpty(orderBy)) {
			sql.append(" order by t.id desc ");
		} else {
			sql.append(" order by ").append(orderBy);
		}

		Page<SysDepartment> page = SysDepartment.dao.paginate(getPaginator(), "select t.* ", //
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
		SysDepartment model = SysDepartment.dao.findById(getParaToInt());
		setAttr("model", model);
		render(path + "view.html");
	}

	public void delete() {

		// 日志添加
		SysDepartment model = new SysDepartment();
		Integer userid = getSessionUser().getUserID();
		String now = getNow();
		model.put("update_id", userid);
		model.put("update_time", now);

		model.deleteById(getParaToInt());
		list();
	}

	public void edit() {
		SysDepartment model = SysDepartment.dao.findById(getParaToInt());
		setAttr("model", model);
		render(path + "edit.html");
	}

	public void save() {
		Integer pid = getParaToInt();
		SysDepartment model = getModel(SysDepartment.class);

		// 日志添加
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
		renderMessage("保存成功");
	}
}
