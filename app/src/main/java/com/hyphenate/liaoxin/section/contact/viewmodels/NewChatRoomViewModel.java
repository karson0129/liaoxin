package com.hyphenate.liaoxin.section.contact.viewmodels;

import android.app.Application;

import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.liaoxin.common.livedatas.SingleSourceLiveData;
import com.hyphenate.liaoxin.common.net.Resource;
import com.hyphenate.liaoxin.common.repositories.EMChatRoomManagerRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class NewChatRoomViewModel extends AndroidViewModel {
    private EMChatRoomManagerRepository repository;
    private SingleSourceLiveData<Resource<EMChatRoom>> chatRoomObservable;
    public NewChatRoomViewModel(@NonNull Application application) {
        super(application);
        repository = new EMChatRoomManagerRepository();
        chatRoomObservable = new SingleSourceLiveData<>();
    }

    public LiveData<Resource<EMChatRoom>> chatRoomObservable() {
        return chatRoomObservable;
    }

    public void createChatRoom(String subject, String description, String welcomeMessage,
                               int maxUserCount, List<String> members) {
        chatRoomObservable.setSource(repository.createChatRoom(subject, description, welcomeMessage, maxUserCount, members));
    }
}
