/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.util.ArrayList;

/**
 *
 * @author nguyen
 */
public class FolderContent {
     // ten cua file 
    private String name;
    // id cua file
    private int id;
    // file path 
    private String path;
    // level cay, con muc 1 level 1, con muc 2 level 2, con muc 3 level 3
    private int level;
    // level cua folder
    private ArrayList<FileContent> listFiles;
    // danh sach cac folder 
    private ArrayList<FolderContent> listFolders;
    // path parent folder
    private String parentPath;

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<FileContent> getListFiles() {
        return listFiles;
    }

    public void setListFiles(ArrayList<FileContent> listFiles) {
        this.listFiles = listFiles;
    }

    public ArrayList<FolderContent> getListFolders() {
        return listFolders;
    }

    public void setListFolders(ArrayList<FolderContent> listFolders) {
        this.listFolders = listFolders;
    }


 
}
