package org.springframework.social.youtube.api.impl;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: lancea10
 * Date: 8/27/12
 * Time: 2:39 PM
 */
@XmlRootElement(name = "error")
public class YoutubeError {

    private String domain;

    private String code;

    private String internalReason;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInternalReason() {
        return internalReason;
    }

    public void setInternalReason(String internalReason) {
        this.internalReason = internalReason;
    }
}
