package com.hyphenate.liaoxin.common.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.hyphenate.liaoxin.common.db.converter.DateConverter;
import com.hyphenate.liaoxin.common.db.dao.AppKeyDao;
import com.hyphenate.liaoxin.common.db.dao.EmUserDao;
import com.hyphenate.liaoxin.common.db.dao.InviteMessageDao;
import com.hyphenate.liaoxin.common.db.dao.MsgTypeManageDao;
import com.hyphenate.liaoxin.common.db.entity.AppKeyEntity;
import com.hyphenate.liaoxin.common.db.entity.EmUserEntity;
import com.hyphenate.liaoxin.common.db.entity.InviteMessage;
import com.hyphenate.liaoxin.common.db.entity.MsgTypeManageEntity;

@Database(entities = {EmUserEntity.class,
        InviteMessage.class,
        MsgTypeManageEntity.class,
        AppKeyEntity.class},
        version = 17)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract EmUserDao userDao();

    public abstract InviteMessageDao inviteMessageDao();

    public abstract MsgTypeManageDao msgTypeManageDao();

    public abstract AppKeyDao appKeyDao();
}
