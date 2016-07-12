package com.bang.project.msg;

import com.bang.component.base.BaseProjectModel;
import com.bang.jfinal.component.annotation.ModelBind;

@ModelBind(table = "tb_article")
public class TbArticle extends BaseProjectModel<TbArticle> {

	private static final long serialVersionUID = 1L;
	public static final TbArticle dao = new TbArticle();

	// columns START
	private String ID = "id"; // id
	private String SITE_ID = "site_id"; // 目录id
	private String FOLDER_ID = "folder_id"; // 目录id
	private String TITLE = "title"; // 文章名称
	private String CONTENT = "content"; // 文件内容
	private String COUNT_VIEW = "count_view"; // 浏览数
	private String COUNT_COMMENT = "count_comment"; // 评论数
	private String TYPE = "type"; // 类型：1 正常 2 预览展示概述 3 程序调用处理
	private String STATUS = "status"; // 状态：2 隐藏 1 显示
	private String IS_COMMENT = "is_comment"; // 是否评论：2 否 1 是
	private String IS_RECOMMEND = "is_recommend"; // 是否推荐：2 否 1 是
	private String SORT = "sort"; // 排序
	private String IMAGE_URL = "image_url"; // 图片路径
	private String IMAGE_NET_URL = "image_net_url"; // 网络图片路径
	private String PUBLISH_TIME = "publish_time"; // 发布时间
	private String PUBLISH_USER = "publish_user"; // 发布者
	private String START_TIME = "start_time"; // 开始时间
	private String END_TIME = "end_time"; // 结束时间
	private String UPDATE_TIME = "update_time"; // 更新时间
	private String CREATE_TIME = "create_time"; // 创建时间
	private String CREATE_ID = "create_id"; // 创建者

	public TbArticle setId(Integer value) {
		set(ID, value);
		return this;
	}

	public Integer getId() {
		return get(ID);
	}


	public TbArticle setSiteId(Integer value) {
		set(SITE_ID, value);
		return this;
	}

	public Integer getSiteId() {
		return get(SITE_ID);
	}

	public TbArticle setFolderId(Integer value) {
		set(FOLDER_ID, value);
		return this;
	}

	public Integer getFolderId() {
		return get(FOLDER_ID);
	}

	public TbArticle setTitle(String value) {
		set(TITLE, value);
		return this;
	}

	public String getTitle() {
		return get(TITLE);
	}

	public TbArticle setContent(String value) {
		set(CONTENT, value);
		return this;
	}

	public String getContent() {
		return get(CONTENT);
	}

	public TbArticle setCountView(Integer value) {
		set(COUNT_VIEW, value);
		return this;
	}

	public Integer getCountView() {
		return get(COUNT_VIEW);
	}

	public TbArticle setCountComment(Integer value) {
		set(COUNT_COMMENT, value);
		return this;
	}

	public Integer getCountComment() {
		return get(COUNT_COMMENT);
	}

	public TbArticle setType(Integer value) {
		set(TYPE, value);
		return this;
	}

	public Integer getType() {
		return get(TYPE);
	}

	public TbArticle setStatus(String value) {
		set(STATUS, value);
		return this;
	}

	public String getStatus() {
		return get(STATUS);
	}

	public TbArticle setIsComment(Integer value) {
		set(IS_COMMENT, value);
		return this;
	}

	public Integer getIsComment() {
		return get(IS_COMMENT);
	}

	public TbArticle setIsRecommend(Integer value) {
		set(IS_RECOMMEND, value);
		return this;
	}

	public Integer getIsRecommend() {
		return get(IS_RECOMMEND);
	}

	public TbArticle setSort(Integer value) {
		set(SORT, value);
		return this;
	}

	public Integer getSort() {
		return get(SORT);
	}

	public TbArticle setImageUrl(String value) {
		set(IMAGE_URL, value);
		return this;
	}

	public String getImageUrl() {
		return get(IMAGE_URL);
	}

	public TbArticle setImageNetUrl(String value) {
		set(IMAGE_NET_URL, value);
		return this;
	}

	public String getImageNetUrl() {
		return get(IMAGE_NET_URL);
	}

	public TbArticle setPublishTime(String value) {
		set(PUBLISH_TIME, value);
		return this;
	}

	public String getPublishTime() {
		return get(PUBLISH_TIME);
	}

	public TbArticle setPublishUser(String value) {
		set(PUBLISH_USER, value);
		return this;
	}

	public String getPublishUser() {
		return get(PUBLISH_USER);
	}

	public TbArticle setStartTime(String value) {
		set(START_TIME, value);
		return this;
	}

	public String getStartTime() {
		return get(START_TIME);
	}

	public TbArticle setEndTime(String value) {
		set(END_TIME, value);
		return this;
	}

	public String getEndTime() {
		return get(END_TIME);
	}

	public TbArticle setUpdateTime(String value) {
		set(UPDATE_TIME, value);
		return this;
	}

	public String getUpdateTime() {
		return get(UPDATE_TIME);
	}

	public TbArticle setCreateTime(String value) {
		set(CREATE_TIME, value);
		return this;
	}

	public String getCreateTime() {
		return get(CREATE_TIME);
	}

	public TbArticle setCreateId(Integer value) {
		set(CREATE_ID, value);
		return this;
	}

	public Integer getCreateId() {
		return get(CREATE_ID);
	}
}