/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Action;

import Constant.Constant;
import Dao.FileDao;
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
@Path("manager/file")
public class FileAction {

    public static String ROOT = "manager/file";

    @POST
    @Path("/uploadFile")
    @Consumes("binary/octet-stream")
    public Response upload(@FormDataParam("file") InputStream fileInputStream
    ) {
        Response result;
        try {
            File tempFile = File.createTempFile("tempFile", ".tmp");
            writeToFile(fileInputStream, tempFile.getAbsolutePath());
            result = Response.status(Constant.NORMAL).entity(tempFile.getAbsolutePath()).build();

        } catch (IOException ex) {
            return Response.status(Constant.EROR).entity("loi tao file").build();
        }
//        fileDao.addNewFile(parentPath,name,fileType);
        return result;
    }

    @POST
    @Path("/moveFileToDes")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response moveFileToDes(@FormParam(Constant.Param.ABSOLUTEPATH) String absolutePath, @FormParam(Constant.Param.PARENTPATH) String parentPath, @FormParam(Constant.Param.FILENAME) String fileName) {

        File fileConfig = new File(Constant.FOLDER_PATH_HW + "/" + Constant.ROOT_FOLDER_NAME + "/" + Constant.FILE_CONFIG);

        FileDao fileDao = new FileDao();
        File fileTemp = new File(absolutePath);
        String fileType = FilenameUtils.getExtension(fileName);
        fileName = FilenameUtils.getBaseName(fileName);

        String newFilePath = Constant.FOLDER_PATH_HW + "/" + parentPath.replace("root/", "").replace("/root", "").replace("root", "") + "/" + fileName + "." + fileType;
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

            if (fileDao.addNewFile(parentPath, fileName, fileType, fileConfig)) {
                return Response.status(Constant.NORMAL).build();
            } else {
                return Response.status(Constant.EROR).build();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Response.status(Constant.EROR).build();
    }

    // ham download file 
    @POST
    @Path("/downloadFile")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response downloadFile(@FormParam(Constant.Param.PARENTPATH) String parentPath, @FormParam(Constant.Param.NAME) String name, @FormParam(Constant.Param.FILETYPE) String fileType) {
        String filePath = Constant.FOLDER_PATH_HW + "/" + parentPath.replace("root/", "").replace("/root", "").replace("root", "") + "/" + name + "." + fileType;
        File file = new File(filePath);
        Response.ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename=newfile.zip");
        return response.build();
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

    @POST
    @Path("/editFileName")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Integer editFileName(@FormParam(Constant.Param.PARENTPATH) String parentPath, @FormParam(Constant.Param.NAME) String name, @FormParam(Constant.Param.NEWNAME) String newName) {
        File fileConfig = new File(Constant.FOLDER_PATH_HW + "/" + Constant.ROOT_FOLDER_NAME + "/" + Constant.FILE_CONFIG);
        FileDao dao = new FileDao();
        if (dao.editFileName(parentPath, name, newName, fileConfig)) {
            return Constant.NORMAL;
        }
        return Constant.EROR;

    }

    @POST
    @Path("/delFile")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Integer removeFile(@FormParam(Constant.Param.PARENTPATH) String parentPath, @FormParam(Constant.Param.NAME) String name) {
        FileDao dao = new FileDao();
        File fileConfig = new File(Constant.FOLDER_PATH_HW + "/" + Constant.ROOT_FOLDER_NAME + "/" + Constant.FILE_CONFIG);

        if (dao.delFile(parentPath, name)) {
            return Constant.NORMAL;
        }
        return Constant.EROR;
    }
}
