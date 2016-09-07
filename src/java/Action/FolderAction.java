/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Action;

import Constant.Constant;
import Dao.FileDao;
import java.io.File;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import json.JsonBase;

/**
 * REST Web Service
 *
 * @author nguyen
 */
@Path("manager/folder")
public class FolderAction {

    public static String ROOT = "manager/folderAction";

    @GET
    @Path("/getListData")
    public Response getListData() {
        try {
            FileDao fileDao = new FileDao();
            fileDao.createFolderHW();
            File fileConfig = new File(Constant.FOLDER_PATH_HW + "/" + Constant.ROOT_FOLDER_NAME + "/" + Constant.FILE_CONFIG);
            String result = JsonBase.generateJSONBase(JsonBase.readFileJson(fileConfig));
            return Response.status(Constant.NORMAL).entity(result).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Constant.EROR).build();
        }
    }

    /***
     * them file moi
     * @param parentPath
     * @param name
     * @return 
     */
    @POST
    @Path("/addFolder")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addFolder(@FormParam(Constant.Param.PARENTPATH) String parentPath, @FormParam(Constant.Param.NAME) String name) {

        try {
            FileDao dao = new FileDao();
            File fileConfig = new File(Constant.FOLDER_PATH_HW + "/" + Constant.ROOT_FOLDER_NAME + "/" + Constant.FILE_CONFIG);
            if (dao.addNewFolder(parentPath, name, fileConfig)) {
                return Response.status(Constant.NORMAL).build();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Response.status(Constant.EROR).build();
    }

}
