package com.jianzhong.lyag.global;

/**
 * 保存一些App使用的常量
 */
public class AppConstants {

    //    public static final String AVATAR_OBJECT_KEY = "dev/avatar/";   //阿里云头像上传的地址
    public static final String AVATAR_OBJECT_KEY = "prod/avatar/"; //生产环境
    //    public static final String HELP_OBJECT_KEY = "dev/help/";       //求助发布上传图片地址
    public static final String HELP_OBJECT_KEY = "prod/help/";    //生产环境
    //    public static final String SHARE_OBJECT_KEY = "dev/share/";       //求助发布上传图片地址
    public static final String SHARE_OBJECT_KEY = "prod/share/";    //生产环境
    //    public static final String AUDIO_OBJECT_KEY = "dev/audio/";        //阿里云音频上传地址 开发环境
    public static final String AUDIO_OBJECT_KEY = "prod/comment/audio/";        //阿里云音频上传地址 生产环境
    //    public static final String COMMENT_IMAGE_KEY = "dev/image/";        //阿里云评论图片上传地址 开发环境
    public static final String COMMENT_IMAGE_KEY = "prod/comment/image/";        //阿里云评论图片上传地址 开发环境
    public static final String BUCKET = "rhm3";                 //阿里云上传的地址

    public static final String EVENT_GOOD_CLASS = "event_good_class";
    public static final String EVENT_DRY_CARGO = "event_dry_cargo";
    public static final String KEY_ACCESS_TOKEN = "access_token"; // 保存登录过的用户信息
    public static final String KEY_IS_LOGIN = "is_login";          // 保存登录过的用户信息
    public static final String KEY_MOBILE = "mobile";              // 用户账号
    public static final String KEY_PWD = "pwd";                    //用户密码
    public static final String KEY_IS_FIRST = "is_first";          //是否为第一次使用app


    public static final String NVAI_FOCUS_LABEL = "navi_focus";       //内容导航推荐的label
    public static final String HOME_LABEL = "home";             //首页按钮的label
    public static final String INTERACT_LABEL = "interact";       //首页按钮的label
    public static final String CLUB_LABEL = "club";         //首页按钮的label
    public static final String MINE_LABEL = "mine";         //首页按钮的label
    public static final String TAG_COURSE = "course";       //导航列表课程的标志
    public static final String TAG_NOTICE = "notice";       //导航列表课程的标志
    public static final String TAG_LIVE = "live";
    public static final String TAG_DISCOVER = "discover";
    public static final String TAG_HELP = "help";
    public static final String TAG_SHARE = "share";       //
    public static final String TAG_AUDIO = "audio";       //导航列表音频的标志
    public static final String TAG_COLUMN = "column";       //导航列表专栏的标志
    public static final String KEY_EXAM = "key_exam";       //通关考试重设Viewpager高度的标志
    public static final String EVENT_ANSWER = "event_answer";    //公布答案
    public static final String EVENT_IS_RIGHT = "event_is_right";    //答案是否正确
    public static final String EVENT_TIME_OUT = "event_time_out";    //
    public static final String EVENT_TIME_OUT_BACK = "event_time_out_back";    //
    public static final String TAG_FAVOR = "favor";
    public static final String TAG_FAVOR_COURSE = "favor_course";
    public static final String TAG_FAVOR_COLUMN = "favor_column";
    public static final String TAG_FAVOR_LIVE = "favor_live";
    public static final String TAG_FAVOR_AUDIO = "favor_audio";
    public static final String TAG_FAVOR_HELP = "favor_help";
    public static final String TAG_FAVOR_SHARE = "favor_share";
    public static final String TAG_FAVOR_NOTICE = "favor_notice";
    public static final String TAG_UPDATE_ASSIGN = "update_assign";
    public static final String TAG_EDIT_COLLECT = "edit_collect";   //编辑收藏
    public static final String TAG_RESET_COLLECT = "reset_collect";   //复位编辑
    public static final String TAG_DICTATE_SEND = "dictate/send";   //代表有发送指令权限
    public static final String TRAN_ERROR_INFO = "数据解析错误";
    public static final String TAG_AUDIO_NAVI = "audio_navi";
    public static final String TAG_COURSE_NAVI = "course_navi";
    public static final String TAG_COLUMN_NAVI = "column_navi";
    public static final String TAG_LIVE_NAVI = "live_navi";
    public static final String TAG_CLOSE_AUDIO = "close_audio";
    public static final String TAG_UPDATE_AUDIO = "update_audio";
    public static final String TAG_CAHCE_EDIT = "cache_edit";
    public static final String TAG_RESET_CACHE_EDIT = "reset_cache_edit";
    public static final String TAG_CACHE_DEL = "cache_del";

    public static final String KEY_PUSH_MSG = "push_msg"; //推送消息的key
    public static final String KEY_PUSH_PACKAGE = "com.jianzhong.lyag"; //应用包名
    public static final String KEY_PUSH_ASSET_TYPE = "push_asset_type"; //推送类型
    public static final String KEY_PUSH_FOREIGN_ID = "push_foreign_id"; //推送ID

    public static final String EVENT_SWITCH_SEARCH = "switch_search";
    public static final String TAG_CLEAR_MSG = "clear_msg";
}
