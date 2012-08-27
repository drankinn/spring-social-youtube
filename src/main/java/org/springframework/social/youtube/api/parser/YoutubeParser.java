package org.springframework.social.youtube.api.parser;

import org.springframework.social.youtube.api.YoutubeDataEntry;
import org.springframework.social.youtube.api.YoutubeDataFormat;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * User: lancea10
 * Date: 8/24/12
 * Time: 11:51 AM
 */
public interface YoutubeParser<Y extends YoutubeDataEntry, O> {

    public boolean supportsFormat(YoutubeDataFormat format);

    public List<YoutubeDataFormat> supportedFormats();

    public Class<Y> supportedClass();

    public Y parse(O o) throws IOException, ParseException;

    public List<Y> parseList(O o) throws IOException, ParseException;
}
