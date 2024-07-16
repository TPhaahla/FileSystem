import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemManagerTest {

    private FileSystemManager fileSystemManager;
    private Folder root;
    private Folder rootSubFolder;
    private final String rootSubFolderName = "rootSubFolder";
    private Folder rootSubFolderCopy;
    private Folder newFolder;
    private final String newFolderName = "newFolder";
    private Folder externalFolder;
    private File file1;
    private final String file1name = "newFile1";
    private File file1Copy;
    private File file2;
    private final String file2name = "newFile2";
    private File file3;
    private final String file3name = "newFile3";
    private final byte[] fileContent = "Basic file content example.".getBytes();
    private final long fileSize = (long) fileContent.length;
    private final String fileExtension = "txt";
    private final Date createdDate = new Date();


    @BeforeEach
    void setUp() {
        fileSystemManager = new FileSystemManager();
        root = fileSystemManager.getRoot();
        rootSubFolder = new Folder(rootSubFolderName, root);
        rootSubFolderCopy = new Folder(rootSubFolder);
        newFolder = new Folder(newFolderName, null);
        externalFolder = new Folder("externalFolder",newFolder);
        file1 = new File(file1name, root, fileSize, createdDate, fileContent, fileExtension);
        file1Copy = new File(file1);
        file2 = new File(file2name, null, fileSize, createdDate, fileContent, fileExtension);
        file3 = new File(file3name, externalFolder, fileSize, createdDate, fileContent, fileExtension);
    }

    @Test
    void getRoot() {
        assertEquals(root, fileSystemManager.getRoot());
    }

    /**
     * Tests and verifies that the createFile() method throws an IllegalArgumentException if the file parameter is null.
     */
    @Test
    void testCreateFileWithNullFile() {
        Exception exception = assertThrows(IllegalArgumentException.class, ()->{
            fileSystemManager.createFile(null);
        });
        assertEquals("File parameter cannot be null.", exception.getMessage());
    }

    /**
     * Tests createFiled() method and verifies that the created File is in the specified parent directory referenced in
     * the File object parameter.
     */
    @Test
    void testCreateFile() {
        file1Copy.setParent(rootSubFolder);
        fileSystemManager.createFile(file1Copy);
        assertTrue(file1Copy.getParent().containsFile(file1Copy));
    }

    /**
     * Tests and verifies that when the createFile() method is passed a File object that does not have a parent, then
     * this file will be added to the root folder.
     */
    @Test
    void testCreateFileWithNullParent() {
        assertFalse(root.containsFile(file2));
        fileSystemManager.createFile(file2);
        assertTrue(root.containsFile(file2));
    }

    /**
     * Tests and verifies that executing the createFile() method with a File object parameter that has a parent
     * outside the current root hierarchy throws an IllegalArgumentException and provides a useful message.
     */
    @Test
    void testCreateFileWithParentNotInFileSystem() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fileSystemManager.createFile(file3);
        });
        assertEquals("Parent folder not part of the root hierarchy. Create the parent before creating the file.", exception.getMessage());
    }

    /**
     * Tests and verifies that executing the createFile() method with a File object that has the same name and extension
     * as a file object in the target folder throws an IllegalArgumentException and provides a useful message.
     */
    @Test
    void testCreateFileWithDuplicateNameAndExtension() {
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> {
            fileSystemManager.createFile(file1Copy);
        });
        assertEquals("A file with the same name and extension already exists in the target folder.", exception.getMessage());
    }

    /**
     * Tests and verifies that executing the createFile() method with a File object that has a null parent and the same name and extension
     * as a file in the root directory throws an IllegalArgumentException and provides a useful message.
     */
    @Test
    void testCreateFileWithNullParentAndDuplicateNameAndExtensionAsFileInRoot() {
        file1Copy.setParent(null);
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> {
            fileSystemManager.createFile(file1Copy);
        });
        assertEquals("A file with the same name and extension already exists in the target folder.", exception.getMessage());
    }

    /**
     * Tests and verifies that the deleteFile() method throws an IllegalArgumentException if the file parameter is null.
     */
    @Test
    void testDeleteFileWithNullFile() {
        Exception exception = assertThrows(IllegalArgumentException.class, ()->{
            fileSystemManager.deleteFile(null);
        });
        assertEquals("File parameter cannot be null.", exception.getMessage());
    }

    /**
     * Tests and verifies that the deleteFile() method deletes the file from the folder which it is contained.
     */
    @Test
    void testDeleteFile() {
        assertTrue(root.containsFile(file1));
        fileSystemManager.deleteFile(file1);
        assertFalse(root.containsFile(file1));
    }

    /**
     * Tests and verifies that the copyFile() method throws an IllegalArgumentException if the file parameter is null.
     */
    @Test
    void testCopyFileWithNullFile() {
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> {
            fileSystemManager.copyFile(null, rootSubFolder);
        });
        assertEquals("File and Folder parameter cannot be null.", exception.getMessage());
    }

    /**
     * Tests and verifies that the copyFile() method throws an IllegalArgumentException if the destination Folder object
     * is null.
     */
    @Test
    void testCopyFileWithNullDestinationFolder() {
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> {
            fileSystemManager.copyFile(file1, null);
        });
        assertEquals("File and Folder parameter cannot be null.", exception.getMessage());
    }

    /**
     * Tests and verifies that the copyFile() method throws an IllegalArgumentException if both the file parameter and
     * destination folder parameter are null.
     */
    @Test
    void testCopyFileBothParametersNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> {
            fileSystemManager.copyFile(null, null);
        });
        assertEquals("File and Folder parameter cannot be null.", exception.getMessage());
    }

    /**
     * Tests that the copyFile() parameter throws an exception when trying to copy to a destination folder that is not
     * part of the current file system root hierarchy.
     */
    @Test
    void testCopyFileToExternalFolder() {
        assertFalse(newFolder.containsFile(file1));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fileSystemManager.copyFile(file1, newFolder);
        });
        assertEquals("Parent folder not part of the root hierarchy. Create the parent before creating the file.", exception.getMessage());
        assertFalse(newFolder.containsFile(file1));
    }

    /**
     * Tests that the copyFile() method functions as expected.
     * Verifies that the file has been copied to the destination folder.
     */
    @Test
    void testCopyFile() {
        assertFalse(rootSubFolder.containsFile(file1));
        fileSystemManager.copyFile(file1, rootSubFolder);
        assertTrue(rootSubFolder.containsFile(file1));
        assertTrue(root.containsFile(file1));
    }

    /**
     * Tests and verifies that the moveFile() method throws an IllegalArgumentException if the file parameter is null.
     */
    @Test
    void testMoveFileWithNullFile() {
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> {
            fileSystemManager.moveFile(null, rootSubFolder);
        });
        assertEquals("File and Folder parameter cannot be null.", exception.getMessage());
    }

    /**
     * Tests and verifies that the moveFile() method throws an IllegalArgumentException if the destination Folder object
     * is null.
     */
    @Test
    void testMoveFileWithNullDestination() {
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> {
            fileSystemManager.moveFile(file1, null);
        });
        assertEquals("File and Folder parameter cannot be null.", exception.getMessage());
    }

    /**
     * Tests and verifies that the moveFile() method throws an IllegalArgumentException if both the file parameter and
     * destination folder parameter are null.
     */
    @Test
    void testMoveFileWithBothParametersNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fileSystemManager.moveFile(null, null);
        });
        assertEquals("File and Folder parameter cannot be null.", exception.getMessage());
    }

    /**
     * Tests that the moveFile() method throws an exception when trying to move to a destination folder that is not
     * part of the current file system root hierarchy.
     */
    @Test
    void testMoveFileToExternalDestination() {
        assertFalse(newFolder.containsFile(file1));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fileSystemManager.moveFile(file1, newFolder);
        });
        assertEquals("Parent folder not part of the root hierarchy. Create the parent before creating the file.", exception.getMessage());
        assertFalse(newFolder.containsFile(file1));
        assertTrue(root.containsFile(file1));
    }

    /**
     * Tests that the moveFile() method functions as expected.
     * Verifies that the file has been moved to the destination folder.
     */
    @Test
    void testMoveFile() {
        assertFalse(rootSubFolder.containsFile(file1));
        fileSystemManager.moveFile(file1, rootSubFolder);
        assertTrue(rootSubFolder.containsFile(file1));
        assertFalse(root.containsFile(file1));
    }

    /**
     * Tests and verifies that the createFolder() method throws an IllegalArgumentException if the folder parameter is null.
     */
    @Test
    void testCreateFolderWithNullFolder() {
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> {
            fileSystemManager.createFolder(null);
        });
        assertEquals("Folder parameter cannot be null.", exception.getMessage());
    }

    /**
     * Tests createFolder() method and verifies that the created Folder is in the specified parent directory referenced in
     * the Folder object parameter.
     */
    @Test
    void testCreateFolder() {
        newFolder.setParent(root);
        assertEquals(1, root.getFolders().size());
        assertFalse(root.containsFolderWithSameName(newFolder.getName()));
        fileSystemManager.createFolder(newFolder);
        assertTrue(root.containsFolderWithSameName(newFolder.getName()));
        assertEquals(2, root.getFolders().size());
    }

    /**
     * Tests and verifies that when the createFolder() method is passed a Folder object that does not have a parent, then
     * this folder will be added to the root folder.
     */
    @Test
    void testCreateFolderWithNullParent() {
        assertFalse(root.containsFolderWithSameName(newFolder.getName()));
        assertEquals(1, root.getFolders().size());
        fileSystemManager.createFolder(newFolder);
        assertTrue(root.containsFolderWithSameName(newFolder.getName()));
        assertEquals(2, root.getFolders().size());
    }

    /**
     * Tests and verifies that executing the createFolder() method with a Folder object parameter that has a parent
     * outside the current root hierarchy throws an IllegalArgumentException and provides a useful message.
     */
    @Test
    void testCreateFolderWithParentNotInFileSystem() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fileSystemManager.createFolder(externalFolder);
        });
        assertEquals("Parent folder not part of the root hierarchy. Create the parent before creating the folder.", exception.getMessage());
    }

    /**
     * Tests and verifies that executing the createFolder() method with a Folder object that has the same name
     * as a folder object in the target folder throws an IllegalArgumentException and provides a useful message.
     */
    @Test
    void testCreateFileWithDuplicateName() {
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> {
            fileSystemManager.createFolder(rootSubFolderCopy);
        });
        assertEquals("A folder with the same name already exists in the target folder.", exception.getMessage());
    }

    /**
     * Tests and verifies that executing the createFile() method with a File object that has a null parent and the same name and extension
     * as a file in the root directory throws an IllegalArgumentException and provides a useful message.
     */
    @Test
    void testCreateFileWithNullParentAndDuplicateFileNameInRoot() {
        newFolder.setName(rootSubFolderName);
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> {
            fileSystemManager.createFolder(newFolder);
        });
        assertEquals("A folder with the same name already exists in the target folder.", exception.getMessage());
    }

    /**
     * Tests and verifies that the deleteFolder() method throws an IllegalArgumentException if the folder parameter is null.
     */
    @Test
    void testDeleteFolderWithNullFolder() {
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> {
            fileSystemManager.deleteFolder(null);
        });
        assertEquals("Folder parameter cannot be null.", exception.getMessage());
    }

    /**
     * Tests and verifies that the deleteFolder() method removes the folder from the folder which it is contained.
     */
    @Test
    void deleteFolder() {
        assertTrue(root.containsFolder(rootSubFolder));
        fileSystemManager.deleteFolder(rootSubFolder);
        assertFalse(root.containsFolder(rootSubFolder));
    }

    /**
     * Tests and verifies that the copyFolder() method throws and IllegalArgumentException if the folder to be copied is
     * given as a null parameter.
     */
    @Test
    void testCopyFolderWithNullOriginalFolder() {
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> {
            fileSystemManager.copyFolder(null, rootSubFolder);
        });
        assertEquals("Folder parameters cannot be null.", exception.getMessage());
    }

    /**
     * Tests and verifies that the copyFolder() method throws an IllegalArgumentException if the destination Folder object
     * is null.
     */
    @Test
    void testCopyFolderWithNullDestinationFolder() {
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> {
            fileSystemManager.copyFolder(rootSubFolder, null);
        });
        assertEquals("Folder parameters cannot be null.", exception.getMessage());
    }

    /**
     * Tests and verifies that the copyFolder() method throws an IllegalArgumentException if both the file parameter and
     * destination folder parameter are null.
     */
    @Test
    void testCopyFolderWithBothParametersNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> {
            fileSystemManager.copyFolder(rootSubFolder, null);
        });
        assertEquals("Folder parameters cannot be null.", exception.getMessage());
    }

    /**
     * Tests that the copyFolder() method throws an Exception when trying to copy to a destination folder that is not
     * part of the current file system root hierarchy.
     */
    @Test
    void testCopyFolderToExternalFolder() {
        assertFalse(newFolder.containsFolderWithSameName(rootSubFolder.getName()));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fileSystemManager.copyFolder(rootSubFolder, newFolder);
        });
        assertEquals("Parent folder not part of the root hierarchy. Create the parent before creating the folder.", exception.getMessage());
        assertFalse(newFolder.containsFolderWithSameName(rootSubFolder.getName()));
    }
    /**
     * Tests that the copyFolder() method functions as expected.
     * Verifies that the folder has been copied to the destination folder.
     */
    @Test
    void testCopyFolder() {
        assertFalse(rootSubFolder.containsFolderWithSameName(newFolderName));
        fileSystemManager.copyFolder(newFolder, rootSubFolder);
        assertTrue(rootSubFolder.containsFolderWithSameName(newFolderName));
        assertNull(newFolder.getParent());
    }

    /**
     * Tests and verifies that the moveFolder() method throws an IllegalArgumentException if the original folder
     * parameter is null.
     */
    @Test
    void testMoveFolderWithNullOriginalFolder() {
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> {
            fileSystemManager.moveFolder(null, rootSubFolder);
        });
        assertEquals("Folder parameters cannot be null.", exception.getMessage());
    }

    /**
     * Tests and verifies that the moveFolder() method throws an IllegalArgumentException if the destination Folder object
     * is null.
     */
    @Test
    void testMoveFolderWithNullDestination() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fileSystemManager.moveFolder(newFolder, null);
        });
        assertEquals("Folder parameters cannot be null.", exception.getMessage());
    }

    /**
     * Tests and verifies that the moveFolder() method throws an IllegalArgumentException if both the folder parameters
     * are null.
     */
    @Test
    void testMoveFolderWithBothParametersNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> {
            fileSystemManager.moveFolder(null, null);
        });
        assertEquals("Folder parameters cannot be null.", exception.getMessage());
    }

    /**
     * Tests that the moveFolder() method throws an exception when trying to move to a destination folder that is not
     * part of the current file system root hierarchy.
     */
    @Test
    void testMoveFolderToExternalDestination() {
        assertFalse(newFolder.containsFolderWithSameName(rootSubFolder.getName()));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fileSystemManager.moveFolder(rootSubFolder, newFolder);
        });
        assertEquals("Parent folder not part of the root hierarchy. Create the parent before creating the folder.", exception.getMessage());
        assertFalse(newFolder.containsFolderWithSameName(rootSubFolder.getName()));
    }

    /**
     * Tests that the moveFolder() method functions as expected.
     * Verifies that the folder has been moved to the destination folder.
     */
    @Test
    void testMoveFolder() {
        assertFalse(rootSubFolder.containsFolderWithSameName(newFolderName));
        fileSystemManager.moveFolder(newFolder, rootSubFolder);
        assertTrue(rootSubFolder.containsFolderWithSameName(newFolderName));
        assertNull(newFolder.getParent());
    }

    /**
     *
     */
    @Test
    void testMoveRootToSubFolderNotAllowed() {
        Exception exception = assertThrows(IllegalStateException.class, ()-> {
            fileSystemManager.moveFolder(root, rootSubFolder);
        });
        assertEquals("Illegal operation: Cannot move a folder into its subfolder.", exception.getMessage());
    }

}