import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import zip.*;

public class ZipCRCPatcher {
    List<FileEntry> zipFileEntries = null;

    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.out.println("Usage: ZipCRCPatcher [zip to copy from] [zip to copy to]");
            return;
        }

        new ZipCRCPatcher().makeZip(args[0], args[1]);
    }

    public void makeZip(String copyFrom, String copyTo) throws IOException {
        makeFilesList(copyFrom);
        ZipFile zipFile = new ZipFile(copyTo);
        ZipOutputStream zipOutputStream = new ZipOutputStream(new File(copyTo.replace(".apk", "-crc.apk")));
        zipOutputStream.setLevel(1);
        Enumeration<ZipEntry> entries = zipFile.getEntries();
        while (entries.hasMoreElements()) {
            ZipEntry nextElement = entries.nextElement();
            String name = nextElement.getName();
            for (int i = 0; i < this.zipFileEntries.size(); i++) {
                if (name.equals(this.zipFileEntries.get(i).getName())) {
                    nextElement.setCrc(this.zipFileEntries.get(i).getCrc());
                }
            }
            zipOutputStream.copyZipEntry(nextElement, zipFile);
        }
        zipOutputStream.close();
    }

    public void makeFilesList(String zip) throws IOException {
        this.zipFileEntries = new ArrayList<>();
        Enumeration<ZipEntry> entries = new ZipFile(zip).getEntries();
        while (entries.hasMoreElements()) {
            ZipEntry nextElement = entries.nextElement();
            this.zipFileEntries.add(new FileEntry(nextElement.getName(), nextElement.getCrc(), nextElement.getTime()));
        }
    }
}
