/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import Entity.FileContent;
import Entity.FolderContent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.catalina.WebResource;

/**
 *
 * @author nguyen
 */
public class JsonBase {

    public static String pathFile = "D:/folderTest/config.txt";
    public static String pathFolderRoot = "D:/folderTest/";
    public static String pathRoot = "root/";
    
    
    /**
     * convert data to Json
     *
     * @param obj
     * @return
     */
    public static String generateJSONBase(Object obj) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String data = null;
        try {
            data = gson.toJson(obj);
        } catch (Exception e) {
            data = null;
        }
        return data;
    }

    // ham ghi file voi gia tri json
    public static boolean writeFileJson(String json) {
        try {
            File file = new File(pathFile);
            FileWriter fileWrite = new FileWriter(file);
            fileWrite.write(json);
            fileWrite.flush();
            fileWrite.close();
            return true;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }

    }

    // ham doc file tra ve json
    public static FolderContent readFileJson() {
        try {
            FileReader fileReader = new FileReader(pathFile);
            StringBuilder data = new StringBuilder();
            Gson gson = new Gson();
            JsonReader jsonReader = new JsonReader(fileReader);
            FolderContent folderContent = (FolderContent) gson.fromJson(jsonReader, FolderContent.class);
            return folderContent;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

   
    
//  
//
//    public static void main(String args[]) {
//
////        FolderContent folderContent = new FolderContent();
////        folderContent.setId(1);
////        folderContent.setName("root");
////        folderContent.setPath(pathRoot);
////        folderContent.setParentPath(null);
////        folderContent.setLevel(0);
////        
////        FileContent fileContent = new FileContent();
////        fileContent.setId(2);
////        fileContent.setName("file1.txt");
////        fileContent.setUrl(pathRoot+fileContent.getName());
////        fileContent.setLevel(1);
////        fileContent.setParentPath(pathRoot);
////        
////        ArrayList<FileContent> fileContents = new ArrayList<FileContent>();
////        fileContents.add(fileContent);
////        folderContent.setListFiles(fileContents);
////        
////        FolderContent folderContent1 = new FolderContent();
////        folderContent1.setId(3);
////        folderContent1.setName("folder2");
////        folderContent1.setPath(pathRoot  +folderContent1.getName());
////        folderContent1.setLevel(1);
////        folderContent1.setParentPath(pathRoot);
////        
////        fileContent = new FileContent();
////        fileContent.setId(2);
////        fileContent.setName("file2.txt");
////        fileContent.setUrl(pathRoot+fileContent.getName()+"/");
////        fileContent.setParentPath(pathRoot+"folder2"+"/");
////        fileContent.setLevel(2);
////        
////        fileContents = new ArrayList<FileContent>();
////        fileContents.add(fileContent);
////        folderContent1.setListFiles(fileContents);
////        
////        ArrayList<FolderContent> folderContents = new ArrayList<FolderContent>();
////        folderContents.add(folderContent1);
////        
////        folderContent.setListFolders(folderContents);
////        
////        String a = JsonBase.generateJSONBase(folderContent);
////        System.out.println(a);
////        JsonBase.writeFileJson(a);
////        String json = "{\"name\":\"rootFolder\",\"Id\":\"1\",\"Path\":\"/rootFolder\",\"lstFile\":[{\"name\":\"file1.txt\",\"url\":\"/rootFolder/file1.txt\",\"Id\":\"2\"}],\"lstFolder\":[{\"name\":\"folder3\",\"Id\":\"3\",\"Path\":\"/rootFolder/folder3\"}]}";
////        Gson gson = new Gson();
////        FolderContent ab123 = (FolderContent) gson.fromJson(json, FolderContent.class);
////        System.out.println(ab123.getName());
////          FolderContent a = JsonBase.readFileJson();
////          System.out.println("asdfdf");
//         //JsonBase.addFolder("root/folder3", "folder10");
//        
//        
//    }

    
    // ham ghi file json vi tri m
    public static boolean writeFileJson(String json,File fileToWrite){
       try{
            FileWriter fileWrite = new FileWriter(fileToWrite);
            fileWrite.write(json);
            fileWrite.flush();
            fileWrite.close();
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
    
    // ham doc json cua file 
    public static FolderContent readFileJson(File file){
        try {
            FileReader fileReader = new FileReader(file.getAbsolutePath());
            StringBuilder data = new StringBuilder();
            Gson gson = new Gson();
            JsonReader jsonReader = new JsonReader(fileReader);
            FolderContent folderContent = (FolderContent) gson.fromJson(jsonReader, FolderContent.class);
            return folderContent;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static FolderContent readFileJson(File file, Class a){
        try {
            FileReader fileReader = new FileReader(file.getAbsolutePath());
            StringBuilder data = new StringBuilder();
            Gson gson = new Gson();
            JsonReader jsonReader = new JsonReader(fileReader);
            FolderContent folderContent = (FolderContent) gson.fromJson(jsonReader, FolderContent.class);
            return folderContent;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
