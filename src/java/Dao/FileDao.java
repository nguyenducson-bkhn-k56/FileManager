/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Entity.FileContent;
import Entity.FolderContent;
import java.io.File;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import javax.swing.filechooser.FileSystemView;
import json.JsonBase;
import Constant.Constant;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author nguyen
 */
public class FileDao {

    public String getListAllFile() {
        FolderContent content = JsonBase.readFileJson();
        return json.JsonBase.generateJSONBase(content);
    }

    public boolean editFolder(String parentPath, String oldName, String newName) {
        try {

            String arrayFile[] = parentPath.replace("/", "-").split("-");
            FolderContent tempFolderContent;
            FolderContent destinyFolderContent = null;
            FolderContent rootFolder;
            boolean isFound = false; //
            rootFolder = JsonBase.readFileJson();
            ArrayList<FolderContent> listFolderContentTemp = new ArrayList<FolderContent>();
            listFolderContentTemp.add(rootFolder);
            // tim ra folder trong parent
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

            if (!isFound) {
                return false;
            }

            // tim file can edit
            FolderContent folderNeedEdit = null;
            for (FolderContent folder : listFolderContentTemp) {
                if (folder.getName().equals(oldName)) {
                    folderNeedEdit = folder;
                    break;
                }
            }
            if (folderNeedEdit == null) {
                return false;
            }
            folderNeedEdit.setName(newName);
            updateFileAfterEdit(folderNeedEdit);
            JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean editFolder(String parentPath, String oldName, String newName, File file) {
        try {
            
            String arrayFile[] = parentPath.replace("/", "-").split("-");
            FolderContent tempFolderContent;
            FolderContent destinyFolderContent = null;
            FolderContent rootFolder;
            boolean isFound = false; //
            rootFolder = JsonBase.readFileJson(file);
            ArrayList<FolderContent> listFolderContentTemp = new ArrayList<FolderContent>();
            listFolderContentTemp.add(rootFolder);
            // tim ra folder trong parent
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

            if (!isFound) {
                return false;
            }

            // tim file can edit
            FolderContent folderNeedEdit = null;
            for (FolderContent folder : listFolderContentTemp) {
                if (folder.getName().equals(oldName)) {
                    folderNeedEdit = folder;
                    break;
                }
            }
            if (folderNeedEdit == null) {
                return false;
            }
            
            String pathOldFolder = Constant.FOLDER_PATH_HW+"/" + folderNeedEdit.getParentPath().replace("root/", "").replace("/root", "").replace("root", "") + "/" + folderNeedEdit.getName();
            File oldFolder = new File(pathOldFolder);
            String pathNewFolder = Constant.FOLDER_PATH_HW+"/" + folderNeedEdit.getParentPath().replace("root/", "").replace("/root", "").replace("root", "") + "/" + newName;
            File newFolder = new File(pathNewFolder);
           
            if (!oldFolder.exists()) {
                return false;
            } else {
                if (!oldFolder.renameTo(newFolder)) {
                    return false;
                }
            }
            folderNeedEdit.setName(newName);
            updateFileAfterEdit(folderNeedEdit);
            JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder),file);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

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
            String filePathHW = Constant.FAVOUR_ROOT_FOLDER_PATH + fileContentNeedEdit.getParentPath().replace(Constant.NAME_ROOT_FOLDER, "") + "/" + fileContentNeedEdit.getName() + "." + fileContentNeedEdit.getFileType();
            File fileHw = new File(filePathHW);
            String newFilePathHw = Constant.FAVOUR_ROOT_FOLDER_PATH + fileContentNeedEdit.getParentPath().replace(Constant.NAME_ROOT_FOLDER, "") + "/" + newName + "." + fileContentNeedEdit.getFileType();
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

    public boolean editFileName(String parentPath, String oldName, String newName) {
        try {

            String arrayFile[] = parentPath.replace("/", "-").split("-");
            FolderContent tempFolderContent;
            FolderContent destinyFolderContent = null;
            FolderContent rootFolder;
            boolean isFound = false; //
            rootFolder = JsonBase.readFileJson();
            ArrayList<FolderContent> listFolderContentTemp = new ArrayList<FolderContent>();
            listFolderContentTemp.add(rootFolder);
            // tim ra folder trong parent
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
                if (fileTemp.getName().equals(oldName)) {
                    fileNeedEdit = fileTemp;
                    break;
                }
            }
            if (fileNeedEdit == null) {
                return false;
            }
            fileNeedEdit.setName(newName);
            fileNeedEdit.setUrl(destinyFolderContent.getPath() + "/" + newName);
            JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    
    public boolean editFileName(String parentPath, String oldName, String newName,File fileConfig) {
        try {

            String arrayFile[] = parentPath.replace("/", "-").split("-");
            FolderContent tempFolderContent;
            FolderContent destinyFolderContent = null;
            FolderContent rootFolder;
            boolean isFound = false; //
            rootFolder = JsonBase.readFileJson(fileConfig);
            ArrayList<FolderContent> listFolderContentTemp = new ArrayList<FolderContent>();
            listFolderContentTemp.add(rootFolder);
            // tim ra folder trong parent
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
                if (fileTemp.getName().equals(oldName)) {
                    fileNeedEdit = fileTemp;
                    break;
                }
            }
            if (fileNeedEdit == null) {
                return false;
            }
            
            String pathOldFile =  Constant.FOLDER_PATH_HW+"/" + fileNeedEdit.getParentPath().replace("root/", "").replace("/root", "").replace("root", "") + "/" + fileNeedEdit.getName() + "." + fileNeedEdit.getFileType();
            String pathNewFile = Constant.FOLDER_PATH_HW+"/" + fileNeedEdit.getParentPath().replace("root/", "").replace("/root", "").replace("root", "") + "/" + newName + "." + fileNeedEdit.getFileType();
            File fileOld = new File(pathOldFile);
            File fileNew = new File(pathNewFile);
            if(fileOld.exists())
            {
                if(!fileOld.renameTo(fileNew))
                    return false;
            }
            else return false;
            
            fileNeedEdit.setName(newName);
            fileNeedEdit.setUrl(destinyFolderContent.getPath() + "/" + newName);
            JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder),fileConfig);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean addNewFolder(String parentPath, String folderName) {
        try {

            String arrayFile[] = parentPath.replace("/", "-").split("-");
            FolderContent tempFolderContent;
            FolderContent destinyFolderContent = null;
            FolderContent rootFolder;
            boolean isFound = false; //
            rootFolder = JsonBase.readFileJson();
            ArrayList<FolderContent> listFolderContentTemp = new ArrayList<FolderContent>();
            listFolderContentTemp.add(rootFolder);
            // tim ra folder trong parent
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

            if (!isFound) {
                return false;
            }

            // tao folder moi
            String pathNewFolder = JsonBase.pathFolderRoot + destinyFolderContent.getPath().replace("root/", "") + "/" + folderName;
            File newFolder = new File(pathNewFolder);
            if (newFolder.exists()) {
                return false;
            } else {
                if (!newFolder.mkdir()) {
                    return false;
                }
            }
            FolderContent folderContentNew = new FolderContent();
            folderContentNew.setLevel(destinyFolderContent.getLevel() + 1);
            folderContentNew.setName(folderName);
            folderContentNew.setParentPath(destinyFolderContent.getPath());
            folderContentNew.setPath(destinyFolderContent.getPath() + "/" + folderName);
            ArrayList<FolderContent> lstFolderContent = destinyFolderContent.getListFolders();
            if (lstFolderContent != null) {
                lstFolderContent.add(folderContentNew);
            } else {
                lstFolderContent = new ArrayList<FolderContent>();
                lstFolderContent.add(folderContentNew);
                destinyFolderContent.setListFolders(lstFolderContent);
            }
            JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean addNewFolder(String parentPath, String folderName, File file) {
        try {
            String arrayFile[] = parentPath.replace("/", "-").split("-");
            FolderContent tempFolderContent;
            FolderContent destinyFolderContent = null;
            FolderContent rootFolder;
            boolean isFound = false; //
            rootFolder = JsonBase.readFileJson(file);
            ArrayList<FolderContent> listFolderContentTemp = new ArrayList<FolderContent>();
            listFolderContentTemp.add(rootFolder);
            // tim ra folder trong parent
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

            if (!isFound) {
                return false;
            }

            // tao folder moi
            String pathNewFolder = Constant.FOLDER_PATH_HW+"/" + destinyFolderContent.getPath().replace("root/", "").replace("/root", "").replace("root", "") + "/" + folderName;
            File newFolder = new File(pathNewFolder);
            if (newFolder.exists()) {
                return false;
            } else {
                if (!newFolder.mkdir()) {
                    return false;
                }
            }
            FolderContent folderContentNew = new FolderContent();
            folderContentNew.setLevel(destinyFolderContent.getLevel() + 1);
            folderContentNew.setName(folderName);
            folderContentNew.setParentPath(destinyFolderContent.getPath());
            folderContentNew.setPath(destinyFolderContent.getPath() + "/" + folderName);
            ArrayList<FolderContent> lstFolderContent = destinyFolderContent.getListFolders();
            if (lstFolderContent != null) {
                lstFolderContent.add(folderContentNew);
            } else {
                lstFolderContent = new ArrayList<FolderContent>();
                lstFolderContent.add(folderContentNew);
                destinyFolderContent.setListFolders(lstFolderContent);
            }
            JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder), file);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean addNewFile(String parentPath, String name, String fileType) {
        try {

            String arrayFile[] = parentPath.replace("/", "-").split("-");
            FolderContent tempFolderContent;
            FolderContent destinyFolderContent = null;
            FolderContent rootFolder;
            boolean isFound = false; //
            rootFolder = JsonBase.readFileJson();
            ArrayList<FolderContent> listFolderContentTemp = new ArrayList<FolderContent>();
            listFolderContentTemp.add(rootFolder);
            // tim ra folder trong parent
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

            if (!isFound) {
                return false;
            }

            // tao folder moi
            String pathNewFile = JsonBase.pathFolderRoot + destinyFolderContent.getPath().replace("root/", "") + "/" + name + "." + fileType;
            File newFile = new File(pathNewFile);
            if (!newFile.exists()) {
                return false;
            }
            FileContent fileContentNew = new FileContent();
            fileContentNew.setFileType(fileType);
            fileContentNew.setName(name);
            fileContentNew.setLevel(destinyFolderContent.getLevel() + 1);
            fileContentNew.setParentPath(destinyFolderContent.getPath());
            fileContentNew.setUrl(destinyFolderContent.getPath() + "/" + name);

            ArrayList<FileContent> lstFilesContent = destinyFolderContent.getListFiles();
            if (lstFilesContent != null) {
                lstFilesContent.add(fileContentNew);
            } else {
                lstFilesContent = new ArrayList<FileContent>();
                lstFilesContent.add(fileContentNew);
                destinyFolderContent.setListFiles(lstFilesContent);
            }
            JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    
    /****
     * them file moi 
     * @param parentPath
     * @param name
     * @param fileType
     * @param fileConfig
     * @return 
     */
    public boolean addNewFile(String parentPath, String name, String fileType, File fileConfig) {
        try {

            String arrayFile[] = parentPath.replace("/", "-").split("-");
            FolderContent tempFolderContent;
            FolderContent destinyFolderContent = null;
            FolderContent rootFolder;
            boolean isFound = false; //
            rootFolder = JsonBase.readFileJson(fileConfig);
            ArrayList<FolderContent> listFolderContentTemp = new ArrayList<FolderContent>();
            listFolderContentTemp.add(rootFolder);
            // tim ra folder trong parent
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

            if (!isFound) {
                return false;
            }

            // tao file moi
            String pathNewFile = Constant.FOLDER_PATH_HW+"/" + destinyFolderContent.getPath().replace("root/", "").replace("/root", "").replace("root", "") + "/" + name + "." + fileType;
            File newFile = new File(pathNewFile);
            if (!newFile.exists()) {
                return false;
            }
            FileContent fileContentNew = new FileContent();
            fileContentNew.setFileType(fileType);
            fileContentNew.setName(name);
            fileContentNew.setLevel(destinyFolderContent.getLevel() + 1);
            fileContentNew.setParentPath(destinyFolderContent.getPath());
            fileContentNew.setUrl(destinyFolderContent.getPath() + "/" + name);

            ArrayList<FileContent> lstFilesContent = destinyFolderContent.getListFiles();
            if (lstFilesContent != null) {
                lstFilesContent.add(fileContentNew);
            } else {
                lstFilesContent = new ArrayList<FileContent>();
                lstFilesContent.add(fileContentNew);
                destinyFolderContent.setListFiles(lstFilesContent);
            }
            JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder),fileConfig);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    

    public boolean addNewFileFavourite(String nameFile, String fileType, File file) {
        try {
            FolderContent rootFolder;
            boolean isFound = false; //
            rootFolder = JsonBase.readFileJson(file);
            ArrayList<FolderContent> listFolderContentTemp = new ArrayList<FolderContent>();
            listFolderContentTemp.add(rootFolder);
            // tim ra folder trong parent
            isFound = true;

            if (!isFound) {
                return false;
            }

            // tao folder moi
            String pathNewFile = Constant.FAVOUR_ROOT_FOLDER_PATH + "/" + rootFolder.getPath().replace("root/", "") + "/" + nameFile + "." + fileType;
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

            ArrayList<FileContent> lstFilesContent = rootFolder.getListFiles();
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
            String filePathHW = Constant.FAVOUR_ROOT_FOLDER_PATH + fileContentNeedDel.getParentPath().replace(Constant.NAME_ROOT_FOLDER, "") + "/" + fileContentNeedDel.getName() + "." + fileContentNeedDel.getFileType();
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

    public boolean delFile(String parentPath, String name, File file) {
        try {

            String arrayFile[] = parentPath.replace("/", "-").split("-");
            FolderContent tempFolderContent;
            FolderContent destinyFolderContent = null;
            FolderContent rootFolder;
            boolean isFound = false; //
            rootFolder = JsonBase.readFileJson(file);
            ArrayList<FolderContent> listFolderContentTemp = new ArrayList<FolderContent>();
            listFolderContentTemp.add(rootFolder);
            // tim ra folder trong parent
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

            if (!isFound) {
                return false;
            }

            // tim file can edit
            FileContent fileNeedRemove = null;
            int posFileNeedRemove = 0;
            ArrayList<FileContent> listFileTemp = destinyFolderContent.getListFiles();
            if (listFileTemp == null) {
                return false;
            }
            for (FileContent fileTemp : listFileTemp) {
                if (fileTemp.getName().equals(name)) {
                    fileNeedRemove = fileTemp;
                    break;
                }
                posFileNeedRemove++;
            }
            if (fileNeedRemove == null) {
                return false;
            }

            File file = new File(JsonBase.pathRoot + "/" + fileNeedRemove.getParentPath().replace("root/", "") + "/" + fileNeedRemove.getName());
            if (file.exists()) {
                if (!file.delete()) {
                    return false;
                }
            } else {
                return false;
            }
            destinyFolderContent.getListFiles().remove(posFileNeedRemove);
            JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder),file);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    
    public boolean delFile(String parentPath, String name) {
        try {

            String arrayFile[] = parentPath.replace("/", "-").split("-");
            FolderContent tempFolderContent;
            FolderContent destinyFolderContent = null;
            FolderContent rootFolder;
            boolean isFound = false; //
            rootFolder = JsonBase.readFileJson();
            ArrayList<FolderContent> listFolderContentTemp = new ArrayList<FolderContent>();
            listFolderContentTemp.add(rootFolder);
            // tim ra folder trong parent
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

            if (!isFound) {
                return false;
            }

            // tim file can edit
            FileContent fileNeedRemove = null;
            int posFileNeedRemove = 0;
            ArrayList<FileContent> listFileTemp = destinyFolderContent.getListFiles();
            if (listFileTemp == null) {
                return false;
            }
            for (FileContent fileTemp : listFileTemp) {
                if (fileTemp.getName().equals(name)) {
                    fileNeedRemove = fileTemp;
                    break;
                }
                posFileNeedRemove++;
            }
            if (fileNeedRemove == null) {
                return false;
            }

            File file = new File(JsonBase.pathRoot + "/" + fileNeedRemove.getParentPath().replace("root/", "") + "/" + fileNeedRemove.getName());
            if (file.exists()) {
                if (!file.delete()) {
                    return false;
                }
            } else {
                return false;
            }
            destinyFolderContent.getListFiles().remove(posFileNeedRemove);
            JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean delFolder(String parentPath, String name) {
        try {

            String arrayFile[] = parentPath.replace("/", "-").split("-");
            FolderContent tempFolderContent;
            FolderContent destinyFolderContent = null;
            FolderContent rootFolder;
            boolean isFound = false; //
            rootFolder = JsonBase.readFileJson();
            ArrayList<FolderContent> listFolderContentTemp = new ArrayList<FolderContent>();
            listFolderContentTemp.add(rootFolder);
            // tim ra folder trong parent
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

            if (!isFound) {
                return false;
            }

            // tim folder can edit
            // tim folder can edit
            int posOfFolderNeedDelete = 0;
            FolderContent folderNeedRemove = null;
            for (FolderContent folder : listFolderContentTemp) {

                if (folder.getName() != null && folder.getName().equals(name)) {
                    folderNeedRemove = folder;
                    break;
                }
                posOfFolderNeedDelete++;
            }
            if (folderNeedRemove == null) {
                return false;
            }

            File folder = new File(JsonBase.pathFolderRoot + "/" + folderNeedRemove.getParentPath().replace("root/", "") + "/" + folderNeedRemove.getName());
            if (folder.exists()) {
                FileUtils.deleteDirectory(folder);
//                if (!folder.delete()) {
//                    return false;
//                }
                if (folder.exists()) {
                    return false;
                }
            } else {
                return false;
            }
            destinyFolderContent.getListFolders().remove(posOfFolderNeedDelete);
            JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    
    public boolean delFolder(String parentPath, String name, File file) {
        try {

            String arrayFile[] = parentPath.replace("/", "-").split("-");
            FolderContent tempFolderContent;
            FolderContent destinyFolderContent = null;
            FolderContent rootFolder;
            boolean isFound = false; //
            rootFolder = JsonBase.readFileJson(file);
            ArrayList<FolderContent> listFolderContentTemp = new ArrayList<FolderContent>();
            listFolderContentTemp.add(rootFolder);
            // tim ra folder trong parent
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

            if (!isFound) {
                return false;
            }

            // tim folder can edit
            // tim folder can edit
            int posOfFolderNeedDelete = 0;
            FolderContent folderNeedRemove = null;
            for (FolderContent folder : listFolderContentTemp) {

                if (folder.getName() != null && folder.getName().equals(name)) {
                    folderNeedRemove = folder;
                    break;
                }
                posOfFolderNeedDelete++;
            }
            if (folderNeedRemove == null) {
                return false;
            }

            File folder = new File(Constant.FOLDER_PATH_HW+"/" + folderNeedRemove.getParentPath().replace(Constant.NAME_ROOT_FOLDER, "") + "/" + folderNeedRemove.getName());
            if (folder.exists()) {
                FileUtils.deleteDirectory(folder);
                if (folder.exists()) {
                    return false;
                }
            } else {
                return false;
            }
            destinyFolderContent.getListFolders().remove(posOfFolderNeedDelete);
            JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder),file);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void updateFileAfterEdit(FolderContent folder) {
        folder.setPath(folder.getParentPath() + "/" + folder.getName());
        ArrayList<FolderContent> listFolders = folder.getListFolders();
        ArrayList<FileContent> listFile = folder.getListFiles();
        if (listFolders != null && listFolders.size() > 0) {
            for (FolderContent tempFolder : listFolders) {
                tempFolder.setParentPath(folder.getPath());
                tempFolder.setPath(folder.getPath() + "/" + tempFolder.getName());
                updateFileAfterEdit(tempFolder);
            }
        }
        if (listFile != null && listFile.size() > 0) {
            for (FileContent tempFile : listFile) {
                tempFile.setParentPath(folder.getPath());
                tempFile.setUrl(folder.getPath() + "/" + tempFile.getName());
            }
        }
    }

    public void createFolderHW() throws IOException {
        if (Constant.FOLDER_PATH_HW.equals("")) {
            Constant.FOLDER_PATH_HW = FileSystemView.getFileSystemView().getDefaultDirectory().getAbsolutePath();
            File folderRoot = new File(Constant.FOLDER_PATH_HW + "/" + Constant.ROOT_FOLDER_NAME);
            if (!folderRoot.exists()) {
                if (folderRoot.mkdir()) {
                    System.out.println(Constant.FOLDER_PATH_HW + "/" + Constant.ROOT_FOLDER_NAME + "/" + Constant.FILE_CONFIG);
                    FolderContent root = new FolderContent();
                    root.setLevel(0);
                    root.setId(0);
                    root.setName(Constant.ROOT_FOLDER_NAME);
                    root.setPath(Constant.ROOT_FOLDER_NAME);
                    root.setParentPath("/");
                    File fileConfig = new File(Constant.FOLDER_PATH_HW + "/" + Constant.ROOT_FOLDER_NAME + "/" + Constant.FILE_CONFIG);
                    fileConfig.createNewFile();
                    JsonBase.writeFileJson(JsonBase.generateJSONBase(root), fileConfig);

                }
            }

        }

    }

    public static void main(String args[]) throws IOException {
      //  FileDao.createFolder();

//                   File file = new File("C:\\Users\\nguyen\\Documents/smartOFF/config.txt");
//                   System.out.println(Constant.FOLDER_PATH_HW);
//                   System.out.println(file.exists());
    }
}
