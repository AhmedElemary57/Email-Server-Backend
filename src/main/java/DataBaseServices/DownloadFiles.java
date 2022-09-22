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
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;

public class DownloadFiles {

    public static ResponseEntity<Blob> downloadFile(String senderID, String emailID, String fileName){
        MongoDatabase database = DataBase.connectToDB(senderID);
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        ObjectId fileId = new ObjectId("63288afa1767d920497e914b");
        try (GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(fileId)) {
            int fileLength = (int) downloadStream.getGridFSFile().getLength();
            byte[] bytesToWriteTo = new byte[fileLength];
            downloadStream.read(bytesToWriteTo);
            System.out.println(new String(bytesToWriteTo, StandardCharsets.UTF_8));
            Blob blob = new javax.sql.rowset.serial.SerialBlob(bytesToWriteTo);

            return ResponseEntity.ok().body(blob);

        } catch (SerialException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResponseEntity<Resource> downloadFile2(String senderID, String fileName){

        MongoDatabase database = DataBase.connectToDB(senderID);
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        GridFSDownloadOptions downloadOptions = new GridFSDownloadOptions().revision(0);
        try (FileOutputStream streamToDownloadTo = new FileOutputStream(fileName)) {
            gridFSBucket.downloadToStream(fileName, streamToDownloadTo, downloadOptions);
            streamToDownloadTo.flush();
            System.out.println("The file was downloaded successfully");
            File file = new File(fileName);
            Resource resource = new UrlResource(file.toURI());

            return new ResponseEntity<>(resource, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;


    }

    public static void main(String[] args) {
        downloadFile2("1",  "1");
    }
}
