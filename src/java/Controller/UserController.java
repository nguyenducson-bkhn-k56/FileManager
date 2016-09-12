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
import javax.ws.rs.core.Response;
import json.JsonBase;

/**
 *
 * @author nguyen
 */
public class UserController {

    // login and create file 
    public Response logIn(@FormParam("userCode") String userCode, @FormParam("passWord") String passWord) {
        try {
            UserDao userDao = new UserDao();
            UserEntity user = userDao.checkUser(userCode, passWord);
            if (user == null) {
                return Response.status(Constant.Constant.WRONG_PASS).build();
            }
            FavouriteDao fDao = new FavouriteDao();
            if (fDao.createFolderFavorite(Constant.Constant.NAME_ROOT_FOLDER, user.getUserId())==Constant.Constant.NORMAL) {
                user.setParentPath(Constant.Constant.NAME_ROOT_FOLDER);
                user.setFolderName(user.getUserId().toString());
                return Response.status(Constant.Constant.NORMAL).entity(JsonBase.generateJSONBase(user)).build();
            } else {
                return Response.status(Constant.Constant.EROR).build();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Constant.Constant.EROR).build();
        }
    }
}
