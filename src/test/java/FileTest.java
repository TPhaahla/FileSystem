import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;

class FileTest {

    private File file;
    private File file2;
    private final String fileName = "testFile";
    private final String fileName1 = "testFile1";
    private Folder parentFolder = new Folder("root", null );
    private final byte[] fileContent = "Basic file content example.".getBytes();
    private final long fileSize = (long) fileContent.length;
    private final String fileExtension = "txt";
    private final Date createdDate = new Date();

    /**
     * Set up the test environment before each test.
    */
    @BeforeEach
    void setUp() {
        file = new File(fileName, parentFolder, fileSize, createdDate, fileContent, fileExtension);
        file2 = new File(fileName1, parentFolder, fileSize, createdDate, fileContent, fileExtension);
    }

    /**
     * Tests the copy constructor and verifies that the new File object is the same
     * as the one passed as a parameter in the Constructor.
     */
    @Test
    void testCopyConstructor() {
        File copiedFile = new File(file);

        assertEquals(file.getName(), copiedFile.getName());
        assertEquals(file.getParent(), copiedFile.getParent());
        assertEquals(file.getSize(), copiedFile.getSize());
        assertEquals(file.getExtension(), copiedFile.getExtension());

        // Check that the created date has the same value but is not the same object.
        assertNotSame(file.getCreatedDate(), copiedFile.getCreatedDate());
        assertEquals(file.getCreatedDate().getTime(), copiedFile.getCreatedDate().getTime());

        // Check that the file content has the same value but is not the same object.
        assertNotSame(file.getContent(), copiedFile.getContent());
        assertArrayEquals(file.getContent(), copiedFile.getContent());
    }

    /**
     * Tests that when an object of the File class is created, it can be found in its parent Folder.
     */
    @Test
    void testFileObjectIsFoundInParent() {
        assertTrue(parentFolder.containsFile(file));
        assertTrue(parentFolder.containsFile(file2));

    }

    /**
     * Tests that the getName() method of the File class returns the expected fileName instantiated in this class.
    */
    @Test
    void testGetFileName() {
        assertEquals(fileName, file.getName());
    }

    /**
     * Tests that the getParent() method of the File class which is inherited from the FileSystemEntity superclass
     * returns the expected value instantiated in this class when creating the File object.
     */
    @Test
    void testGetParent() {
        assertEquals(parentFolder, file.getParent());
    }

    /**
     * Tests that the getSize() method of the File class returns the expected fileSize instantiated in this class and used to create the File object.
     */
    @Test
    void testGetSize(){
        assertEquals(fileSize, file.getSize());
    }

    /**
     * Tests that the getCreatedDate() method of the File class returns expected date instantiated in this class used to create the File object.
     */
    @Test
    void testGetCreatedDate() {
        assertEquals(createdDate, file.getCreatedDate());
    }

    /**
     * Tests that the getContent() method of the File class returns the expected value instantiated in this class and used to create the File object.
     */
    @Test
    void testGetContent() {
        assertEquals(fileContent, file.getContent());
    }

    /**
     * Tests that the getExtension() method of the File class returns the expected value instantiated in this class and used to create the File object.
     */
    @Test
    void testGetExtension() {
        assertEquals(fileExtension, file.getExtension());
    }

    /**
     * Tests that the setParent() method of the File class which is inherited from the FileSystemEntity superclass,
     * changes the parent of the File object to the local instance of the new Folder object.
     */
    @Test
    void testSetParent() {
        Folder newParentFolder = new Folder("newParentFolder", null);
        file.setParent(newParentFolder);
        assertEquals(newParentFolder, file.getParent());
    }

    /**
     * Tests that the setName() method of the File class which is inherited from the FileSystemEntity superclass, changes
     * the name of the file to the local instance of the newFileName String.
     */
    @Test
    void testSetName() {
        String newFileName = "newTestFileName";
        file.setName(newFileName);
        assertEquals(newFileName, file.getName());
    }

    /**
     * Tests that the setParent() method of the File class allows for the parent of the File object to be set to null.
     */
    @Test
    void testSetParentToNull() {
        file.setParent(null);
        assertNull(file.getParent());
    }

    /**
     * Tests that setting the name attribute of the File object to an empty String throws an exception with a message confirming
     * that the name cannot be empty or null.
     */
    @Test
    void testSetNameToEmptyString() {
        String emptyFileName = "";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            file.setName(emptyFileName);
        });
        assertEquals("Name cannot be null or empty.", exception.getMessage());
    }

    /**
     * Tests that setting the name attribute of the File object to null throws an exception with a message confirming
     * that the name cannot be empty or null.
     */
    @Test
    void testSetNameToNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            file.setName(null);
        });
        assertEquals("Name cannot be null or empty.", exception.getMessage());
    }

    /**
     * Tests that setting a file name to the same name but with the same extension as a file in the same folder is not allowed.
     * Verifies that an exception is thrown and the name is unchanged.
     */
    @Test
    void testSetNameToSameNameAndExtensionNotAllowed() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            file2.setName(fileName);
        });
        assertEquals("A file with the name \"testFile\" and extension \"txt\" already exists in the parent folder.", exception.getMessage());

    }

    /**
     * Tests that setting a file name to the same name but with a different extension is allowed.
     * Verifies that no exception is thrown and the name is changed correctly.
     */
    @Test
    void testSetNameToSameNameWithDifferentExtensionAllowed() {
        File file3 = new File("anotherFile", parentFolder, fileSize, createdDate, fileContent, "doc");
        assertDoesNotThrow(() -> {
            file3.setName(fileName);
        });

        assertEquals(fileName, file3.getName());
    }

    /**
     * Tests that setting a file name does not change the parent. The setName() method in the File class is inherited
     * from the FileSystemEntity superclass and future changes to either of these classes may inadvertently affect the expected behaviour.
     */
    @Test
    void testSetNameDoesNotChangeParent() {
        String newFileName = "newTestFile";
        file.setName(newFileName);
        assertEquals(parentFolder, file.getParent());
    }

    /**
     * Tests that setting a parent does not change the fileName. The setParent() method in the File class is inherited
     * from the FileSystemEntity superclass and future changes to either of these classes may inadvertently affect the expected behaviour.
     */
    @Test
    void testSetParentDoesNotChangeName() {
        Folder newParentFolder = new Folder("newParentFolder", null);
        file.setParent(newParentFolder);
        assertEquals(fileName, file.getName());
    }

    /**
     * Tests that a File object can exist with no parent Folder object.
     */
    @Test
    void testFileObjectWithNoParent() {
        assertDoesNotThrow(()-> {
            File standAloneFile = new File(fileName, null, fileSize, createdDate, fileContent, fileExtension);
            assertNull(standAloneFile.getParent());
        });
    }
}