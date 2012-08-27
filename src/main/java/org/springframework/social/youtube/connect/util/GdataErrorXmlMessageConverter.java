package org.springframework.social.youtube.connect.util;

import org.springframework.http.MediaType;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.social.youtube.api.impl.YoutubeErrors;

import java.util.ArrayList;
import java.util.List;

/**
 * User: lancea10
 * Date: 8/27/12
 * Time: 2:02 PM
 */
public class GdataErrorXmlMessageConverter extends MarshallingHttpMessageConverter {

    private List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();


    public GdataErrorXmlMessageConverter(){
        super();
        supportedMediaTypes.add(new MediaType("application", "vnd.google.gdata.error+xml"));
        setSupportedMediaTypes(supportedMediaTypes);
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(YoutubeErrors.class);
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
