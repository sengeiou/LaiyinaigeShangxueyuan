package com.jianzhong.lyag.http;

/**
 * Created by zhengwencheng on 2018/1/17.
 * package com.jianzhong.bs.http
 *
 * 请求Api
 */
public class HttpConfig {

    public static final int STATUS_FAILURE = 0;    // 调用失败
    public static final int STATUS_SUCCESS = 1;    // 调用成功
    // 外网测试环境
    public static String URL_BASE = "http://dev.api.lyagsxy.com/index.php?r=";
    //正式环境
//    public static String URL_BASE = "http://api.lyagsxy.com/index.php?r=";
    //webview的基础UR测试
    public static String WEB_URL_BASE = "http://dev.api.lyagsxy.com/";
    //webview的基础UR正式
//    public static String WEB_URL_BASE = "http://api.lyagsxy.com/";
    //上传地址的时候组合
    public static String PIC_URL_BASE = "http://lovica.oss-cn-shenzhen.aliyuncs.com/";
    //阿里云图片地址基础
    public static String PIC_URL_BASE_GET = "http://image.lyagsxy.com/";
    // 登录
    public static final String URL_LOGIN = "user/login";
    //获取短信验证码
    public static final String URL_MSG_CODE = "user/sms-code";
    //忘记密码
    public static final String URL_FORGET_PWD = "user/password-reset";
    //
    public static final String URL_USER_INFO = "user/info";
    //用户资料修改
    public static final String URL_USER_UPDATE = "user/update";
    //
    public static final String URL_HOME_INDEX = "home/index";
    //每日推荐换一换
    public static final String URL_HOME_FOCUS_LOOP = "home/focus-loop";
    //精品课程主题下换一换
    public static final String URL_HOME_COURSE_CAT_LOOP = "home/course-cat-loop";
    //专栏换一换
    public static final String URL_HOME_COLUMN_LOOP = "home/column-loop";
    //每日音频主体切换
    public static final String URL_AUDIO_CAT_TAG = "home/audio-cat-tag";
    //精品课题主体切换
    public static final String URL_COURSE_CAT_TAG = "home/course-cat-tag";
    //内容导航栏目
    public static final String URL_NAVI_NOW = "navi/show";
    //导航栏列表
    public static final String URL_NAVI_SEARCH = "navi/search";
    //成长通关父任务
    public static final String URL_TASK_LSIT = "task/list";
    //成长通关[子任务]
    public static final String URL_TASK_SUB_LSIT = "task/sub-list";
    //通关任务[子任务]参考资料
    public static final String URL_TASK_REFER = "task/refer";
    //通关考试开始
    public static final String URL_TASK_EXAM_START = "task/exam-start";
    //通关任务[子任务]考试结果提交
    public static final String URL_TASK_PICK = "task/pick";
    //课程评论列表 course/comment-list
    public static final String URL_COMMNET_LIST = "comment/list";
    //课件列表
    public static final String URL_WARE_LIST = "course/ware-list";
    //添加评论
    public static final String URL_COMMENT_ADD = "comment/add";
    //获取重要通知标签
    public static final String URL_NOTICE_TAG = "notice/tag";
    //获取重要通知详情
    public static final String URL_NOTICE_DETAIL = "notice/detail";
    //专栏详情
    public static final String URL_COLUMN_DETAIL = "column/detail";
    //课程详情
    public static final String URL_COURSE_DETAIL = "course/detail";
    //课程已学习用户记录
    public static final String URL_HAS_STUDY_USER = "course/has-study-user-simple";
    //相关课程列表
    public static final String URL_COURSE_RELATE = "course/relate";
    //每日音频详细
    public static final String URL_AUDIO_DETAIL = "audio/detail";
    //全部已学习成员
    public static final String URL_STUDY_USER_ALL = "course/has-study-user-all";
    //删除评论
    public static final String URL_COMMENT_DEL = "comment/del";
    //获取重要通知列表
    public static final String URL_NOTICE_LIST = "notice/search-by-tag";
    //通知未读人列表
    public static final String URL_NOTICE_UNREAD_USER = "notice/unread-user-simple";
    //
    public static final String URL_NOTICE_UNREAD_USER_ALL = "notice/unread-user-all";
    //点赞
    public static final String URL_DO_LIKE = "like/do-like";
    //取消点赞
    public static final String URL_DIS_LIKE = "like/dis-like";
    //添加收藏
    public static final String URL_ADD_FAVOR = "favor/add";
    //删除收藏
    public static final String URL_DEL_FAVOR = "favor/del";
    //观看历史
    public static final String URL_COURSE_HISTORY = "course/history";
    //获取收藏
    public static final String URL_FAVOR_LIST = "favor/list";
    //指令范围
    public static final String URL_DICTATE_RANGE = "dictate/range";
    //发送指令
    public static final String URL_DICTATE_SEND = "dictate/do-send";
    //批量删除收藏
    public static final String URL_FAVOR_BATCH_DEL = "favor/batch-del";
    //直播详细
    public static final String URL_LIVE_DETAIL = "live/detail";
    //互动首页
    public static final String URL_INTERACT_INDEX = "interact/index";
    //求助标签
    public static final String URL_HELP_TAG = "help/tag";
    //求助排序
    public static final String URL_HELP_SORT_FIELD = "help/sort-field";
    //求助列表
    public static final String URL_HELP_LIST = "help/list";
    //求助发布
    public static final String URL_HELP_ADD = "help/add";
    //互动(求助/分享)发布邀请范围
    public static final String URL_INVITE_RANGE = "interact/invite-range";
    //分享排序字段
    public static final String URL_SHARE_SORT_FIELD = "share/sort-field";
    //分享标签
    public static final String URL_SHARE_TAG = "share/tag";
    //分享列表
    public static final String URL_SHARE_LIST = "share/list";
    //发布分享
    public static final String URL_SHARE_ADD = "share/add";
    //
    public static final String URL_HELP_DETAIL = "help/detail";
    //求助删除
    public static final String URL_HELP_DEL = "help/del";
    //求助编辑
    public static final String URL_HELP_UPDATE = "help/update";
    //求助 点赞的人
    public static final String URL_HELP_LIKE_USER = "help/like-user-simple";
    //
    public static final String URL_HELP_LIKE_USER_ALL = "help/like-user-all";
    //分享详情
    public static final String URL_SHARE_DETAIL = "share/detail";
    //
    public static final String URL_SHARE_LIKE_USER = "share/like-user-simple";
    //
    public static final String URL_SHARE_LIKE_USER_ALL = "share/like-user-all";
    //分享删除
    public static final String URL_SHARE_DEL = "share/del";
    //分享修改
    public static final String URL_SHARE_UPDATE = "share/update";
    //获取学习任务列表
    public static final String URL_LEARN_DICTATE = "learn/dictate";
    //学习任务未完成用户列表
    public static final String URL_DICTATE_UNFINISH_USER = "dictate/un-finish-user";
    //我发出的学习任务
    public static final String URL_DICTATE_MY_SEND = "dictate/my-send";
    //我收到的学习任务
    public static final String URL_DICTATE_MY_RECEIVE = "dictate/my-receive";
    //互动排名new
    public static final String URL_INTERACT_RANK = "interact/rank";
    //课程学习记录提交
    public static final String URL_COURSE_STUDY = "course/study";
    public static final String URL_COURSE_JUDGE = "course/judge";
    //
    public static final String URL_LIVE_WATCHER_SIMPLE= "live/watch-user-simple";
    public static final String URL_LIVE_WATCHER_ALL= "live/watch-user-all";
    //学分排名
    public static final String URL_AC_POINT_RANK = "user/ac-point-rank";
    //学分明细new
    public static final String URL_POINT_DETAIL = "user/ac-point-detail";
    //搜索热词
    public static final String URL_SEARCH_HIT_WORD = "search/hit-word";
    //搜索发现
    public static final String URL_SERCH_FIND = "search/find";
    //搜索课程
    public static final String URL_SERCH_FIND_COURSE = "search/find-course";
    public static final String URL_SERCH_FIND_COLUMN = "search/find-column";
    public static final String URL_SERCH_FIND_LIVE = "search/find-live";
    public static final String URL_SERCH_FIND_AUDIO = "search/find-audio";
    //相关音频
    public static final String URL_AUDIO_RELATE = "audio/relate";
    public static final String URL_AUDIO_FINISH = "audio/finish";
    //音频文稿
    public static final String URL_AUDIO_TEXT = "audio/text/";
    public static final String URL_INTERACT_RULE = "interact/rule";
    public static final String URL_POINT_RULE = "ac-point/rule";
    public static final String URL_TASK_RULE = "task/rule";
    public static final String URL_LIVE_INTRODUCE = "live/introduce";
    //
    public static final String URL_TASK_PICK_LIST = "task/pick-list";
    //密码重置
    public static final String URL_PWD_UPDATE = "user/password-update";
    public static final String URL_NAVI_BANNER = "navi/banner";
    //直播结束
    public static final String URL_LIVE_FINISH = "live/finish";
    //消息列表
    public static final String URL_USER_GET_PUSH = "user/my-get-push";
    //我收到的学习任务打开任务
    public static final String URL_DICTATE_READ = "dictate/read";
    //获取在线人数
    public static final String URL_ONLINE_USER_NUM = "live/get-online-user-num";
}
