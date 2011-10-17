package com.foobnix.android.simple.core;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Environment;

import com.foobnix.commons.LOG;

public class FileItemProvider {
    private final static File ROOT_PATH = Environment.getExternalStorageDirectory();
    private final static FileItemComparator comparator = new FileItemComparator();
    private final static Mp3FileNameFilter filter = new Mp3FileNameFilter();
    
    public static List<FileItem> getFilesAndFoldersWithRoot(File rootPath) {
        List<FileItem> items = new ArrayList<FileItem>();
       
        if (!rootPath.equals(ROOT_PATH)) {
            items.add(new TopFileItem(rootPath.getParentFile()));
        }
        items.addAll(getFilesAndFoldersByPath(rootPath));
        return items;
        
    }

    public static List<FileItem> getFilesAndFoldersByPath(File rootPath) {
        final List<FileItem> items = new ArrayList<FileItem>();
        File[] listFiles = rootPath.listFiles(filter);
        if (listFiles == null) {
            return Collections.emptyList();
        }
        for (File file : listFiles) {
            items.add(new FileItem(file));
        }
        Collections.sort(items, comparator);
        return items;

    }

    public static List<FileItem> getFilesByPath(File rootPath) {
        LOG.d("get getFilesByPath", rootPath.getPath());

        final List<FileItem> items = new ArrayList<FileItem>();
        File[] listFiles = rootPath.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.isFile() && file.getName().toLowerCase().endsWith(".mp3");

            }
        });
        if (listFiles == null) {
            return Collections.emptyList();
        }
        for (File file : listFiles) {
            items.add(new FileItem(file));
        }
        Collections.sort(items, comparator);
        return items;

    }

}