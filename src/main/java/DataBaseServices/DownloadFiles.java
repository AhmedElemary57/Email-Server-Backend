package DataBaseServices;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSDownloadOptions;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DownloadFiles {

    public static ResponseEntity<Resource> downloadFile(String senderID, String fileName){

        MongoDatabase database = DataBase.connectToDB(senderID);
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        GridFSDownloadOptions downloadOptions = new GridFSDownloadOptions().revision(0);
        try (FileOutputStream streamToDownloadTo = new FileOutputStream(fileName)) {
            gridFSBucket.downloadToStream(fileName, streamToDownloadTo, downloadOptions);
            System.out.println("The file was downloaded successfully");
            File file = new File(fileName);
            Resource resource = new UrlResource(file.toURI());

            return new ResponseEntity<>(resource, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;


    }

}
