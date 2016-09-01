/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Entity.UserEntity;
import java.io.File;
import java.util.ArrayList;
import json.JsonBase;

/**
 *
 * @author nguyen
 */
public class UserDao {
    public UserEntity checkUser(String userCode, String pass){
        ArrayList<UserEntity> lstUser = new ArrayList<UserEntity>();
        File fileUser = new File(Constant.Constant.FAVOUR_ROOT_FOLDER_PATH+"/"+"fileUser.txt");
        lstUser = JsonBase.readFileJson(fileUser);
    }
    
//    public static void main (String args[]){
//        try{
//        ArrayList<UserEntity> lstUser = new ArrayList<UserEntity>();
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUserName("ducson");
//        userEntity.setUserCode("ducson");
//        userEntity.setPassWord("hoalan93");
//        userEntity.setUserId(123456);
//        lstUser.add(userEntity);
//        
//        userEntity = new UserEntity();
//        userEntity.setUserName("bachBui");
//        userEntity.setUserCode("bachBui");
//        userEntity.setPassWord("123456");
//        userEntity.setUserId(123454);
//        lstUser.add(userEntity);
//        
//        userEntity = new UserEntity();
//        userEntity.setUserName("sonDang");
//        userEntity.setUserCode("sonDang");
//        userEntity.setPassWord("123456");
//        userEntity.setUserId(123453);
//        lstUser.add(userEntity);
//        
//        userEntity = new UserEntity();
//        userEntity.setUserName("binhMac");
//        userEntity.setUserCode("binhMac");
//        userEntity.setPassWord("123456");
//        userEntity.setUserId(123452);
//        lstUser.add(userEntity);
//       
//        
//        File fileUser = new File(Constant.Constant.FAVOUR_ROOT_FOLDER_PATH+"/"+"fileUser.txt");
//        if(fileUser.exists())
//            fileUser.createNewFile();
//        JsonBase.writeFileJson(JsonBase.generateJSONBase(lstUser), fileUser);
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//        
//        
//    }
}
