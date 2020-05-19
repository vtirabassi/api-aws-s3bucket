package com.example.apiawss3bucket.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import com.example.apiawss3bucket.utils.*;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

@Service
public class FileService {

	@Autowired
	AmazonS3Client amazonS3Client;

	@Value("${app.awsServices.bucketName}")
	String defaultBucketName;

	public List<Bucket> getAllBuckets() {
		return amazonS3Client.listBuckets();
	}

	public void uploadFile(File uploadFile) {
		amazonS3Client.putObject(defaultBucketName, uploadFile.getName(), uploadFile);
	}

	public void uploadFile(String name, byte[] content) {
		File file = new File("/tmp/" + name);
		file.canWrite();
		file.canRead();
		FileOutputStream iofs = null;
		try {
			iofs = new FileOutputStream(file);
			iofs.write(content);
			amazonS3Client.putObject(defaultBucketName, "/" + file.getName(), file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public byte[] getFile(String key) {
		S3Object obj = amazonS3Client.getObject(defaultBucketName, "/" + key);
		S3ObjectInputStream stream = obj.getObjectContent();
		try {
			byte[] content = IOUtils.toByteArray(stream);
			obj.close();
			return content;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ObjectListing GetAllObjects() {
		ObjectListing objectListing = amazonS3Client.listObjects(defaultBucketName);
		return objectListing;
	}
	
	public String GetObject(String id) {
		
		java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);
        
		GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(defaultBucketName, id)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);
		
		URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
		return url.toString();
	}
	
	public void SendFile(String nameFile) {
		amazonS3Client.putObject(
				defaultBucketName, 
				  nameFile, 
				  new File("/Users/vinicius/Downloads/" + nameFile)
				);
	}
}
