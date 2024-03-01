/*
 * SPDX-License-Identifier: Apache-2.0
 * Author: Davide Garberi (dade.garberi@gmail.com)
 */

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

        if (args[0].equals(null) || args[1].equals(null))
            throw new IllegalArgumentException("Error: The source and destination files can't be null");

        if (args[0].isEmpty() || args[1].isEmpty())
            throw new IllegalArgumentException("Error: The source and destination files can't be empty");

        if (args[0].equals(args[1]))
            throw new IllegalArgumentException("Error: The source and destination files can't be the same");

        new ZipCRCPatcher().makeZip(args[0], args[1]);
    }

    public void makeZip(String copyFrom, String copyTo) throws IOException {
        makeFilesList(copyFrom);
        ZipFile zipFile;
        ZipOutputStream zipOutputStream;

        try {
            zipFile = new ZipFile(copyTo);
        } catch (IOException e) {
            throw new IOException("Error: Can't open the file " + copyTo + " as a zip file.");
        }

        String extension = copyTo.substring(copyTo.lastIndexOf("."));
        if (extension.equals(null) || extension.isEmpty()) {
            zipFile.close();
            throw new IllegalArgumentException("Error: The file " + copyTo + " has no extension.");
        }

        if (!extension.equals(".apk") || !extension.equals(".zip")) {
            zipFile.close();
            throw new IllegalArgumentException("Error: The file " + copyTo + " is not a zip file.");
        }

        String newCopyTo = copyTo.replace(extension, "-crc" + extension);

        try {
            zipOutputStream = new ZipOutputStream(new File(newCopyTo));
        } catch (IOException e) {
            zipFile.close();
            throw new IOException("Error: Can't write to the file " + newCopyTo);
        }
        
        zipOutputStream.setLevel(1);
        Enumeration<ZipEntry> entries = zipFile.getEntries();
        while (entries.hasMoreElements()) {
            ZipEntry nextElement = entries.nextElement();
            String name = nextElement.getName();
            for (int i = 0; i < this.zipFileEntries.size(); i++)
                if (name.equals(this.zipFileEntries.get(i).getName()))
                    nextElement.setCrc(this.zipFileEntries.get(i).getCrc());

            zipOutputStream.copyZipEntry(nextElement, zipFile);
        }
        zipOutputStream.close();
    }

    public void makeFilesList(String zip) throws IOException {
        this.zipFileEntries = new ArrayList<>();
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(zip);
        } catch (IOException e) {
            throw new IOException("Error: Can't open the file " + zip + " as a zip file.");
        }
        Enumeration<ZipEntry> entries = zipFile.getEntries();
        while (entries.hasMoreElements()) {
            ZipEntry nextElement = entries.nextElement();
            this.zipFileEntries.add(new FileEntry(nextElement.getName(), nextElement.getCrc(), nextElement.getTime()));
        }
        zipFile.close();
    }
}
