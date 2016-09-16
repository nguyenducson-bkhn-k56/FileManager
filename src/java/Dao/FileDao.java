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
import javax.swing.filechooser.FileSystemView;
import json.JsonBase;
import Constant.Constant;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author nguyen
 */
public class FileDao {

    /**
     *
     * @param parentPath
     * @param oldName
     * @param newName
     * @param file
     * @return
     */
    public boolean editFoldername(String parentPath, String oldName, String newName, File file) {
        try {

            String arrayFile[] = parentPath.replace("/", "-").split("-");
            FolderContent destinyFolderContent = null;
            FolderContent rootFolder;
            rootFolder = JsonBase.readFileJson(file);
            destinyFolderContent = findFolderByParentPath(parentPath, rootFolder);
            if (destinyFolderContent == null) {
                return false;
            }
            // tim file can edit
            FolderContent folderNeedEdit = null;
            for (FolderContent folder : destinyFolderContent.getListFolders()) {
                if (folder.getName().equals(oldName)) {
                    folderNeedEdit = folder;
                    break;
                }
            }
            if (folderNeedEdit == null) {
                return false;
            }

            String pathOldFolder = Constant.FOLDER_PATH_HW + "/" + folderNeedEdit.getParentPath().replace("root/", "").replace("/root", "").replace("root", "") + "/" + folderNeedEdit.getName();
            File oldFolder = new File(pathOldFolder);
            String pathNewFolder = Constant.FOLDER_PATH_HW + "/" + folderNeedEdit.getParentPath().replace("root/", "").replace("/root", "").replace("root", "") + "/" + newName;
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
            JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder), file);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * *
     * @author sonnd sua ten file
     * @param parentPath
     * @param oldName
     * @param newName
     * @param fileType
     * @param fileConfig
     * @return
     */
    public boolean editFileName(String parentPath, String oldName, String newName, String fileType, File fileConfig) {
        try {

            String arrayFile[] = parentPath.replace("/", "-").split("-");
            FolderContent destinyFolderContent = null;
            FolderContent rootFolder;
            rootFolder = JsonBase.readFileJson(fileConfig);
            destinyFolderContent = findFolderByParentPath(parentPath, rootFolder);
            if (destinyFolderContent == null) {
                return false;
            }
            // tim file can edit
            FileContent fileNeedEdit = null;
            ArrayList<FileContent> listFileTemp = destinyFolderContent.getListFiles();
            if (listFileTemp == null) {
                return false;
            }
            for (FileContent fileTemp : listFileTemp) {
                if (fileTemp.getName().equals(oldName) && fileTemp.getFileType().equals(fileType)) {
                    fileNeedEdit = fileTemp;
                    break;
                }
            }
            if (fileNeedEdit == null) {
                return false;
            }

            String pathOldFile = Constant.FOLDER_PATH_HW + "/" + fileNeedEdit.getParentPath().replace("root/", "").replace("/root", "").replace("root", "") + "/" + fileNeedEdit.getName() + "." + fileType;
            String pathNewFile = Constant.FOLDER_PATH_HW + "/" + fileNeedEdit.getParentPath().replace("root/", "").replace("/root", "").replace("root", "") + "/" + newName + "." + fileType;
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
     * *
     * them moi thu muc
     *
     * @param parentPath
     * @param folderName
     * @param file
     * @return
     */
    public boolean addNewFolder(String parentPath, String folderName, File file) {
        try {
            String arrayFile[] = parentPath.replace("/", "-").split("-");
            FolderContent tempFolderContent;
            FolderContent destinyFolderContent = null;
            FolderContent rootFolder;
            rootFolder = JsonBase.readFileJson(file);
            destinyFolderContent = findFolderByParentPath(parentPath, rootFolder);
            if (destinyFolderContent == null) {
                return false;
            }
            // tao folder moi
            String pathNewFolder = Constant.FOLDER_PATH_HW + "/" + destinyFolderContent.getPath().replace("root/", "").replace("/root", "").replace("root", "") + "/" + folderName;
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

    /**
     * **
     * them file moi
     *
     * @param parentPath
     * @param name
     * @param fileType
     * @param fileConfig
     * @return
     */
    public boolean addNewFile(String parentPath, String fileName, String fileType, File fileConfig) {
        try {

            String arrayFile[] = parentPath.replace("/", "-").split("-");
            FolderContent destinyFolderContent = null;
            FolderContent rootFolder;
            rootFolder = JsonBase.readFileJson(fileConfig);
            destinyFolderContent = findFolderByParentPath(parentPath, rootFolder);
            if (destinyFolderContent == null) {
                return false;
            }
            // tao file moi
            String pathNewFile = Constant.FOLDER_PATH_HW + "/" + destinyFolderContent.getPath().replace("root/", "").replace("/root", "").replace("root", "") + "/" + fileName + "." + fileType;
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
            JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder), fileConfig);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * *
     * xoa mot file
     *
     * @param parentPath
     * @param name
     * @param fileConfig
     * @return
     */
    public boolean delFile(String parentPath, String fileName, String fileType, File fileConfig) {
        try {
            FolderContent destinyFolderContent = null;
            FolderContent rootFolder;
            rootFolder = JsonBase.readFileJson(fileConfig);
            destinyFolderContent = findFolderByParentPath(parentPath, rootFolder);
            if (destinyFolderContent == null) {
                return false;
            }
            // tim file can edit
            FileContent fileNeedRemove = null;
            Integer posFileNeedRemove = 0;
            ArrayList<FileContent> listFileTemp = destinyFolderContent.getListFiles();
            fileNeedRemove = findFileByParentPath(fileName, fileType, destinyFolderContent, posFileNeedRemove);
            if (fileNeedRemove == null) {
                return false;
            }
            File file = new File(Constant.FOLDER_PATH_HW + "/" + fileNeedRemove.getParentPath().replace("root/", "").replace("/root", "").replace("root", "") + "/" + fileNeedRemove.getName() + "." + fileNeedRemove.getFileType());
            if (file.exists()) {
                if (!file.delete()) {
                    return false;
                }
            } else {
                return false;
            }
            destinyFolderContent.getListFiles().remove(posFileNeedRemove.intValue());
            JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder), fileConfig);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * @author sonnd xoa file
     * @param parentPath
     * @param name
     * @param file
     * @return
     */
    public boolean delFolder(String parentPath, String folderName, File fileConfig) {
        try {

            String arrayFile[] = parentPath.replace("/", "-").split("-");
            FolderContent tempFolderContent;
            FolderContent destinyFolderContent = null;
            FolderContent rootFolder;
            rootFolder = JsonBase.readFileJson(fileConfig);
            destinyFolderContent = findFolderByParentPath(parentPath, rootFolder);
            if (destinyFolderContent == null) {
                return false;
            }

            // tim folder can edit
            // tim folder can edit
            int posOfFolderNeedDelete = 0;
            FolderContent folderNeedRemove = null;
            for (FolderContent folder : destinyFolderContent.getListFolders()) {

                if (folder.getName() != null && folder.getName().equals(folderName)) {
                    folderNeedRemove = folder;
                    break;
                }
                posOfFolderNeedDelete++;
            }
            if (folderNeedRemove == null) {
                return false;
            }

            File folder = new File(Constant.FOLDER_PATH_HW + "/" + folderNeedRemove.getParentPath().replace(Constant.NAME_ROOT_FOLDER, "") + "/" + folderNeedRemove.getName());
            if (folder.exists()) {
                FileUtils.deleteDirectory(folder);
                if (folder.exists()) {
                    return false;
                }
            } else {
                return false;
            }
            destinyFolderContent.getListFolders().remove(posOfFolderNeedDelete);
            JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder), fileConfig);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * *
     * sonnd sua lai cau truc json sau khi thuc hien sua folder hoac file
     *
     * @param folder
     */
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

    /**
     * *
     *
     * @throws IOException
     */
    public boolean createFolderHW() throws IOException {
        if (Constant.FOLDER_PATH_HW.equals("")) {
            Constant.FOLDER_PATH_HW = FileSystemView.getFileSystemView().getDefaultDirectory().getAbsolutePath();
            File folderRoot = new File(Constant.FOLDER_PATH_HW + "/" + Constant.ROOT_FOLDER_NAME);
            File folderRootFavourite = new File(Constant.FOLDER_PATH_HW + "/" + Constant.ROOT_FOLDER_FAVOURITE_NAME);
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
                if (folderRootFavourite.mkdir()) {
                    UserDao userDao = new UserDao();
                    userDao.create();
                    return true;
                } else {
                    FileUtils.deleteDirectory(folderRoot);
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }

    }

    /**
     * *
     * @author sonnd chuyen file tu thu muc nay sang thu muc khac
     * @param pathParentSrcFile
     * @param pathParentDesFile
     * @param fileName
     * @param fileType
     * @param fileConfig
     * @return
     */
    public Integer moveFile(String pathParentSrcFile, String pathParentDesFile, String fileName, String fileType, File fileConfig) {
        String arrayFile[] = pathParentSrcFile.replace("/", "-").split("-");
        FolderContent parentFolderContentSrc = null;
        FolderContent parentFolderContentDes = null;
        FolderContent rootFolder;
        rootFolder = JsonBase.readFileJson(fileConfig);
        parentFolderContentSrc = findFolderByParentPath(pathParentSrcFile, rootFolder);
        parentFolderContentDes = findFolderByParentPath(pathParentDesFile, rootFolder);
        if (parentFolderContentSrc == null || parentFolderContentDes == null) {
            return Constant.EROR_FOLDER_FILE_NOT_EXIST;
        }

        // tim file can edit
        FileContent fileNeedMove = null;
        Integer posFileSrc = 0;
        fileNeedMove = findFileByParentPath(fileName, fileType, parentFolderContentSrc, posFileSrc);
        if (fileNeedMove == null) {
            return Constant.EROR_FOLDER_FILE_NOT_EXIST;
        }

        String pathHwSrcFile = Constant.FOLDER_PATH_HW + "/" + pathParentSrcFile.replace("root/", "").replace("/root", "").replace("root", "") + "/" + fileName + "." + fileType;
        String pathHwDesFile = Constant.FOLDER_PATH_HW + "/" + pathParentDesFile.replace("root/", "").replace("/root", "").replace("root", "") + "/" + fileName + "." + fileType;
        File srcFile = new File(pathHwSrcFile);
        File desFile = new File(pathHwDesFile);
        if (desFile.exists()) {
            return Constant.EROR_FOLDER_FILE_EXIST;
        } else {
            try {
                InputStream inStream = null;
                OutputStream outStream = null;
                inStream = new FileInputStream(srcFile);
                outStream = new FileOutputStream(desFile);

                byte[] buffer = new byte[1024];

                int length;
                //copy the file content in bytes
                while ((length = inStream.read(buffer)) > 0) {

                    outStream.write(buffer, 0, length);

                }
                inStream.close();
                outStream.close();

                //delete the original file
                //desFile.delete();
                FileContent fileContentNew = new FileContent();
                fileContentNew.setFileType(fileType);
                fileContentNew.setName(fileName);
                fileContentNew.setLevel(parentFolderContentDes.getLevel() + 1);
                fileContentNew.setParentPath(parentFolderContentDes.getPath());
                fileContentNew.setUrl(parentFolderContentDes.getPath() + "/" + fileName);

                ArrayList<FileContent> lstFilesContent = parentFolderContentDes.getListFiles();
                if (lstFilesContent != null) {
                    lstFilesContent.add(fileContentNew);
                } else {
                    lstFilesContent = new ArrayList<FileContent>();
                    lstFilesContent.add(fileContentNew);
                    parentFolderContentDes.setListFiles(lstFilesContent);
                }
                parentFolderContentSrc.getListFiles().remove(posFileSrc.intValue());
                if (JsonBase.writeFileJson(JsonBase.generateJSONBase(rootFolder), fileConfig)) {
                    srcFile.delete();
                    return Constant.NORMAL;
                }else{
                    desFile.delete();
                    return Constant.EROR;
                }

            } catch (IOException ex) {
                desFile.delete();
                return Constant.EROR;
            }
        }
    }

    /**
     * @author sonnd ham tim kiem file trong mot folderContent theo cau truc
     * json
     * @param parentPath
     * @param fileName
     * @param fileType
     * @param folderNeedFind
     * @param posOfFile
     * @return
     */
    public FileContent findFileByParentPath(String fileName, String fileType, FolderContent folderNeedFind, Integer posOfFile) {
        ArrayList<FileContent> listFileTemp = folderNeedFind.getListFiles();
        FileContent fileNeedFind = null;
        if (listFileTemp == null) {
            return null;
        }
        for (FileContent fileTemp : listFileTemp) {
            if (fileTemp.getName().equals(fileName) && fileTemp.getFileType().equals(fileType)) {
                fileNeedFind = fileTemp;
                break;
            }
            posOfFile++;
        }
        return fileNeedFind;
    }

    /**
     * **
     * @author sonnd ham tim kiem folderContent tu parentPath
     * @param parentPath
     * @param folderName
     * @param rootFolder
     * @return
     */
    public FolderContent findFolderByParentPath(String parentPath, FolderContent rootFolder) {
        String arrayFile[] = parentPath.replace("/", "-").split("-");
        FolderContent destinyFolderContent = null;
        ArrayList<FolderContent> listFolderContentTemp = new ArrayList<FolderContent>();
        listFolderContentTemp.add(rootFolder);
        boolean isFound = false;
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
        if (isFound) {
            return destinyFolderContent;
        } else {
            return null;
        }
    }
}
