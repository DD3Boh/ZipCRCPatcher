public class FileEntry implements Comparable<FileEntry> {
    long crc;
    String name;
    long time;

    @Override
    public int compareTo(FileEntry file) {
        return this.name.compareToIgnoreCase(file.name);
    }

    public FileEntry(String name, long crc, long time) {
        this.name = name;
        this.crc = crc;
        this.time = time;
    }

    public String getName() {
        return this.name;
    }

    public long getTime() {
        return this.time;
    }

    public long getCrc() {
        return this.crc;
    }

    public String toString() {
        return this.name;
    }
}
