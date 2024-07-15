/**
 * @author Tshiamo
 */
public abstract class FileSystemEntity {
    private String name;
    private Folder parent;

    public FileSystemEntity(String name, Folder parent) {
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public Folder getParent() {
        return parent;
    }

    /**
     * Sets the name of the file system entity.
     * Ensures that the name is not null or empty.
     * @param name the new name of the file system entity.
     * @throws IllegalArgumentException if the name is null or empty.
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name;
    }

    public void setParent(Folder parent) {
        this.parent = parent;
    }

}
