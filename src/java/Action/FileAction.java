/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Action;

import Constant.Constant;
import Dao.FileDao;
import Entity.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import json.JsonBase;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.mime.MultipartEntity;

/**
 * REST Web Service
 *
 * @author nguyen
 */
@Path("/fileManager")
public class FileAction {

    public static String root = "fileManager";

    @POST
    @Path("/addFolder")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addFolder(@FormParam(Constant.Param.PARENTPATH) String parentPath, @FormParam(Constant.Param.NAME) String name) {
        FileDao dao = new FileDao();
        if (dao.addNewFolder(parentPath, name)) {
            return Response.status(Constant.NORMAL).build();
        }
        return Response.status(Constant.EROR).build();
    }

    //xoa folder 
    @POST
    @Path("/deleteFolder")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Integer delFolder(@FormParam(Constant.Param.PARENTPATH) String parentPath, @FormParam(Constant.Param.NAME) String name) {
        FileDao dao = new FileDao();
        if (dao.delFolder(parentPath, name)) {
            return Constant.NORMAL;
        }
        return Constant.EROR;
    }
//
    // ham sua doi ten folder

    @POST
    @Path("/editFolderName")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Integer editFolderName(@FormParam(Constant.Param.PARENTPATH) String parentPath, @FormParam(Constant.Param.NAME) String name, @FormParam(Constant.Param.NEWNAME) String newName) {

        FileDao dao = new FileDao();
        if (dao.editFolder(parentPath, name, newName)) {
            return Constant.NORMAL;
        }
        return Constant.EROR;

    }

//    // ham tao folder
    @POST
    @Path("/delFile")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Integer removeFile(@FormParam(Constant.Param.PARENTPATH) String parentPath, @FormParam(Constant.Param.NAME) String name) {
        FileDao dao = new FileDao();
        if (dao.delFile(parentPath, name)) {
            return Constant.NORMAL;
        }
        return Constant.EROR;
    }
//
    // lay danh sach tat cac cac file

    @GET
    @Path("/getListAllFile")
    public Response getListAllFile() {
        FileDao fileDao = new FileDao();
        return Response.status(Constant.NORMAL).entity(fileDao.getListAllFile()).build();
    }
//
//    // thay doi ten file

    @POST
    @Path("/editFileName")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Integer editFileName(@FormParam(Constant.Param.PARENTPATH) String parentPath, @FormParam(Constant.Param.NAME) String name, @FormParam(Constant.Param.NEWNAME) String newName) {
        FileDao dao = new FileDao();
        if (dao.editFileName(parentPath, name, newName)) {
            return Constant.NORMAL;
        }
        return Constant.EROR;

    }

    // ham download file 
    @POST
    @Path("/downloadFile")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response downloadFile(@FormParam(Constant.Param.PARENTPATH) String parentPath, @FormParam(Constant.Param.NAME) String name) {
        String filePath = JsonBase.pathFolderRoot + parentPath.replace("root/", "") + "/" + name + ".docx";
        File file = new File(filePath);
        ResponseBuilder response = Response.ok((Object) file);
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
    @Path("/upload")
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
        FileDao fileDao = new FileDao();
        File fileTemp = new File(absolutePath);
        String fileType = FilenameUtils.getExtension(fileName);
        fileName = FilenameUtils.getBaseName(fileName);
        
        String newFilePath = JsonBase.pathFolderRoot + parentPath.replace("root/", "") + "/" + fileName +"." + fileType;
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

            if (fileDao.addNewFile(parentPath, fileName, fileType)) {
                return Response.status(Constant.NORMAL).build();
            } else {
                return Response.status(Constant.EROR).build();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Response.status(Constant.EROR).build();
    }

}
