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
import org.apache.commons.io.FileUtils;

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

    /****
     * 
     * @param parentPath
     * @param userId
     * @return 
     */
    // ham tao mot folder favorite
    public Integer createFolderFavorite(String parentPath, int userId) {
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
            folder.setPath(String.valueOf(userId));
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
        String pathFile = Constant.FOLDER_PATH_HW + "/" + Constant.ROOT_FOLDER_FAVOURITE_NAME + "/" + Constant.ROOT_FOLDER_FAVOURITE_NAME;
        return pathFile + "/" + labelFolderLevel1 + "/" + labelFolderLevel2 + "/" + idUser;
    }

    /**
     * *
     * them file moi vao favourite
     *
     * @param parentPath
     * @param nameFile
     * @param fileType
     * @param file
     * @return
     */
    public boolean addNewFileFavourite(String pathHw, String parentPath, String nameFile, String fileType, File file) {
        try {
            FolderContent rootFolder;
            boolean isFound = false; //
            boolean findRecursive = false;
            rootFolder = JsonBase.readFileJson(file);
            ArrayList<FolderContent> listFolderContentTemp = new ArrayList<FolderContent>();
            listFolderContentTemp.add(rootFolder);
            FolderContent destinyFolderContent = null;
            // tim ra folder trong parent
            if (parentPath != null && parentPath.equals("")) {
                isFound = true;
                destinyFolderContent = rootFolder;
            } else {
                findRecursive = true;
            }
            if (findRecursive) {
                String arrayFile[] = parentPath.replace("/", "-").split("-");
                for (String nameFolderTemp : arrayFile) {
                    for (FolderContent folder : listFolderContentTemp) {
                        if (nameFolderTemp.equals(folder.getName())) {
                            isFound = true;
                            destinyFolderContent = folder;
                            listFolderContentTemp = folder.getListFolders();
                            break;
                        } else {
                            isFound = false;
                        }

                    }
                }
            }

            if (!isFound) {
                return false;
            }

            String pathNewFile;
            if (parentPath == null || parentPath.equals("")) {
                pathNewFile = pathHw + "/" + nameFile + "." + fileType;
            } else {
                pathNewFile = pathHw + "/" + parentPath + "/" + nameFile + "." + fileType;
            }

//            String pathNewFile = Constant.FOLDER_PATH_HW + "/" + Constant.ROOT_FOLDER_FAVOURITE_NAME + "/" + rootFolder.getPath().replace("root/", "") + "/" + nameFile + "." + fileType;
            File newFile = new File(pathNewFile);
            if (!newFile.exists()) {
                return false;
            }
            FileContent fileContentNew = new FileContent();
            fileContentNew.setFileType(fileType);
            fileContentNew.setName(nameFile);
            fileContentNew.setLevel(rootFolder.getLevel() + 1);
            fileContentNew.setParentPath(rootFolder.getPath());
            fileContentNew.setUrl(rootFolder.getPath() + "/" + nameFile);

            ArrayList<FileContent> lstFilesContent = destinyFolderContent.getListFiles();
            if (lstFilesContent != null) {
                lstFilesContent.add(fileContentNew);
            } else {
                lstFilesContent = new ArrayList<FileContent>();
                lstFilesContent.add(fileContentNew);
                rootFolder.setListFiles(lstFilesContent);
            }
            JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder), file);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    
    /****
     * 
     * @param parentPath
     * @param name
     * @param userId
     * @param file
     * @return 
     */
    public boolean delFileFavourite(String parentPath, String name, String userId, File file) {
        try {

            FolderContent rootFolder;
            boolean isFound = false; //
            rootFolder = JsonBase.readFileJson(file);
            ArrayList<FolderContent> listFolderContentTemp = new ArrayList<FolderContent>();
            listFolderContentTemp.add(rootFolder);
            // tim ra folder trong parent
            isFound = true;

            // found file in list
            int positionTempFile = 0;
            FileContent fileContentNeedDel = null;
            ArrayList<FileContent> lstFileContent = rootFolder.getListFiles();
            for (FileContent tempFileContent : lstFileContent) {
                if (tempFileContent.getName().equals(name)) {
                    fileContentNeedDel = tempFileContent;
                    break;
                }
                positionTempFile++;
            }

            if (!isFound) {
                return false;
            }

            // tao folder moi
            // xoa file
            String filePathHW = Constant.FOLDER_PATH_HW + "/" + Constant.ROOT_FOLDER_FAVOURITE_NAME + "/" + fileContentNeedDel.getParentPath().replace(Constant.NAME_ROOT_FOLDER, "") + "/" + fileContentNeedDel.getName() + "." + fileContentNeedDel.getFileType();
            File fileHw = new File(filePathHW);
            if (fileHw.exists() && fileHw.delete()) {
                rootFolder.getListFiles().remove(positionTempFile);

                JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder), file);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /***
     * 
     * @param parentPath
     * @param name
     * @param newName
     * @param userId
     * @param file
     * @return 
     */
    public boolean editFileFavourite(String parentPath, String name, String newName, String userId, File file) {
        try {

            FolderContent rootFolder;
            boolean isFound = false; //
            rootFolder = JsonBase.readFileJson(file);
            ArrayList<FolderContent> listFolderContentTemp = new ArrayList<FolderContent>();
            listFolderContentTemp.add(rootFolder);
            // tim ra folder trong parent
            isFound = true;

            // found file in list
            Integer positionTempFile = 0;
            FileContent fileContentNeedEdit = null;
            ArrayList<FileContent> lstFileContent = rootFolder.getListFiles();
            for (FileContent tempFileContent : lstFileContent) {
                if (tempFileContent.getName().equals(name)) {
                    fileContentNeedEdit = tempFileContent;
                    break;
                }
                positionTempFile++;
            }

            if (!isFound) {
                return false;
            }

            // tao folder moi
            // xoa file
            String filePathHW = Constant.FOLDER_PATH_HW + "/" + Constant.ROOT_FOLDER_FAVOURITE_NAME + "/" + fileContentNeedEdit.getParentPath().replace(Constant.NAME_ROOT_FOLDER, "") + "/" + fileContentNeedEdit.getName() + "." + fileContentNeedEdit.getFileType();
            File fileHw = new File(filePathHW);
            String newFilePathHw = Constant.FOLDER_PATH_HW + "/" + Constant.ROOT_FOLDER_FAVOURITE_NAME + "/" + fileContentNeedEdit.getParentPath().replace(Constant.NAME_ROOT_FOLDER, "") + "/" + newName + "." + fileContentNeedEdit.getFileType();
            if (fileHw.exists() && fileHw.renameTo(new File(newFilePathHw))) {
                fileContentNeedEdit.setName(newName);
                fileContentNeedEdit.setUrl(fileContentNeedEdit.getParentPath() + "/" + newName + "." + fileContentNeedEdit.getFileType());
                JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder), file);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * **
     *
     * @param pathHW
     * @param parentPath
     * @param nameFolder
     * @param fileConfig
     * @return
     */
    public boolean createNewFolder(String pathHW, String parentPath, String nameFolder, File fileConfig, int userId) {
        try {
            FolderContent rootFolder;
            boolean isFound = false; //
            boolean findRecursive = false;
            rootFolder = JsonBase.readFileJson(fileConfig);
            if(rootFolder==null)
                return false;
            ArrayList<FolderContent> listFolderContentTemp = new ArrayList<FolderContent>();
            listFolderContentTemp.add(rootFolder);
            FolderContent destinyFolderContent = null;
            // tim ra folder trong parent
            // kiem tra xem co can dung de quy de tim kiem folder cha khong
            if (parentPath != null && parentPath.equals("")) {
                isFound = true;
                destinyFolderContent = rootFolder;
            } else {
                findRecursive = true;
            }
            // su dung de quy de tim kiem folder cha
            if (findRecursive) {
                String arrayFile[] = parentPath.replace("/", "-").split("-");
                for (String nameFolderTemp : arrayFile) {
                    for (FolderContent folder : listFolderContentTemp) {
                        if (nameFolderTemp.equals(folder.getName())) {
                            isFound = true;
                            destinyFolderContent = folder;
                            listFolderContentTemp = folder.getListFolders();
                            break;
                        } else {
                            isFound = false;
                        }

                    }
                }
            }

            // kiem tra xem co tim thay folder cha khong, neu khong thi bo qua
            if (!isFound) {
                return false;
            }
            // tao duong dan cho folder moi
            String pathNewFile;
            if (parentPath == null || parentPath.equals("")) {
                pathNewFile = pathHW + "/" + nameFolder;
            } else {
                parentPath = parentPath.replaceFirst(String.valueOf(userId)+"/", "");
                pathNewFile = pathHW + "/" + parentPath + "/" + nameFolder;
            }

            // kiem tra folder da ton tai chua, neu roi tra ve false, khong thi
            // tao folder tuong ung
            File folder = new File(pathNewFile);
            if (!folder.exists()) {
               if(!folder.mkdir())
                   return false;
            } else {
                return false;
            }
            // lay list folder 
            ArrayList<FolderContent> listFolder = destinyFolderContent.getListFolders();
            if (listFolder == null) {
                listFolder = new ArrayList<>();
            }
            FolderContent newFolderContent = new FolderContent();
            newFolderContent.setLevel(destinyFolderContent.getLevel() + 1);
            newFolderContent.setListFiles(null);
            newFolderContent.setListFolders(null);
            newFolderContent.setName(nameFolder);
            newFolderContent.setParentPath(destinyFolderContent.getPath());
            newFolderContent.setPath(destinyFolderContent.getPath() + "/" + nameFolder);
            listFolder.add(newFolderContent);
            destinyFolderContent.setListFolders(listFolder);

            // ghi file config cho jsonbase
            JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder), fileConfig);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    
    
    /**
     * **
     *
     * @param pathHW
     * @param parentPath
     * @param nameFolder
     * @param fileConfig
     * @return
     */
    public boolean delFolder(String pathHW, String parentPath, String nameFolder, File fileConfig) {
        try {
            FolderContent rootFolder;
            boolean isFound = false; //
            boolean findRecursive = false;
            rootFolder = JsonBase.readFileJson(fileConfig);
            ArrayList<FolderContent> listFolderContentTemp = new ArrayList<FolderContent>();
            listFolderContentTemp.add(rootFolder);
            FolderContent destinyFolderContent = null;
            // tim ra folder trong parent
            // kiem tra xem co can dung de quy de tim kiem folder cha khong
            if (parentPath != null && parentPath.equals("")) {
                isFound = true;
                destinyFolderContent = rootFolder;
            } else {
                findRecursive = true;
            }
            // su dung de quy de tim kiem folder cha
            if (findRecursive) {
                String arrayFile[] = parentPath.replace("/", "-").split("-");
                for (String nameFolderTemp : arrayFile) {
                    for (FolderContent folder : listFolderContentTemp) {
                        if (nameFolderTemp.equals(folder.getName())) {
                            isFound = true;
                            destinyFolderContent = folder;
                            listFolderContentTemp = folder.getListFolders();
                            break;
                        } else {
                            isFound = false;
                        }

                    }
                }
            }

            // kiem tra xem co tim thay folder cha khong, neu khong thi bo qua
            if (!isFound) {
                return false;
            }
            // tao duong dan cho folder moi
            String pathNewFile;
            if (parentPath == null || parentPath.equals("")) {
                pathNewFile = pathHW + "/" + nameFolder;
            } else {
                pathNewFile = pathHW + "/" + parentPath + "/" + nameFolder;
            }

            // kiem tra folder da ton tai chua, neu roi tra ve false, khong thi
            // tao folder tuong ung
            File folder = new File(pathNewFile);
            if (!folder.exists()) {
                return false;
            } else {
                FileUtils.deleteDirectory(folder);
                if(folder.exists())
                    return false;
            }
            // lay list folder, xoa folder da chon khoi list forder 
            ArrayList<FolderContent> listFolder = destinyFolderContent.getListFolders();
            if(listFolder==null)
                return false;
            int numberFolder=0;
            for(FolderContent folderContent: listFolder){
                if(folderContent.getName().equals(nameFolder))
                {
                    break;
                }
                numberFolder++;
            }
            listFolder.remove(numberFolder);
            // ghi file config cho jsonbase
            JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder), fileConfig);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
