# ZipCRCPatcher

## Overview
The ZipCRCPatcher program is a command-line tool that allows you to replace CRC32 signatures of files inside a zip or apk file. It is useful for modifying the integrity of files within an archive.

## Usage
```
java -jar ZipCRCPatcher.jar <archive to copy CRCs from> <archive to copy CRCs to>
```

Replace `<archive to copy CRCs from>` with the path to the source archive file from which you want to copy the CRCs, and `<archive to copy CRCs to>` with the path to the destination archive file where you want to apply the modified CRCs.

**Note:** The destination file does not get overridden. Instead, the modified zip is saved as the output file with `-crc` added to the filename.

## License
This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for more information.