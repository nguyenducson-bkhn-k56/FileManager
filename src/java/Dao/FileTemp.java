/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Entity.FileContent;
import Entity.FolderContent;
import java.io.File;
import java.util.ArrayList;
import json.JsonBase;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author nguyen
 */
public class FileTemp {

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

    public String getListAllFile() {
        FolderContent content = JsonBase.readFileJson();
        return json.JsonBase.generateJSONBase(content);
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
}
