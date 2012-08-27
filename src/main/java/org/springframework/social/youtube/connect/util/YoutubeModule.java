/**
 * User: lance
 * Date: 3/1/12
 * Time: 4:16 PM
 */
package org.springframework.social.youtube.connect.util;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.module.SimpleModule;

/**
 * @author lance
 *         <p/>
 *         <p>
 *         ${CLASSNAME}
 *         </p>
 */
public class YoutubeModule extends SimpleModule {

    public YoutubeModule(String name, Version version) {
        super(name, version);
    }

    public YoutubeModule(){
        super("youtube", new Version(0,0,1,"snapshot"));
    }
    @Override
    public String getModuleName() {
        return "youtube";
    }

    @Override
    public Version version() {
        return new Version(0,0,1,"snapshot");
    }
}
