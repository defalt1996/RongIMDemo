package com.guozongkui.testimsdk.net.service;

import com.guozongkui.testimsdk.model.LoginResult;
import com.guozongkui.testimsdk.model.RegisterResult;
import com.guozongkui.testimsdk.model.Result;
import com.guozongkui.testimsdk.model.VerifyResult;
import com.guozongkui.testimsdk.net.SealTalkUrl;

import java.util.List;


import io.rong.imlib.model.UserInfo;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {
    @POST(SealTalkUrl.LOGIN)
    Call<Result<LoginResult>> login(@Body RequestBody body);
//
//    @GET(SealTalkUrl.GET_TOKEN)
//    Call<Result<LoginResult>> getToken();
//
//    @GET(SealTalkUrl.GET_USER_INFO)
//    Call<Result<UserInfo>> getUserInfo(@Path("user_id") String userId);

    @POST(SealTalkUrl.SEND_CODE)
    Call<Result<String>> sendCode(@Body RequestBody body);

    @POST(SealTalkUrl.VERIFY_CODE)
    Call<Result<VerifyResult>> verifyCode(@Body RequestBody body);

    @POST(SealTalkUrl.REGISTER)
    Call<Result<RegisterResult>> register(@Body RequestBody body);
//
//    @GET(SealTalkUrl.REGION_LIST)
//    Call<Result<List<RegionResult>>> getRegionList();
//
    @POST(SealTalkUrl.CHECK_PHONE_AVAILABLE)
    Call<Result<Boolean>> checkPhoneAvailable(@Body RequestBody body);
//
//    @POST(SealTalkUrl.RESET_PASSWORD)
//    Call<Result<String>> resetPassword(@Body RequestBody body);
//
//    @POST(SealTalkUrl.SET_NICK_NAME)
//    Call<Result> setMyNickName(@Body RequestBody requestBody);
//
//    @POST(SealTalkUrl.SET_ST_ACCOUNT)
//    Call<Result> setStAccount(@Body RequestBody requestBody);
//
//    @POST(SealTalkUrl.SET_GENDER)
//    Call<Result> setGender(@Body RequestBody requestBody);
//
//    @GET(SealTalkUrl.GET_IMAGE_UPLOAD_TOKEN)
//    Call<Result<UploadTokenResult>> getImageUploadToken();
//
//    @POST(SealTalkUrl.SET_PORTRAIT)
//    Call<Result> setPortrait(@Body RequestBody body);
//
//    @POST(SealTalkUrl.CHANGE_PASSWORD)
//    Call<Result> changePassword(@Body RequestBody body);
//
//
//    /**
//     * 获取黑名单信息
//     *
//     * @return
//     */
//    @GET(SealTalkUrl.GET_BLACK_LIST)
//    Call<Result<List<FriendBlackInfo>>> getFriendBlackList();
//
//    /**
//     * 添加到黑名单
//     *
//     * @param body
//     * @return
//     */
//    @POST(SealTalkUrl.ADD_BLACK_LIST)
//    Call<Result> addToBlackList(@Body RequestBody body);
//
//    /**
//     * 移除黑名单
//     *
//     * @param body
//     * @return
//     */
//    @POST(SealTalkUrl.REMOVE_BLACK_LIST)
//    Call<Result> removeFromBlackList(@Body RequestBody body);
//
//    /**
//     * 获取通讯录中的群组列表
//     *
//     * @return
//     */
//    @GET(SealTalkUrl.GROUP_GET_ALL_IN_CONTACT)
//    Call<Result<ContactGroupResult>> getGroupListInContact();
//
//    /**
//     * 设置接收戳一下消息状态
//     *
//     * @param body
//     * @return
//     */
//    @POST(SealTalkUrl.SET_RECEIVE_POKE_MESSAGE_STATUS)
//    Call<Result> setReceivePokeMessageStatus(@Body RequestBody body);
//
//    /**
//     * 获取接收戳一下消息状态
//     *
//     * @return
//     */
//    @GET(SealTalkUrl.GET_RECEIVE_POKE_MESSAGE_STATUS)
//    Call<Result<GetPokeResult>> getReceivePokeMessageStatus();
}
