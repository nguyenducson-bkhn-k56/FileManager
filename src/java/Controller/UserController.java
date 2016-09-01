/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Dao.FavouriteDao;
import Dao.UserDao;
import Entity.UserEntity;
import javax.ws.rs.FormParam;
import json.JsonBase;

/**
 *
 * @author nguyen
 */
public class UserController {

    public String logIn(@FormParam("userCode") String userCode, @FormParam("passWord") String passWord) {
        try {
            UserDao userDao = new UserDao();
            UserEntity user = userDao.checkUser(userCode, passWord);
            if (user == null) {
                return String.valueOf(Constant.Constant.WRONG_PASS);
            }
            FavouriteDao fDao = new FavouriteDao();
            if (fDao.createFolderFavorite(Constant.Constant.NAME_ROOT_FOLDER, user.getUserId().toString())==1) {
                user.setParentPath(Constant.Constant.NAME_ROOT_FOLDER);
                user.setFolderName(user.getUserId().toString());
                return JsonBase.generateJSONBase(user);
            } else {
                return String.valueOf(Constant.Constant.EROR);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return String.valueOf(Constant.Constant.EROR);
        }
    }
}
