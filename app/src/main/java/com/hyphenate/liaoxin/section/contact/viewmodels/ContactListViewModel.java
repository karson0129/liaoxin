package com.hyphenate.liaoxin.section.contact.viewmodels;

import android.app.Application;

import com.hyphenate.liaoxin.common.livedatas.SingleSourceLiveData;
import com.hyphenate.liaoxin.common.net.Resource;
import com.hyphenate.liaoxin.common.repositories.EMContactManagerRepository;
import com.hyphenate.easeui.domain.EaseUser;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ContactListViewModel extends AndroidViewModel {
    private EMContactManagerRepository mRepository;
    private SingleSourceLiveData<Resource<List<EaseUser>>> contactListObservable;

    public ContactListViewModel(@NonNull Application application) {
        super(application);
        mRepository = new EMContactManagerRepository();
        contactListObservable = new SingleSourceLiveData<>();
    }

    public LiveData<Resource<List<EaseUser>>> getContactListObservable() {
        return contactListObservable;
    }

    public void getContactList() {
        contactListObservable.setSource(mRepository.getContactList(false));
    }
}
