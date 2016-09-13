/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Action;

import Controller.UserController;
import Dao.FileDao;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author nguyen
 */
@Path("user")
public class UserAction {

    public static String root = "UserAction";

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response logIn(@FormParam("userCode") String userCode, @FormParam("passWord") String passWord) {
        try {
            FileDao fileDao = new FileDao();
            fileDao.createFolderHW();
            UserController userController = new UserController();
            return userController.logIn(userCode, passWord);
        } catch (Exception ex) {

        }
        return Response.status(Constant.Constant.EROR).build();
    }

}
