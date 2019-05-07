package com.jianzhong.lyag.global;

import android.view.ViewGroup;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.jianzhong.lyag.base.BaseApplication;
import com.jianzhong.lyag.model.AudioDetailModel;
import com.jianzhong.lyag.model.ContactsModel;
import com.jianzhong.lyag.model.DepartmentModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 全局的单例
 * Created by zhengwencheng on 2018/1/17.
 * package com.jianzhong.bs.global
 */

public class GroupVarManager {

    private volatile static GroupVarManager _p = null;
    public Map<Integer,ViewGroup.LayoutParams> mViewPagerMap = new HashMap<>();
    public Map<Integer,ViewGroup.LayoutParams> mGoodClassMap = new HashMap<>();
    public Map<Integer,ViewGroup.LayoutParams> mExamMap = new HashMap<>();
    public OSS oss;
    private static final String ACCESS_KEY_ID = "LTAIa6ARcHFowUsh";
    private static final String ACCESS_KEY_SECRET = "hWZX9TrpUzbymjDMk8ea3gzdUgQZkp";
    private static final String END_POINT = "http://oss-cn-shenzhen.aliyuncs.com/";
    //记录当前精品课程选中的是哪一个
    public int coursePosition;
    //记录当前每日音频选中的是哪一项
    public int dryCargoPosition;
    //导航栏的三级选择标签
    public String path_1;
    public String path_2;
    public String path_3;
    //当前试题是否已经有选 1： 有答案 2：已提交
    public int isSelectedAnswer;
    public int is_right;
    //当前题目的下标
    public int examIndex;
    //是否刷新评论列表
    public int isUpdateComment = 0;
    //组织架构的选择
    public List<ContactsModel> mContactList = new ArrayList<>();
    public LinkedList<DepartmentModel> mDepartmentModels = new LinkedList<>(); //选中的部门
    public LinkedList<DepartmentModel> mAnecyModels = new LinkedList<>();      //选中的经销商
    public LinkedList<ContactsModel> mMemberModels = new LinkedList<>();      //选中的成员
    public LinkedList<DepartmentModel> mPostModels = new LinkedList<>();      //按岗位选择列表
    //选中的收藏列表item_id
    public HashSet<String> mCollectList = new HashSet<>();
    //选中的缓存列表key
    public HashSet<String> mCacheList = new HashSet<>();
    //记录是够编辑收藏列表
    public int isEditCollect;
    //记录当前搜索关键字
    public String keyWord;
    //当前播放的音频
    public AudioDetailModel mAudioDetailModel;
    //记录是否编辑缓存列表
    public int isEditCache;
    //搜索结果的切换
    public int curSearch;
    //编辑缓存是否全选
    public int isAll = 1;
    public int isAtAudio;
    //查看课件
    public int isCheckWare;
    //是否搜索同事
    public int isSearchMember;
    public static GroupVarManager getInstance() {
        if (_p == null) {
            synchronized (GroupVarManager.class) {
                if (_p == null) {
                    _p = new GroupVarManager();
                }
            }
        }
        return _p;
    }

    private GroupVarManager() {

    }

    /**
     * 初始化oss
     */
    public OSS initOSS() {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        oss = new OSSClient(BaseApplication.getInstance(), END_POINT, credentialProvider, conf);
        return oss;
    }
}
