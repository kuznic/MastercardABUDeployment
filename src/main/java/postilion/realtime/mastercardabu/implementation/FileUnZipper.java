package postilion.realtime.mastercardabu.implementation;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import postilion.realtime.mastercardabu.config.AbuConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
@ComponentScan
@Slf4j
public class FileUnZipper {

    @Autowired
    AbuConfig abuConfig;

    public void unZipFile(String zippedFileLocation, String abuSolutionFolder) throws IOException {
        File destDir;



        zippedFileLocation = zippedFileLocation + "Mastercard_abu_setup.zip";
        System.out.println();
        System.out.println();
        System.out.println("The Mastercard ABU solution will be deployed to " + abuConfig.getDeployFolder());
        destDir = new File(abuSolutionFolder);



        if(destDir.exists())
        {
            System.out.println();
            System.out.println();
            System.out.println(" Path " + destDir + " already exists. No change has been made ");
            System.out.println();
            System.out.println();

        }

        else
        {
            byte[] buffer = new byte[1024];
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zippedFileLocation));
            ZipEntry zipEntry = zin.getNextEntry();
            while (zipEntry != null){
                File newFile = newFile(destDir,zipEntry);
                if(zipEntry.isDirectory()){
                    if(!newFile.isDirectory() && !newFile.mkdirs()){
                        throw new IOException("Failed to create directory " + newFile);
                    }
                }else{
                    File parent = newFile.getParentFile();
                    if(!parent.isDirectory() && !parent.mkdirs()){
                        throw new IOException("Failed to create directory " + parent);
                    }

                    FileOutputStream fot = new FileOutputStream(newFile);
                    int len;
                    while((len = zin.read(buffer))> 0){
                        fot.write(buffer,0,len);
                    }
                    fot.close();
                }
                zipEntry = zin.getNextEntry();
            }
            zin.closeEntry();
            zin.close();
            System.out.println();
            System.out.println();
            System.out.println("Successfully created directory C:\\mastercardabu");
        }





    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

}
