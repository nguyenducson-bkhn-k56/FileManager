/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Constant.Constant;
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

    public boolean login(String userName, String pass) {
        try {
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // ham tao mot folder favorite
    public Integer createFolderFavorite(String parentPath, int  userId) {
        try {
            String hwarePath = getPathHW(userId);
            File folderNew = new File(hwarePath);
            if (folderNew.exists()) {
                return Constant.NORMAL;
            }
            if (!folderNew.mkdirs()) {
                return Constant.EROR;
            }
            File fileConfig = new File(hwarePath + "/" + Constant.FILE_CONFIG);
            if (!fileConfig.createNewFile()) {
                return Constant.EXCEPTION;
            }
            FolderContent folder = new FolderContent();
            folder.setLevel(1);
            folder.setName(String.valueOf(userId));
            folder.setPath("");
            folder.setParentPath("");
            folder.setListFiles(new ArrayList<FileContent>());
            if (JsonBase.writeFileJson(JsonBase.generateJSONBase(folder), fileConfig)) {
                return Constant.NORMAL;
            } else {
                fileConfig.delete();
                folderNew.delete();
                return Constant.EROR;
            }
        } catch (Exception ex) {

        }
        return Constant.EROR;
    }

    public String getPathHW(int idUser) {
        int numberFolderLevel1 = idUser / Constant.TreeFavourite.LEVEL1MAX;
        int value = idUser % Constant.TreeFavourite.LEVEL1MAX;
        int numberFolderLevel2 = value / Constant.TreeFavourite.LEVEL2MAX;
        value = value % Constant.TreeFavourite.LEVEL2MAX;
        String labelFolderLevel1 = String.valueOf(numberFolderLevel1 * Constant.TreeFavourite.LEVEL1MAX) + "_" + String.valueOf((numberFolderLevel1 + 1) * (Constant.TreeFavourite.LEVEL1MAX) - 1);
        String labelFolderLevel2 = String.valueOf(numberFolderLevel1 * Constant.TreeFavourite.LEVEL1MAX + numberFolderLevel2 * Constant.TreeFavourite.LEVEL2MAX) + "_" + String.valueOf(numberFolderLevel1 * Constant.TreeFavourite.LEVEL1MAX + (numberFolderLevel2 + 1) * (Constant.TreeFavourite.LEVEL2MAX) - 1);
        String pathFile = Constant.FOLDER_PATH_HW+"/"+ Constant.ROOT_FOLDER_FAVOURITE_NAME + "/" + Constant.ROOT_FOLDER_FAVOURITE_NAME;
        return pathFile + "/" + labelFolderLevel1 + "/" + labelFolderLevel2 + "/" + idUser;
    }
    
}
