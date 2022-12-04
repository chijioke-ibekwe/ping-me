package com.project.pingme.component;

import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class Base64FileParser {

    private byte[] content;
    private String contentType;
    private String extension;

    public Base64FileParser() {
    }

    public void parse(String b64ImageData) throws Exception {

        if (StringUtils.isEmpty(b64ImageData)) {
            throw new Exception("Invalid base64 data: " + b64ImageData);
        }

        String[] base64Components = b64ImageData.split(",");

        if (base64Components.length != 2) {
            throw new Exception("Invalid base64 data: " + b64ImageData);
        }

        String base64Data = base64Components[0];
        this.contentType = base64Data.substring(base64Data.indexOf(':') + 1, base64Data.indexOf(';'));

        this.extension = base64Data.substring(base64Data.indexOf('/') + 1, base64Data.indexOf(';'));

        String base64Image = base64Components[1];
        this.content = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);

    }

    public byte[] getContent()
    {
        return content;
    }


    public String getContentType()
    {
        return contentType;
    }


    public String getExtension()
    {
        return extension;
    }

}

