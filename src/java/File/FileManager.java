/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package File;

import Dao.FileDao;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ContentDisposition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import json.JsonBase;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 * REST Web Service
 *
 * @author nguyen
 */
@Path("/fileManager")
public class FileManager {

    public static String root = "fileManager";

//    //xoa folder 
//    @DELETE
//    @Path("/deleteFolder")
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    public String delFolder(@FormParam("parentPath") String parentPath, @FormParam("name") String name) {
//        FileManagerController controller = new FileManagerController();
//        return controller.removeFolder(parentPath, name);
//    }
//
    // ham sua doi ten folder
//    @POST
//    @Path("/editFolderName")
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    public String editFolderName(@FormParam("parentPath") String parentPath, @FormParam("name") String name, @FormParam("newName") String newName) {
//        
//        return controller.editFolderName(parentPath, oldName, newName);
//
//    }
//
//    // ham xoa folder
//    @POST
//    @Path("/removeFolder")
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    public String removeFolder(@FormParam("data") String data, @Context HttpServletResponse servletResponse) {
//        FileManagerController controller = new FileManagerController();
//        return null;
//    }
//
//    // ham tao folder
    @POST
    @Path("/addFolder")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String addFolder(@FormParam("parentPath") String parentPath, @FormParam("name") String name) {
        FileDao dao = new FileDao();
        if (dao.addNewFolder(parentPath, name)) {
            return "200";
        }
        return "404";
    }
//
    // lay danh sach tat cac cac file

    @GET
    @Path("/getListAllFile")
    public String getListAllFile() {
        FileDao fileDao = new FileDao();
        return fileDao.getListAllFile();
    }
//
//    // thay doi ten file
//    @POST
//    @Path("/editFileName")
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    public String editFileName(@FormParam("data") String data) {
//        FileManagerController controller = new FileManagerController();
//        return controller.editFileName(data);
//
//    }

    // ham download file 
    @POST
    @Path("/downloadFile")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response downloadFile(@FormParam("parentPath") String parentPath, @FormParam("name") String name) {
        String filePath = JsonBase.pathFolderRoot + parentPath.replace("root/", "") + "/" + name + ".docx";
        File file = new File(filePath);
        ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename=newfile.zip");
        return response.build();

    }

    @POST
    @Path("/uploadFile")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormParam("parentPath") String parentPath ,
            @FormParam("name") String name,
            @FormParam("fileType") String fileType) {

        String uploadedFileLocation = JsonBase.pathRoot +"/" + parentPath.replace("root/", "") + "/" + name +".docx";
        FileDao fileDao = new FileDao();
        // save it
        writeToFile(uploadedInputStream, uploadedFileLocation);
        String output = "File uploaded to : " + uploadedFileLocation;
        
        Response result = Response.status(200).entity(output).build();
        fileDao.addNewFile(parentPath,name,fileType);
        return result;
    }

    // save uploaded file to new location
    private void writeToFile(InputStream uploadedInputStream,
            String uploadedFileLocation) {

        try {
            OutputStream out = new FileOutputStream(new File(
                    uploadedFileLocation));
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

//    @POST
//    @Path("/uploadFile")
//    @Consumes("multipart/form-data")
//    public Response uploadFile(MultipartFormDataInput input) throws IOException {
//        //Get API input data
//        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
//
//        //Get file name
//        String fileName = uploadForm.get("fileName").get(0).getBodyAsString();
//
//        //Get parentPath
//        String parentPath = uploadForm.get("parentPath").get(1).getBodyAsString();
//
//        //Get file data to save
//        List<InputPart> inputParts = uploadForm.get("attachment");
//
//        for (InputPart inputPart : inputParts) {
//            try {
//                //Use this header for extra processing if required
//                @SuppressWarnings("unused")
//                MultivaluedMap<String, String> header = inputPart.getHeaders();
//
//                // convert the uploaded file to inputstream
//                InputStream inputStream = inputPart.getBody(InputStream.class, null);
//
//                byte[] bytes = IOUtils.toByteArray(inputStream);
//                // constructs upload file path
//                fileName = JsonBase.pathRoot + "/" + parentPath.replace("root/", "") + "/" + fileName;
//                writeFile(bytes, fileName);
//                System.out.println("Success !!!!!");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return Response.status(200)
//                .entity("Uploaded file name : " + fileName).build();
//
//    }
//
//    //Utility method
//    private void writeFile(byte[] content, String filename) throws IOException {
//        File file = new File(filename);
//        if (!file.exists()) {
//            file.createNewFile();
//        }
//        FileOutputStream fop = new FileOutputStream(file);
//        fop.write(content);
//        fop.flush();
//        fop.close();
//    }

    
}
