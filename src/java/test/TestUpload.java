/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;


import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import java.io.File;
import java.io.IOException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
/**
 *
 * @author nguyen
 */
public class TestUpload {

    public static void main(String[] args) throws IOException {
//        final Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
//
//        final FileDataBodyPart filePart = new FileDataBodyPart("file", new File("D:/test.docx"));
//        FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
//        final FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart.field("foo", "bar").bodyPart(filePart);
//
//        final WebTarget target = client.target("http://localhost:8084/FileManager/webresources/fileManager/upload");
//        final Response response = target.request().post(Entity.entity(multipart, multipart.getMediaType()));
//
//    //Use response object to verify upload success
//        formDataMultiPart.close();
//        multipart.close();
    }
}
