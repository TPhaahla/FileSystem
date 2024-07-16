import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * @author Tshiamo
 */
public class File extends FileSystemEntity {
    private final long size;
    private final Date created_date;
    private final byte[] content;
    private final String extension;

    /**
     * File constructor
     * @param name the name of the file as a string.
     * @param parent the parent Folder object of the file.
     * @param size the size of the file contents as a long.
     * @param created_date the date which the file was created, as a Date object.
     * @param content the file content as a byte array.
     * @param extension the file extension for the file type, as a string.
     */
    public File(String name, Folder parent, long size, Date created_date, byte[] content, String extension) {
        super(name, parent);
        this.size = size;
        this.created_date = created_date;
        this.content = content;
        this.extension = extension;
        if(parent != null) {
            parent.addFile(this);
        }
    }

    /**
     * File Copy Constructor
     * Creates a new File object by copying the properties of the specified File object
     * @param f the File object to copy.
     */
    public File(File f) {
        super(f.getName(), f.getParent());
        this.size = f.getSize();
        this.created_date = new Date(f.created_date.getTime());
        this.content = Arrays.copyOf(f.getContent(), (int) f.getSize());
        this.extension = f.getExtension();
    }

    public long getSize() {
        return size;
    }

    public Date getCreatedDate() {
        return created_date;
    }

    public byte[] getContent() {
        return content;
    }

    public String getExtension() {
        return extension;
    }

    /**
     * Sets the name of the file. If the file is part of a folder, it ensures that no other file
     * in the same folder has the same name and extension to avoid conflicts.
     * @param name the new name of the file
     * @throws IllegalArgumentException if a file with the same name and extension already exists in the parent folder.
     */
    @Override
    public void setName(String name) {
        Folder parentFolder = getParent();
        if (parentFolder != null && parentFolder.containsFileWithSameName(name, this.extension)) {
            throw new IllegalArgumentException("A file with the name \""+name+"\" and extension \"" +this.extension+"\" already exists in the parent folder.");
        }
        super.setName(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return size == file.size && Objects.equals(getName(), file.getName()) && Objects.equals(extension, file.extension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSize(), getExtension());
    }
}
