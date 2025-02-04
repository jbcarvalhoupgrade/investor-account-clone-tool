package org.com.credify.investorMgtSrvc.tool.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class OffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {
    private static final DateTimeFormatter DATE_TIME_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");


    @Override
    public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        String dateAsString = jsonParser.getText();
        if (dateAsString == null) {
            throw new IOException("OffsetDateTime argument is null.");
        }

        LocalDateTime datetime = LocalDateTime.parse(dateAsString, DATE_TIME_FORMATTER);
        ZonedDateTime zoned = datetime.atZone(ZoneId.of("America/Los_Angeles"));

        return zoned.toOffsetDateTime();
    }
}
