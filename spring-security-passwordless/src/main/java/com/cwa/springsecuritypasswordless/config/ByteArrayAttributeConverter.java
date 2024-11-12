package com.cwa.springsecuritypasswordless.config;

import com.yubico.webauthn.data.ByteArray;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ByteArrayAttributeConverter implements AttributeConverter<ByteArray, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(ByteArray byteArray) {
        return byteArray.getBytes();
    }

    @Override
    public ByteArray convertToEntityAttribute(byte[] bytes) {
        return new ByteArray(bytes);
    }
}
