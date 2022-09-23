package DataBaseServices;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;

public class UploadFileToDB {

    public static void uploadFile(byte[] data,String userID ,String fileName){
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

}
