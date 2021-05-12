package com.hyphenate.liaoxin.common.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.hyphenate.liaoxin.common.db.entity.MsgTypeManageEntity;

import java.util.List;

@Dao
public interface MsgTypeManageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(MsgTypeManageEntity... entities);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(MsgTypeManageEntity... entities);

    @Query("select * from em_msg_type")
    List<MsgTypeManageEntity> loadAllMsgTypeManage();

    @Query("select * from em_msg_type where type = :type")
    MsgTypeManageEntity loadMsgTypeManage(String type);

    @Delete
    void delete(MsgTypeManageEntity... entities);
}
