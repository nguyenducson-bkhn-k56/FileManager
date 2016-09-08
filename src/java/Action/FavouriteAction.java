/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Action;

import Constant.Constant;
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
import org.apache.commons.io.FilenameUtils;

/**
 * REST Web Service
 *
 * @author nguyen
 */
@Path("FavouriteAction")
public class FavouriteAction {

    public static String root = "FavouriteAction";
    
    // upload file va ghi vao trang thai file tam
    @POST
    @Path("/uploadFileFavourite")
    @Consumes("binary/octet-stream")
    public Response upload(@FormDataParam("file") InputStream fileInputStream
    ) {
        try {
            File tempFile = File.createTempFile("tempFile", ".tmp");
            writeToFile(fileInputStream, tempFile.getAbsolutePath());
            Response result = Response.status(Constant.NORMAL).entity(tempFile.getAbsolutePath()).build();
            return result;
        } catch (IOException ex) {
            ex.printStackTrace();
            return Response.status(Constant.EROR).entity("loi up file").build();
        }
    }

    // chuyen file da upload vao dung folder cua nguoi su dung
    @POST
    @Path("/moveFileFavoriteToFolder")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response moveFileToDes(@FormParam(Constant.Param.ABSOLUTEPATH) String absolutePath, @FormParam(Constant.Param.PARENTPATH) String parentPath, @FormParam(Constant.Param.USERID) String userId, @FormParam(Constant.Param.FILENAME) String fileName, @FormParam(Constant.Param.FILETYPE) String fileType) {
        FileDao fileDao = new FileDao();
        File fileTemp = new File(absolutePath);
        fileType = FilenameUtils.getExtension(fileName);
        fileName = FilenameUtils.getBaseName(fileName);
        String newFilePath = Constant.FAVOUR_ROOT_FOLDER_PATH + parentPath.replace("root", "") + "/" + userId + "/" + fileName + "." + fileType;
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

            if (fileDao.addNewFileFavourite(fileName, fileType, new File(Constant.FAVOUR_ROOT_FOLDER_PATH + parentPath.replace("root", "") + "/" + userId + "/" + Constant.FILE_CONFIG))) {
                return Response.status(Constant.NORMAL).build();
            } else {
                return Response.status(Constant.EROR).build();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Response.status(Constant.EROR).build();
    }

    // ghi vao file tam 
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

    // OutsoureTeam-sonnd
    // download file 
    @POST
    @Path("/downloadFile")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response downloadFile(@FormParam(Constant.Param.ABSOLUTEPATH) String parentPath, @FormParam(Constant.Param.NAME) String name, @FormParam(Constant.Param.USERID) String idUser, @FormParam(Constant.Param.FILETYPE) String fileType) {
        String filePath = Constant.FAVOUR_ROOT_FOLDER_PATH + "/" + parentPath.replace("root/", "") + "/" + name + "." + fileType;
        File file = new File(filePath);
        if (!file.exists()) {
            return Response.status(Constant.EROR_FOLDER_FILE_NOT_EXIST).build();
        }
        Response.ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename=newfile.zip");
        return response.build();
    }

    /**
     * *
     *
     * @param userId
     * @return
     */
    @POST
    @Path("/getListFileFavourite")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getListFileFavourite(@FormParam("userId") String userId) {
        try {
            String filePath = Constant.FAVOUR_ROOT_FOLDER_PATH + "/" + userId + "/" + Constant.FILE_CONFIG;
            File fileConfig = new File(filePath);
            if (!fileConfig.exists()) {
                return Response.status(Constant.EROR_FOLDER_FILE_NOT_EXIST).build();
            }
            FolderContent folderContent = (FolderContent) JsonBase.readFileJson(fileConfig, FolderContent.class);
            if (folderContent != null) {
                return Response.status(Constant.NORMAL).entity(JsonBase.generateJSONBase(folderContent)).build();
            }
            return Response.status(Constant.EROR).build();

        } catch (Exception ex) {
            return Response.status(Constant.EROR).build();
        }
    }

    // xoa file favourite theo ten
    @POST
    @Path("/delFileFavourite")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response delFileFavourite(@FormParam("parentPath") String parentPath, @FormParam("userId") String userId, @FormParam("fileName") String fileName, @FormParam("fileType") String fileType) {
        try {
            FileDao dao = new FileDao();
            String filePath = Constant.FAVOUR_ROOT_FOLDER_PATH + "/" + userId + "/" + Constant.FILE_CONFIG;
            File fileConfig = new File(filePath);
            if (!fileConfig.exists()) {
                return Response.status(Constant.EROR_FOLDER_FILE_NOT_EXIST).build();
            }
            if (dao.delFileFavourite(parentPath, fileName, userId, fileConfig)) {
                return Response.status(Constant.NORMAL).build();
            }
        } catch (Exception ex) {
            return Response.status(Constant.EROR).build();
        }

        return Response.status(Constant.EROR).build();
    }

    /***
     * doi ten file favourite
     * @param parentPath
     * @param userId
     * @param fileName
     * @param newName
     * @param fileType
     * @return 
     */
    @POST
    @Path("/renameFile")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response renameFileFavourite(@FormParam(Constant.Param.PARENTPATH) String parentPath, @FormParam(Constant.Param.USERID) String userId, @FormParam(Constant.Param.FILENAME) String fileName, @FormParam(Constant.Param.NEWNAME) String newName, @FormParam(Constant.Param.FILETYPE) String fileType) {
        try {

            FileDao dao = new FileDao();
            String filePath = Constant.FAVOUR_ROOT_FOLDER_PATH + "/" + userId + "/" + Constant.FILE_CONFIG;
            File fileConfig = new File(filePath);
            if (!fileConfig.exists()) {
                return Response.status(Constant.EROR_FOLDER_FILE_NOT_EXIST).build();
            }
            dao.editFileFavourite(parentPath, fileName, newName, userId, fileConfig);
            return Response.status(Constant.NORMAL).build();
        } catch (Exception ex) {
            return Response.status(Constant.EROR).build();
        }
    }

}
