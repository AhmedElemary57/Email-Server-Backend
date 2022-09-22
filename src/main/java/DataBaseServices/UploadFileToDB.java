package DataBaseServices;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadFileToDB {

    public static void uploadFile(byte[] data,String userID ,String fileName) throws IOException {
        MongoDatabase database = DataBase.connectToDB(userID);
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        GridFSUploadOptions options = new GridFSUploadOptions();
        try (GridFSUploadStream uploadStream = gridFSBucket.openUploadStream(fileName, options)) {
            uploadStream.write(data);
            uploadStream.flush();
            System.out.println("The file id of the uploaded file is: " + uploadStream.getObjectId().toHexString());
        } catch (Exception e) {
            System.err.println("The file upload failed: " + e);
        }
    }

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("D:\\test.mp4");
        byte[] data = Files.readAllBytes(path);
        uploadFile(data,"63266057310bdf79298f3dfe","test.mp4");

    }
}
