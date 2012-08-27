package org.springframework.social.youtube.api.parser.json;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.social.youtube.api.YoutubeDataEntry;
import org.springframework.social.youtube.api.YoutubeDataFormat;
import org.springframework.social.youtube.api.parser.YoutubeParser;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * User: lancea10
 * Date: 8/24/12
 * Time: 11:40 AM
 */
public abstract class YoutubeJsonParser<Y extends YoutubeDataEntry> implements YoutubeParser<Y, JsonNode> {

    protected ObjectMapper mapper;

    public YoutubeJsonParser(ObjectMapper mapper){
        this.mapper = mapper;
    }

    @Override
    public boolean supportsFormat(YoutubeDataFormat format) {
        return (format == YoutubeDataFormat.JSON);
    }

    public List<YoutubeDataFormat> supportedFormats(){
       return Collections.singletonList(YoutubeDataFormat.JSON);
    }

    public Y parse(JsonNode node) throws IOException, ParseException {
        if(node.has("entry")){
        node = mapper.readValue(node.get("entry"), JsonNode.class);
        }
        return parseInternal(node);
    }

    public List<Y> parseList(JsonNode node) throws IOException, ParseException {
        List<Y> nodeList = new ArrayList<Y>();
        if(node.has("feed")){
            node = node.get("feed");
        }
        if(node.has("entry")){
            node = node.get("entry");
        }

        Iterator<JsonNode> i = node.iterator();
        JsonNode entryNode;
        while(i.hasNext()){
            entryNode = i.next();
            nodeList.add(parseInternal(entryNode));
        }
        return nodeList;
    }

    public Y parseInternal(JsonNode node) throws IOException, ParseException {
        return  mapper.readValue(node, supportedClass());
    }

    protected String parseT(String name, JsonNode node){
        return parseArray(name, "$t", node);
    }

    protected String parseArray(String outer, String inner, JsonNode node ){
        JsonNode outerNode = node.get(outer);
        if(null != outerNode){
            JsonNode innerNode = outerNode.get(inner);
            if(null != innerNode){
                return innerNode.asText();
            }
        }
        return "";
    }
}
