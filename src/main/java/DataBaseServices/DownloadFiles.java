package DataBaseServices;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSDownloadOptions;
import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.sql.rowset.serial.SerialException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;

public class DownloadFiles {

    public static ResponseEntity<Resource> downloadFile2(String senderID, String attachmentID, String fileName){
        MongoDatabase database = DataBase.connectToDB(senderID);
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        ObjectId fileId = new ObjectId(attachmentID);
        try (GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(fileId)) {
            int fileLength = (int) downloadStream.getGridFSFile().getLength();
            byte[] bytesToWriteTo = new byte[fileLength];
            downloadStream.read(bytesToWriteTo);
            File file = new File(fileName);
            Resource resource = new UrlResource(file.toURI());
            file.delete();
            return new ResponseEntity<>(resource, HttpStatus.OK);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResponseEntity<Resource> downloadFile(String senderID, String fileName){

        MongoDatabase database = DataBase.connectToDB(senderID);
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        GridFSDownloadOptions downloadOptions = new GridFSDownloadOptions().revision(0);
        try (FileOutputStream streamToDownloadTo = new FileOutputStream(fileName)) {
            gridFSBucket.downloadToStream(fileName, streamToDownloadTo, downloadOptions);
            streamToDownloadTo.flush();
            System.out.println("The file was downloaded successfully");
            File file = new File(fileName);
            Resource resource = new UrlResource(file.toURI());
            file.delete();
            return new ResponseEntity<>(resource, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;


    }

}
