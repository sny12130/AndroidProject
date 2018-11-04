package com.example.yao.pm;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageEntity {

}

class  UserEntity{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public Bitmap getProfile() {
        return profile;
    }

    public void setProfile(Bitmap profile) {
        this.profile = profile;
    }

    private String name;
    private int uid;
    private String profileUrl = "";
    private Bitmap profile;
    public  UserEntity(){}
    public  UserEntity(int uid,String name,String profileUrl){
        this.name = name;
        this.uid = uid;
        this.profileUrl = profileUrl;
    }

    public void downloadProfile(){


        try{

            if(profileUrl!=null || !"".equals(profileUrl))
            {
                Bitmap p = NetworkFactory.getImage(profileUrl);

                if(p!=null)
                {
                    this.profile = p;
                }
            }
        }
        catch (Exception ex){
            Log.d("testImageError","url:"+profileUrl+", Error:"+ex);
        }

    }
}


class IMEntity{
    //message body
    //uid
    private int sender;
    private String message;
    private UserEntity userEntity;

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    private String sendTime;
    public boolean isLocal;


    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }


    public String getName() {
        return userEntity.getName();
    }
    public int getUid() {
        return userEntity.getUid();
    }

    public Bitmap getProfile() {
        return userEntity.getProfile();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }


    public IMEntity(){}

    public IMEntity(UserEntity userEntity, String message){
        this.sender = userEntity.getUid();
        this.userEntity = userEntity;
        this.message = message;
    }

}

class MessageRoomSchema{


    public UserEntity getLocal() {
        return local;
    }

    public UserEntity getTarget() {
        return target;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isInited() {
        return isInited;
    }

    UserEntity local;
    UserEntity target;
    String roomId;
    boolean isInited = false;

    public void get(String str){
        local=new UserEntity();
        target=new UserEntity();
        try{
            JSONObject jobj = new JSONObject(str);
            JSONObject jData = jobj.getJSONObject("data");
            roomId = jData.getString("RoomID");
            local.setUid(jData.getInt("Local"));
            local.setName(jData.getString("LocalName"));
            local.setProfileUrl(jData.getString("LocalProfile"));
            target.setUid(jData.getInt("Target"));
            target.setName(jData.getString("TargetName"));
            target.setProfileUrl(jData.getString("TargetProfile"));

            isInited = true;
        }
        catch (Exception ex){
            Log.d("testRoomInit",ex.getLocalizedMessage());
        }
    }

}


class IMFactory{

    public static ArrayList<IMEntity> createEntity(String json,MessageRoomSchema schema,Boolean fromAjax){
        //you need deserialize to data field or you will get error
        ArrayList<IMEntity> list = new ArrayList<>();
        try{
            JSONObject jobj = new JSONObject(json);
            if(fromAjax)
            {
                jobj = jobj.getJSONObject("data");
            }
            String srcType = jobj.getString("SrcType");
            if(!(srcType.equals("IM") || srcType.equals("IMInit")))
            {
                //only parse im type
                return list;
            }
            JSONArray bodes = jobj.getJSONArray("Body");
            for (int i =0; i < bodes.length(); i++ )
            {
                IMEntity entity = new IMEntity();
                JSONObject jData = bodes.getJSONObject(i);
                Log.d("testRoomMessageIndex","index:"+i);

                int uid = jData.getInt("Sender");

                if(schema.getLocal().getUid() == uid)
                {
                    entity.setUserEntity(schema.getLocal());
                }
                else{
                    entity.setUserEntity(schema.getTarget());
                }
                entity.setMessage(jData.getString("Message"));
                entity.setSendTime(jData.getString("TimeStamp"));
                entity.setLocal(uid == schema.local.getUid());
                list.add(0,entity);
            }
        }
        catch (Exception ex){
            Log.d("testRoomInit",ex.getLocalizedMessage());
        }
        return list;
    }

    public static ArrayList<IMEntity> createEntityFromSocket(String json,MessageRoomSchema schema){
        //you need deserialize to data field or you will get error
        ArrayList<IMEntity> list = new ArrayList<>();

        try{
            Log.d("testRoomMessageIndex","init");

            JSONArray jarr = new JSONArray(json);
            for(int i = 0 ; i< jarr.length();i++)
            {
                JSONObject jobj = jarr.getJSONObject(i);
                String srcType = jobj.getString("SrcType");
                if(!(srcType.equals("IM") || srcType.equals("IMInit")))
                {
                    //only parse im type
                    return list;
                }
                JSONArray bodes = jobj.getJSONArray("Body");
                for (int j =0; j < bodes.length(); j++ )
                {
                    IMEntity entity = new IMEntity();
                    JSONObject jData = bodes.getJSONObject(i);
                    Log.d("testRoomMessageIndex","index:"+i);

                    int uid = jData.getInt("Sender");

                    if(schema.getLocal().getUid() == uid)
                    {
                        entity.setUserEntity(schema.getLocal());
                    }
                    else{
                        entity.setUserEntity(schema.getTarget());
                    }
                    entity.setMessage(jData.getString("Message"));
                    entity.setSendTime(jData.getString("TimeStamp"));
                    entity.setLocal(uid == schema.local.getUid());
                    list.add(0,entity);
                }
            }
        }
        catch (Exception ex){
            Log.d("testRoomInit",ex.getLocalizedMessage());
        }
        return list;
    }

    public static void sendMessage(int localuid,int targetuid){

    }
}

