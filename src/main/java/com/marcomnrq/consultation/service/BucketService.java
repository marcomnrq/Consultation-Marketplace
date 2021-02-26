package com.marcomnrq.consultation.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.marcomnrq.consultation.exception.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BucketService {

    private final AmazonS3 amazonS3;

    public void save(String path, String fileName, Optional<Map<String, String>> metadata, InputStream inputStream){
        // Saving a file to an S3 Bucket
        ObjectMetadata objectMetadata = new ObjectMetadata();
        metadata.ifPresent(map ->{
            if(!map.isEmpty()){
                map.forEach(objectMetadata::addUserMetadata);
            }
        });
        try{
            amazonS3.putObject(path, fileName, inputStream, objectMetadata);
        } catch (AmazonServiceException e){
            throw new CustomException("Failed to store file to S3");
        }
    }
}
