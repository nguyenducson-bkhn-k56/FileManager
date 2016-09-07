/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constant;

/**
 *
 * @author nguyen
 */
public class Constant {
 
    // value return after call respone
    public static final int NORMAL = 208;
    public static final int EROR = 423;
    public static final int EXCEPTION = 433;
    public static final int EROR_FOLDER_FILE_EXIST = 422;
    public static final int EROR_FOLDER_FILE_NOT_EXIST = 446;
    public static final int NOT_FOUND_USER = 421;
    public static final int WRONG_PASS = 423;
    // value of Folder Favour
    public static final String FAVOUR_ROOT_FOLDER_PATH = "D:/favourite";
    public static final String NAME_ROOT_FOLDER = "root";
    public static final String FILE_CONFIG = "config.txt";
    public static final String PATH_FOLDER_TEMP_FILE = "D:/favourite/";
    public static final String NAME_FILE_ROOT_FAVOUR = "/favourite";
    public static final String NAME_FILE_ROOT_FOLDER = "/root";
    public static String FOLDER_PATH_HW = "";
    public static final String ROOT_FOLDER_NAME = "smartOFF";
    public static final String ROOT_FOLDER_FAVOURITE_NAME = "smartOFFFavourite";
    // user 
    public final  class Param{
        public Param(){
        }
        public static final String NAME = "name";
        public static final String FILENAME = "fileName";
        public static final String USERID = "userId";
        public static final String USERCODE = "userCode";
        public static final String PASSWORD = "password";
        public static final String FILETYPE = "fileType";
        public static final String PARENTPATH = "parentPath";
        public static final String ABSOLUTEPATH = "absolutePath";
        public static final String NEWNAME = "newName";
    }
}
