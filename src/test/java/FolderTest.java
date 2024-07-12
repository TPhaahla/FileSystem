import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FolderTest {

    private Folder rootFolder;
    private Folder subFolder;
    private Folder subFolder1;
    private File file1;
    private File file2;
    private final String fileName1 = "testFile1";
    private final String fileName2 = "testFile2";
    private final String fileExtension = "txt";
    public final byte[] fileContent = "Basic file content example.".getBytes();
    private final long fileSize = (long) fileContent.length;
    private final Date createdDate = new Date();

    private final String rootFolderName = "rootFolder";
    private final String subFolderName = "subFolder";
    private final String subFolder1Name = "subFolder1";

    /**
     * Set up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        rootFolder = new Folder(rootFolderName,null);
        subFolder = new Folder(subFolderName, rootFolder);
        subFolder1 = new Folder(subFolder1Name, rootFolder);
        file1 = new File(fileName1, rootFolder, fileSize, createdDate, fileContent, fileExtension);
        file2 = new File(fileName2, rootFolder, fileSize, createdDate, fileContent, fileExtension);
    }

    /**
     * Tests that the getName() method of the File class returns the expected folder name instantiated in this class.
     * The root folder is used in this test.
     */
    @Test
    void testGetFolderName() {
        assertEquals(rootFolderName, rootFolder.getName() );
    }

    /**
     * Tests that the setName() method of the Folder class which is inherited from the FileSystemEntity superclass, changes
     * the name of the folder to the local instance of the newFolderName String.
     */
    @Test
    void testSetFolderName() {
        String newFolderName = "newRootFolderName";
        rootFolder.setName(newFolderName);
        assertEquals(newFolderName, rootFolder.getName());
    }

    /**
     * Tests that the getParent() method of the Folder class which is inherited from the FileSystemEntity superclass
     * returns the expected value instantiated in this class when creating the Folder object.
     * This test checks that the subFolder has the rootFolder as it's parent and that the rootFolder has a parent of null.
     */
    @Test
    void testGetParent() {
        assertEquals(rootFolder, subFolder.getParent());
        assertNull(rootFolder.getParent());
    }

    /**
     * Tests that the setParent() method of the Folder class which is inherited from the FileSystemEntity superclass,
     * changes the parent of the Folder object to the local instance of the new Folder object.
     */
    @Test
    void testSetParent() {
        Folder newParentFolder = new Folder("newParentFolder", null);
        subFolder.setParent(newParentFolder);
        assertEquals(newParentFolder, subFolder.getParent());
    }

    /**
     * Tests that the setParent() method of the Folder class allows for the parent of the Folder object to be set to null.
     * This means the Folder object would now be considered to be in the root directory.
     */
    @Test
    void testSetParentToNull() {
        subFolder.setParent(null);
        assertNull(subFolder.getParent());
    }

    /**
     * Tests that setting the name attribute of the Folder object to an empty String throws an exception with a message confirming
     * that the name cannot be empty or null.
     */
    @Test
    void testSetNameToEmptyString() {
        String emptyFolderName = "";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            rootFolder.setName(emptyFolderName);
        });
        assertEquals("Name cannot be null or empty.", exception.getMessage());
    }

    /**
     * Tests that setting the name attribute of the Folder object to null throws an exception with a message confirming
     * that the name cannot be empty or null.
     */
    @Test
    void testSetNameToNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            rootFolder.setName(null);
        });
        assertEquals("Name cannot be null or empty.", exception.getMessage());
    }

    /**
     * Tests that setting a Folder name to the same name as a Folder with the same parent is not allowed.
     * Verifies that an exception is thrown and the name is unchanged.
     */
    @Test
    void testSetNameToSameAsFolderWithSameParent() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            subFolder1.setName(subFolderName);
        });
        assertEquals("A folder with the name \"subFolder\" already exists in the same folder.", exception.getMessage());
    }

    /**
     * Tests that setting a folder name does not change the parent. The setName() method in the Folder class is inherited
     * from the FileSystemEntity superclass and future changes to either of these classes may inadvertently affect the expected behaviour.
     */
    @Test
    void testSetNameDoesNotChangeParent() {
        String newFolderName = "newFolderName";
        subFolder.setName(newFolderName);
        assertEquals(rootFolder, subFolder.getParent());
    }

    /**
     * Tests that the list of files in the rootFolder Folder object is the same as the local list of File objects which contains the files
     * initialised when setting up this FolderTest class.
     */
    @Test
    void testGetFiles() {
        List<File> files = new ArrayList<>(List.of(file1, file2));
        assertEquals(files, rootFolder.getFiles());
    }

    /**
     * Tests that the list of folders in the rootFolder Folder object is the same as the local list of Folder objects which contains the folders
     * initialised when setting up this FolderTest class.
     */
    @Test
    void testGetFolders() {
        List<Folder> folders = new ArrayList<>(List.of(subFolder, subFolder1));
        assertEquals(folders, rootFolder.getFolders());
    }

    /**
     * Tests setFiles() method by setting a list of File objects as Files in the subFolder Folder object and checking
     * that the expected local list of files is the same as the actual in the subFolder Folder class.
     */
    @Test
    void testSetFiles() {
        List<File> files = new ArrayList<>(List.of(file1, file2));
        subFolder.setFiles(files);
        assertEquals(files, subFolder.getFiles());
    }

    /**
     * Tests setFolders() method by setting a list of Folder objects as Folders in the subFolder Folder object and checking
     * that the expected local list of folders is the same as the actual in the subFolder Folder class.
     */
    @Test
    void testSetFolders() {
        List<Folder> folders = new ArrayList<>(List.of(subFolder,subFolder1));
        subFolder.setFolders(folders);
        assertEquals(folders, subFolder.getFolders());
    }

    /**
     * Tests adding a sub-folder to a folder.
     * Verifies that the folder list contains the added sub-folder.
     */
    @Test
    void testAddFolder() {
        String folderName = "newFolder";
        Folder newFolderToAdd = new Folder(folderName, rootFolder);
        List<Folder> folders = rootFolder.getFolders();
        assertEquals(3, folders.size());
        assertTrue(rootFolder.containsFolderWithSameName(folderName));
    }

    /**
     * Tests adding a file to a folder.
     * Verifies that the file list contains the added file.
     */
    @Test
    void testAddFile() {
        subFolder.addFile(file1);
        List<File> files = subFolder.getFiles();
        assertEquals(1, files.size());
        assertTrue(subFolder.containsFile(file1));
    }

    /**
     * Tests the containsFile() method to check if a folder contains a file.
     * Verifies that the root folder contains file1 initialised in the test setup method, and
     * that the subFolder does not contain file1.
     */
    @Test
    void testContainsFile() {
        assertTrue(rootFolder.containsFile(file1));
        assertFalse(subFolder.containsFile(file1));
    }

    /**
     * Tests the containsFolder() method to check if a folder contains another folder.
     * Verifies that the root folder contains the subFolder and subFolder1 but that the subFolder does not contain subFolder1.
     */
    @Test
    void testContainsFolder() {
        assertTrue(rootFolder.containsFolder(subFolder));
        assertTrue(rootFolder.containsFolder(subFolder1));
        assertFalse(subFolder.containsFolder(subFolder1));
    }

    /**
     * Tests the containsFileWithSameName() method.
     * Verifies that the root folder contains a file with initialised fileName1 and fileExtension, but
     * that this is not the case in the subFolder.
     */
    @Test
    void testContainsFileWithSameName() {
        assertTrue(rootFolder.containsFileWithSameName(fileName1, fileExtension));
        assertFalse(subFolder.containsFileWithSameName(fileName1, fileExtension));
    }

    /**
     * Tests the containsFolderWithSameName() method.
     * Verifies that the root folder contains a folder with initialised subFolderName, but
     * that this is not the case in the subFolder.
     */
    @Test
    void testContainsFolderWithSameName() {
        assertTrue(rootFolder.containsFolderWithSameName(subFolderName));
        assertFalse(subFolder.containsFolderWithSameName(subFolderName));
    }

    @Test
    void testIsParentOf() {
        assertTrue(rootFolder.isParentOf(subFolder));
        assertFalse(subFolder.isParentOf(rootFolder));
    }

    @Test
    void testSetParentToSubFolderNotAllowed() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            rootFolder.setParent(subFolder);
            assertNull(rootFolder.getParent());
        });
        assertEquals("Cannot set parent of a root folder to any of its subfolders.", exception.getMessage());
    }







}