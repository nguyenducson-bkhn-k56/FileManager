/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Entity.ListUser;
import Entity.UserEntity;
import java.io.File;
import java.util.ArrayList;
import json.JsonBase;

/**
 *
 * @author nguyen
 */
public class UserDao {

    public UserEntity checkUser(String userCode, String pass) {
        
        File fileUser = new File(Constant.Constant.FOLDER_PATH_HW + "/" + Constant.Constant.ROOT_FOLDER_FAVOURITE_NAME + "/" + "fileUser.txt");
        ListUser lstUser = (ListUser) JsonBase.readFileJson(fileUser, ListUser.class);
        UserEntity userNeedFind = null;
        for (UserEntity userEntity : lstUser.getListUser()) {
            if (userEntity.getUserCode().equals(userCode)) {
                userNeedFind = userEntity;
            }
        }

        if (!userNeedFind.getPassWord().equals(pass)) {
            userNeedFind = null;
        }
        return userNeedFind;
    }

//   public static void main(String arg[]) {
//        try {
//            new UserDao().checkUser("ducson", "hoalan93");
//        } catch (Exception ex) {
//        }
//    }
    
    public boolean create(){
        try{
            ArrayList<UserEntity> lstUser = new ArrayList<UserEntity>();
            UserEntity userEntity = new UserEntity();
            userEntity.setUserName("ducson");
            userEntity.setUserCode("ducson");
            userEntity.setPassWord("hoalan93");
            userEntity.setUserId(123456);
            userEntity.setRole(false);
            lstUser.add(userEntity);

            userEntity = new UserEntity();
            userEntity.setUserName("bachBui");
            userEntity.setUserCode("bachBui");
            userEntity.setPassWord("1");
            userEntity.setUserId(123454);
            userEntity.setRole(false);
            lstUser.add(userEntity);

            userEntity = new UserEntity();
            userEntity.setUserName("sonDang");
            userEntity.setUserCode("sonDang");
            userEntity.setPassWord("1");
            userEntity.setUserId(123453);
            userEntity.setRole(false);
            lstUser.add(userEntity);

            userEntity = new UserEntity();
            userEntity.setUserName("binhMac");
            userEntity.setUserCode("binhMac");
            userEntity.setPassWord("1");
            userEntity.setUserId(123452);
            userEntity.setRole(false);
            lstUser.add(userEntity);
            
            
            userEntity = new UserEntity();
            userEntity.setUserName("admin");
            userEntity.setUserCode("admin");
            userEntity.setPassWord("1");
            userEntity.setUserId(989089);
            userEntity.setRole(true);
            lstUser.add(userEntity);

            userEntity = new UserEntity();
            userEntity.setUserName("admin2");
            userEntity.setUserCode("admin2");
            userEntity.setPassWord("1");
            userEntity.setUserId(452345);
            userEntity.setRole(true);
            lstUser.add(userEntity);
            
            userEntity = new UserEntity();
            userEntity.setUserName("user");
            userEntity.setUserCode("user");
            userEntity.setPassWord("1");
            userEntity.setUserId(198749);
            userEntity.setRole(true);
            lstUser.add(userEntity);
            
            ListUser listUser = new ListUser();
            listUser.setListUser(lstUser);
            File fileUser = new File(Constant.Constant.FOLDER_PATH_HW +"/" + Constant.Constant.ROOT_FOLDER_FAVOURITE_NAME + "/" + "fileUser.txt");
            if (fileUser.exists()) {
                fileUser.createNewFile();
            }
            JsonBase.writeFileJson(JsonBase.generateJSONBase(listUser), fileUser);
            return true;
        }catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
    }
    public static void main(String args[]) {
        try {
            ArrayList<UserEntity> lstUser = new ArrayList<UserEntity>();
            UserEntity userEntity = new UserEntity();
            userEntity.setUserName("ducson");
            userEntity.setUserCode("ducson");
            userEntity.setPassWord("hoalan93");
            userEntity.setUserId(123456);
            lstUser.add(userEntity);

            userEntity = new UserEntity();
            userEntity.setUserName("bachBui");
            userEntity.setUserCode("bachBui");
            userEntity.setPassWord("123456");
            userEntity.setUserId(123454);
            lstUser.add(userEntity);

            userEntity = new UserEntity();
            userEntity.setUserName("sonDang");
            userEntity.setUserCode("sonDang");
            userEntity.setPassWord("123456");
            userEntity.setUserId(123453);
            lstUser.add(userEntity);

            userEntity = new UserEntity();
            userEntity.setUserName("binhMac");
            userEntity.setUserCode("binhMac");
            userEntity.setPassWord("123456");
            userEntity.setUserId(123452);
            lstUser.add(userEntity);

            ListUser listUser = new ListUser();
            listUser.setListUser(lstUser);
            File fileUser = new File(Constant.Constant.FAVOUR_ROOT_FOLDER_PATH + "/" + "fileUser.txt");
            if (fileUser.exists()) {
                fileUser.createNewFile();
            }
            JsonBase.writeFileJson(JsonBase.generateJSONBase(listUser), fileUser);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    
}
