/**
 * @author Tshiamo
 */
public class FileSystemManager {

    private Folder root;

    public FileSystemManager() {
        this.root = new Folder("root", null);
    }

    public Folder getRoot() {
        return root;
    }

    // Methods for managing file

    /**
     * Creates a new file in the file system.
     * If this file is not already assigned a parent, it's parent
     * will be set to the root folder.
     * @param f the File object representing the file to be created.
     */
    public void createFile(File f) {
        if (f == null) {
            throw new IllegalArgumentException("File parameter cannot be null.");
        }
        File file = new File(f);
        Folder fileParentFolder = file.getParent();
        if(fileParentFolder != null ){
            if(root.isParentOf(fileParentFolder)) {

                if (fileParentFolder.containsFileWithSameName(file.getName(), file.getExtension())) {
                    throw new IllegalArgumentException("A file with the same name and extension already exists in the target folder.");
                }
                fileParentFolder.addFile(file);
            } else {
                throw new IllegalArgumentException("Parent folder not part of the root hierarchy. Create the parent before creating the file.");
            }
        } else {
            file.setParent(root);
            if (root.containsFileWithSameName(file.getName(), file.getExtension())) {
                throw new IllegalArgumentException("A file with the same name and extension already exists in the target folder.");
            }
            root.addFile(file);
        }
    }

    /**
     * Deletes a file in the file system.
     * This method works by removing references to the parent by removing this file from the
     * list of files in the parent directory. Finally, it sets the file instance to null.
     * @param f the File object representing the file to be deleted.
     */
    public void deleteFile(File f) {
        if (f == null) {
            throw new IllegalArgumentException("File parameter cannot be null.");
        }
        Folder fileParentFolder = f.getParent();
        if (fileParentFolder != null) {
            fileParentFolder.getFiles().remove(f);
        }
    }

    /**
     * This method copies a file to a destination folder. It works by creating a new instance of the original File object
     * in the destination folder.
     * @param f a File object representing the file to be copied.
     * @param d the Folder object representing the copied File object destination.
     */
    public void copyFile(File f, Folder d) {
        if (f == null || d == null) {
            throw new IllegalArgumentException("File and Folder parameter cannot be null.");
        }
        File copiedFile = new File(f);
        copiedFile.setParent(d);
        createFile(copiedFile);
    }

    /**
     * This method moves a file from its current folder to a destination folder.
     * @param f a File object representing the file to be moved.
     * @param d a Folder object representing the destination folder.
     */
    public void moveFile(File f, Folder d) {
        if (f == null || d == null) {
            throw new IllegalArgumentException("File and Folder parameter cannot be null.");
        }
        copyFile(f, d);
        deleteFile(f);
    }


    /**
     * Creates a new folder in the file system. If the folder is not already assigned a parent, its
     * parent will be set to the root folder.
     * @param f the Folder object representing the folder to be created.
     */
    public void createFolder(Folder f) {
        if (f == null) {
            throw new IllegalArgumentException("Folder parameter cannot be null.");
        }
        Folder folder = new Folder(f);
        Folder folderParentFolder = folder.getParent();
        if(folderParentFolder != null) {
            if (root.isParentOf(folderParentFolder)) {
                if (folderParentFolder.containsFolderWithSameName(folder.getName())) {
                    throw new IllegalArgumentException("A folder with the same name already exists in the target folder.");
                }
                folderParentFolder.addFolder(folder);
            } else {
                throw new IllegalArgumentException("Parent folder not part of the root hierarchy. Create the parent before creating the folder.");
            }
        } else {
            folder.setParent(root);
            if(root.containsFolderWithSameName(folder.getName())) {
                throw new IllegalArgumentException("A folder with the same name already exists in the target folder.");
            }
            root.addFolder(folder);
        }
    }

    /**
     * This method deletes a folder in the file system. It works by removing references to the parent by removing
     * this folder from the list of folders in the parent directory. Finally, it sets the folder instance to ull.
     * @param f the Folder object representing the folder to be deleted.
     */
    public void deleteFolder(Folder f) {
        if (f == null) {
            throw new IllegalArgumentException("Folder parameter cannot be null.");
        }
        if (f.getParent() != null) {
            f.getParent().getFolders().remove(f);
        }
    }

    /**
     * This method copies a folder to a destination folder. It works by  creating a new instance of the original Folder
     * object in the destination Folder.
     * @param f a Folder object representing the folder to be copied.
     * @param d a Folder object representing the destination folder.
     */
    public void copyFolder(Folder f, Folder d) {
        if (f == null || d == null) {
            throw new IllegalArgumentException("Folder parameters cannot be null.");
        }
        Folder copiedFolder = new Folder(f);
        copiedFolder.setParent(d);
        createFolder(copiedFolder);
    }

    /**
     * This method moves a folder from its current folder to the destination folder.
     * @param f a Folder object representing the folder to be moved.
     * @param d a Folder object representing the new Folder destination
     */
    public void moveFolder(Folder f, Folder d) {
        if (f == null || d == null) {
            throw new IllegalArgumentException("Folder parameters cannot be null.");
        }
        copyFolder(f, d);
        deleteFolder(f);
    }
}
