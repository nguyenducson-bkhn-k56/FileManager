/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Entity.FileContent;
import Entity.FolderContent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import json.JsonBase;

/**
 *
 * @author nguyen
 */
public class FavouriteDao {
    public boolean login(String userName,String pass){
        try{
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
    
    // ham tao mot folder favorite
    public Integer createFolderFavorite(String parentPath,String nameFile){
        try{           
            String hwarePath = Constant.Constant.FAVOUR_ROOT_FOLDER_PATH  +  parentPath.replace(Constant.Constant.NAME_ROOT_FOLDER, "") +"/" + nameFile;
            File folderNew= new File(hwarePath);
            if(folderNew.exists()){
                return Constant.Constant.NORMAL;
            }
            if(!folderNew.mkdir())
                return Constant.Constant.EROR;
            File fileConfig = new File(hwarePath+"/"+Constant.Constant.FILE_CONFIG);
            if(!fileConfig.createNewFile())
                return Constant.Constant.EXCEPTION;
            FolderContent folder = new FolderContent();
            folder.setLevel(1);
            folder.setName(nameFile);
            folder.setPath(Constant.Constant.NAME_ROOT_FOLDER+"/" + nameFile);
            folder.setParentPath(Constant.Constant.NAME_ROOT_FOLDER );
            folder.setListFiles(new ArrayList<FileContent>());
            if(JsonBase.writeFileJson(JsonBase.generateJSONBase(folder),fileConfig))
                return Constant.Constant.NORMAL;
            else {
                fileConfig.delete();
                folderNew.delete();
                return Constant.Constant.EROR;
            }
        }catch(Exception ex){
             
        }
        return Constant.Constant.EROR;
    }
    
    
}
