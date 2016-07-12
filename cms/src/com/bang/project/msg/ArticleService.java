package com.bang.project.msg;

import com.jfinal.plugin.activerecord.Db;
import com.bang.component.util.JFlyFoxUtils;
import com.bang.util.DateUtils;
import com.bang.util.NumberUtils;

public class ArticleService {

	/**
	 * 获取tag标签
	 *
	 * 2016年4月17日 下午9:14:02
	 *
	 * @param model
	 * @return
	 */
	public String getTags(TbArticle model) {
		String tags = Db.findFirst("select group_concat(tagname) tags " //
				+ " from tb_tags where article_id = ? order by id", model.getInt("id")).getStr("tags");
		return tags;
	}

	/**
	 * 复制文章
	 *
	 * 2016年4月17日 下午9:14:09
	 *
	 * @param id
	 * @param userid
	 * @param folders
	 */
	public void copy(int id, Integer userid, String folders) {
		TbArticle model = TbArticle.dao.findById(id);
		for (String folderStr : folders.split(",")) {
			String now = DateUtils.getNow(DateUtils.DEFAULT_REGEX_YYYY_MM_DD_HH_MIN_SS);
			model.remove("id");
			model.setFolderId(NumberUtils.parseInt(folderStr));
			if (JFlyFoxUtils.ARTICLE_APPROVE) {
				model.set("approve_status", ArticleConstant.APPROVE_STATUS_UPDATE);
			} else {
				model.set("approve_status", ArticleConstant.APPROVE_STATUS_PASS);
			}
			model.set("create_id", userid);
			model.set("create_time", now);
			if (model.get("sort") == null)
				model.put("sort", 10);
			model.save();
		}
	}
}