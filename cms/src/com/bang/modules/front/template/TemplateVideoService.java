package com.bang.modules.front.template;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.bang.jfinal.base.BaseService;
import com.bang.jfinal.base.Paginator;
import com.bang.modules.admin.video.model.TbVideo;
import com.bang.modules.admin.video.model.TbVideoAlbum;
import com.bang.modules.front.service.FrontVideoService;
import com.bang.util.extend.RandomStrUtils;

/**
 * 模板方法接口
 *
 * 2016年1月18日 下午6:05:54
 */
public class TemplateVideoService extends BaseService {

	private final static FrontVideoService service = new FrontVideoService();

	public String randomAlbumId() {
		return RandomStrUtils.randomAlphanumeric(5);
	}

	public List<TbVideoAlbum> albums() {
		return service.getAlbumList();
	}

	public TbVideoAlbum album(Integer albumId) {
		return service.getAlbum(albumId);
	}

	public Page<TbVideo> videos(int pageNo, int pageSize) {
		return service.getVideo(new Paginator(pageNo, pageSize));
	}

	public Page<TbVideo> videos(int pageNo, int pageSize, int albumId) {
		return service.getVideo(new Paginator(pageNo, pageSize), albumId);
	}

	public TbVideo video(Integer iamgeId) {
		return service.getVideo(iamgeId);
	}

	public Page<TbVideo> recommendVideos(int pageNo, int pageSize) {
		return service.getRecommendVideos(new Paginator(pageNo, pageSize));
	}
}