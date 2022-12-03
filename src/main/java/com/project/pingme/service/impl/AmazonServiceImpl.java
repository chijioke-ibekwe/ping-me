package com.project.pingme.service.impl;

import com.project.pingme.service.AmazonService;
import org.apache.tomcat.util.http.fileupload.impl.IOFileUploadException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AmazonServiceImpl implements AmazonService {

    private final Base64FileParser base64FileParser;

    private final AmazonS3 amazonS3;

    private final AmazonS3Properties amazonS3Properties;

    private static final List<String> VALID_IMAGE_TYPES;
    private static final List<String> VALID_DOCUMENT_TYPES;

    static {
        VALID_IMAGE_TYPES = Arrays.asList("jpg", "jpeg", "png");
        VALID_DOCUMENT_TYPES = Stream.concat(VALID_IMAGE_TYPES.stream(), Stream.of("pdf", "doc", "docx"))
                .collect(Collectors.toList());
    }

    public AmazonServiceImpl(Base64FileParser base64FileParser,
                             AmazonS3 amazonS3,
                             AmazonS3Properties amazonS3Properties) {
        this.base64FileParser = base64FileParser;
        this.amazonS3 = amazonS3;
        this.amazonS3Properties = amazonS3Properties;
    }

    private String upload(String path, Base64FileParser base64FileParser, String bucketName, AccessControlList acl)
            throws Exception {
        try (InputStream stream = new ByteArrayInputStream(base64FileParser.getContent())) {
            String key = path == null ? UUID.randomUUID().toString() : path;
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(base64FileParser.getContent().length);
            metadata.setContentType(base64FileParser.getContentType());
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, stream, metadata);
            if (acl != null) {
                putObjectRequest.setAccessControlList(acl);
            }
            this.amazonS3.putObject(putObjectRequest);
            return this.amazonS3.getUrl(bucketName, key).toExternalForm();
        }
    }


    public String uploadDoc(String path, String data, DocumentType documentType) throws Exception {
        base64FileParser.parse(data);
        if (!VALID_DOCUMENT_TYPES.contains(base64FileParser.getExtension())) {
            throw new IOFileUploadException(String.format("Invalid document format %s. " +
                    "Allowed formats %s", base64FileParser.getExtension(), VALID_DOCUMENT_TYPES));
        }
        AccessControlList acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);

        return this.upload(null, base64FileParser, amazonS3Properties.getBucketName(), acl);
    }
}

