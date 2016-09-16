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

    /**
     * **
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
    public boolean addNewFileFavourite(String pathHw, String parentPath, String fileName, String fileType, int userId, File file) {
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
                pathNewFile = pathHw + "/" + fileName + "." + fileType;
            } else {
                if (parentPath.contains(String.valueOf(userId) + "/")) {
                    pathNewFile = pathHw + "/" + parentPath.replaceFirst(String.valueOf(userId) + "/", "") + "/" + fileName + "." + fileType;
                } else {
                    pathNewFile = pathHw + "/" + parentPath.replaceFirst(String.valueOf(userId), "") + "/" + fileName + "." + fileType;
                }
            }

//            String pathNewFile = Constant.FOLDER_PATH_HW + "/" + Constant.ROOT_FOLDER_FAVOURITE_NAME + "/" + rootFolder.getPath().replace("root/", "") + "/" + nameFile + "." + fileType;
            File newFile = new File(pathNewFile);
            if (!newFile.exists()) {
                return false;
            }
            FileContent fileContentNew = new FileContent();
            fileContentNew.setFileType(fileType);
            fileContentNew.setName(fileName);
            fileContentNew.setLevel(destinyFolderContent.getLevel() + 1);
            fileContentNew.setParentPath(destinyFolderContent.getPath());
            fileContentNew.setUrl(destinyFolderContent.getPath() + "/" + fileName);

            ArrayList<FileContent> lstFilesContent = destinyFolderContent.getListFiles();
            if (lstFilesContent != null) {
                lstFilesContent.add(fileContentNew);
            } else {
                lstFilesContent = new ArrayList<FileContent>();
                lstFilesContent.add(fileContentNew);
                destinyFolderContent.setListFiles(lstFilesContent);
            }
            JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder), file);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * **
     *
     * @param parentPath
     * @param name
     * @param userId
     * @param file
     * @return
     */
    public boolean delFileFavourite(String pathHW,String parentPath, String fileName,String fileType, int userId, File fileConfig) {
         try {
            FolderContent rootFolder;
            boolean isFound = false; //
            boolean findRecursive = false;
            rootFolder = JsonBase.readFileJson(fileConfig);
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

            // tim file can edit
            FileContent fileNeedEdit = null;
            int numberOfFile = 0;
            ArrayList<FileContent> listFileTemp = destinyFolderContent.getListFiles();
            if (listFileTemp == null) {
                return false;
            }
            for (FileContent fileTemp : listFileTemp) {
                if (fileTemp.getName().equals(fileName) && fileTemp.getFileType().equals(fileType)) {
                    fileNeedEdit = fileTemp;
                    break;
                }
                numberOfFile++;
            }
            if (fileNeedEdit == null) {
                return false;
            }

            // thay doi file name
            String pathOldFile=null;
            if (parentPath == null || parentPath.equals("")) {
                pathOldFile = pathHW + "/" + fileName + "." + fileType;
            } else {
                if (parentPath.contains(String.valueOf(userId) + "/")) {
                    pathOldFile = pathHW + "/" + parentPath.replaceFirst(String.valueOf(userId) + "/", "") + "/" + fileName + "." + fileType;
               } else {
                    pathOldFile = pathHW + "/" + parentPath.replaceFirst(String.valueOf(userId), "") + "/" + fileName + "." + fileType;
                }
            }
           
            File fileOld = new File(pathOldFile);
            if (fileOld.exists()) {
                if (!fileOld.delete()) {
                    return false;
                }
            } else {
                return false;
            }

            listFileTemp.remove(numberOfFile);
            destinyFolderContent.setListFiles(listFileTemp);
            JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder), fileConfig);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * *
     *
     * @param parentPath
     * @param name
     * @param newName
     * @param userId
     * @param file
     * @return
     */
    public boolean editFileFavourite(String pathHw, String parentPath, String fileName, String newName, String fileType, int userId, File fileConfig) {
        try {
            FolderContent rootFolder;
            boolean isFound = false; //
            boolean findRecursive = false;
            rootFolder = JsonBase.readFileJson(fileConfig);
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

            // tim file can edit
            FileContent fileNeedEdit = null;
            ArrayList<FileContent> listFileTemp = destinyFolderContent.getListFiles();
            if (listFileTemp == null) {
                return false;
            }
            for (FileContent fileTemp : listFileTemp) {
                if (fileTemp.getName().equals(fileName) && fileTemp.getFileType().equals(fileType)) {
                    fileNeedEdit = fileTemp;
                    break;
                }
            }
            if (fileNeedEdit == null) {
                return false;
            }

            // thay doi file name
            String pathOldFile=null;
            String pathNewFile=null ;
            if (parentPath == null || parentPath.equals("")) {
                pathNewFile = pathHw + "/" + fileName + "." + fileType;
            } else {
                if (parentPath.contains(String.valueOf(userId) + "/")) {
                    pathOldFile = pathHw + "/" + parentPath.replaceFirst(String.valueOf(userId) + "/", "") + "/" + fileName + "." + fileType;
                    pathNewFile = pathHw + "/" + parentPath.replaceFirst(String.valueOf(userId) + "/", "") + "/" + newName + "." + fileType;
                } else {
                    pathOldFile = pathHw + "/" + parentPath.replaceFirst(String.valueOf(userId), "") + "/" + fileName + "." + fileType;
                    pathNewFile = pathHw + "/" + parentPath.replaceFirst(String.valueOf(userId), "") + "/" + newName + "." + fileType;
                }
            }
           
            File fileOld = new File(pathOldFile);
            File fileNew = new File(pathNewFile);
            if (fileOld.exists()) {
                if (!fileOld.renameTo(fileNew)) {
                    return false;
                }
            } else {
                return false;
            }

            fileNeedEdit.setName(newName);
            fileNeedEdit.setUrl(destinyFolderContent.getPath() + "/" + newName);
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
    public boolean createNewFolder(String pathHW, String parentPath, String nameFolder, File fileConfig, int userId) {
        try {
            FolderContent rootFolder;
            boolean isFound = false; //
            boolean findRecursive = false;
            rootFolder = JsonBase.readFileJson(fileConfig);
            if (rootFolder == null) {
                return false;
            }
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
                if (parentPath.contains(String.valueOf(userId) + "/")) {
                    parentPath = parentPath.replaceFirst(String.valueOf(userId) + "/", "");
                } else {
                    parentPath = parentPath.replaceFirst(String.valueOf(userId), "");
                }
                if (!parentPath.isEmpty()) {
                    pathNewFile = pathHW + "/" + parentPath + "/" + nameFolder;
                } else {
                    pathNewFile = pathHW + "/" + nameFolder;
                }
            }

            // kiem tra folder da ton tai chua, neu roi tra ve false, khong thi
            // tao folder tuong ung
            File folder = new File(pathNewFile);
            if (!folder.exists()) {
                if (!folder.mkdir()) {
                    return false;
                }
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
    public boolean delFolder(String pathHW, String parentPath, String nameFolder, int userId, File fileConfig) {
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
                if (parentPath.contains(String.valueOf(userId) + "/")) {
                    parentPath = parentPath.replaceFirst(String.valueOf(userId) + "/", "");
                } else {
                    parentPath = parentPath.replaceFirst(String.valueOf(userId), "");
                }
                pathNewFile = pathHW + "/" + parentPath + "/" + nameFolder;
            }

            // kiem tra folder da ton tai chua, neu roi tra ve false, khong thi
            // tao folder tuong ung
            File folder = new File(pathNewFile);
            if (!folder.exists()) {
                return false;
            } else {
                FileUtils.deleteDirectory(folder);
                if (folder.exists()) {
                    return false;
                }
            }
            // lay list folder, xoa folder da chon khoi list forder 
            ArrayList<FolderContent> listFolder = destinyFolderContent.getListFolders();
            if (listFolder == null) {
                return false;
            }
            int numberFolder = 0;
            for (FolderContent folderContent : listFolder) {
                if (folderContent.getName().equals(nameFolder)) {
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
