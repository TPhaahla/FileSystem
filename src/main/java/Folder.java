import java.util.ArrayList;
import java.util.List;

/**
 * @author Tshiamo
 */
public class Folder extends FileSystemEntity {
    private List<File> files;
    private List<Folder> folders;

    /**
     * Folder constructor
     * Constructs a new Folder with the specified name and parent.
     * @param name The name of the folder.
     * @param parent The parent folder. If not null, this folder will be added to the parent's list of subfolders.
     */
    public Folder(String name, Folder parent) {
        super(name, parent);
        this.files = new ArrayList<>();
        this.folders = new ArrayList<>();
        if (parent != null) {
            parent.addFolder(this);
        }
    }

    /**
     * Folder Copy Constructor
     * Creates a new Folder object by copying the properties of the specified Folder object
     * @param f the Folder object to copy.
     */
    public Folder(Folder f) {
        super(f.getName(), f.getParent());
        this.files = new ArrayList<>(f.getFiles());
        this.folders = new ArrayList<>(f.getFolders());
    }

    public List<File> getFiles() {
        return files;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    /**
     * Adds a file to the folder. Ensures that no other file in the folder has the same
     * name and extension to avoid conflicts.
     * @param file the file to be added to the folder.
     * @throws IllegalArgumentException if a file with the same name and extension already exists in the folder.
     */
    public void addFile(File file) {
        String fileName = file.getName();
        String fileExtension = file.getExtension();
        if (containsFileWithSameName(fileName, fileExtension)) {
            throw new IllegalArgumentException("A file with the name \""+fileName+"\" and extension \""+fileExtension+"\" already exists in this folder.");
        } else {
            files.add(file);
        }
    }

    /**
     * Adds a folder to the current folder. Ensures that no other folder in the
     *  current folder has the same name to avoid conflicts.
     *  @param folder the folder to be added to the current folder.
     *  @throws IllegalArgumentException if a folder with the same name already exists in the current folder.
     */
    public void addFolder(Folder folder) {
        String folderName = folder.getName();
        if(containsFolderWithSameName(folderName)) {
            throw new IllegalArgumentException("A folder with the name \""+folderName+"\" already exists in this folder.");
        }
        folders.add(folder);
    }

    public boolean containsFileWithSameName(String name, String extension) {
        for (File file : files) {
            if (file.getName().equalsIgnoreCase(name) && file.getExtension().equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsFolderWithSameName(String name) {
        for (Folder folder: folders) {
            if(folder.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsFile(File file) {
        return files.contains(file);
    }

    public boolean containsFolder(Folder folder) {
        return folders.contains(folder);
    }

    /**
     * Sets the name of the folder. If the folder is part of a folder, it ensures that no other folder
     * in the same folder has the same name to avoid conflicts.
     * @param name the new name of the folder.
     * @throws IllegalArgumentException if a folder with the same name already exists in the parent folder.
     */
    @Override
    public void setName(String name) {
        Folder parent = getParent();
        if (parent != null && parent.containsFolderWithSameName(name)) {
            throw new IllegalArgumentException("A folder with the name \""+name+"\" already exists in the same folder.");
        }

        super.setName(name);
    }

    /**
     * Sets the parent of the current folder. Ensures that the current folder is not set as a parent
     * of any of its subfolders to avoid circular dependencies.
     * @param folder the folder to be set as the parent of the current folder.
     * @throws IllegalArgumentException if the current folder is being set as the parent of one of its subfolders.
     */
    @Override
    public void setParent(Folder folder){
        if (isParentOf(folder)){
            throw new IllegalArgumentException("Cannot set parent of a root folder to any of its subfolders.");
        }
        super.setParent(folder);
    }

    /**
     * Checks if the current folder is a parent (direct or indirect) of the specified folder.
     * @param folder the folder to check.
     * @return true if the current folder is a parent of the specified folder, false otherwise.
     */
    public boolean isParentOf(Folder folder) {
        Folder currentFolder = folder;
        while(currentFolder !=null) {
            if (currentFolder == this) {
                return true;
            }
            currentFolder = currentFolder.getParent();
        }
        return false;
    }


}
