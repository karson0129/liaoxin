package com.hyphenate.liaoxin.section.chat.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMUserInfo;
import com.hyphenate.easecallkit.EaseCallKit;
import com.hyphenate.easecallkit.base.EaseCallType;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.modules.chat.EaseChatMessageListLayout;
import com.hyphenate.easeui.modules.chat.interfaces.IChatPrimaryMenu;
import com.hyphenate.liaoxin.DemoHelper;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.constant.DemoConstant;
import com.hyphenate.liaoxin.common.constant.UserConstant;
import com.hyphenate.liaoxin.common.db.PrefUtils;
import com.hyphenate.liaoxin.common.livedatas.LiveDataBus;
import com.hyphenate.liaoxin.common.model.EmojiconExampleGroupData;
import com.hyphenate.liaoxin.common.net.bean.HuanXinBean;
import com.hyphenate.liaoxin.common.utils.PreferenceManager;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.common.widget.NoticeTitleDialog;
import com.hyphenate.liaoxin.section.base.BaseActivity;
import com.hyphenate.liaoxin.section.chat.activity.ForwardMessageActivity;
import com.hyphenate.liaoxin.section.chat.activity.ImageGridActivity;
import com.hyphenate.liaoxin.section.chat.activity.PickAtUserActivity;
import com.hyphenate.liaoxin.section.chat.activity.SelectUserCardActivity;
import com.hyphenate.liaoxin.section.chat.activity.SingleRedEnvelopeActivity;
import com.hyphenate.liaoxin.section.chat.myclass.EaseChatFragment;
import com.hyphenate.liaoxin.section.chat.myclass.EaseChatInputMenu;
import com.hyphenate.liaoxin.section.chat.viewmodel.MessageViewModel;
import com.hyphenate.liaoxin.section.conference.ConferenceInviteActivity;
import com.hyphenate.liaoxin.section.dialog.DemoDialogFragment;
import com.hyphenate.liaoxin.section.dialog.DemoListDialogFragment;
import com.hyphenate.liaoxin.section.dialog.FullEditDialogFragment;
import com.hyphenate.liaoxin.section.contact.activity.ContactDetailActivity;
import com.hyphenate.liaoxin.section.dialog.SimpleDialogFragment;
import com.hyphenate.liaoxin.section.group.GroupHelper;
import com.hyphenate.liaoxin.section.me.activity.PayManageActivity;
import com.hyphenate.liaoxin.section.me.activity.UserDetailActivity;
import com.hyphenate.easeui.constants.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseEvent;
import com.hyphenate.easeui.modules.chat.interfaces.IChatExtendMenu;
import com.hyphenate.easeui.modules.chat.interfaces.OnRecallMessageResultListener;
import com.hyphenate.easeui.modules.menu.EasePopupWindowHelper;
import com.hyphenate.easeui.modules.menu.MenuItemBean;
import com.hyphenate.liaoxin.section.me.activity.WithdrawActivity;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.UriUtils;

import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class ChatFragment extends EaseChatFragment implements OnRecallMessageResultListener {
    private static final String TAG = ChatFragment.class.getSimpleName();
    private MessageViewModel viewModel;
    protected ClipboardManager clipboard;

    private static final int REQUEST_CODE_SELECT_AT_USER = 15;
    private static final String[] calls = {"????????????", "????????????"};
    private OnFragmentInfoListener infoListener;
    private Dialog dialog;
    private NoticeTitleDialog exitDialog;

    private String clientId;

    @Override
    public void initView() {
        super.initView();
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        viewModel = new ViewModelProvider(this).get(MessageViewModel.class);

        //???????????????????????????
        EaseChatMessageListLayout messageListLayout = chatLayout.getChatMessageListLayout();
        //????????????????????????
        //messageListLayout.setBackground(new ColorDrawable(Color.parseColor("#DA5A4D")));
        //??????????????????
        //messageListLayout.setAvatarDefaultSrc(ContextCompat.getDrawable(mContext, R.drawable.ease_default_avatar));
        //??????????????????
        messageListLayout.setAvatarShapeType(1);
        //????????????????????????
        //messageListLayout.setItemTextSize((int) EaseCommonUtils.sp2px(mContext, 18));
        //????????????????????????
        //messageListLayout.setItemTextColor(ContextCompat.getColor(mContext, R.color.red));
        //????????????????????????
        //messageListLayout.setTimeBackground(ContextCompat.getDrawable(mContext, R.color.gray_normal));
        //??????????????????????????????
        //messageListLayout.setTimeTextSize((int) EaseCommonUtils.sp2px(mContext, 18));
        //??????????????????????????????
        //messageListLayout.setTimeTextColor(ContextCompat.getColor(mContext, R.color.black));
        //???????????????????????????????????????????????????
        //messageListLayout.setItemShowType(EaseChatMessageListLayout.ShowType.LEFT);

        //??????????????????????????????
        EaseChatInputMenu chatInputMenu = chatLayout.getChatInputMenu();
        //???????????????????????????
        IChatPrimaryMenu primaryMenu = chatInputMenu.getPrimaryMenu();
        //if(primaryMenu != null) {
            //??????????????????????????????????????????
        //    primaryMenu.setMenuShowType(EaseInputMenuStyle.ONLY_TEXT);
        //}
        fetchSelfInfo();
    }


    private void fetchSelfInfo(){
        String[] userId = new String[1];
        userId[0] = conversationId;
        EMUserInfo.EMUserInfoType[] userInfoTypes = new EMUserInfo.EMUserInfoType[3];
        userInfoTypes[0] = EMUserInfo.EMUserInfoType.NICKNAME;
        userInfoTypes[1] = EMUserInfo.EMUserInfoType.AVATAR_URL;
        userInfoTypes[2] = EMUserInfo.EMUserInfoType.EXT;
        EMClient.getInstance().userInfoManager().fetchUserInfoByAttribute(userId, userInfoTypes,new EMValueCallBack<Map<String, EMUserInfo>>() {
            @Override
            public void onSuccess(Map<String, EMUserInfo> userInfos) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        EMUserInfo userInfo = userInfos.get(conversationId);
                        if (userInfo != null){
                            Log.i(TAG,"??????????????????" + userInfo.getNickName() +" | ava:"+userInfo.getAvatarUrl()+" | ext:"+userInfo.getExt());

                            if (!TextUtils.isEmpty(userInfo.getExt())){
                                try {
                                    HuanXinBean bean = new Gson().fromJson(userInfo.getExt(),HuanXinBean.class);
                                    if (bean != null){
                                        clientId = bean.clientId;
                                    }
                                }catch (Exception e){}
                            }
                        }
                    }
                });
            }

            @Override
            public void onError(int error, String errorMsg) {
                EMLog.e("MainActivity","fetchUserInfoByIds error:" + error + " errorMsg:" + errorMsg);
            }
        });
    }

    private void addItemMenuAction() {
        MenuItemBean itemMenu = new MenuItemBean(0, R.id.action_chat_forward, 11, getString(R.string.action_forward));
        itemMenu.setResourceId(R.drawable.ease_chat_item_menu_forward);
        chatLayout.addItemMenu(itemMenu );
    }

    private void resetChatExtendMenu() {
        IChatExtendMenu chatExtendMenu = chatLayout.getChatInputMenu().getChatExtendMenu();
        chatExtendMenu.clear();
        chatExtendMenu.registerMenuItem(R.string.zhaopian, R.drawable.chat_more_photo, R.id.extend_item_picture);
        chatExtendMenu.registerMenuItem(R.string.paizhao, R.drawable.chat_more_shooting, R.id.extend_item_take_picture);
        chatExtendMenu.registerMenuItem(R.string.hongbao, R.drawable.chat_more_red_packet, R.id.extend_item_hongbao);
        chatExtendMenu.registerMenuItem(R.string.attach_video, R.drawable.em_chat_video_selector, R.id.extend_item_video);

        //???????????????
        if(chatType == EaseConstant.CHATTYPE_SINGLE){
            //inputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.drawable.em_chat_voice_call_selector, EaseChatInputMenu.ITEM_VOICE_CALL, this);
            chatExtendMenu.registerMenuItem(R.string.attach_media_call, R.drawable.em_chat_video_call_selector, R.id.extend_item_video_call);
        }
        if (chatType == EaseConstant.CHATTYPE_GROUP) { // ???????????????
            chatExtendMenu.registerMenuItem(R.string.voice_and_video_conference, R.drawable.em_chat_video_call_selector, R.id.extend_item_conference_call);
            //????????????????????????????????????????????????????????????????????????????????????
            //inputMenu.registerExtendMenuItem(R.string.title_live, R.drawable.em_chat_video_call_selector, EaseChatInputMenu.ITEM_LIVE, this);
        }
//        chatExtendMenu.registerMenuItem(R.string.attach_location, R.drawable.ease_chat_location_selector, R.id.extend_item_location);
//        chatExtendMenu.registerMenuItem(R.string.attach_file, R.drawable.em_chat_file_selector, R.id.extend_item_file);
        //????????????
        chatExtendMenu.registerMenuItem(R.string.mingpian, R.drawable.chat_more_business, R.id.extend_item_user_card);
        //??????????????????????????????????????????owner
        if(chatType == EaseConstant.CHATTYPE_GROUP && EMClient.getInstance().getOptions().getRequireAck()) {
            EMGroup group = DemoHelper.getInstance().getGroupManager().getGroup(conversationId);
            if(GroupHelper.isOwner(group)) {
                chatExtendMenu.registerMenuItem(R.string.em_chat_group_delivery_ack, R.drawable.demo_chat_delivery_selector, R.id.extend_item_delivery);
            }
        }
        //??????????????????
        chatLayout.getChatInputMenu().getEmojiconMenu().addEmojiconGroup(EmojiconExampleGroupData.getData());
    }

    @Override
    public void initListener() {
        super.initListener();
        chatLayout.setOnRecallMessageResultListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        resetChatExtendMenu();
        addItemMenuAction();

        chatLayout.getChatInputMenu().getPrimaryMenu().getEditText().setText(getUnSendMsg());
        chatLayout.turnOnTypingMonitor(DemoHelper.getInstance().getModel().isShowMsgTyping());

        LiveDataBus.get().with(DemoConstant.MESSAGE_CHANGE_CHANGE).postValue(new EaseEvent(DemoConstant.MESSAGE_CHANGE_CHANGE, EaseEvent.TYPE.MESSAGE));
        LiveDataBus.get().with(DemoConstant.MESSAGE_CALL_SAVE, Boolean.class).observe(getViewLifecycleOwner(), event -> {
            if(event == null) {
                return;
            }
            if(event) {
                chatLayout.getChatMessageListLayout().refreshToLatest();
            }
        });

        LiveDataBus.get().with(DemoConstant.CONVERSATION_DELETE, EaseEvent.class).observe(getViewLifecycleOwner(), event -> {
            if(event == null) {
                return;
            }
            if(event.isMessageChange()) {
                chatLayout.getChatMessageListLayout().refreshMessages();
            }
        });

        LiveDataBus.get().with(DemoConstant.MESSAGE_CHANGE_CHANGE, EaseEvent.class).observe(getViewLifecycleOwner(), event -> {
            if(event == null) {
                return;
            }
            if(event.isMessageChange()) {
                chatLayout.getChatMessageListLayout().refreshToLatest();
            }
        });
        LiveDataBus.get().with(DemoConstant.CONVERSATION_READ, EaseEvent.class).observe(getViewLifecycleOwner(), event -> {
            if(event == null) {
                return;
            }
            if(event.isMessageChange()) {
                chatLayout.getChatMessageListLayout().refreshMessages();
            }
        });

        //??????????????????????????????
        LiveDataBus.get().with(DemoConstant.CONTACT_ADD, EaseEvent.class).observe(getViewLifecycleOwner(), event -> {
            if(event == null) {
                return;
            }
            if(event != null) {
                chatLayout.getChatMessageListLayout().refreshMessages();
            }
        });

        LiveDataBus.get().with(DemoConstant.CONTACT_UPDATE, EaseEvent.class).observe(getViewLifecycleOwner(), event -> {
            if(event == null) {
                return;
            }
            if(event != null) {
                chatLayout.getChatMessageListLayout().refreshMessages();
            }
        });
    }

    private void showDeliveryDialog() {
        new FullEditDialogFragment.Builder((BaseActivity) mContext)
                .setTitle(R.string.em_chat_group_read_ack)
                .setOnConfirmClickListener(R.string.em_chat_group_read_ack_send, new FullEditDialogFragment.OnSaveClickListener() {
                    @Override
                    public void onSaveClick(View view, String content) {
                        chatLayout.sendTextMessage(content, true);
                    }
                })
                .setConfirmColor(R.color.em_color_brand)
                .setHint(R.string.em_chat_group_read_ack_hint)
                .show();
    }

    private void showSelectDialog() {
        new DemoListDialogFragment.Builder((BaseActivity) mContext)
                //.setTitle(R.string.em_single_call_type)
                .setData(calls)
                .setCancelColorRes(R.color.black)
                .setWindowAnimations(R.style.animate_dialog)
                .setOnItemClickListener(new DemoListDialogFragment.OnDialogItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        switch (position) {
                            case 0 :
                                EaseCallKit.getInstance().startSingleCall(EaseCallType.SINGLE_VIDEO_CALL,conversationId,null);
                                break;
                            case 1 :
                                EaseCallKit.getInstance().startSingleCall(EaseCallType.SINGLE_VOICE_CALL,conversationId,null);
                                break;
                        }
                    }
                })
                .show();
    }

    @Override
    public void onUserAvatarClick(String username) {
        if(!TextUtils.equals(username, DemoHelper.getInstance().getCurrentUser())) {
            EaseUser user = DemoHelper.getInstance().getUserInfo(username);
            if(user == null){
                    user = new EaseUser(username);
                }
                boolean isFriend =  DemoHelper.getInstance().getModel().isContact(username);
                if(isFriend){
                    user.setContact(0);
                }else{
                    user.setContact(3);
                }
                ContactDetailActivity.actionStart(mContext, user);
        }else{
            UserDetailActivity.actionStart(mContext,null,null);
        }
    }

    @Override
    public void onUserAvatarLongClick(String username) {

    }

    @Override
    public boolean onBubbleLongClick(View v, EMMessage message) {
        return false;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!chatLayout.getChatMessageListLayout().isGroupChat()) {
            return;
        }
        if(count == 1 && "@".equals(String.valueOf(s.charAt(start)))){
            PickAtUserActivity.actionStartForResult(ChatFragment.this, conversationId, REQUEST_CODE_SELECT_AT_USER);
        }
    }

    @Override
    protected void selectVideoFromLocal() {
        super.selectVideoFromLocal();
        Intent intent = new Intent(getActivity(), ImageGridActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
    }

    @Override
    public boolean onBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onChatExtendMenuItemClick(View view, int itemId) {
        super.onChatExtendMenuItemClick(view, itemId);
        switch (itemId) {
            case R.id.extend_item_video_call:
                showSelectDialog();
                break;
            case R.id.extend_item_conference_call:
                Intent intent = new Intent(getContext(), ConferenceInviteActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(DemoConstant.EXTRA_CONFERENCE_GROUP_ID, conversationId);
                 getContext().startActivity(intent);
                 break;
            case R.id.extend_item_delivery://???????????????
                showDeliveryDialog();
                break;
            case R.id.extend_item_user_card://??????
                EMLog.d(TAG,"select user card");
                Intent userCardIntent = new Intent(this.getContext(), SelectUserCardActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK);
                userCardIntent.putExtra("toUser",conversationId);
                this.getContext().startActivity(userCardIntent);
                break;
            case R.id.extend_item_hongbao://??????
                //??????????????????????????????
                if (PrefUtils.getBoolean(mContext, UserConstant.isCoinPassword,false)){
                    SingleRedEnvelopeActivity.actionStart(mContext,clientId);
                }else {
                    showDialog();
                }
                break;
        }
    }

    /**
     * ??????
     * */
    private void showDialog(){
        if (exitDialog == null) {
            exitDialog = new NoticeTitleDialog(mContext, "???????????????????????????????????????");
            exitDialog.setTitle("??????");
            exitDialog.setCancel("??????");
            exitDialog.setAction("??????");
            exitDialog.setType(1);
            exitDialog.setOnItemClickListener(new NoticeTitleDialog.onItemClickListener() {
                @Override
                public void onActionClick() {
                    if (exitDialog != null) {
                        exitDialog.dismiss();
                    }
                    PayManageActivity.actionStart(mContext);
                }

                @Override
                public void onCancelClick() {
                    if (exitDialog != null) {
                        exitDialog.dismiss();
                    }
                }
            });
        }
        exitDialog.show();
    }

    @Override
    public void onChatError(int code, String errorMsg) {
        if(infoListener != null) {
            infoListener.onChatError(code, errorMsg);
        }
    }

    @Override
    public void onOtherTyping(String action) {
        if(infoListener != null) {
            infoListener.onOtherTyping(action);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_AT_USER :
                    if(data != null){
                        String username = data.getStringExtra("username");
                        chatLayout.inputAtUsername(username, false);
                    }
                    break;
                case REQUEST_CODE_SELECT_VIDEO: //send the video
                    if (data != null) {
                        int duration = data.getIntExtra("dur", 0);
                        String videoPath = data.getStringExtra("path");
                        String uriString = data.getStringExtra("uri");
                        EMLog.d(TAG, "path = "+videoPath + " uriString = "+uriString);
                        if(!TextUtils.isEmpty(videoPath)) {
                            chatLayout.sendVideoMessage(Uri.parse(videoPath), duration);
                        }else {
                            Uri videoUri = UriUtils.getLocalUriFromString(uriString);
                            chatLayout.sendVideoMessage(videoUri, duration);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //????????????????????????????????????
        if(mContext != null && mContext.isFinishing()) {
            if(chatLayout.getChatInputMenu() != null) {
                saveUnSendMsg(chatLayout.getInputContent());
                LiveDataBus.get().with(DemoConstant.MESSAGE_NOT_SEND).postValue(true);
            }
        }
    }

    //================================== for video and voice start ====================================

    /**
     * ????????????????????????????????????
     * @param content
     */
    private void saveUnSendMsg(String content) {
        DemoHelper.getInstance().getModel().saveUnSendMsg(conversationId, content);
    }

    private String getUnSendMsg() {
        return DemoHelper.getInstance().getModel().getUnSendMsg(conversationId);
    }

    @Override
    public void onPreMenu(EasePopupWindowHelper helper, EMMessage message) {
        //????????????????????????????????????
        if(System.currentTimeMillis() - message.getMsgTime() > 2 * 60 * 1000) {
            helper.findItemVisible(R.id.action_chat_recall, false);
        }
        EMMessage.Type type = message.getType();
        helper.findItemVisible(R.id.action_chat_forward, false);
        switch (type) {
            case TXT:
                if(!message.getBooleanAttribute(DemoConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false)
                        && !message.getBooleanAttribute(DemoConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    helper.findItemVisible(R.id.action_chat_forward, true);
                }
                break;
            case IMAGE:
                helper.findItemVisible(R.id.action_chat_forward, true);
                break;
        }

        if(chatType == DemoConstant.CHATTYPE_CHATROOM) {
            helper.findItemVisible(R.id.action_chat_forward, true);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItemBean item, EMMessage message) {
        switch (item.getItemId()) {
            case R.id.action_chat_forward :
                ForwardMessageActivity.actionStart(mContext, message.getMsgId());
                return true;
            case R.id.action_chat_delete:
                showDeleteDialog(message);
                return true;
            case R.id.action_chat_recall :
                showProgressBar();
                chatLayout.recallMessage(message);
                return true;
        }
        return false;
    }

    private void showProgressBar() {
        View view = View.inflate(mContext, R.layout.demo_layout_progress_recall, null);
        dialog = new Dialog(mContext,R.style.dialog_recall);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(view, layoutParams);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void showDeleteDialog(EMMessage message) {
        new SimpleDialogFragment.Builder((BaseActivity) mContext)
                .setTitle(getString(R.string.em_chat_delete_title))
                .setConfirmColor(R.color.red)
                .setOnConfirmClickListener(getString(R.string.delete), new DemoDialogFragment.OnConfirmClickListener() {
                    @Override
                    public void onConfirmClick(View view) {
                        chatLayout.deleteMessage(message);
                    }
                })
                .showCancelButton(true)
                .show();
    }

    public void setOnFragmentInfoListener(OnFragmentInfoListener listener) {
        this.infoListener = listener;
    }

    @Override
    public void recallSuccess(EMMessage message) {
        if(dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void recallFail(int code, String errorMsg) {
        if(dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public interface OnFragmentInfoListener {
        void onChatError(int code, String errorMsg);

        void onOtherTyping(String action);
    }
}