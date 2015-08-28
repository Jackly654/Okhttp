package daimamiao.com.myokhttp;

import java.util.Date;

/**
 * Created by pengying on 2015/8/28.
 */
public class NetWorkConfig {


    public static final String ROOT = "http://iosnews.chinadaily.com.cn/newsdata";
    public static String CdnPath = "http://iosnews.chinadaily.com.cn/newsdata";
    //public static String CdnPath = "http://124.127.52.199/newsdata/app.shtml";
    //public static String CdnPath="http://124.127.52.199/newsdata";
    public static String NewsPath = "news";

    /**
     * 点赞
     */
    public static String getGreat(){
        return String.format("%s/interact.shtml?tab=upArticle",CdnPath);
    }
    //http://app.zytzb.gov.cn/newsdata/interact.shtml?tab=upCount&articleIds=2175

    /**
     * 点赞数
     */
    public static String getGreatCount(String articleIds){
        return String.format("%s/interact.shtml?tab=upCount&articleIds=%s",CdnPath,articleIds);
    }

    /**
     *
     * 分页数据
     */
    public static String LoadMoreData(String columnId,String pagnation){
        return String.format(
                "%s/%s/columns/%s_column_v4_%s.json",
                CdnPath,NewsPath,columnId,pagnation);
    }

    /**
     *
     * 分页数据3页后
     */
    public static String LoadMoreDataOther(String columnId,String pagnation){
        //http://app.zytzb.gov.cn/newsdata/tzb/columns.shtml?page=3&columnId=22&updateTime=1436239073
        return String.format(
                "%s/%s/columns.shtml?page=%s&columnId=%s&updateTime=1436239073",
                CdnPath,NewsPath,pagnation,columnId);
    }

    /**
     * 推荐新闻
     *
     * @return
     */
    public static String recommendNews() {
        return String.format(
                "%s/%s/recommend_v4.json?time=" + new Date().getTime(),
                CdnPath, NewsPath);
    }

    /**
     * 数据源(left menu)
     *
     * @return
     *//*
	public static String source() {
		return String.format(
				"%s/%s/source_v4.json?time=" + new Date().getTime(), CdnPath,
				NewsPath);
	}*/

    /**
     * 数据源(left menu)
     *
     * @return
     */
    public static String source() {
        return String.format(
                "%s/%s/source_v4.json" , CdnPath,
                NewsPath);
    }

    /**
     * 新闻 by left menu
     *
     * @param columnId
     * @return
     */
    public static String newsByColumnId(String columnId) {
        return String.format("%s/%s/columns/%s_column_v4.json?time="
                + new Date().getTime(), CdnPath, NewsPath, columnId);
    }

    /**
     * 图片路径
     *
     * @param articlePath
     * @param articleId
     * @param fileName
     * @return
     */
    public static String image(String articlePath, String articleId,
                               String fileName) {
        return String.format("%s/%s/%s/%s/%s", CdnPath, NewsPath, articlePath,
                articleId, fileName);
    }

    public static String specialImg(String columnId) {
        // http://iosnews.chinadaily.com.cn/newsdata/news/columns/columnId_icon1@2x.jpg
        return String.format("%s/%s/columns/%s_icon1.jpg", CdnPath, NewsPath,
                columnId);
    }

    public static String specialImgHD(String columnId) {
        // http://iosnews.chinadaily.com.cn/newsdata/news/columns/columnId_icon1@2x.jpg
        return String.format("%s/%s/columns/%s_icon1@2x.jpg", CdnPath,
                NewsPath, columnId);
    }

    /**
     * 文章详情
     *
     * @param articlePath
     * @return
     */
    public static String articleDetail(String articlePath, String articleId) {
        return String.format("%s/%s/%s/%s/article.json", CdnPath, NewsPath,
                articlePath, articleId);
    }

    public static String video(String articlePath, String articleId, String file) {
        return String.format("%s/%s/%s/%s/%s", CdnPath, NewsPath, articlePath,
                articleId, file);
    }
}
