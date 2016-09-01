/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import javax.ws.rs.FormParam;

/**
 *
 * @author nguyen
 */
public class UserController {
    public Integer logIn(@FormParam("userCode") String userCode, @FormParam("passWord") String passWord){
        try{
            
            return Constant.Constant.NORMAL;
        }catch(Exception ex){
            ex.printStackTrace();
            return Constant.Constant.EROR;
        }
    }
}
