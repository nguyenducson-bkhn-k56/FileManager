/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Action;

import Dao.FileDao;
import Entity.FolderContent;
import com.sun.jersey.multipart.FormDataParam;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import json.JsonBase;

/**
 * REST Web Service
 *
 * @author nguyen
 */
@Path("FavouriteAction")
public class FavouriteAction {

    public static String root = "FavouriteAction";

    @POST
    @Path("/uploadFileFavourite")
    @Consumes("binary/octet-stream")
    public Response upload(@FormDataParam("file") InputStream fileInputStream
    ) {
        try {
            File tempFile = File.createTempFile("tempFile", ".tmp");
            writeToFile(fileInputStream, tempFile.getAbsolutePath());
            Response result = Response.status(200).entity(tempFile.getAbsolutePath()).build();
//        fileDao.addNewFile(parentPath,name,fileType);
            return result;
        } catch (IOException ex) {
            ex.printStackTrace();
            return Response.status(Constant.Constant.EROR).entity("loi up file").build();
        }

    }

    @POST
    @Path("/moveFileFavoriteToFolder")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Integer moveFileToDes(@FormParam("absolutePath") String absolutePath, @FormParam("parentPath") String parentPath, @FormParam("fileName") String fileName, @FormParam("fileType") String fileType) {
        FileDao fileDao = new FileDao();
        File fileTemp = new File(absolutePath);
        String newFilePath = JsonBase.pathFolderRoot + parentPath.replace("root/", "") + "/" + fileName + fileType;
        File newFile = new File(newFilePath);
        try {
            InputStream inStream = null;
            OutputStream outStream = null;
            inStream = new FileInputStream(fileTemp);
            outStream = new FileOutputStream(newFile);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0) {

                outStream.write(buffer, 0, length);

            }
            inStream.close();
            outStream.close();

            //delete the original file
            fileTemp.delete();
            newFile.setExecutable(true);
            newFile.setReadable(true);
            newFile.setWritable(true);

            System.out.println("File is copied successful!");

            if (fileDao.addNewFile(parentPath, fileName, ".docx")) {
                return Constant.Constant.NORMAL;
            } else {
                return Constant.Constant.EROR;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Constant.Constant.EROR;
    }

    private void writeToFile(InputStream uploadedInputStream,
            String uploadedFileLocation) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File(
                    uploadedFileLocation));
            int read = 0;
            byte[] bytes = new byte[1024];
            File file = new File(uploadedFileLocation);
            out = new FileOutputStream(file);
            int length;
            while ((read = uploadedInputStream.read(bytes)) > 0) {
                out.write(bytes, 0, read);
            }

        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }

    }

    // ham download file 
    @POST
    @Path("/downloadFile")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response downloadFile(@FormParam("parentPath") String parentPath, @FormParam("name") String name, @FormParam("idUser") String idUser, @FormParam("fileType") String fileType) {
        String filePath = Constant.Constant.FAVOUR_ROOT_FOLDER_PATH + "/" + parentPath.replace("root", idUser) + "/" + name + fileType;
        File file = new File(filePath);
        Response.ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename=newfile.zip");
        return response.build();

    }

    /***
     * 
     * @param userId
     * @return 
     */
    @POST
    @Path("/getListFileFavourite")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getListFileFavourite(@FormParam("userId") String userId) {

        try {
            String filePath = Constant.Constant.FAVOUR_ROOT_FOLDER_PATH + "/" + userId + "/" + Constant.Constant.FILE_CONFIG;
            File fileConfig = new File(filePath);
            if (!fileConfig.exists()) {
                return Response.status(Constant.Constant.EROR_FOLDER_FILE_NOT_EXIST).build();
            }
            FolderContent folderContent = (FolderContent) JsonBase.readFileJson(fileConfig, FolderContent.class);
            if (folderContent != null) {
                return Response.status(Constant.Constant.NORMAL).entity(JsonBase.generateJSONBase(folderContent)).build();
            }
            return Response.status(Constant.Constant.EROR).build();

        } catch (Exception ex) {
            return Response.status(Constant.Constant.EROR).build();
        }
    }
}
